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

    public MoveResponse moveCreature(CreatureName creature, int fromx, int fromy, int tox, int toy, int dist){
        if(!isOccupied(fromx,fromy))
            return new MoveResponse(MoveResult.MOVE_ERROR,"PIECE IS MISSING");
        if(isOccupied(tox,toy))
            return new MoveResponse(MoveResult.MOVE_ERROR,"SPOT IS OCCUPIED");
        if(!reachable(new Hex(fromx,fromy),new Hex(tox,toy),dist))
            return new MoveResponse(MoveResult.MOVE_ERROR,"SPOT TOO FAR");

        return new MoveResponse(MoveResult.OK);
    }

}
