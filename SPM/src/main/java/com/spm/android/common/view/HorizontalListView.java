package com.spm.android.common.view;

import java.util.LinkedList;
import java.util.Queue;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.Scroller;

public class HorizontalListView extends AdapterView<ListAdapter> {
	
	private ListAdapter listAdapter;
	private int leftViewIndex = -1;
	private int rightViewIndex = 0;
	private int currentX;
	private int nextX;
	private int maxX = Integer.MAX_VALUE;
	private int displayOffset = 0;
	private Scroller scroller;
	private GestureDetector gesture;
	private Queue<View> removedViewQueue = new LinkedList<View>();
	private OnItemSelectedListener onItemSelected;
	private OnItemClickListener onItemClicked;
	private OnItemLongClickListener onItemLongClicked;
	private boolean dataChanged = false;
	
	public HorizontalListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}
	
	private synchronized void initView() {
		leftViewIndex = -1;
		rightViewIndex = 0;
		displayOffset = 0;
		currentX = 0;
		nextX = 0;
		maxX = Integer.MAX_VALUE;
		scroller = new Scroller(getContext());
		gesture = new GestureDetector(getContext(), onGesture);
	}
	
	/**
	 * @see android.widget.AdapterView#setOnItemSelectedListener(android.widget.AdapterView.OnItemSelectedListener)
	 */
	@Override
	public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener) {
		onItemSelected = listener;
	}
	
	/**
	 * @see android.widget.AdapterView#setOnItemClickListener(android.widget.AdapterView.OnItemClickListener)
	 */
	@Override
	public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
		onItemClicked = listener;
	}
	
	/**
	 * @see android.widget.AdapterView#setOnItemLongClickListener(android.widget.AdapterView.OnItemLongClickListener)
	 */
	@Override
	public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener listener) {
		onItemLongClicked = listener;
	}
	
	private DataSetObserver dataObserver = new DataSetObserver() {
		
		@Override
		public void onChanged() {
			synchronized (HorizontalListView.this) {
				dataChanged = true;
			}
			invalidate();
			requestLayout();
		}
		
		@Override
		public void onInvalidated() {
			reset();
			invalidate();
			requestLayout();
		}
	};
	
	/**
	 * @see android.widget.AdapterView#getAdapter()
	 */
	@Override
	public ListAdapter getAdapter() {
		return listAdapter;
	}
	
	/**
	 * @see android.widget.AdapterView#getSelectedView()
	 */
	@Override
	public View getSelectedView() {
		return null;
	}
	
	/**
	 * @see android.widget.AdapterView#setAdapter(android.widget.Adapter)
	 */
	@Override
	public void setAdapter(ListAdapter adapter) {
		if (listAdapter != null) {
			listAdapter.unregisterDataSetObserver(dataObserver);
		}
		listAdapter = adapter;
		listAdapter.registerDataSetObserver(dataObserver);
		reset();
	}
	
	private synchronized void reset() {
		initView();
		removeAllViewsInLayout();
		requestLayout();
	}
	
	/**
	 * @see android.widget.AdapterView#setSelection(int)
	 */
	@Override
	public void setSelection(int position) {
	}
	
	private void addAndMeasureChild(final View child, int viewPos) {
		LayoutParams params = child.getLayoutParams();
		if (params == null) {
			params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		}
		
		addViewInLayout(child, viewPos, params, true);
		child.measure(MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.AT_MOST),
			MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.AT_MOST));
	}
	
	/**
	 * @see android.widget.AdapterView#onLayout(boolean, int, int, int, int)
	 */
	@Override
	protected synchronized void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		
		if (listAdapter == null) {
			return;
		}
		
		if (dataChanged) {
			int oldCurrentX = currentX;
			initView();
			removeAllViewsInLayout();
			nextX = oldCurrentX;
			dataChanged = false;
		}
		
		if (scroller.computeScrollOffset()) {
			int scrollx = scroller.getCurrX();
			nextX = scrollx;
		}
		
		if (nextX <= 0) {
			nextX = 0;
			scroller.forceFinished(true);
		}
		if (nextX >= maxX) {
			nextX = maxX;
			scroller.forceFinished(true);
		}
		
		int dx = currentX - nextX;
		
		removeNonVisibleItems(dx);
		fillList(dx);
		positionItems(dx);
		
		currentX = nextX;
		
		if (!scroller.isFinished()) {
			post(new Runnable() {
				
				@Override
				public void run() {
					requestLayout();
				}
			});
			
		}
	}
	
	private void fillList(final int dx) {
		int edge = 0;
		View child = getChildAt(getChildCount() - 1);
		if (child != null) {
			edge = child.getRight();
		}
		fillListRight(edge, dx);
		
		edge = 0;
		child = getChildAt(0);
		if (child != null) {
			edge = child.getLeft();
		}
		fillListLeft(edge, dx);
		
	}
	
	private void fillListRight(int rightEdge, final int dx) {
		while (((rightEdge + dx) < getWidth()) && (rightViewIndex < listAdapter.getCount())) {
			
			View child = listAdapter.getView(rightViewIndex, removedViewQueue.poll(), this);
			addAndMeasureChild(child, -1);
			rightEdge += child.getMeasuredWidth();
			
			if (rightViewIndex == (listAdapter.getCount() - 1)) {
				maxX = (currentX + rightEdge) - getWidth();
			}
			
			if (maxX < 0) {
				maxX = 0;
			}
			rightViewIndex++;
		}
		
	}
	
	private void fillListLeft(int leftEdge, final int dx) {
		while (((leftEdge + dx) > 0) && (leftViewIndex >= 0)) {
			View child = listAdapter.getView(leftViewIndex, removedViewQueue.poll(), this);
			addAndMeasureChild(child, 0);
			leftEdge -= child.getMeasuredWidth();
			leftViewIndex--;
			displayOffset -= child.getMeasuredWidth();
		}
	}
	
	private void removeNonVisibleItems(final int dx) {
		View child = getChildAt(0);
		while ((child != null) && ((child.getRight() + dx) <= 0)) {
			displayOffset += child.getMeasuredWidth();
			removedViewQueue.offer(child);
			removeViewInLayout(child);
			leftViewIndex++;
			child = getChildAt(0);
			
		}
		
		child = getChildAt(getChildCount() - 1);
		while ((child != null) && ((child.getLeft() + dx) >= getWidth())) {
			removedViewQueue.offer(child);
			removeViewInLayout(child);
			rightViewIndex--;
			child = getChildAt(getChildCount() - 1);
		}
	}
	
	private void positionItems(final int dx) {
		if (getChildCount() > 0) {
			displayOffset += dx;
			int left = displayOffset;
			for (int i = 0; i < getChildCount(); i++) {
				View child = getChildAt(i);
				int childWidth = child.getMeasuredWidth();
				child.layout(left, 0, left + childWidth, child.getMeasuredHeight());
				left += childWidth;
			}
		}
	}
	
	public synchronized void scrollTo(int x) {
		scroller.startScroll(nextX, 0, x - nextX, 0);
		requestLayout();
	}
	
	/**
	 * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		boolean handled = gesture.onTouchEvent(event);
		return handled;
	}
	
	protected boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
		synchronized (HorizontalListView.this) {
			scroller.fling(nextX, 0, (int)-velocityX, 0, 0, maxX, 0, 0);
		}
		requestLayout();
		
		return true;
	}
	
	protected boolean onDown(MotionEvent event) {
		scroller.forceFinished(true);
		return true;
	}
	
	private OnGestureListener onGesture = new GestureDetector.SimpleOnGestureListener() {
		
		/**
		 * @see android.view.GestureDetector.SimpleOnGestureListener#onDown(android.view.MotionEvent)
		 */
		@Override
		public boolean onDown(MotionEvent event) {
			return HorizontalListView.this.onDown(event);
		}
		
		/**
		 * @see android.view.GestureDetector.SimpleOnGestureListener#onFling(android.view.MotionEvent,
		 *      android.view.MotionEvent, float, float)
		 */
		@Override
		public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
			return HorizontalListView.this.onFling(event1, event2, velocityX, velocityY);
		}
		
		/**
		 * @see android.view.GestureDetector.SimpleOnGestureListener#onScroll(android.view.MotionEvent,
		 *      android.view.MotionEvent, float, float)
		 */
		@Override
		public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX, float distanceY) {
			
			synchronized (HorizontalListView.this) {
				nextX += (int)distanceX;
			}
			requestLayout();
			
			return true;
		}
		
		/**
		 * @see android.view.GestureDetector.SimpleOnGestureListener#onSingleTapConfirmed(android.view.MotionEvent)
		 */
		@Override
		public boolean onSingleTapConfirmed(MotionEvent event) {
			Rect viewRect = new Rect();
			for (int i = 0; i < getChildCount(); i++) {
				View child = getChildAt(i);
				int left = child.getLeft();
				int right = child.getRight();
				int top = child.getTop();
				int bottom = child.getBottom();
				viewRect.set(left, top, right, bottom);
				if (viewRect.contains((int)event.getX(), (int)event.getY())) {
					if (onItemClicked != null) {
						onItemClicked.onItemClick(HorizontalListView.this, child, leftViewIndex + 1 + i,
							listAdapter.getItemId(leftViewIndex + 1 + i));
					}
					if (onItemSelected != null) {
						onItemSelected.onItemSelected(HorizontalListView.this, child, leftViewIndex + 1 + i,
							listAdapter.getItemId(leftViewIndex + 1 + i));
					}
					break;
				}
				
			}
			return true;
		}
		
		/**
		 * @see android.view.GestureDetector.SimpleOnGestureListener#onLongPress(android.view.MotionEvent)
		 */
		@Override
		public void onLongPress(MotionEvent event) {
			Rect viewRect = new Rect();
			int childCount = getChildCount();
			for (int i = 0; i < childCount; i++) {
				View child = getChildAt(i);
				int left = child.getLeft();
				int right = child.getRight();
				int top = child.getTop();
				int bottom = child.getBottom();
				viewRect.set(left, top, right, bottom);
				if (viewRect.contains((int)event.getX(), (int)event.getY())) {
					if (onItemLongClicked != null) {
						onItemLongClicked.onItemLongClick(HorizontalListView.this, child, leftViewIndex + 1 + i,
							listAdapter.getItemId(leftViewIndex + 1 + i));
					}
					break;
				}
				
			}
		}
	};
}
