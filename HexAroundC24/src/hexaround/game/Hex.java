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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Hex (CreatureName creature, Hex coord){
        this.creature = creature;
        this.x = coord.getX();
        this.y = coord.getY();
    }

    public void setCreature(CreatureName creature) {
        this.creature = creature;
    }

    public CreatureName getCreature() {
        return creature;//on the certain hex is stored the certain creature
    }

    public int getDistance(Hex otherCoord){
        int deltaX = getX() - otherCoord.getX();
        int deltaY = getY() - otherCoord.getY();
        int sign = deltaY*deltaX;
        if(sign<0){//if its negative the best choice is either the max of x or y
            return Math.max(Math.abs(deltaX),Math.abs(deltaY));
        }
        return Math.abs(deltaX+deltaY);
    }

    public void changeLoc(int tox, int toy) {
        this.x = tox;
        this.y = toy;
    }
}
