package com.spm.android.common.gps;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import com.spm.android.common.AndroidApplication;
import com.spm.android.common.utils.AlarmManagerUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class LocalizationManager implements LocationListener {
	
	private static final String TAG = LocalizationManager.class.getSimpleName();
	private static final String GPS_TIMEOUT_ACTION = "ACTION_GPS_TIMEOUT";
	private static final long GPS_TIMEOUT = 30000;
	
	private static final LocalizationManager INSTANCE = new LocalizationManager();
	private static final int LOCATION_MIN_TIME = 10000;
	private Location location;
	private LocationManager locationManager;
	private Boolean started = false;
	
	public static LocalizationManager get() {
		return INSTANCE;
	}
	
	public LocalizationManager() {
		
		// Acquire a reference to the system Location Manager
		locationManager = (LocationManager)AndroidApplication.get().getSystemService(Context.LOCATION_SERVICE);
	}
	
	/**
	 * Register the listener with the Location Manager to receive location updates
	 */
	public void startLocalization() {
		if (!started) {
			started = true;
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_MIN_TIME, 0, this);
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_MIN_TIME, 0, this);
			
			IntentFilter gpsIntentFilter = new IntentFilter(GPS_TIMEOUT_ACTION);
			
			Context context = AndroidApplication.get();
			context.registerReceiver(new BroadcastReceiver() {
				
				@Override
				public void onReceive(Context context, Intent intent) {
					stopLocalization();
				}
			}, gpsIntentFilter);
			
			Intent gpsIntent = new Intent(GPS_TIMEOUT_ACTION);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, gpsIntent,
				PendingIntent.FLAG_CANCEL_CURRENT);
			
			AlarmManagerUtils.scheduleAlarm(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + GPS_TIMEOUT,
				pendingIntent);
			
			Log.i(TAG, "Localization started");
		}
	}
	
	/**
	 * Remove the listener to receive location updates
	 */
	public void stopLocalization() {
		if (started) {
			locationManager.removeUpdates(this);
			if (location == null) {
				location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if (location == null) {
					location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				}
			}
			started = false;
			Log.i(TAG, "Localization stopped");
		}
	}
	
	/**
	 * @see android.location.LocationListener#onLocationChanged(android.location.Location)
	 */
	@Override
	public void onLocationChanged(Location location) {
		this.location = location;
	}
	
	/**
	 * @see android.location.LocationListener#onStatusChanged(java.lang.String, int, android.os.Bundle)
	 */
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// Do Nothing
	}
	
	/**
	 * @see android.location.LocationListener#onProviderEnabled(java.lang.String)
	 */
	@Override
	public void onProviderEnabled(String provider) {
		Log.i(TAG, "Provider enabled: " + provider);
		// Do Nothing
	}
	
	/**
	 * @see android.location.LocationListener#onProviderDisabled(java.lang.String)
	 */
	@Override
	public void onProviderDisabled(String provider) {
		Log.i(TAG, "Provider disabled: " + provider);
		// Do Nothing
	}
	
	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}
	
	public Boolean hasLocation() {
		return location != null;
	}
	
	public Double getLatitude() {
		return location != null ? location.getLatitude() : null;
	}
	
	public Double getLongitude() {
		return location != null ? location.getLongitude() : null;
	}
}
