package hexaround.game;

import hexaround.structures.*;

public interface IHexAround1 extends IHexAroundGameManager{
    CreatureName getCreatureAt(int x, int y);
    boolean hasProperty(int x, int y, CreatureProperty property);
    boolean isOccupied(int x, int y);
    boolean canReach(int x1, int y1, int x2, int y2);
}
