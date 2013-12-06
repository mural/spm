package com.spm.android.common.map;

import java.util.List;
import android.graphics.drawable.Drawable;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.google.inject.internal.Lists;
import com.spm.android.common.AndroidApplication;

/**
 * 
 * @author Maxi Rosson
 * @param <T>
 */
public class ListBalloonItemizedOverlay<T extends OverlayItem> extends BalloonItemizedOverlay<T> {
	
	private BalloonOverlayTapListener<T> tapListener;
	private List<T> overlayItems = Lists.newArrayList();
	
	/**
	 * @param mapView
	 * @param tapListener
	 * @param defaultMarkerId The id of the {@link Drawable} to be used as marker
	 * @param overlayItems
	 */
	public ListBalloonItemizedOverlay(MapView mapView, BalloonOverlayTapListener<T> tapListener, int defaultMarkerId,
			List<T> overlayItems) {
		this(mapView, tapListener, defaultMarkerId);
		this.overlayItems = overlayItems;
		populate();
	}
	
	/**
	 * @param mapView
	 * @param tapListener
	 * @param defaultMarkerId The id of the {@link Drawable} to be used as marker
	 */
	public ListBalloonItemizedOverlay(MapView mapView, BalloonOverlayTapListener<T> tapListener, int defaultMarkerId) {
		this(mapView, tapListener, AndroidApplication.get().getResources().getDrawable(defaultMarkerId));
	}
	
	/**
	 * @param mapView
	 * @param tapListener
	 * @param defaultMarker The {@link Drawable} to be used as marker
	 */
	public ListBalloonItemizedOverlay(MapView mapView, BalloonOverlayTapListener<T> tapListener, Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker), mapView);
		this.tapListener = tapListener;
	}
	
	public void addItem(T overlayItem) {
		overlayItems.add(overlayItem);
		populate();
	}
	
	/**
	 * @see com.google.android.maps.ItemizedOverlay#createItem(int)
	 */
	@Override
	protected T createItem(int index) {
		return overlayItems.get(index);
	}
	
	/**
	 * @see com.google.android.maps.ItemizedOverlay#size()
	 */
	@Override
	public int size() {
		return overlayItems.size();
	}
	
	/**
	 * @see com.spm.android.common.map.BalloonItemizedOverlay#onBalloonTap(int)
	 */
	@Override
	protected void onBalloonTap(int index) {
		T item = overlayItems.get(index);
		tapListener.onBallonItemTap(item);
		super.onTap(index);
	}
}
