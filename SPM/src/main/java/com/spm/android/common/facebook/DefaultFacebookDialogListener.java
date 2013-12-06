package com.spm.android.common.facebook;

import android.os.Bundle;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.spm.android.exception.ExceptionHandler;
import com.spm.common.exception.CommonErrorCode;

/**
 * 
 * @author Estefania Caravatti
 */
public class DefaultFacebookDialogListener implements DialogListener {
	
	/**
	 * @see com.facebook.android.Facebook.DialogListener#onComplete(android.os.Bundle)
	 */
	@Override
	public void onComplete(Bundle values) {
		// Nothing by default.
	}
	
	/**
	 * @see com.facebook.android.Facebook.DialogListener#onFacebookError(com.facebook.android.FacebookError)
	 */
	@Override
	public void onFacebookError(FacebookError e) {
		ExceptionHandler.getInstance().handleException(Thread.currentThread(),
			CommonErrorCode.FACEBOOK_ERROR.newApplicationException(e));
	}
	
	/**
	 * @see com.facebook.android.Facebook.DialogListener#onError(com.facebook.android.DialogError)
	 */
	@Override
	public void onError(DialogError e) {
		ExceptionHandler.getInstance().handleException(Thread.currentThread(),
			CommonErrorCode.FACEBOOK_ERROR.newApplicationException(e));
	}
	
	/**
	 * @see com.facebook.android.Facebook.DialogListener#onCancel()
	 */
	@Override
	public void onCancel() {
		// Nothing by default.
	}
	
}
