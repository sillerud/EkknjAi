package net.theunnameddude.protocol.packets;

import lombok.Getter;
import net.theunnameddude.handler.PacketHandler;
import net.theunnameddude.protocol.NetworkEngine;

import java.io.IOException;
import java.util.ArrayList;

@Getter
public class MapPacket extends BasePacket {
    String[] content;

    @Override
    public void read(NetworkEngine engine) throws IOException {
        ArrayList<String> content = new ArrayList<>();
        String line;
        while ( !( line = engine.readLine() ).equals( "ENDMAP" ) ) {
            content.add( line );
        }
        this.content = content.toArray( new String[ content.size() ] );
    }

    @Override
    public void handle(PacketHandler handler) throws Exception {
        handler.handle( this );
    }
}
