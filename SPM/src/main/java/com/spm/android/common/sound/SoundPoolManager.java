package com.spm.android.common.sound;

import java.util.Map;
import android.media.AudioManager;
import android.media.SoundPool;
import com.google.inject.internal.Maps;

/**
 * 
 * @author Maria Ines Casadei
 */
public class SoundPoolManager {
	
	// Currently has no effect. Use a value of 1 for future compatibility.
	private static final int SOUND_PRIORITY = 1;
	
	// Use 0 for mode no loop.
	private static final int SOUND_LOOP = 0;
	
	// Currently has no effect. Use 0 for the default.
	private static final int SOUND_QUALITY = 0;
	
	// The maximum number of simultaneous streams for this SoundPool object.
	private static final int MAX_STREAMS = 1;
	
	private SoundPool soundPool;
	private Map<Integer, Integer> soundPoolMap = Maps.newHashMap();
	
	private static final SoundPoolManager INSTANCE = new SoundPoolManager();
	
	/**
	 * @return the singleton instance
	 */
	public static SoundPoolManager getInstance() {
		return INSTANCE;
	}
	
	private SoundPoolManager() {
	}
	
	public void resume() {
		soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_NOTIFICATION, SOUND_QUALITY);
		
		// TODO Add sounds here
		// soundPoolMap.put(SOUND_WON, soundPool.load(AndroidApplication.get(), R.raw.won, SOUND_PRIORITY));
	}
	
	public void playSound(int sound) {
		// In case the sounds have not been loaded yet.
		if ((soundPool != null) && (soundPoolMap.get(sound) != null)) {
			soundPool.play(soundPoolMap.get(sound), AudioManager.STREAM_NOTIFICATION, AudioManager.STREAM_NOTIFICATION,
				SOUND_PRIORITY, SOUND_LOOP, 1f);
		}
	}
	
	public void release() {
		if (soundPool != null) {
			soundPool.release();
			soundPool = null;
			soundPoolMap.clear();
		}
	}
}