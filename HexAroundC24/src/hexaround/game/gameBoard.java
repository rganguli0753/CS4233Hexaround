package hexaround.game;

import java.util.ArrayList;
import java.util.Collection;

import hexaround.config.CreatureDefinition;
import hexaround.required.*;

public class gameBoard {
    private Collection<Hex> hexBoard = new ArrayList<>();

    public gameBoard(){}//used to just make an empty in the constructor so not null


    public Collection<Hex> getHexBoard() {
        return hexBoard;//getter for future use although not used now
    }
    public void placePiece(CreatureName creature,Hex coord){
        hexBoard.add(new Hex(creature,coord));//specific addition for the placing creature
    }

    public void addBoard(Hex hex){
        hexBoard.add(hex);//not used but needed if making place without creature
    }

    public boolean isOccupied(int x, int y){
        Hex checkCoord = new Hex(x,y);
        for(Hex coord: this.hexBoard)//checks if the distance between what is being checked
            if(coord.getDistance(checkCoord)==0)
                return true;
        return false;
    }

    public Hex getHex(int x, int y){
        Hex spot = new Hex(x,y);
        for(Hex coords: this.hexBoard){//returns specific spot if distance checked is zero
            if (coords.getDistance(spot)==0)
                return coords;
        }
        return null;
    }

    public boolean reachable(Hex from, Hex to, int maxDist){
        int dist = from.getDistance(to);
        return maxDist>=dist;
    }

    public boolean propertyExist(CreatureDefinition def, CreatureProperty prop){
        for(CreatureProperty p: def.properties()){
            if(p.toString().equals(prop.toString()))
                return true;
        }
        return false;
    }

    public void updateLocation(int fromx, int fromy, int tox, int toy){
        Hex currentSpot = new Hex(fromx,fromy);
        for(Hex coord: hexBoard){
            if(coord.getDistance(currentSpot)==0){
                coord.changeLoc(tox,toy);
            }
        }
        hexBoard.add(currentSpot);
    }

    public boolean isDisconnected(int tox, int toy, gameBoard board){
        Hex spot = new Hex(tox,toy);
        Collection<Hex> neighbors = spot.getNeighbors();
        for(Hex neighboring: neighbors){
            if(board.isOccupied(neighboring.getX(), neighboring.getY()))
                return false;
        }
        return true;
    }

}
