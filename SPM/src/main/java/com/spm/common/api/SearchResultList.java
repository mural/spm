package com.spm.common.api;

import java.util.List;
import com.google.inject.internal.Lists;

/**
 * Represents a list result in a paginated API call.
 * 
 * @param <T> The list item.
 * @author Estefania Caravatti
 */
public class SearchResultList<T> {
	
	private boolean lastPage;
	private List<T> results = Lists.newArrayList();
	
	/**
	 * @param lastPage Whether the paginates list contains the last page or not.
	 */
	public SearchResultList(boolean lastPage) {
		this.lastPage = lastPage;
	}
	
	public SearchResultList() {
		this(true);
	}
	
	/**
	 * Adds a result item to the list.
	 * 
	 * @param result The result to add.
	 */
	public void addResult(T result) {
		results.add(result);
	}
	
	/**
	 * @return the results
	 */
	public List<T> getResults() {
		return results;
	}
	
	/**
	 * @return the lastPage
	 */
	public boolean isLastPage() {
		return lastPage;
	}
}
