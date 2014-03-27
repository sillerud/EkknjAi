package net.theunnameddude.handler;

import lombok.RequiredArgsConstructor;
import net.theunnameddude.Bot;
import net.theunnameddude.api.EnemyBot;
import net.theunnameddude.api.GameMap;
import net.theunnameddude.api.Tile;
import net.theunnameddude.protocol.packets.*;

import java.util.ArrayList;
import java.util.HashMap;

@RequiredArgsConstructor
public abstract class PacketHandler {
    final Bot bot;
    public void handle(MapPacket mapPacket) {
        if ( bot.getMap() == null ) {
            bot.setMap( new GameMap( mapPacket.getContent() ) );
        } else {
            HashMap<Tile, Tile.TileMaterial> tileTypes = bot.getMap().updateTiles( mapPacket.getContent() );
            if ( !tileTypes.isEmpty() && bot.isDebug() ) {
                bot.getLogger().info( "Updated tiles: " + tileTypes.toString() );
            }
        }
    }

    public void handle(BombMappingPacket bombMappingPacket) {
        if ( bot.getMap() != null ) {
            bot.getMap().updateBombs( bombMappingPacket.getBombs() );
        } else {
            bot.getLogger().severe( "Got bomb info and map is null!" );
        }
    }

    public void handle(PlayerListPacket playerListPacket) {
        ArrayList<EnemyBot> updated = bot.updatePlayers( playerListPacket.getEnemies() );
        if ( !updated.isEmpty() && bot.isDebug() ) {
            bot.getLogger().info( "Updated players: " + updated );
        }
    }
}
