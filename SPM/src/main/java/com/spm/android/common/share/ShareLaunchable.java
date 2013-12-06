package com.spm.android.common.share;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

/**
 * App that can be used to share some content.
 * 
 * @author Estefania Caravatti
 */
public class ShareLaunchable {
	
	/** Intent type to share plain text */
	private static final String SHARE_INTENT_TYPE = "text/plain";
	
	private CharSequence name;
	private Drawable icon;
	private String packageName;
	private String publicName;
	
	public ShareLaunchable(ResolveInfo resolveInfo, PackageManager packageManager) {
		name = resolveInfo.loadLabel(packageManager);
		icon = resolveInfo.loadIcon(packageManager);
		packageName = resolveInfo.activityInfo.packageName;
		publicName = resolveInfo.activityInfo.name;
	}
	
	protected ShareLaunchable(String name, Drawable icon) {
		this.name = name;
		this.icon = icon;
	}
	
	/**
	 * @return the name
	 */
	public CharSequence getName() {
		return name;
	}
	
	/**
	 * @return the icon
	 */
	public Drawable getIcon() {
		return icon;
	}
	
	public static Intent getShareIntent() {
		Intent intent = new Intent(android.content.Intent.ACTION_SEND);
		intent.setType(SHARE_INTENT_TYPE);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		return intent;
	}
	
	public void share(Activity context, String shareSubject, String shareMessage, String link, String linkName,
			String caption, String linkImageURL) {
		
		Intent intent = getShareIntent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		intent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
		intent.putExtra(Intent.EXTRA_TEXT, shareMessage);
		intent.setComponent(new ComponentName(packageName, publicName));
		
		context.startActivity(intent);
		context.finish();
	}
}
