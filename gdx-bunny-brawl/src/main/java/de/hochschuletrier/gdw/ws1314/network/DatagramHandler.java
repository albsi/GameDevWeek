package de.hochschuletrier.gdw.ws1314.network;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagramHandler;
import de.hochschuletrier.gdw.ws1314.network.datagrams.*;

//public interface DatagramHandler {
public interface DatagramHandler extends INetDatagramHandler {

	void handle(ChatSendDatagram chatDatagram, NetConnection connection);

	void handle(ChatDeliverDatagram chatDeliverDatagram, NetConnection connection);

    void handle (PlayerReplicationDatagram playerReplicationDatagram, NetConnection connection);

    void handle (PlayerUpdateDatagram playerUpdateDatagram, NetConnection connection);

    void handle (LevelObjectReplicationDatagram levelObjectReplicationDatagram, NetConnection connection);

    void handle (ProjectileReplicationDatagram projectileReplicationDatagram, NetConnection connection);

    void handle (ActionDatagram actionDatagram, NetConnection connection);

    void handle (EventDatagram eventDatagram, NetConnection connection);

    void handle (LobbyUpdateDatagram lobbyUpdateDatagram, NetConnection connection);

    void handle (MatchUpdateDatagram matchUpdateDatagram, NetConnection connection);

    void handle (DespawnDatagram despawnDatagram, NetConnection connection);
}
