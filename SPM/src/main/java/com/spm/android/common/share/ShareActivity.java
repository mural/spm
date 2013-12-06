package com.spm.android.common.share;

import java.util.Collections;
import java.util.List;
import roboguice.inject.InjectExtra;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import com.spm.android.common.activity.AbstractListActivity;

/**
 * Activity with the list of applications that accept a share intent. This activity overrides facebook default option
 * with a custom behavior.
 * 
 * @author Estefania Caravatti
 */
public abstract class ShareActivity extends AbstractListActivity<ShareLaunchable> {
	
	public static final String SUBJECT_EXTRA = "subject";
	public static final String MESSAGE_EXTRA = "message";
	public static final String LINK_EXTRA = "link";
	public static final String LINK_NAME_EXTRA = "linkName";
	public static final String LINK_IMAGE_URL_EXTRA = "linkImage";
	public static final String CAPTION_EXTRA = "caption";
	
	@InjectExtra(SUBJECT_EXTRA)
	private String shareSubject;
	
	@InjectExtra(MESSAGE_EXTRA)
	private String shareMessage;
	
	@InjectExtra(LINK_EXTRA)
	private String link;
	
	@InjectExtra(LINK_NAME_EXTRA)
	private String linkName;
	
	@InjectExtra(LINK_IMAGE_URL_EXTRA)
	private String linkImageURL;
	
	@InjectExtra(CAPTION_EXTRA)
	private String linkCaption;
	
	/**
	 * @see com.spm.android.common.activity.AbstractListActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get the list of activities that can handle the share intent.
		PackageManager packageManager = getPackageManager();
		List<ResolveInfo> launchables = packageManager.queryIntentActivities(ShareLaunchable.getShareIntent(), 0);
		Collections.sort(launchables, new ResolveInfo.DisplayNameComparator(packageManager));
		
		setListAdapter(new ShareAdapter(this, launchables, packageManager, getFacebookAppId(), getAccessToken()));
	}
	
	/**
	 * @see com.spm.android.common.activity.AbstractListActivity#onListItemClick(java.lang.Object)
	 */
	@Override
	protected void onListItemClick(ShareLaunchable shareLaunchable) {
		shareLaunchable.share(this, shareSubject, shareMessage, link, linkName, linkCaption, linkImageURL);
	}
	
	protected abstract String getFacebookAppId();
	
	protected abstract String getAccessToken();
	
}
