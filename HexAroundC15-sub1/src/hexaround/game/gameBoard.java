package hexaround.game;

import java.util.ArrayList;
import java.util.Collection;
import hexaround.required.*;

public class gameBoard {
    private Collection<Hex> hexBoard = new ArrayList<>();

    public Collection<Hex> getHexBoard() {
        return hexBoard;
    }
    public void placePiece(Hex coord){
        hexBoard.add(coord);
    }

    public boolean isOccupied(int x, int y){
        Hex checkCoord = new Hex(x,y);
        for(Hex coord: this.hexBoard)
            if(coord.getDistance(checkCoord)==0)
                return false;
        return true;
    }

    public Hex getHex(int x, int y){
        Hex spot = new Hex(x,y);
        for(Hex coords: this.hexBoard){
            if (coords.getDistance(spot)==0)
                return spot;
        }
        return null;
    }


}
