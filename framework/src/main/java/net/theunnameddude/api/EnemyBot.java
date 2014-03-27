package net.theunnameddude.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString(doNotUseGetters = true)
public class EnemyBot {
    int x;
    int y;
    int id;

    public void updateLocation(Location location) {
        this.x = location.getX();
        this.y = location.getY();
    }
}
