package com.spm.android.common.utils;

import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.spm.R;
import com.spm.android.common.AndroidApplication;

/**
 * @author Luciano Rey
 * @author Fernando Perez
 * @author Maxi Rosson
 */
public final class ToastUtils {
	
	private static final int DEFAULT_DURATION = Toast.LENGTH_LONG;
	private static final int DEFAULT_GRAVITY = Gravity.CENTER;
	private static final int DEFAULT_X_OFFSET = 0;
	private static final int DEFAULT_Y_OFFSET = 0;
	
	private static final Handler HANDLER = new Handler() {
		
		@Override
		public void handleMessage(android.os.Message msg) {
			showToast((String)msg.obj);
		};
	};
	
	public static final void init() {
		// nothing...
	}
	
	/**
	 * Show the {@link Toast} on the UI Thread.
	 * 
	 * @param message The text to show. Can be formatted text.
	 */
	public static void showToastOnUIThread(String message) {
		HANDLER.sendMessage(HANDLER.obtainMessage(1, message));
	}
	
	/**
	 * Show the {@link Toast} on the UI Thread.
	 * 
	 * @param messageId The id of the text to show.
	 */
	public static void showToastOnUIThread(int messageId) {
		showToastOnUIThread(LocalizationUtils.getString(messageId));
	}
	
	/**
	 * Show the {@link Toast} on the current Thread.
	 * 
	 * @param messageId The id of the text to show.
	 */
	public static void showToast(int messageId) {
		showToast(LocalizationUtils.getString(messageId), DEFAULT_DURATION);
	}
	
	/**
	 * Show the {@link Toast} on the current Thread.
	 * 
	 * @param message The text to show. Can be formatted text.
	 */
	public static void showToast(String message) {
		showToast(message, DEFAULT_DURATION);
	}
	
	/**
	 * Show the {@link Toast} on the current Thread.
	 * 
	 * @param message The text to show. Can be formatted text.
	 * @param duration How long to display the message. Either {@link Toast#LENGTH_SHORT} {@link Toast#LENGTH_LONG}
	 */
	public static void showToast(String message, int duration) {
		showToast(message, duration, DEFAULT_GRAVITY, DEFAULT_X_OFFSET, DEFAULT_Y_OFFSET);
	}
	
	/**
	 * Show the {@link Toast} on the current Thread.
	 * 
	 * @param message The text to show. Can be formatted text.
	 * @param duration How long to display the message. Either {@link Toast#LENGTH_SHORT} {@link Toast#LENGTH_LONG}
	 * @param gravity The location at which the notification should appear on the screen.
	 * @param xOffset The X offset in pixels to apply to the gravity's location.
	 * @param yOffset The Y offset in pixels to apply to the gravity's location.
	 */
	public static void showToast(String message, int duration, int gravity, int xOffset, int yOffset) {
		showCustomToast(null, message, duration, gravity, xOffset, yOffset);
	}
	
	/**
	 * Show an info {@link Toast} on the current Thread.
	 * 
	 * @param messageId The id of the text to show.
	 */
	public static void showInfoToast(int messageId) {
		showInfoToast(LocalizationUtils.getString(messageId), DEFAULT_DURATION);
	}
	
	/**
	 * Show an info {@link Toast} on the current Thread.
	 * 
	 * @param message The text to show. Can be formatted text.
	 */
	public static void showInfoToast(String message) {
		showInfoToast(message, DEFAULT_DURATION);
	}
	
	/**
	 * Show an info {@link Toast} on the current Thread.
	 * 
	 * @param message The text to show. Can be formatted text.
	 * @param duration How long to display the message. Either {@link Toast#LENGTH_SHORT} {@link Toast#LENGTH_LONG}
	 */
	public static void showInfoToast(String message, int duration) {
		showInfoToast(message, duration, DEFAULT_GRAVITY, DEFAULT_X_OFFSET, DEFAULT_Y_OFFSET);
	}
	
	/**
	 * Show an info {@link Toast} on the current Thread.
	 * 
	 * @param message The text to show. Can be formatted text.
	 * @param duration How long to display the message. Either {@link Toast#LENGTH_SHORT} {@link Toast#LENGTH_LONG}
	 * @param gravity The location at which the notification should appear on the screen.
	 * @param xOffset The X offset in pixels to apply to the gravity's location.
	 * @param yOffset The Y offset in pixels to apply to the gravity's location.
	 */
	public static void showInfoToast(String message, int duration, int gravity, int xOffset, int yOffset) {
		showCustomToast(R.drawable.toast_info, message, duration, gravity, xOffset, yOffset);
	}
	
	/**
	 * Show a warning {@link Toast} on the current Thread.
	 * 
	 * @param messageId The id of the text to show.
	 */
	public static void showWarningToast(int messageId) {
		showWarningToast(LocalizationUtils.getString(messageId), DEFAULT_DURATION);
	}
	
	/**
	 * Show a warning {@link Toast} on the current Thread.
	 * 
	 * @param message The text to show. Can be formatted text.
	 */
	public static void showWarningToast(String message) {
		showWarningToast(message, DEFAULT_DURATION);
	}
	
	/**
	 * Show a warning {@link Toast} on the current Thread.
	 * 
	 * @param message The text to show. Can be formatted text.
	 * @param duration How long to display the message. Either {@link Toast#LENGTH_SHORT} {@link Toast#LENGTH_LONG}
	 */
	public static void showWarningToast(String message, int duration) {
		showWarningToast(message, duration, DEFAULT_GRAVITY, DEFAULT_X_OFFSET, DEFAULT_Y_OFFSET);
	}
	
	/**
	 * Show a warning {@link Toast} on the current Thread.
	 * 
	 * @param message The text to show. Can be formatted text.
	 * @param duration How long to display the message. Either {@link Toast#LENGTH_SHORT} {@link Toast#LENGTH_LONG}
	 * @param gravity The location at which the notification should appear on the screen.
	 * @param xOffset The X offset in pixels to apply to the gravity's location.
	 * @param yOffset The Y offset in pixels to apply to the gravity's location.
	 */
	public static void showWarningToast(String message, int duration, int gravity, int xOffset, int yOffset) {
		showCustomToast(R.drawable.toast_warning, message, duration, gravity, xOffset, yOffset);
	}
	
	/**
	 * Show an error {@link Toast} on the current Thread.
	 * 
	 * @param messageId The id of the text to show.
	 */
	public static void showErrorToast(int messageId) {
		showErrorToast(LocalizationUtils.getString(messageId), DEFAULT_DURATION);
	}
	
	/**
	 * Show an error {@link Toast} on the current Thread.
	 * 
	 * @param message The text to show. Can be formatted text.
	 */
	public static void showErrorToast(String message) {
		showErrorToast(message, DEFAULT_DURATION);
	}
	
	/**
	 * Show an error {@link Toast} on the current Thread.
	 * 
	 * @param message The text to show. Can be formatted text.
	 * @param duration How long to display the message. Either {@link Toast#LENGTH_SHORT} {@link Toast#LENGTH_LONG}
	 */
	public static void showErrorToast(String message, int duration) {
		showErrorToast(message, duration, DEFAULT_GRAVITY, DEFAULT_X_OFFSET, DEFAULT_Y_OFFSET);
	}
	
	/**
	 * Show an error {@link Toast} on the current Thread.
	 * 
	 * @param message The text to show. Can be formatted text.
	 * @param duration How long to display the message. Either {@link Toast#LENGTH_SHORT} {@link Toast#LENGTH_LONG}
	 * @param gravity The location at which the notification should appear on the screen.
	 * @param xOffset The X offset in pixels to apply to the gravity's location.
	 * @param yOffset The Y offset in pixels to apply to the gravity's location.
	 */
	public static void showErrorToast(String message, int duration, int gravity, int xOffset, int yOffset) {
		showCustomToast(R.drawable.toast_error, message, duration, gravity, xOffset, yOffset);
	}
	
	/**
	 * Show the {@link Toast} on the current Thread.
	 * 
	 * @param imageId The id of the image to display
	 * @param message The text to show. Can be formatted text.
	 * @param duration How long to display the message. Either {@link Toast#LENGTH_SHORT} {@link Toast#LENGTH_LONG}
	 * @param gravity The location at which the notification should appear on the screen.
	 * @param xOffset The X offset in pixels to apply to the gravity's location.
	 * @param yOffset The Y offset in pixels to apply to the gravity's location.
	 */
	public static void showCustomToast(Integer imageId, String message, int duration, int gravity, int xOffset,
			int yOffset) {
		AndroidApplication androidApplication = AndroidApplication.get();
		if (!androidApplication.isInBackground()) {
			
			LayoutInflater inflater = LayoutInflater.from(androidApplication);
			View view = inflater.inflate(R.layout.toast_custom, null);
			
			if (imageId != null) {
				ImageView imageView = (ImageView)view.findViewById(R.id.toastImage);
				imageView.setImageResource(imageId);
				imageView.setVisibility(View.VISIBLE);
			}
			
			TextView textView = (TextView)view.findViewById(R.id.toastMessage);
			textView.setText(message);
			
			Toast toast = new Toast(androidApplication);
			toast.setGravity(gravity, xOffset, yOffset);
			toast.setDuration(duration);
			toast.setView(view);
			toast.show();
		}
	}
	
	/**
	 * Show a "This feature is not available yet" message with a Toast on the current Thread.
	 */
	public static void showNotAvailableToast() {
		showToast("This feature is not available yet", Toast.LENGTH_SHORT);
	}
}
