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
        addBoard(currentSpot);
    }

    public boolean viablePath(CreatureDefinition def,CreatureName creature, int fromX, int fromY, int toX, int toY){

        return true;
    }

    Collection<Hex> activeNeighbors(Hex coord){
        Collection<Hex> currentNeighbors = coord.getNeighbors();
        Collection<Hex> actives = new ArrayList<>();
        for(Hex hex: hexBoard){
            if(currentNeighbors.contains(hex)&&hex.getTopPiece()!=null)
                actives.add(hex);
        }
        return actives;
    }

    boolean lineararity(Hex from,Hex to){
        int fromZ = -from.getX()- from.getY();
        int toZ = -to.getX()- to.getY();
        return (from.getX()==to.getY()||to.getX()==to.getY()||toZ==fromZ);
    }

    boolean flyPath(int fromX, int fromY, int toX, int toY){
        Hex from = new Hex(fromX,fromY);
        Hex to = new Hex(toX,toY);
        if(from.getNeighbors().size()==6)
            return false;
        if(to.getNeighbors().size()==0)
            return false;
        return true;
    }

    //jump path must move in a straight line
    boolean jumpPath(int fromX, int fromY, int toX, int toY){
        Hex to = new Hex(toX,toY);
        if(!lineararity(new Hex(fromX,fromY),new Hex(toX,toY)))
            return false;
        if(to.getNeighbors().size()==0)
            return false;
        return true;
    }

    public MoveResponse checkEndGame(){
        boolean redWin = false;
        boolean blueWin = false;
        for(Hex coord: hexBoard){
            if(coord.getTopPiece()!=null&&coord.getCreature().equals(CreatureName.BUTTERFLY)){
                if(activeNeighbors(coord).size()==6){
                    if(coord.getTopPiece().getPlayer()==PlayerName.BLUE)
                        redWin=true;
                    if(coord.getTopPiece().getPlayer()==PlayerName.RED)
                        blueWin=true;
                }
            }
        }
        if(blueWin&&redWin){
            return new MoveResponse(MoveResult.DRAW);
        } else if (blueWin) {
            return new MoveResponse(MoveResult.BLUE_WON);
        } else if (redWin) {
            return new MoveResponse(MoveResult.RED_WON);
        }
        return new MoveResponse(MoveResult.OK);
    }

    public boolean isDisconnected(int tox, int toy, gameBoard board) {
        Hex spot = new Hex(tox,toy);
        Collection<Hex> neighbors = spot.getNeighbors();
        for(Hex neighboring: neighbors){
            if(board.isOccupied(neighboring.getX(), neighboring.getY()))
                return false;
        }
        return true;
    }
}
