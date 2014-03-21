package de.hochschuletrier.gdw.ws1314.network;

import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.NetReception;
import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagram;
import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagramFactory;
import de.hochschuletrier.gdw.commons.netcode.datagram.NetDatagramDistributor;
import de.hochschuletrier.gdw.commons.utils.StringUtils;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.input.PlayerIntention;
import de.hochschuletrier.gdw.ws1314.network.datagrams.ChatDeliverDatagram;
import de.hochschuletrier.gdw.ws1314.network.datagrams.ChatSendDatagram;
import de.hochschuletrier.gdw.ws1314.network.datagrams.LobbyUpdateDatagram;
import de.hochschuletrier.gdw.ws1314.network.datagrams.MatchUpdateDatagram;
import de.hochschuletrier.gdw.ws1314.network.datagrams.PlayerUpdateDatagram;
import de.hochschuletrier.gdw.ws1314.network.datagrams.LobbyUpdateDatagram.PlayerData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NetworkManager {

    private static final Logger logger = LoggerFactory.getLogger(NetworkManager.class);
	private static NetworkManager instance = new NetworkManager();

	private NetConnection clientConnection=null;
	private ArrayList<NetConnection> serverConnections=null;
	private NetReception serverReception=null;
	private INetDatagramFactory datagramFactory=new DatagramFactory();
	
    private NetDatagramDistributor serverGameDgramDistributor = new NetDatagramDistributor(new ServerGameDatagramHandler());
	private NetDatagramDistributor serverLobbyDgramDistributor = new NetDatagramDistributor(new ServerLobbyDatagramHandler());
	private NetDatagramDistributor clientGameDgramDistributor = new NetDatagramDistributor(new ClientGameDatagramHandler());
	private NetDatagramDistributor clientLobbyDgramDistributor = new NetDatagramDistributor(new ClientLobbyDatagramHandler());
	private ArrayList<ChatListener> chatListeners = new ArrayList<ChatListener>();
    
	private int nextPlayerNumber = 1;

	private NetworkManager(){}
	public static NetworkManager getInstance(){
		return instance;
	}
	
	public void connect(String ip, int port){
		if(isClient()){
			logger.warn("Ignoring new connect command because we are already connected.");
		}
		try {
			clientConnection=new NetConnection(ip, port, datagramFactory);
			if(clientConnection.isAccepted()) logger.info("Connected.");
		} catch (IOException e) {
			logger.error("Can't connect.",e);
		}
	}
	
	public void listen(String ip, int port, int maxConnections){
		if(isServer()){
			logger.warn("Ignoring new listen command because we are already a server.");
		}
		serverConnections=new ArrayList<NetConnection>();
		try {
			serverReception = new NetReception(ip, port, maxConnections, datagramFactory);
			if(serverReception.isRunning()) logger.info("Listening.");
		} catch (IOException e) {
			logger.error("Can't listen for connections.", e);
			serverConnections=null;
			serverReception=null;
		}
	}
	
	public boolean isServer() {
		return serverConnections!=null && serverReception!=null && serverReception.isRunning();
	}

	public boolean isClient() {
		return clientConnection!=null && clientConnection.isConnected();
	}

	public void sendEntityEvent(long id, int eventPlayerIntention){
		//TODO: Implement
	}
	
	public void sendAction(PlayerIntention eventPlayerIntention){
		//TODO: Implement
	}

	public void despawnEntity(long id){
		//TODO: Implement
	}
	
	public void sendMatchUpdate(String map){
		if(!isClient())
			return;
		clientConnection.send(new MatchUpdateDatagram(map));
	}
	
	public void sendPlayerUpdate(String playerName, EntityType type, byte team, boolean accept){
		if(!isClient())
			return;
		clientConnection.send(new PlayerUpdateDatagram(playerName, type, team, accept));
	}
	
	public void sendLobbyUpdate(String map, PlayerData[] players){
		if(!isServer())
			return;
		clientConnection.send(new LobbyUpdateDatagram(map, players));
	}
	
	public void sendChat(String text){
		if(isClient()){
			clientConnection.send(new ChatSendDatagram(text));
		}
		else if (isServer()){
			broadcastToClients(new ChatDeliverDatagram("SERVER",text));
			receiveChat("SERVER", text);
		}
		else {
			logger.error("Can't send chat message, when not connected.");
		}
	}
	
	public void addChatListener(ChatListener listener){
		chatListeners.add(listener);
	}
	
	public void removeChatListener(ChatListener listener){
		chatListeners.remove(listener);
	}
	
	/**
	 * Wird von der Verarbeitungslogik für Chat-Datagramme verwendet, um Chat-Nachrichten an den Listener zuzustellen.
	 * Aufruf von anderer Stelle ist eher nicht sinnvoll. 
	 * @param sender
	 * @param text
	 */
	void receiveChat(String sender,String text){
		for(ChatListener l : chatListeners){
			l.chatMessage(sender,text);
		}
	}
	
	/**
	 * Wird innerhalb der server-seitigen NEtzwerklogik verwendet, um Pakete an alle Clients zu schicken.
	 * @param dgram
	 */
	void broadcastToClients(INetDatagram dgram){
		if(!isServer()){
			logger.warn("Request to broadast datagram to clients will be ignored because of non-server context.");
			return;
		}
		for(NetConnection con : serverConnections){
			con.send(dgram);
		}
	}
	
	public void disconnectFromServer(){
		if(isClient()){
			clientConnection.shutdown();
			clientConnection=null;
		}
	}
	
	public void init(){
		Main.getInstance().console.register(connectCmd);
		Main.getInstance().console.register(listenCmd);
		Main.getInstance().console.register(sayCmd);
		addChatListener(new ConsoleChatListener());
	}
	
	public void update(){
		handleNewConnections();
		handleDatagramsClient();
		handleDatagramsServer();
	}
	
	private void handleNewConnections(){
		if(isServer()) {
			NetConnection connection = serverReception.getNextNewConnection();
			while (connection != null) {
				connection.setAccepted(true);
				connection.setAttachment("Player "+(nextPlayerNumber++));
				serverConnections.add(connection);
				logger.info("Client connected.");
				connection = serverReception.getNextNewConnection();
			}
		}
	}
	
	private void handleDatagrams(NetConnection connection, NetDatagramDistributor distributor){
		connection.sendPendingDatagrams();
		while(connection.hasIncoming()){
			INetDatagram dgram = connection.receive();
            try {
                distributor.handle(dgram, connection);
            } catch (InvocationTargetException e) {
                logger.error("Exception thrown by handle method", e);
            }
		}
	}
	
	private void handleDatagramsClient(){
		if(!isClient()) return;
        
        handleDatagrams(clientConnection, clientGameDgramDistributor);
	}
	
	private void handleDatagramsServer(){
		if(!isServer()) return;
		
		Iterator<NetConnection> it = serverConnections.iterator();
		while (it.hasNext()) {
			NetConnection connection = it.next();
            handleDatagrams(connection, serverGameDgramDistributor);
			
			if (!connection.isConnected()) {
				logger.info("Client disconnected.");
				it.remove();
				continue;
			}
		}
	}
	private ConsoleCmd connectCmd = new ConsoleCmd("connect",0,"Connect to a server.",2) {
		
		@Override
		public void showUsage() {
			showUsage("<ip> <port>");
		}

		@Override
		public void execute(List<String> args) {
			try {
				String ip = args.get(1);
				int port = Integer.parseInt(args.get(2));
				connect(ip, port);
			} catch (NumberFormatException e) {
				showUsage();
			}
		}
	};
	private ConsoleCmd listenCmd = new ConsoleCmd("listen",0,"Start listening for client connections. (Become a server.)",2) {
		
		@Override
		public void showUsage() {
			showUsage("<interface-ip> <port> [max-connections = 10]");
		}

		@Override
		public void execute(List<String> args) {
			try {
				String ip = args.get(1);
				int port = Integer.parseInt(args.get(2));
				int maxConnections = 10;
				if(args.size()>3){
					maxConnections=Integer.parseInt(args.get(3));
				}
				listen(ip, port, maxConnections);
			} catch (NumberFormatException e) {
				showUsage();
			}
		}
	};
	private ConsoleCmd sayCmd = new ConsoleCmd("say",0,"Post a message in chat.",1) {
		@Override
		public void showUsage() {
			showUsage("<message-text>");
		}


        @Override
        public void execute(List<String> args) {
            sendChat(StringUtils.untokenize(args, 1, -1, false));
        }
    };
    public void setPlayerEntityId(int playerId, long entityId){
    	//TODO Implement
    }
}
