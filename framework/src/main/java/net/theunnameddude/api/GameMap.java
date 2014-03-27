package net.theunnameddude.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class GameMap {
    /* Format is y, then x coordinates */
    final Tile[][] tiles;
    @Getter
    ArrayList<EnemyBot> enemies = new ArrayList<>();

    public GameMap(String[] mapContent) {
        tiles = new Tile[ mapContent.length ][];
        for ( int y = 0; y < tiles.length; y++ ) {
            tiles[ y ] = new Tile[ tiles.length ];
            for ( int x = 0; x < tiles.length; x++ ) {
                tiles[ y ][ x ] = new Tile( x, y, Tile.TileMaterial.fromChar( mapContent[ y ].charAt( x ) ), this );
            }
        }
    }

    public HashMap<Tile, Tile.TileMaterial> updateTiles(String[] mapContent) {
        HashMap<Tile, Tile.TileMaterial> updated = new HashMap<>();
        for ( int y = 0; y < tiles.length; y++ ) {
            for ( int x = 0; x < tiles[ y ].length; x++ ) {
                Tile.TileMaterial newMaterial = Tile.TileMaterial.fromChar(mapContent[y].charAt(x));
                Tile.TileMaterial oldMaterial = tiles[ y ][ x ].getMaterial();
                if ( newMaterial != oldMaterial ) {
                    updated.put( tiles[ y ][ x ], oldMaterial );
                }
                tiles[ y ][ x ].setMaterial( newMaterial );
            }
        }
        return updated;
    }

    public void updateBombs(HashMap<Location, Integer> bombs) {
        for ( Map.Entry<Location, Integer> bombInfo : bombs.entrySet() ) {
            getTile( bombInfo.getKey() ).setBombTicksLeft( bombInfo.getValue() );
        }
    }

    public Tile getTile(int x, int y) {
        return tiles[ y ][ x ];
    }

    public Tile getTile(Location location) {
        return getTile( location.getX(), location.getY() );
    }
}
