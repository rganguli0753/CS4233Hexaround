package hexaround.game;

import java.util.ArrayList;
import java.util.Collection;
import hexaround.required.*;

public class gameBoard {
    private Collection<Hex> hexBoard = new ArrayList<>();

    public gameBoard(){}


    public Collection<Hex> getHexBoard() {
        return hexBoard;
    }
    public void placePiece(CreatureName creature,Hex coord){
        hexBoard.add(new Hex(creature,coord));
    }

    public boolean isOccupied(int x, int y){
        Hex checkCoord = new Hex(x,y);
        for(Hex coord: this.hexBoard)
            if(coord.getDistance(checkCoord)==0)
                return true;
        return false;
    }

    public Hex getHex(int x, int y){
        Hex spot = new Hex(x,y);
        for(Hex coords: this.hexBoard){
            if (coords.getDistance(spot)==0)
                return coords;
        }
        return null;
    }


}
