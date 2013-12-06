package com.spm.android.common.map;

import com.google.android.maps.OverlayItem;

/**
 * 
 * @author Maxi Rosson
 * @param <T>
 */
public interface BalloonOverlayTapListener<T extends OverlayItem> {
	
	public void onBallonItemTap(T overlayItem);
	
}
