package hexaround.game;

import hexaround.config.CreatureDefinition;
import hexaround.config.PlayerConfiguration;
import hexaround.required.*;
import hexaround.game.Hex;

import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;

public class HexAroundFirstSubmission implements IHexAround1{

    private Dictionary<PlayerName, PlayerConfiguration> playerInf = new Hashtable<>();
    private Dictionary<CreatureName, CreatureDefinition> creatureInf = new Hashtable<>();
    private gameBoard board;

    /**
     * This is the default constructor, and the only constructor
     * that you can use. The builder creates an instance using
     * this connector. You should add getters and setters as
     * necessary for any instance variables that you create and
     * will be filled in by the builder.
     */
    public HexAroundFirstSubmission() {
        // Nothing to do.
        board = new gameBoard();
    }


    public Dictionary<PlayerName, PlayerConfiguration> getPlayerInf() {
        return playerInf;
    }

    public Dictionary<CreatureName, CreatureDefinition> getCreatureInf() {
        return creatureInf;
    }

    public gameBoard getBoard() {
        return board;
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

        return false;
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


        return false;
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
        Hex spot = new Hex(x,y);
        board.placePiece(creature, spot);
        return new MoveResponse(MoveResult.OK);
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
        return null;
    }
}
