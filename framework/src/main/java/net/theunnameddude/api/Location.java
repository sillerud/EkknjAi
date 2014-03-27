package net.theunnameddude.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString(doNotUseGetters = true)
@Getter
public class Location {
    final int x;
    final int y;

    @Override
    public boolean equals(Object obj) {
        if ( obj.getClass() != getClass() )
            return false;
        Location location = (Location)obj;
        return location.getX() == x && location.getY() == y;
    }

}
