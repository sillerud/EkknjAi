package net.theunnameddude.protocol;

import net.theunnameddude.protocol.packets.*;

import java.util.HashMap;

public class Protocol {
    private final static HashMap<String, Class<? extends BasePacket>> packets = new HashMap<>();

    static {
        packets.put( "MAP", MapPacket.class );
        packets.put( "BOMBS", BombMappingPacket.class );
        packets.put( "PLAYERS", PlayerListPacket.class );
    }

    public static BasePacket getPacket(String packetId) {
        try {
            if ( !packets.containsKey( packetId ) )
                return null;
            return packets.get( packetId ).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
