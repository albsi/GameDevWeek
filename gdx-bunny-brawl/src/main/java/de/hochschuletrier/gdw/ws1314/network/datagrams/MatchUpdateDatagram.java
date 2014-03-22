package de.hochschuletrier.gdw.ws1314.network.datagrams;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagram;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageOut;
import de.hochschuletrier.gdw.ws1314.network.DatagramHandler;

public class MatchUpdateDatagram extends BaseDatagram{
	public static final byte MATCH_UPDATE_DATAGRAM = INetDatagram.Type.FIRST_CUSTOM + 0x11;
	private String map;

	public MatchUpdateDatagram(byte type, short id, short param1, short param2){
		super(MessageType.NORMAL, type, id, param1, param2);
	}

	public MatchUpdateDatagram(String map){
		super(MessageType.NORMAL, MATCH_UPDATE_DATAGRAM, (short) 0, (short) 0, (short) 0);
		this.map = map;
	}

	@Override
	public void handle(DatagramHandler handler, NetConnection connection){
		handler.handle(this, connection);
	}

	@Override
	public void writeToMessage(INetMessageOut message){
		message.putString(map);
	}

	@Override
	public void readFromMessage(INetMessageIn message){
		map = message.getString();
	}

	public String getMap(){
		return map;
	}
}
