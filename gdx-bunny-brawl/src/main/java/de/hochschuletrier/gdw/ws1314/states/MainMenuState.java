package de.hochschuletrier.gdw.ws1314.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.hud.MainMenuStage;
import de.hochschuletrier.gdw.ws1314.network.NetworkManager;
import de.hochschuletrier.gdw.ws1314.sound.LocalMusic;

/**
 * Menu state
 *
 * @author Santo Pfingsten
 */
public class MainMenuState extends GameState {
    private LocalMusic music;
	private int stateChangeDuration=500;
	private MainMenuStage stage;
	
	private Logger logger;
	private OptionListener optionListener;
	private ExitListener exitlistener;
	private PlayServerListener playServerListener;
	private GameBrowserListener gameBrowserListener;
	
	//test
	private StartServerAndPlayListener startServerAndPlayListener;

    public MainMenuState() {
    }

    @Override
    public void init(AssetManagerX assetManager) {
        super.init(assetManager);

		logger = LoggerFactory.getLogger(MainMenuState.class);
		this.music = Main.musicManager.getMusicStreamByStateName(GameStates.MAINMENU);
		
        stage = new MainMenuStage();
        stage.init(assetManager);
		stage.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		this.optionListener = new OptionListener();
		this.exitlistener = new ExitListener();
		this.playServerListener = new PlayServerListener();
		this.gameBrowserListener = new GameBrowserListener();
        startServerAndPlayListener = new StartServerAndPlayListener();
	}
	
    public void render() {
		stage.render();
	}

	float stateTime = 0f; 

	public void update(float delta) {
		stateTime += delta;
		music.update();
		Main.musicManager.getMusicStreamByStateName(GameStates.DUALGAMEPLAY).update();
    }

    public void onEnter() {
        if (this.music.isMusicPlaying()) {
			this.music.setFade('i', 2500);
        }
		else{
        	this.music.play("music-lobby-loop");
        }

		stage.init(assetManager);
	    Main.inputMultiplexer.addProcessor(stage);
		
		stage.getOptionsButton().addListener(this.optionListener);
		stage.getExitButton().addListener(this.exitlistener);
		stage.getPlayServerButton().addListener(this.playServerListener);
		stage.getGameBrowserButton().addListener(this.gameBrowserListener);
		stage.getStartServerAndPlayButton().addListener(startServerAndPlayListener);
	}

    public void onLeave() {
    	if (this.music.isMusicPlaying()) {
    		this.music.setFade('o', 2500);
    	}

    	stage.getStartServerAndPlayButton().removeListener(startServerAndPlayListener);
    	stage.getGameBrowserButton().removeListener(this.gameBrowserListener);
		stage.getPlayServerButton().removeListener(this.playServerListener);
		stage.getOptionsButton().removeListener(this.optionListener);
		stage.getExitButton().removeListener(this.exitlistener);
		Main.inputMultiplexer.removeProcessor(stage);
		stage.clear();
	}

	public void onLeaveComplete() {
	}

	public void dispose() {
		if (this.stage != null)
			stage.dispose();
	}

	public boolean keyDown(int keycode) {
		return false;
	}

	public boolean keyUp(int keycode) {
		return false;
	}

	public boolean keyTyped(char character) {
		return false;
	}

	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	public boolean scrolled(int amount) {
		return false;
	}
	
	//private listener 
	private class GameBrowserListener extends ClickListener {
		public void clicked(InputEvent event, float x, float y) {
			logger.info("Change to GameBrowserState");
			
			GameStates.CLIENTGAMEBROWSER.init(assetManager);
			GameStates.CLIENTGAMEBROWSER.activate();
		}
	}
	
	private class PlayServerListener extends ClickListener {
		public void clicked(InputEvent event, float x, float y) {
			logger.info("Change to StartServerState");
			GameStates.STARTSERVER.init(assetManager);
			GameStates.STARTSERVER.activate();
		}
	}
	
	private class OptionListener extends ClickListener {
		public void clicked(InputEvent event, float x, float y) {
			logger.info("Change to OptionState");
			GameStates.OPTIONS.init(assetManager);
			GameStates.OPTIONS.activate();
		}
	}
	
	private class CreditsListener extends ClickListener {
		public void clicked(InputEvent event, float x, float y) {
			logger.info("Change to CreditsState");
			GameStates.CREDITS.init(assetManager);
			GameStates.CREDITS.activate();
		}
	}
	
	private class ExitListener extends ClickListener {
		public void clicked(InputEvent event, float x, float y) {
			Gdx.app.exit();
		}
	}
	
	//test	
	private class StartServerAndPlayListener extends ClickListener {
    	public void clicked(InputEvent event, float x, float y) {
    		logger.warn("Changing to DualGameplayState - to be removed");
			GameStates.DUALGAMEPLAY.init(assetManager);
			GameStates.DUALGAMEPLAY.activate();
    	}
    }
}
