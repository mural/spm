package com.spm.android.common.search;

import android.content.SearchRecentSuggestionsProvider;
import com.spm.android.common.utils.AndroidUtils;

/**
 * Declare on your manifest as:
 * 
 * <pre>
 * &lt;provider android:name=".android.common.search.TwoLinesSuggestionsProvider"
 * 	android:authorities="[PackageName].TwoLinesSuggestionsProvider" />
 * </pre>
 * 
 * @author Maxi Rosson
 */
public class TwoLinesSuggestionsProvider extends SearchRecentSuggestionsProvider {
	
	public final static String AUTHORITY = AndroidUtils.getPackageName() + ".TwoLinesSuggestionsProvider";
	public final static int MODE = DATABASE_MODE_QUERIES | DATABASE_MODE_2LINES;
	
	public TwoLinesSuggestionsProvider() {
		setupSuggestions(AUTHORITY, MODE);
	}
}