package com.spm.android.common.map;

import java.util.List;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.spm.android.common.utils.MeasureUtils;
import com.spm.R;

/**
 * An abstract extension of ItemizedOverlay for displaying an information balloon upon screen-tap of each marker
 * overlay.
 * 
 * @author Jeff Gilfelt
 * @param <Item>
 */
public abstract class BalloonItemizedOverlay<Item> extends ItemizedOverlay<OverlayItem> {
	
	private static final int DEFAULT_BOTTOM_OFFSET = 36;
	
	private MapView mapView;
	private BalloonOverlayView balloonView;
	private View clickRegion;
	private int viewOffset;
	
	/**
	 * Create a new BalloonItemizedOverlay
	 * 
	 * @param defaultMarker - A bounded Drawable to be drawn on the map for each item in the overlay.
	 * @param mapView - The view upon which the overlay items are to be drawn.
	 */
	public BalloonItemizedOverlay(Drawable defaultMarker, MapView mapView) {
		super(defaultMarker);
		this.mapView = mapView;
		setBalloonBottomOffset(DEFAULT_BOTTOM_OFFSET);
	}
	
	/**
	 * Set the horizontal distance between the marker and the bottom of the information balloon. The default is 0 which
	 * works well for center bounded markers. If your marker is center-bottom bounded, call this before adding overlay
	 * items to ensure the balloon hovers exactly above the marker.
	 * 
	 * @param pixels - The padding between the center point and the bottom of the information balloon.
	 */
	public void setBalloonBottomOffset(int pixels) {
		viewOffset = MeasureUtils.pxToDp(pixels);
	}
	
	/**
	 * Override this method to handle a "tap" on a balloon. By default, does nothing and returns false.
	 * 
	 * @param index - The index of the item whose balloon is tapped.
	 */
	protected abstract void onBalloonTap(int index);
	
	/**
	 * @see com.google.android.maps.ItemizedOverlay#onTouchEvent(android.view.MotionEvent,
	 *      com.google.android.maps.MapView)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event, MapView mapView) {
		hideBalloon();
		return super.onTouchEvent(event, mapView);
	}
	
	/**
	 * @see com.google.android.maps.ItemizedOverlay#onTap(int)
	 */
	@Override
	protected final boolean onTap(int index) {
		
		boolean isRecycled;
		OverlayItem overlayItem = createItem(index);
		
		GeoPoint point = overlayItem.getPoint();
		
		if (balloonView == null) {
			balloonView = new BalloonOverlayView(mapView.getContext(), viewOffset);
			clickRegion = balloonView.findViewById(R.id.balloon_inner_layout);
			isRecycled = false;
		} else {
			isRecycled = true;
		}
		
		balloonView.setVisibility(View.GONE);
		
		List<Overlay> mapOverlays = mapView.getOverlays();
		if (mapOverlays.size() > 1) {
			hideOtherBalloons(mapOverlays);
		}
		
		balloonView.setData(overlayItem);
		
		MapView.LayoutParams params = new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				point, MapView.LayoutParams.BOTTOM_CENTER);
		params.mode = MapView.LayoutParams.MODE_MAP;
		
		setBalloonTouchListener(index);
		
		balloonView.setVisibility(View.VISIBLE);
		
		if (isRecycled) {
			balloonView.setLayoutParams(params);
		} else {
			mapView.addView(balloonView, params);
		}
		
		mapView.getController().animateTo(point);
		
		return true;
	}
	
	/**
	 * Sets the visibility of this overlay's balloon view to GONE.
	 */
	private void hideBalloon() {
		if (balloonView != null) {
			balloonView.setVisibility(View.GONE);
		}
	}
	
	/**
	 * Hides the balloon view for any other BalloonItemizedOverlay instances that might be present on the MapView.
	 * 
	 * @param overlays - list of overlays (including this) on the MapView.
	 */
	private void hideOtherBalloons(List<Overlay> overlays) {
		
		for (Overlay overlay : overlays) {
			if ((overlay instanceof BalloonItemizedOverlay<?>) && (overlay != this)) {
				((BalloonItemizedOverlay<?>)overlay).hideBalloon();
			}
		}
		
	}
	
	/**
	 * Sets the onTouchListener for the balloon being displayed, calling the overridden onBalloonTap if implemented.
	 * 
	 * @param thisIndex - The index of the item whose balloon is tapped.
	 */
	private void setBalloonTouchListener(final int thisIndex) {
		
		clickRegion.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				View l = ((View)v.getParent()).findViewById(R.id.balloon_main_layout);
				Drawable d = l.getBackground();
				
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					int[] states = { android.R.attr.state_pressed };
					if (d.setState(states)) {
						d.invalidateSelf();
					}
					return true;
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					int newStates[] = {};
					if (d.setState(newStates)) {
						d.invalidateSelf();
					}
					// call overridden method
					onBalloonTap(thisIndex);
					return true;
				} else {
					return false;
				}
				
			}
		});
	}
	
}
