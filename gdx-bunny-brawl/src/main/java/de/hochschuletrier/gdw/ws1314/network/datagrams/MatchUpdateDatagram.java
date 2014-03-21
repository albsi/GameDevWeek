package de.hochschuletrier.gdw.ws1314.network.datagrams;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagram;
import de.hochschuletrier.gdw.commons.netcode.datagram.NetDatagram;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageOut;
import de.hochschuletrier.gdw.ws1314.network.DatagramHandler;

/**
 * Created by albsi on 17.03.14.
 */
public class MatchUpdateDatagram extends NetDatagram {
    public static final byte MATCH_UPDATE_DATAGRAM = INetDatagram.Type.FIRST_CUSTOM + 0x11;
    private String map;

    public MatchUpdateDatagram (byte type, short id, short param1, short param2) {
        super (MessageType.DELTA, type, id, param1, param2);
    }

    public MatchUpdateDatagram (String map) {
        super (MessageType.DELTA, MATCH_UPDATE_DATAGRAM, (short) 0, (short) 0, (short) 0);
        this.map = map;
    }

    @Override
    public void writeToMessage (INetMessageOut message) {
        message.putString (map);
    }

    @Override
    public void readFromMessage (INetMessageIn message) {
        map = message.getString ();
    }

    public String getMap () {
        return map;
    }
}
