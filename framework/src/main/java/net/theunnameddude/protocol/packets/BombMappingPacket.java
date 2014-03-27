package net.theunnameddude.protocol.packets;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.theunnameddude.api.Location;
import net.theunnameddude.handler.PacketHandler;
import net.theunnameddude.protocol.NetworkEngine;

import java.io.IOException;
import java.util.HashMap;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BombMappingPacket extends BasePacket {
    HashMap<Location, Integer> bombs;
    @Override
    public void read(NetworkEngine engine) throws IOException {
        bombs = new HashMap<>();
        String line;
        while ( !( line = engine.readLine() ).equals( "ENDBOMBS" ) ) {
            String[] meta = line.split( " |," );
            int x = Integer.parseInt( meta[ 0 ] );
            int y = Integer.parseInt( meta[ 1 ] );
            int ticksLeft = Integer.parseInt( meta[ 2 ] );
            bombs.put( new Location( x, y ), ticksLeft );
        }
    }

    @Override
    public void handle(PacketHandler handler) throws Exception {
        handler.handle( this );
    }
}
