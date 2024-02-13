package hexaround.game;

import hexaround.required.*;

public class Hex {
    private int x,y;
    private CreatureName creature;

    public Hex(CreatureName creature, int x, int y) {
        this.x = x;
        this.y = y;
        this.creature = creature;
    }

    public Hex(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Hex(CreatureName creature, Hex coord) {
        this.creature = creature;
        this.x = coord.x;
        this.y = coord.y;
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
