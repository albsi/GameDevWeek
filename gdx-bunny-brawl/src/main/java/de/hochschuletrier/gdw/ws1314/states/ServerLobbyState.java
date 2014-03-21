package de.hochschuletrier.gdw.ws1314.states;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.ws1314.hud.ServerLobbyStage;
import de.hochschuletrier.gdw.ws1314.lobby.IServerLobbyListener;
import de.hochschuletrier.gdw.ws1314.lobby.ServerLobbyManager;
import de.hochschuletrier.gdw.ws1314.network.NetworkManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;

public class ServerLobbyState extends GameState implements IServerLobbyListener {
	private static final Logger logger = LoggerFactory.getLogger(ServerLobbyState.class);
	
	protected ServerLobbyManager serverLobby;
	
	private ServerLobbyStage stage;
	
    @Override
    public void init (AssetManagerX assetManager) {
        super.init (assetManager);
       
        
        // TODO: Temporär nur zum localen Testen
        if (!NetworkManager.getInstance().isServer())
        {
        	NetworkManager.getInstance().listen(NetworkManager.getInstance().getDefaultServerIp(), NetworkManager.getInstance().getDefaultPort(), 10);
        }
        
    	serverLobby = new ServerLobbyManager();
    	serverLobby.addServerLobbyListener(this);
    	logger.info("Server-Lobby created.");
    	
    	this.stage = new ServerLobbyStage();
    	this.stage.init(assetManager);

    	stage.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render () {
    	this.stage.render();
    }

    @Override
    public void update (float delta) {
        // TODO
    }

    @Override
    public void dispose () {
        // TODO
    }

	@Override
	public void startGame() {
		((ServerGamePlayState) GameStates.SERVERGAMEPLAY.get()).setPlayerDatas(this.serverLobby.getPlayers());
		GameStates.SERVERGAMEPLAY.init(assetManager);
		GameStates.SERVERGAMEPLAY.activate();
		logger.info("Sending GameStateChange to Clients");
		NetworkManager.getInstance().sendGameState(GameStates.CLIENTGAMEPLAY);
	}
}