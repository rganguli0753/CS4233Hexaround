package hexaround;

import hexaround.game.*;
import hexaround.required.*;
import org.junit.jupiter.api.*;

import java.io.*;

import static hexaround.required.CreatureName.*;
import static org.junit.jupiter.api.Assertions.*;

public class Submission1Test {
    HexAroundManager gameManager = null;


    void build() throws IOException {
        String hgcFile = "HexAroundC24/testConfigurations/FirstConfiguration.hgc";
        this.gameManager =
                (HexAroundManager) HexAroundGameBuilder.buildGameManager(
                        "HexAroundC24/testConfigurations/FirstConfiguration.hgc");
    }

    void makeFirstMoves() throws IOException {
        build();
        gameManager.placeCreature(GRASSHOPPER,1,1);
        gameManager.placeCreature(BUTTERFLY,2,1);
        gameManager.placeCreature(BUTTERFLY,5,1);

    }

    @Test
    void firstTest() throws IOException {
        build();
        gameManager.placeCreature(GRASSHOPPER, 5, 42);
        assertEquals(GRASSHOPPER,gameManager.getCreatureAt(0, 0));
    }

    @Test
    void occupiedTestNot() throws IOException{
        build();
        gameManager.placeCreature(GRASSHOPPER, 5, 42);
        assertFalse(gameManager.isOccupied(5,45));
    }

    @Test
    void occupiedTestIs() throws IOException{
        build();
        gameManager.placeCreature(GRASSHOPPER, 5, 42);
        assertTrue(gameManager.isOccupied(0,0));
    }

    @Test
    void propertyTestFalse() throws IOException{
        build();
        gameManager.placeCreature(GRASSHOPPER, 5, 42);
        assertFalse(gameManager.hasProperty(0,0, CreatureProperty.WALKING));
    }

    @Test
    void propertyTestTrue() throws IOException{
        build();
        gameManager.placeCreature(GRASSHOPPER, 5, 42);
        assertTrue(gameManager.hasProperty(0,0, CreatureProperty.JUMPING));
        assertTrue(gameManager.hasProperty(0,0, CreatureProperty.INTRUDING));
    }

    @Test
    void canReachFalse() throws IOException{
        build();
        gameManager.placeCreature(GRASSHOPPER, 5, 42);
        assertFalse(gameManager.canReach(0,0,5,50));
    }

    @Test
    void canReachTrue() throws IOException{
        build();
        gameManager.placeCreature(GRASSHOPPER, 5, 42);
        assertTrue(gameManager.canReach(0,0,3,0));
    }

    @Test
    void someMovementTest() throws IOException {
        makeFirstMoves();
        MoveResult result = MoveResult.OK;
        String msg = null;
        MoveResponse mr = gameManager.moveCreature(GRASSHOPPER, 0, 0, -1, 1);
        assertEquals(result, mr.moveResult());
        assertEquals(msg, mr.message());
    }

    @Test
    void illegalTooFarMoveTest() throws IOException{
        makeFirstMoves();
        MoveResult result = MoveResult.MOVE_ERROR;
        String msg = "SPOT TOO FAR";
        MoveResponse mr = gameManager.moveCreature(GRASSHOPPER, 0, 0, 5, 2);
        assertEquals(result, mr.moveResult());
        assertEquals(msg, mr.message());
    }

    @Test
    void illegalOccupiedMoveTest() throws IOException{
        makeFirstMoves();
        MoveResult result = MoveResult.MOVE_ERROR;
        String msg = "SPOT IS OCCUPIED";
        MoveResponse mr = gameManager.moveCreature(GRASSHOPPER, 0, 0, 2, 1);
        assertEquals(result, mr.moveResult());
        assertEquals(msg, mr.message());
    }

    @Test
    void pieceMissingTest() throws IOException{
        makeFirstMoves();
        MoveResult result = MoveResult.MOVE_ERROR;
        String msg = "PIECE IS MISSING";
        gameManager.moveCreature(GRASSHOPPER, 1, 1, 3, 1);
        MoveResponse mr = gameManager.moveCreature(GRASSHOPPER, 1, 1, 3, 1);
        assertEquals(result, mr.moveResult());
        assertEquals(msg, mr.message());
    }

    @Test
    void incorrectCreature() throws IOException{
        makeFirstMoves();
        MoveResult result = MoveResult.MOVE_ERROR;
        String msg = "INCORRECT CREATURE MOVEMENT";
        MoveResponse mr = gameManager.moveCreature(GRASSHOPPER, 2, 1, 3, 1);
        assertEquals(result, mr.moveResult());
        assertEquals(msg, mr.message());
    }

    @Test
    void willDisconnect() throws IOException{
        makeFirstMoves();
        MoveResult result = MoveResult.MOVE_ERROR;
        String msg = "WILL DISCONNECT";
        MoveResponse mr = gameManager.moveCreature(GRASSHOPPER, 0, 0, -2, 1);
        assertEquals(result, mr.moveResult());
        assertEquals(msg, mr.message());
    }


    @Test
    void getPlayerTurn() throws IOException{
        makeFirstMoves();
        assertEquals(PlayerName.RED,gameManager.getTurnNum());
    }

    @Test
    void playerChanges() throws IOException{
        makeFirstMoves();
        assertEquals(PlayerName.RED,gameManager.getTurnNum());
        gameManager.moveCreature(BUTTERFLY,2,1,-1,1);
        assertEquals(PlayerName.BLUE,gameManager.getTurnNum());
    }

}
