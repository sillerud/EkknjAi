package net.theunnameddude.protocol;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.theunnameddude.Bot;
import net.theunnameddude.handler.PacketHandler;
import net.theunnameddude.protocol.packets.BasePacket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

@RequiredArgsConstructor
public class NetworkEngine implements Runnable {
    final Bot bot;
    final Socket socket;
    @Getter
    @Setter
    @NonNull
    PacketHandler handler;
    BufferedReader reader;
    PrintStream writer;

    @Override
    public void run() {
        try {
            reader = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
            writer = new PrintStream( socket.getOutputStream() );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        bot.connected( this );
        while ( bot.isRunning() ) {
            String packetId = readLine();
            BasePacket packet = Protocol.getPacket( packetId );
            if ( packet == null ) {
                // Might be metadata
                if ( packetId.startsWith( "X" ) ) {
                    bot.setX( Integer.parseInt( packetId.split( " " )[ 1 ] ) );
                } else if ( packetId.startsWith( "Y" ) ) {
                    bot.setY( Integer.parseInt( packetId.split( " " )[ 1 ] ) );
                    // Lets ignore the rest if its not in debug mode, width and height of map is not needed as the
                    // parser takes care of this
                } else {
                    if ( bot.isDebug() ) {
                        if ( !packetId.startsWith( "WIDTH" ) && !packetId.startsWith( "HEIGHT" ) ) {
                            bot.getLogger().warning("Unknown packet " + packetId);
                        }
                    }
                }
            } else {
                try {
                    packet.read( this );
                    if ( getHandler() != null ) {
                        packet.handle( getHandler() );
                    } else {
                        bot.getLogger().warning( "Missing handler" );
                    }
                } catch ( Exception e ) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String readLine() {
        try {
            String line = reader.readLine();
            if ( bot.isLoggingProtocol() ) {
                bot.getLogger().info("Server> " + line);
            }
            return line;
        } catch ( IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void writeLine(String line) {
        writer.println( line );
    }
}
