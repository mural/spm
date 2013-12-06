package com.spm.android.common.barcode;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import com.spm.android.common.AndroidApplication;
import com.spm.android.common.utils.MarketUtils;
import com.spm.common.utils.IdGenerator;
import com.spm.R;

/**
 * <p>
 * A utility class which helps ease integration with Barcode Scanner via {@link Intent}s. This is a simple way to invoke
 * barcode scanning and receive the result, without any need to integrate, modify, or learn the project's source code.
 * </p>
 * 
 * <p>
 * It does require that the Barcode Scanner application is installed. The {@link #initiateScan()} method will prompt the
 * user to download the application, if needed.
 * </p>
 * 
 * @author Luciano Rey
 */
public final class BarcodeIntent {
	
	private static final int REQUEST_CODE = IdGenerator.getIntId();
	
	private static final String PACKAGE = "com.google.zxing.client.android";
	
	private static final String SCAN_RESULT = "SCAN_RESULT";
	private static final String SCAN_RESULT_FORMAT = "SCAN_RESULT_FORMAT";
	
	/**
	 * @return AlertDialog
	 */
	public static AlertDialog initiateScan() {
		Activity activity = AndroidApplication.get().getCurrentActivity();
		
		Intent intentScan = new Intent(PACKAGE + ".SCAN");
		intentScan.setPackage(PACKAGE);
		intentScan.addCategory(Intent.CATEGORY_DEFAULT);
		
		try {
			activity.startActivityForResult(intentScan, REQUEST_CODE);
			return null;
		} catch (ActivityNotFoundException e) {
			return MarketUtils.showDownloadDialog(activity, R.string.barcodeScanner, PACKAGE);
		}
	}
	
	/**
	 * <p>
	 * Call this from your {@link Activity}'s {@link Activity#onActivityResult(int, int, Intent)} method.
	 * </p>
	 * 
	 * @param requestCode
	 * @param resultCode
	 * @param intent
	 * 
	 * @return null if the event handled here was not related to this class, or else an {@link BarcodeIntentResult}
	 *         containing the result of the scan. If the user cancelled scanning, the fields will be null.
	 */
	public static BarcodeIntentResult parseActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				String contents = intent.getStringExtra(SCAN_RESULT);
				String formatName = intent.getStringExtra(SCAN_RESULT_FORMAT);
				return new BarcodeIntentResult(contents, formatName);
			}
		}
		return null;
	}
}
