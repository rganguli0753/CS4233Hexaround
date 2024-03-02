package hexaround.game;

import hexaround.config.CreatureDefinition;
import hexaround.config.PlayerConfiguration;
import hexaround.structures.*;

import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedList;

public class HexAroundManager implements IHexAround1{

    private Dictionary<CreatureName, CreatureDefinition> creatureInf = new Hashtable<>();
    private gameBoard board;
    private PlayerName playerTurn;
    private Dictionary<PlayerName,PlayerConfiguration> playerInf = new Hashtable<>();
    int numPlace;

    int blueTurns=1;
    int redTurns=1;
    LinkedList<CreatureName> blueCreatures = new LinkedList<>();
    LinkedList<CreatureName> redCreatures = new LinkedList<>();


    /**
     * This is the default constructor, and the only constructor
     * that you can use. The builder creates an instance using
     * this connector. You should add getters and setters as
     * necessary for any instance variables that you create and
     * will be filled in by the builder.
     */
    public HexAroundManager() {
        // Nothing to do.
        board = new gameBoard();
        playerTurn = PlayerName.BLUE;
        numPlace=0;
    }

    public Dictionary<CreatureName, CreatureDefinition> getCreatureInf() {
        return creatureInf;
    }

    public void setCreatureInf(Collection<CreatureDefinition> defs) {
       for(CreatureDefinition def: defs){
           creatureInf.put(def.name(),def);
       }
    }

    public void setPlayerInfo(Collection<PlayerConfiguration> players){
        for(PlayerConfiguration player: players)
            playerInf.put(player.Player(),player);
    }

    private void changePlayerTurn() {
        if(playerTurn==PlayerName.BLUE) {
            playerTurn = PlayerName.RED;
        }
        else {
            playerTurn = PlayerName.BLUE;
        }
    }

    boolean hasButterfly(){
        if(playerTurn.equals(PlayerName.BLUE)){
            if(blueCreatures.contains(CreatureName.BUTTERFLY))
                return true;
        } else if (playerTurn.equals(PlayerName.RED)) {
            if(redCreatures.contains(CreatureName.BUTTERFLY))
                return true;
        }
        return false;
    }




    /**
     * Given the x and y-coordinates for a hex, return the name
     * of the creature on that coordinate.
     * @param x
     * @param y
     * @return the name of the creature on (x, y), or null if there
     *  is no creature.
     */
    @Override
    public CreatureName getCreatureAt(int x, int y) {
        return board.getHex(x,y).getCreature();
    }

    public Hex getHexAt(int x, int y){
        return board.getHex(x,y);
    }

    /**
     * Determine if the creature at the x and y-coordinates has the specified
     * property. You can assume that there will be a creature at the specified
     * location.
     * @param x
     * @param y
     * @param property the property to look for.
     * @return true if the creature at (x, y) has the specified property,
     *  false otherwise.
     */
    @Override
    public boolean hasProperty(int x, int y, CreatureProperty property) {
        CreatureName creature = getCreatureAt(x,y);
        return board.propertyExist(creatureInf.get(creature),property);
    }

    /**
     * Given the x and y-coordinate of a hex, determine if there is a
     * piece on that hex on the board.
     * @param x
     * @param y
     * @return true if there is a piece on the hex, false otherwise.
     */
    @Override
    public boolean isOccupied(int x, int y) {
        return board.isOccupied(x,y);
    }

    /**
     * Given the coordinates for two hexes, (x1, y1) and (x2, y2),
     * return whether the piece at (x1, y1) could reach the other
     * hex.
     * You can assume that there will be a piece at (x1, y1).
     * The distance is just the distance between the two hexes. You
     * do not have to do any other checking.
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return true if the distance between the two hexes is less
     * than or equal to the maximum distance property for the piece
     * at (x1, y1). Return false otherwise.
     */
    @Override
    public boolean canReach(int x1, int y1, int x2, int y2) {
        Hex fromCoord = new Hex(x1,y1);
        Hex toCoord = new Hex(x2,y2);
        int maxDist = creatureInf.get(getCreatureAt(x1,y1)).maxDistance();
        return board.reachable(fromCoord,toCoord, maxDist);
    }

    /**
     * For this submission, just put the piece on the board. You
     * can assume that the hex (x, y) is empty. You do not have to do
     * any checking.
     * @param creature
     * @param x
     * @param y
     * @return a response, or null. It is not going to be checked.
     */
    @Override
    public MoveResponse placeCreature(CreatureName creature, int x, int y) {
        if(playerInf.get(playerTurn).creatures().containsKey(creature)){
            int amount = playerInf.get(playerTurn).creatures().get(creature);
            if (numPlace >= 3 && !hasButterfly() && !creature.equals(CreatureName.BUTTERFLY)) {
                return new MoveResponse(MoveResult.MOVE_ERROR, "BUTTERFLY NOT PLACED");
            }
            if (numPlace == 0) {
                board.placePiece(creature, new Hex(0, 0));
                blueCreatures.add(creature);//enforces that it's placed at 0,0
                board.setBlues(blueCreatures);
                numPlace++;
                changePlayerTurn();//we know that the first turn is done by blue
                return new MoveResponse(MoveResult.OK);
            }
            if (playerInf.get(playerTurn).creatures().containsKey(creature)) {
                if (!board.isOccupied(x, y)) {
                    if (!board.isDisconnected(x, y, board)) {
                        board.placePiece(playerTurn,creature, new Hex(x, y));
                        if (playerTurn == PlayerName.BLUE) {
                            blueCreatures.add(creature);
                            board.setBlues(blueCreatures);
                        } else {
                            redCreatures.add(creature);
                            board.setReds(redCreatures);
                            numPlace++;
                        }
                        removeFromHand(creature,amount);
                        changePlayerTurn();
                        return board.checkEndGame();
                    }
                    return new MoveResponse(MoveResult.MOVE_ERROR, "PLACEMENT DISCONNECTED");
                }
                return new MoveResponse(MoveResult.MOVE_ERROR, "SPOT NOT AVAILABLE");
            }
        }
        return new MoveResponse(MoveResult.MOVE_ERROR, "CREATURE DNE");
    }

    public int getAmount(CreatureName creature){
        return playerInf.get(playerTurn).creatures().get(creature);//made for purpose of testing
    }

    void removeFromHand(CreatureName creature,int amount){
        if(amount<=1){
            playerInf.get(playerTurn).creatures().remove(creature,amount);
        }else {
            playerInf.get(playerTurn).creatures().replace(creature, amount, amount - 1);
        }
    }

    void returnToHand(CreatureName creature){
        int amount = playerInf.get(playerTurn).creatures().get(creature);
        playerInf.get(playerTurn).creatures().replace(creature, amount, amount + 1);
    }

    public PlayerName getPlayerTurn() {
        return playerTurn;
    }

    /**
     * This is never used in this submission. You do not have to do anything.
     * @param creature
     * @param fromX
     * @param fromY
     * @param toX
     * @param toY
     * @return
     */
    @Override
    public MoveResponse moveCreature(CreatureName creature, int fromX, int fromY, int toX, int toY) {
        if (numPlace >= 3 && !hasButterfly() && !creature.equals(CreatureName.BUTTERFLY)) {
            return new MoveResponse(MoveResult.MOVE_ERROR, "BUTTERFLY NOT PLACED");
        }
        if(!isOccupied(fromX,fromY)|| getCreatureAt(fromX,fromY)==null) {
            return new MoveResponse(MoveResult.MOVE_ERROR, "PIECE IS MISSING");
        }
        if(fromX==toX&&fromY==toY){
            return new MoveResponse(MoveResult.MOVE_ERROR,"CANNOT MOVE TO SAME SPOT");
        }
        if(creature!=getCreatureAt(fromX,fromY)) {
            return new MoveResponse(MoveResult.MOVE_ERROR, "INCORRECT CREATURE MOVEMENT");
        }
        if(isOccupied(toX,toY) &&
                !((hasProperty(fromX,fromY,CreatureProperty.INTRUDING)||
                hasProperty(fromX,fromY,CreatureProperty.TRAPPING)||
                        hasProperty(fromX,fromY,CreatureProperty.KAMIKAZE)||
                        hasProperty(fromX,fromY,CreatureProperty.SWAPPING)))) {
           return new MoveResponse(MoveResult.MOVE_ERROR, "SPOT IS OCCUPIED");
        }

        int creaturedist = creatureInf.get(getCreatureAt(fromX,fromY)).maxDistance();

        if(!board.reachable(new Hex(fromX, fromY), new Hex(toX, toY), creaturedist)) {
            return new MoveResponse(MoveResult.MOVE_ERROR, "SPOT TOO FAR");
        }

        if(hasProperty(fromX,fromY,CreatureProperty.KAMIKAZE)) {
            if(kamikazePath(fromX,fromY,toX,toY)){
                if(playerTurn==PlayerName.RED)
                    numPlace++;
                changePlayerTurn();
                return new MoveResponse(MoveResult.OK);
            }else{
                return new MoveResponse(MoveResult.MOVE_ERROR, "KAMIKAZE DISCONNECTS");
            }

        }
        if(!board.viablePath(creatureInf.get(getCreatureAt(fromX,fromY)),creature,fromX,fromY,toX,toY)) {
            return new MoveResponse(MoveResult.MOVE_ERROR, "WILL DISCONNECT");
        }
        if(playerTurn==PlayerName.RED)
            numPlace++;
        changePlayerTurn();
        board.updateLocation(fromX,fromY,toX,toY);
        return board.checkEndGame();
    }

    private boolean kamikazePath(int fromX, int fromY, int toX, int toY) {
        Hex from = board.getHex(fromX,fromY);
        Hex to = board.getHex(toX,toY);
        CreatureName fromCreature = from.getCreature();
        CreatureName toCreature = to.getCreature();//made for place holding
        from.setCreatureNull();
        to.setCreatureNull();

        if(playerTurn==PlayerName.BLUE){
            blueCreatures.remove(fromCreature);//remove from board and hand
            board.setBlues(blueCreatures);
            redCreatures.remove(toCreature);//just remove from board
            board.setReds(redCreatures);
            if(board.BFSColonyConnectivity(to)) {
                returnToHand(toCreature);
                return true;
            }else{
                blueCreatures.add(fromCreature);
                redCreatures.add(toCreature);
                board.setBlues(blueCreatures);
                board.setReds(redCreatures);
            }
        }else{
            blueCreatures.remove(toCreature);
            board.setBlues(blueCreatures);
            redCreatures.remove(fromCreature);
            board.setReds(redCreatures);
            if(board.BFSColonyConnectivity(to)) {
                returnToHand(toCreature);
                return true;
            }else{
                blueCreatures.add(toCreature);
                redCreatures.add(fromCreature);
                board.setBlues(blueCreatures);
                board.setReds(redCreatures);
            }
        }

       return false;
    }
}

