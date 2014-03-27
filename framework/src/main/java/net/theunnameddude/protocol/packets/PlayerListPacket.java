package net.theunnameddude.protocol.packets;

import lombok.Getter;
import net.theunnameddude.api.Location;
import net.theunnameddude.handler.PacketHandler;
import net.theunnameddude.protocol.NetworkEngine;

import java.io.IOException;
import java.util.HashMap;

@Getter
public class PlayerListPacket extends BasePacket {
    HashMap<Integer, Location> enemies = new HashMap<>();
    @Override
    public void read(NetworkEngine engine) throws IOException {
        String line;
        while ( !( line = engine.readLine() ).equals( "ENDPLAYERS" ) ) {
            String[] metadata = line.split( " |," );
            int id = Integer.parseInt( metadata[ 0 ] );
            int x = Integer.parseInt( metadata[ 1 ] );
            int y = Integer.parseInt( metadata[ 2 ] );
            enemies.put( id, new Location( x, y ) );
        }
    }

    @Override
    public void handle(PacketHandler handler) throws Exception {
        handler.handle( this );
    }
}
