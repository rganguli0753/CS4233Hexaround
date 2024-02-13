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

    public int getDistance(Hex otherCoord){
        int deltaX = x-otherCoord.x;
        int deltaY = y- otherCoord.y;
        int sign = deltaY*deltaX;
        if(sign<0){
            return Math.max(Math.abs(deltaX),Math.abs(deltaY));
        }
        return Math.abs(deltaX+deltaY);
    }
}
