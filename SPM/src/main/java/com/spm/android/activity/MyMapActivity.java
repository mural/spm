package com.spm.android.activity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.spm.R;
import com.spm.android.common.utils.ToastUtils;

/**
 * 
 * @author agustinsgarlata
 */
public class MyMapActivity extends MapActivity {
	
	// Acquire a reference to the system Location Manager
	LocationManager locationManager;
	
	// Define a listener that responds to location updates
	LocationListener gpsLocationListener;
	LocationListener networkLocationListener;
	
	private MapView mapView;
	private MapController mapController;
	
	/**
	 * @see com.google.android.maps.MapActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		setContentView(R.layout.my_map_activity);
		
		mapView = (MapView)findViewById(R.id.mapView);
		// enable Street view by default ?
		mapView.setStreetView(true);
		mapController = mapView.getController();
		mapController.setZoom(16);
		
		locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		gpsLocationListener = new LocationListener() {
			
			@Override
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the gps location provider.
				if (location != null) {
					GeoPoint point = new GeoPoint((int)(location.getLatitude() * 1E6),
							(int)(location.getLongitude() * 1E6));
					
					String address = ConvertPointToLocation(point);
					
					TextView locacion = (TextView)findViewById(R.id.location);
					locacion.setText(address.substring(0, 15) + "(" + location.getProvider() + ")" + "\nLat.: "
							+ new Double(location.getLatitude()).toString().substring(0, 5) + " Long.: "
							+ new Double(location.getLongitude()).toString().substring(0, 5));
					
					mapController.animateTo(point);
					mapController.setZoom(16);
					mapView.invalidate();
					
					// add marker
					MapOverlay mapOverlay = new MapOverlay();
					mapOverlay.setPointToDraw(point);
					List<Overlay> listOfOverlays = mapView.getOverlays();
					listOfOverlays.clear();
					listOfOverlays.add(mapOverlay);
				}
			}
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				ToastUtils.showToast("GPS: " + status);
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				ToastUtils.showToast("GPS ON");
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				ToastUtils.showToast("GPS OFF");
			}
			
		};
		networkLocationListener = new LocationListener() {
			
			@Override
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the network location provider.
				if (location != null) {
					GeoPoint point = new GeoPoint((int)(location.getLatitude() * 1E6),
							(int)(location.getLongitude() * 1E6));
					
					String address = ConvertPointToLocation(point);
					
					TextView locacion = (TextView)findViewById(R.id.location);
					locacion.setText(address + "(" + location.getProvider() + ")" + "\nLat.: "
							+ new Double(location.getLatitude()).toString().substring(0, 5) + " Long.: "
							+ new Double(location.getLongitude()).toString().substring(0, 5));
					
					mapController.animateTo(point);
					mapController.setZoom(16);
					mapView.invalidate();
					
					// add marker
					MapOverlay mapOverlay = new MapOverlay();
					mapOverlay.setPointToDraw(point);
					List<Overlay> listOfOverlays = mapView.getOverlays();
					listOfOverlays.clear();
					listOfOverlays.add(mapOverlay);
				}
			}
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				ToastUtils.showToast("NETWORK: " + status);
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				ToastUtils.showToast("NETWORK ON");
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				ToastUtils.showToast("NETWORK OFF");
			}
			
		};
		
		// Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, gpsLocationListener);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, networkLocationListener);
		
	}
	
	/**
	 * @see com.google.android.maps.MapActivity#isRouteDisplayed()
	 */
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	/**
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
		locationManager.removeUpdates(gpsLocationListener);
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
	
	private static final int TWO_MINUTES = 1000 * 60 * 2;
	
	/**
	 * Determines whether one Location reading is better than the current Location fix
	 * 
	 * @param location The new Location that you want to evaluate
	 * @param currentBestLocation The current Location fix, to which you want to compare the new one
	 */
	protected boolean isBetterLocation(Location location, Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}
		
		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
		boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
		boolean isNewer = timeDelta > 0;
		
		// If it's been more than two minutes since the current location, use the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must be worse
		} else if (isSignificantlyOlder) {
			return false;
		}
		
		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int)(location.getAccuracy() - currentBestLocation.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;
		
		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());
		
		// Determine location quality using a combination of timeliness and accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
			return true;
		}
		return false;
	}
	
	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}
	
	class MapOverlay extends Overlay {
		
		private GeoPoint pointToDraw;
		
		public void setPointToDraw(GeoPoint point) {
			pointToDraw = point;
		}
		
		public GeoPoint getPointToDraw() {
			return pointToDraw;
		}
		
		@Override
		public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
			super.draw(canvas, mapView, shadow);
			
			// convert point to pixels
			Point screenPts = new Point();
			mapView.getProjection().toPixels(pointToDraw, screenPts);
			
			// add marker
			Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.map_marker);
			canvas.drawBitmap(bmp, screenPts.x, screenPts.y - 24, null);
			return true;
		}
	}
}
