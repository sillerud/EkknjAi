package net.theunnameddude.api;

import lombok.*;

import java.util.ArrayList;

@RequiredArgsConstructor
@Getter
@ToString(doNotUseGetters = true, exclude = "map")
public class Tile {
    final int x;
    final int y;
    @NonNull
    @Setter
    TileMaterial material;
    final GameMap map;
    @Setter
    int bombTicksLeft = 0;

    public ArrayList<EnemyBot> getPlayers() {
        ArrayList<EnemyBot> bots = new ArrayList<>();
        for ( EnemyBot bot : map.getEnemies() ) {
            if ( bot.getX() == x && bot.getY() == y ) {
                bots.add( bot );
            }
        }
        return bots;
    }

    public enum TileMaterial {
        Indestructable, Grass, Rock;
        public static TileMaterial fromChar(char ch) {
            if ( ch == '+' ) {
                return Indestructable;
            } else if ( ch == '.' ) {
                return Grass;
            } else if ( ch == '#' ) {
                return Rock;
            }
            return null;
        }
    }
}
