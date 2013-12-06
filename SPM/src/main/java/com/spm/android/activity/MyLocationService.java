package com.spm.android.activity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import com.google.android.maps.GeoPoint;
import com.spm.common.utils.ThreadUtils;
import com.spm.domain.Order;
import com.spm.domain.Visit;
import com.spm.repository.DBOrderRepository;
import com.spm.repository.DBVisitRepository;
import com.spm.repository.OrderRepository;
import com.spm.repository.VisitRepository;

/**
 * 
 * @author agustin.sgarlata
 */
public class MyLocationService extends IntentService {
	
	/**
	 * @param name
	 */
	public MyLocationService(String name) {
		super(name);
	}
	
	public MyLocationService() {
		super("MyLocationService");
	}
	
	// Acquire a reference to the system Location Manager
	LocationManager locationManager;
	
	// Define a listener that responds to location updates
	LocationListener gpsLocationListener;
	LocationListener networkLocationListener;
	
	String coordinates = "";
	String address = "";
	int maxAttemps = 3;
	int attemps = 0;
	int time = 0;
	int maxTime = 5;
	
	/**
	 * @see android.app.IntentService#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("GPS for SPM", "--- SERVICIO ON ! ---");
	}
	
	/**
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d("GPS for SPM", "--- ON HANLDE ! ---");
		locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		
		locationToString(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
		if (coordinates.length() == 0) {
			time = 10;
		}
		
		networkLocationListener = new LocationListener() {
			
			@Override
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the network location provider.
				locationToString(location);
			}
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// ToastUtils.showToast("NETWORK: " + status);
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				// ToastUtils.showToast("NETWORK ON");
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				// ToastUtils.showToast("NETWORK OFF");
			}
			
		};
		
		// Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, networkLocationListener);
		
		ThreadUtils.sleep(1);
		while ((coordinates.length() == 0) && (attemps < maxAttemps) && (time < maxTime)) {
			ThreadUtils.sleep(1);
			time++;
		}
		if (coordinates.length() == 0) {
			coordinates = "No disponible";
		}
		
		try {
			OrderRepository orderRepository = new DBOrderRepository(getApplicationContext());
			Order order = orderRepository.get(intent.getLongExtra("ID", 0));
			order.modifyPosition(coordinates, address);
			orderRepository.add(order);
		} catch (Exception e) {
			Log.e(MyLocationService.class.getCanonicalName(), "Not an order");
		}
		
		try {
			VisitRepository visitRepository = new DBVisitRepository(getApplicationContext());
			Visit visit = visitRepository.get(intent.getLongExtra("ID", 0));
			visit.modifyPosition(coordinates, address);
			visitRepository.add(visit);
		} catch (Exception e) {
			Log.e(MyLocationService.class.getCanonicalName(), "Not a visit");
		}
		
		Log.d("GPS for SPM", "--- OFF handle ! ---");
	}
	
	/**
	 * @see android.app.IntentService#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		locationManager.removeUpdates(networkLocationListener);
	}
	
	public String ConvertPointToLocation(GeoPoint point) {
		String address = "";
		Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
		try {
			List<Address> addresses = geoCoder.getFromLocation(point.getLatitudeE6() / 1E6,
				point.getLongitudeE6() / 1E6, 1);
			
			if (addresses.size() > 0) {
				for (int index = 0; index < addresses.get(0).getMaxAddressLineIndex(); index++) {
					address += addresses.get(0).getAddressLine(index) + " ";
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return address;
	}
	
	/**
	 * @param location
	 */
	private void locationToString(Location location) {
		if (location != null) {
			GeoPoint point = new GeoPoint((int)(location.getLatitude() * 1E6), (int)(location.getLongitude() * 1E6));
			
			address = ConvertPointToLocation(point);
			
			coordinates = "Lat: " + new Double(location.getLatitude()).toString().substring(0, 8) + " Long: "
					+ new Double(location.getLongitude()).toString().substring(0, 8);
			
			attemps++;
		}
	}
}
