package com.spm.android.common.activity;

import java.io.File;
import android.app.Activity;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import com.spm.android.common.AndroidApplication;
import com.spm.common.utils.IdGenerator;
import com.spm.R;

/**
 * Menu {@link Activity} that displays the ways of changing the user picture.
 * 
 */
public class PictureMenuActivity extends AbstractActivity implements OnCancelListener {
	
	/** Request ID number for the picture setting. */
	public static final int SET_PICTURE = IdGenerator.getIntId();
	
	/** ID of the extra that will contain the URI of the picture returned. */
	public static final String PICTURE_URI = PictureMenuActivity.class.getSimpleName() + ".URI";
	
	private static final String IMAGE_TYPE = "image/*";
	
	private static final int TAKE_PHOTO_REQUEST_CODE = 1;
	private static final int CHOOSE_FROM_GALLERY_REQUEST_CODE = 2;
	
	private Uri outputFileUri;
	
	/**
	 * @see com.spm.android.common.activity.AbstractActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.picture_menu_activity);
		
		// Set the onclick to close the activity.
		findView(R.id.cancelPicture).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				PictureMenuActivity.this.setResult(RESULT_CANCELED);
				PictureMenuActivity.this.finish();
			}
		});
		
		// Configure the take photo button.
		findView(R.id.takePhoto).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				File file = new File(AndroidApplication.get().getCacheDirectory(), System.currentTimeMillis() + ".png");
				outputFileUri = Uri.fromFile(file);
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
				startActivityForResult(intent, TAKE_PHOTO_REQUEST_CODE);
			}
		});
		
		// Configure the choose from library button.
		findView(R.id.chooseFromLibrary).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent imagePickerIntent = new Intent(Intent.ACTION_PICK);
				imagePickerIntent.setType(IMAGE_TYPE);
				startActivityForResult(imagePickerIntent, CHOOSE_FROM_GALLERY_REQUEST_CODE);
			}
		});
	}
	
	/**
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Uri path = null;
			switch (requestCode) {
			
			// Set the default path for the camera pictures if the picture is
			// obtained from the camera.
				case TAKE_PHOTO_REQUEST_CODE:
					path = outputFileUri;
					break;
				
				// Set the obtained path if the picture is obtained from the device's gallery.
				case CHOOSE_FROM_GALLERY_REQUEST_CODE:
					path = data.getData();
					break;
			}
			Intent intent = new Intent();
			intent.putExtra(PICTURE_URI, path);
			setResult(resultCode, intent);
			finish();
		}
	}
}
