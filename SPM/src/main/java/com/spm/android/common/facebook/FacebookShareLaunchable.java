package com.spm.android.common.facebook;

import android.app.Activity;
import android.os.Bundle;
import com.facebook.android.Facebook;
import com.spm.android.common.AndroidApplication;
import com.spm.android.common.share.ShareLaunchable;
import com.spm.R;

/**
 * {@link ShareLaunchable} that shares content using the Facebook SDK.
 * 
 * @author Estefania Caravatti
 */
public class FacebookShareLaunchable extends ShareLaunchable {
	
	private String facebookAppId;
	private String accessToken;
	
	public FacebookShareLaunchable(String facebookAppId, String accessToken) {
		super("Facebook", AndroidApplication.get().getResources().getDrawable(R.drawable.ic_facebook_launcher));
		this.facebookAppId = facebookAppId;
		this.accessToken = accessToken;
	}
	
	/**
	 * @see com.spm.android.common.share.ShareLaunchable#share(android.app.Activity, java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void share(final Activity context, String shareSubject, String shareMessage, String link, String linkName,
			String caption, String linkImageURL) {
		
		Facebook facebook = new Facebook(facebookAppId);
		facebook.setAccessToken(accessToken);
		
		Bundle parameters = new Bundle();
		parameters.putString("description", shareMessage);
		parameters.putString("caption", caption);
		parameters.putString("name", linkName);
		parameters.putString("link", link);
		parameters.putString("picture", linkImageURL);
		
		facebook.dialog(context, "feed", parameters, new DefaultFacebookDialogListener() {
			
			@Override
			public void onComplete(Bundle values) {
				context.finish();
			}
			
			@Override
			public void onCancel() {
				context.finish();
			}
		});
	}
}
