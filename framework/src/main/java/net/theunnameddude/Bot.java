package net.theunnameddude;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.theunnameddude.api.EnemyBot;
import net.theunnameddude.api.GameMap;
import net.theunnameddude.api.Location;
import net.theunnameddude.handler.PacketHandler;
import net.theunnameddude.protocol.NetworkEngine;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

@RequiredArgsConstructor
@Getter
@Setter
public class Bot {
    final String host;
    final int port;
    final String name;
    @Setter(AccessLevel.NONE)
    boolean running = true;
    final boolean debug;
    final boolean loggingProtocol;
    private int x;
    private int y;
    NetworkEngine engine;
    public Logger logger = initLogger();
    private GameMap map;
    HashMap<Integer, EnemyBot> bots = new HashMap<>();

    /**
     * Meeh, could done this better.
     * @return A logger
     */
    private Logger initLogger() {
        Logger logger = Logger.getLogger( getClass().getName() );
        logger.setUseParentHandlers( false );
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter( new Formatter() {
            @Override
            public String format(LogRecord record) {
                return "[" + record.getLevel() + "] " + record.getMessage() + "\n";
            }
        } );
        logger.addHandler( handler );
        return logger;
    }

    public void connect() {
        try {
            new Thread( new NetworkEngine( this, new Socket( host, port), new PacketHandler( this ) {}  ) ).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connected(NetworkEngine engine) {
        this.engine = engine;
        writeLine( "NAME " + name );
        getLogger().info( "Bot is initialized and connecting, waiting for server..." );

    }

    public void writeLine(String str) {
        engine.writeLine( str );
    }

    public ArrayList<EnemyBot> updatePlayers(HashMap<Integer, Location> enemies) {
        ArrayList<EnemyBot> updated = new ArrayList<>();
        for ( Map.Entry<Integer, EnemyBot> entry : new ArrayList<>( getBots().entrySet() ) ) { // Pls no concurrent
            if ( enemies.containsKey( entry.getKey() ) ) {
                Location location = enemies.get( entry.getKey() );
                if ( entry.getValue().getX() != location.getX() || entry.getValue().getY() != location.getY() ) {
                    updated.add( entry.getValue() );
                }
                entry.getValue().updateLocation( location );
            } else {
                updated.add( entry.getValue() );
                getBots().remove( entry.getKey() );
            }
        }
        for ( Map.Entry<Integer, Location> entry : enemies.entrySet() ) {
            if ( !getBots().containsKey( entry.getKey() ) ) {
                EnemyBot enemyBot = new EnemyBot( entry.getKey(), entry.getValue().getX(), entry.getValue().getY() );
                getBots().put( entry.getKey(), enemyBot );
                updated.add( enemyBot );
            }
        }
        return updated;
    }
}
