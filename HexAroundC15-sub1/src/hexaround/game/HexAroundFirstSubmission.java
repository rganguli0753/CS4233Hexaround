package hexaround.game;

import hexaround.required.*;

import java.util.HashMap;

public class HexAroundFirstSubmission implements IHexAround1{

    HashMap<String,CreatureName> gameCreatures = new HashMap<>();

    /**
     * This is the default constructor, and the only constructor
     * that you can use. The builder creates an instance using
     * this connector. You should add getters and setters as
     * necessary for any instance variables that you create and
     * will be filled in by the builder.
     */
    public HexAroundFirstSubmission() {
        // Nothing to do.
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
        return gameCreatures.get(x+""+y+"");
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
        if(!(getCreatureAt(x,y)==null)){
            return true;
        }
        return false;
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
     * @return itrue if the distance between the two hexes is less
     * than or equal to the maximum distance property for the piece
     * at (x1, y1). Return false otherwise.
     */
    @Override
    public boolean canReach(int x1, int y1, int x2, int y2) {
        CreatureName creature = getCreatureAt(x1,y1);
        CreatureProperty property = null;
        switch(property){
            case WALKING:
                if(Math.abs(x2-x1)==1||Math.abs(y2-y1)==1)
                    return true;
                break;
            case QUEEN:
                break;
            case FLYING:
                break;
            case JUMPING:
                break;
            case RUNNING:
                break;
            case HATCHING:
                break;
            case KAMIKAZE:
                break;
            case SWAPPING:
                break;
            case TRAPPING:
                break;
            case INTRUDING:
                break;
        }
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
        if(getCreatureAt(x,y)==null){
            gameCreatures.put(x+""+y+"",creature);
            return new MoveResponse(MoveResult.OK);
        }
        return null;
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
