package com.spm.android.common.utils;

import java.util.Calendar;
import java.util.Random;
import android.content.Context;
import android.telephony.TelephonyManager;
import com.spm.android.common.AndroidApplication;

/**
 * @author Luciano Rey
 */
public class DeviceUtils {
	
	private static final TelephonyManager TELEPHONY_MANAGER;
	private static String DEVICE_ID = null;
	private static final String EMULATOR_DEVICE_ID_KEY = "emulador.device.id";
	
	static {
		Context context = AndroidApplication.get();
		TELEPHONY_MANAGER = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
	}
	
	/**
	 * @return The device Id.
	 */
	// TODO This is not correct. If the device has not telephony, it will return null
	// Google recommends to use UUID.randomUUID() and store it on the shared preferences as an uniqueID
	public static String getDeviceID() {
		if (DEVICE_ID == null) {
			if (DeviceUtils.isRunOnEmulator()) {
				DEVICE_ID = DeviceUtils.getEmulatorDeviceID();
			} else {
				DEVICE_ID = TELEPHONY_MANAGER.getDeviceId();
			}
		}
		return DEVICE_ID;
	}
	
	/**
	 * @return true if run on emulator else false.
	 */
	public static boolean isRunOnEmulator() {
		String id = TELEPHONY_MANAGER.getDeviceId();
		if (id != null) {
			try {
				return Long.valueOf(id) == 0;
			} catch (NumberFormatException e) {
				// If the id is not a number, then the app is not running on the emulator
				return false;
			}
		} else {
			return true;
		}
	}
	
	/**
	 * @return
	 */
	private static String randomizeDeviceID() {
		Random random = new Random(Calendar.getInstance().getTimeInMillis());
		return String.valueOf(random.nextInt());
	}
	
	/**
	 * @return
	 */
	private static String getEmulatorDeviceID() {
		String emulatorID = SharedPreferencesUtils.loadPreference(EMULATOR_DEVICE_ID_KEY, (String)null);
		if (emulatorID == null) {
			emulatorID = randomizeDeviceID();
			SharedPreferencesUtils.savePreference(EMULATOR_DEVICE_ID_KEY, emulatorID);
		}
		return emulatorID;
	}
}
