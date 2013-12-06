package com.spm.android.common.map;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;

/**
 * 
 * @author Maxi Rosson
 */
public class MapUtils {
	
	public static GeoPoint createGeoPoint(Double latitude, Double longitude) {
		return new GeoPoint((int)(latitude * 1000000), (int)(longitude * 1000000));
	}
	
	public static void fitOvelays(MapView mapView, ItemizedOverlay<?> itemizedOverlay) {
		
		int nwLat = -90 * 1000000;
		int nwLng = 180 * 1000000;
		int seLat = 90 * 1000000;
		int seLng = -180 * 1000000;
		
		for (int i = 0; i < itemizedOverlay.size(); i++) {
			GeoPoint point = itemizedOverlay.getItem(i).getPoint();
			nwLat = Math.max(nwLat, point.getLatitudeE6());
			nwLng = Math.min(nwLng, point.getLongitudeE6());
			seLat = Math.min(seLat, point.getLatitudeE6());
			seLng = Math.max(seLng, point.getLongitudeE6());
		}
		
		GeoPoint center = new GeoPoint((nwLat + seLat) / 2, (nwLng + seLng) / 2);
		// add padding in each direction
		int spanLatDelta = (int)(Math.abs(nwLat - seLat) * 1.1);
		int spanLngDelta = (int)(Math.abs(seLng - nwLng) * 1.1);
		
		// fit map to points
		mapView.getController().animateTo(center);
		mapView.getController().zoomToSpan(spanLatDelta, spanLngDelta);
	}
}
