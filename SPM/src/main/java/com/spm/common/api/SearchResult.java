package com.spm.common.api;

import java.util.List;
import android.util.Log;
import com.spm.common.exception.AndroidException;
import com.spm.common.utils.ThreadUtils;

/**
 * 
 * @author Maxi Rosson
 * @param <E>
 */
public abstract class SearchResult<E> {
	
	private final static String TAG = SearchResult.class.getSimpleName();
	
	private final static int PAGE_SIZE = 20;
	private SortingListener<E> sortingListener;
	private PaginationListener<E> paginationListener;
	private int pageOffset = 1;
	private SortingType sortingType;
	private SearchResultList<E> searchResultList;
	
	/**
	 * @param defaultSortingType
	 */
	public SearchResult(SortingType defaultSortingType) {
		sortingType = defaultSortingType;
		populate();
	}
	
	public SearchResult() {
		this(null);
	}
	
	private List<E> populate() {
		searchResultList = doPopulate(pageOffset, PAGE_SIZE, sortingType);
		Log.i(TAG, "Results: " + searchResultList.getResults().size() + " / Page Offset: " + pageOffset
				+ " / Sorting: " + sortingType);
		return searchResultList.getResults();
	}
	
	public void nextPage() {
		ThreadUtils.execute(new Runnable() {
			
			@Override
			public void run() {
				synchronized (SearchResult.class) {
					paginationListener.onStartPagination();
					pageOffset++;
					try {
						List<E> items = populate();
						paginationListener.onFinishSuccessfulPagination(items);
					} catch (AndroidException e) {
						pageOffset--;
						paginationListener.onFinishInvalidPagination(e);
					}
				}
			}
		});
	}
	
	public void sortBy(SortingType sortingType) {
		sortingListener.onStartSorting();
		pageOffset = 1;
		this.sortingType = sortingType;
		
		ThreadUtils.execute(new Runnable() {
			
			@Override
			public void run() {
				synchronized (SearchResult.class) {
					try {
						List<E> items = populate();
						sortingListener.onFinishSuccessfulSorting(items);
					} catch (AndroidException e) {
						sortingListener.onFinishInvalidSorting(e);
					}
				}
			}
		});
	}
	
	/**
	 * @return Whether this list is on the last page or not
	 */
	public boolean isLastPage() {
		return searchResultList.isLastPage();
	}
	
	protected abstract SearchResultList<E> doPopulate(int pageOffset, int pageSize, SortingType sortingType);
	
	/**
	 * @param sortingListener the sortingListener to set
	 */
	public void setSortingListener(SortingListener<E> sortingListener) {
		this.sortingListener = sortingListener;
	}
	
	/**
	 * @param paginationListener the paginationListener to set
	 */
	public void setPaginationListener(PaginationListener<E> paginationListener) {
		this.paginationListener = paginationListener;
	}
	
	/**
	 * @return the results
	 */
	public List<E> getResults() {
		return searchResultList.getResults();
	}
	
	public interface SortingListener<E> {
		
		/**
		 * Called before the call to sorting the list starts
		 */
		public void onStartSorting();
		
		/**
		 * Called after the successful call to sorting the list
		 * 
		 * @param items
		 */
		public void onFinishSuccessfulSorting(List<E> items);
		
		/**
		 * Called after an invalid call to sorting the list
		 * 
		 * @param androidException The {@link AndroidException} with the error
		 */
		public void onFinishInvalidSorting(AndroidException androidException);
		
	}
	
	public interface PaginationListener<E> {
		
		/**
		 * Called before the call to paginate the list starts
		 */
		public void onStartPagination();
		
		/**
		 * Called after the successful call to paginate the list
		 * 
		 * @param items
		 */
		public void onFinishSuccessfulPagination(List<E> items);
		
		/**
		 * Called after an invalid call to paginate the list
		 * 
		 * @param androidException The {@link AndroidException} with the error
		 */
		public void onFinishInvalidPagination(AndroidException androidException);
		
	}
	
	/**
	 * @return the searchResultList
	 */
	public SearchResultList<E> getSearchResultList() {
		return searchResultList;
	}
}
