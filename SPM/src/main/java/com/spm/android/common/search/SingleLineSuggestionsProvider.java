package com.spm.android.common.search;

import android.content.SearchRecentSuggestionsProvider;
import com.spm.android.common.utils.AndroidUtils;

/**
 * Declare on your manifest as:
 * 
 * <pre>
 * &lt;provider android:name=".android.common.search.SingleLineSuggestionsProvider"
 * 	android:authorities="[PackageName].SingleLineSuggestionsProvider" />
 * </pre>
 * 
 * @author Maxi Rosson
 */
public class SingleLineSuggestionsProvider extends SearchRecentSuggestionsProvider {
	
	public final static String AUTHORITY = AndroidUtils.getPackageName() + ".SingleLineSuggestionsProvider";
	public final static int MODE = DATABASE_MODE_QUERIES;
	
	public SingleLineSuggestionsProvider() {
		setupSuggestions(AUTHORITY, MODE);
	}
}