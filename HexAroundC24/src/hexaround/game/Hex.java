package hexaround.game;

import hexaround.structures.*;

import java.util.ArrayList;
import java.util.Collection;

public class Hex {
    private int x,y;
    private CreatureName creature;


    PlayerName playerPiece;//keep track of who owns which butterfly for endgame





    public Hex(int x, int y) {
        this.x = x;
        this.y = y;
        this.creature=null;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Hex (PlayerName name,CreatureName creature, Hex coord){
        this.creature = creature;
        this.x = coord.getX();
        this.y = coord.getY();
        this.playerPiece=name;
    }


    public PlayerName getPlayerPiece(){
        return this.playerPiece;
    }

    public CreatureName getCreature() {
        return creature;//on the certain hex is stored the certain creature
    }

    public void setCreatureNull(){
        this.creature=null;
    }//sets creature null when kamikaze

    /**
     * @param otherCoord
     * @return the distance to the other coordinate
     */
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

    /**
     *
     * @return the list of neighbors, important to note that it does not check if neighbor is occupied
     */
    public Collection<Hex> getNeighbors(){
        Collection<Hex> neighborCoords = new ArrayList<>();
        neighborCoords.add(new Hex(x,y+1));
        neighborCoords.add(new Hex(x+1,y));
        neighborCoords.add(new Hex(x+1,y-1));
        neighborCoords.add(new Hex(x,y-1));
        neighborCoords.add(new Hex(x-1,y));
        neighborCoords.add(new Hex(x-1,y+1));
        return neighborCoords;
    }

    /**
     * overrides the equals method, because hexes are the same if they have the same coords
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hex hex = (Hex) o;
        return x == hex.x && y == hex.y;
    }
}
