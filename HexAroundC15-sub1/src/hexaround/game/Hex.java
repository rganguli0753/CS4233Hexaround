package hexaround.game;

import hexaround.required.*;

public class Hex {
    private int x,y;
    private PlayerName hexOwner;
    private CreatureName creature;

    public Hex(int x, int y, PlayerName hexOwner, CreatureName creature) {
        this.x = x;
        this.y = y;
        this.hexOwner = hexOwner;
        this.creature = creature;
    }

    public Hex(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void  move(int x, int y){
        this.x=x;
        this.y=y;
    }

    public CreatureName getCreature() {
        return creature;
    }
}
