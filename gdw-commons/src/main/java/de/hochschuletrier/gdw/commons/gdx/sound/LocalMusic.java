package de.hochschuletrier.gdw.commons.gdx.sound;

import com.badlogic.gdx.audio.*;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;

/**
 * Class for handling the music in GameplayState
 * should be initialized when needed
 * 
 * @author MikO
 */
public class LocalMusic {
	private AssetManagerX assetManager;
	private Music musicHandle;
	
	private static float SystemVolume = 0.9f;
	
	/**
	 * Change the general volume for music
	 * The volume of all music will be a percentage of this systemVolume
	 * 
	 * @param systemVolume
	 */
	public static void setSystemVolume(float systemVolume) {
		LocalMusic.SystemVolume = systemVolume;
	}
	
	/**
	 * Get the current set general volume for music
	 * @return
	 */
	public static float getSystemVolume() {
		return LocalMusic.SystemVolume;
	}

	/**
	* Constructor of the class LocalMusic
	* 	
	* @param assetManager
	*/
	public LocalMusic(AssetManagerX assetManager) {
		this.assetManager = assetManager;
		this.musicHandle = null;
	}
	
	public void update() {
		
	}
	
	/**
	 * Plays the music with the given title
	 * 
	 * @param title
	 */
	public void play(String title) {
		this.musicHandle = this.assetManager.getMusic(title);
		this.musicHandle.play();
		this.musicHandle.setVolume(LocalMusic.SystemVolume);
		this.musicHandle.setLooping(true);
	}
	
	/**
	 * Stops actual playing music
	 */
	public void stop() {
		this.musicHandle.stop();
	}
	
	/**
	 * pauses actual playing music
	 */
	public void pause() {
		this.musicHandle.pause();
	}
	
	/**
	 * Sets the volume of the actual playing track to zero
	 */
	public void mute() {
		this.musicHandle.setVolume(0.0f);
	}
	
	/**
	 * Turns volume of the actual playing track back on
	 */
	public void deMute() {
		this.musicHandle.setVolume(LocalMusic.SystemVolume);
	}
	
	/**
	 * returns true if music ist playing and false if not
	 * 
	 * @return isMusicPlaying
	 */
	public boolean isMusicPlaying() {
		if (this.musicHandle != null)
			return this.musicHandle.isPlaying();
		else 
			return false;
	}
}