package hexaround;

import hexaround.game.*;
import hexaround.structures.*;
import org.junit.jupiter.api.*;

import java.io.*;

import static hexaround.structures.CreatureName.*;
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
        gameManager.placeCreature(GRASSHOPPER,0,0);
        gameManager.placeCreature(BUTTERFLY,1,0);

    }

    void setUp() throws IOException {
        build();
        gameManager.placeCreature(CreatureName.BUTTERFLY, 0, 0);//blue
        gameManager.placeCreature(CreatureName.BUTTERFLY, 0, 1);//red
        gameManager.placeCreature(CreatureName.DOVE, 0, -1);//blue
        gameManager.placeCreature(CreatureName.TURTLE, -1, 2);//red
        gameManager.placeCreature(CreatureName.TURTLE, -1, -1);//blue
        gameManager.placeCreature(CreatureName.DOVE, 0, 2);//red
        gameManager.placeCreature(CreatureName.DOVE, 0, -2);//blue
        gameManager.placeCreature(CreatureName.DOVE, 1, 1);//red
        gameManager.placeCreature(CreatureName.TURTLE, 1, -2);//blue
        gameManager.placeCreature(CreatureName.TURTLE, 2, 0);//red
        gameManager.placeCreature(RABBIT,2,-2);
        gameManager.placeCreature(RABBIT, 1,0);
    }

    @Test
    void levelOneTests() throws IOException{
        setUp();
        assertEquals(PlayerName.BLUE,gameManager.getPlayerTurn());
        MoveResponse mr = gameManager.moveCreature(TURTLE,-1,-1,-1,0);
        assertEquals(MoveResult.OK,mr.moveResult());
        assertEquals(gameManager.getCreatureAt(-1,0),TURTLE);
        assertEquals(PlayerName.RED,gameManager.getPlayerTurn());
        assertTrue(gameManager.hasProperty(0,0,CreatureProperty.WALKING));
        assertTrue(gameManager.hasProperty(0,0,CreatureProperty.QUEEN));
        assertFalse(gameManager.hasProperty(0,-1,CreatureProperty.WALKING));
        assertTrue(gameManager.hasProperty(0,-1,CreatureProperty.FLYING));
    }

    @Test
    void correctDoveFly() throws IOException {
        setUp();
        MoveResponse mr = gameManager.moveCreature(DOVE,0,-2,0,3);
        assertEquals(MoveResult.OK,mr.moveResult());
    }

    @Test
    void incorrectDoveMove() throws IOException{
        setUp();
        assertEquals(MoveResult.MOVE_ERROR, gameManager.moveCreature(DOVE,0,-1,0,3).moveResult());
    }

    @Test
    void correctRabbitJump() throws IOException{
        setUp();
        assertEquals(MoveResult.OK,gameManager.moveCreature(RABBIT,2,-2,2,1).moveResult());
    }

    @Test
    void validTurtleWalk() throws IOException{
        setUp();
        assertEquals(MoveResult.OK,gameManager.moveCreature(TURTLE,1,-2,1,-1).moveResult());
    }

    @Test
    void invalidRabbitJump() throws IOException{
        setUp();
        assertEquals(MoveResult.MOVE_ERROR,gameManager.moveCreature(RABBIT,2,-2,2,2).moveResult());
        assertEquals(MoveResult.MOVE_ERROR,gameManager.moveCreature(RABBIT,2,-2,3,0).moveResult());
    }

    @Test
    void blueVictory() throws IOException{
        setUp();
        assertEquals(MoveResult.BLUE_WON, gameManager.moveCreature(TURTLE,-1,-1,-1,1).moveResult());
    }

    @Test
    void invalidTurtleMove() throws IOException{
        setUp();
        assertEquals(MoveResult.MOVE_ERROR, gameManager.moveCreature(TURTLE,-1,-1,-2,1).moveResult());
    }

    @Test
    void redVictory() throws IOException{
        setUp();
        gameManager.moveCreature(RABBIT, 2,-2,1,-1);
        gameManager.moveCreature(TURTLE,-1,2,-1,1);
        assertEquals(MoveResult.RED_WON, gameManager.moveCreature(TURTLE,-1,-1,-1,0).moveResult());
    }

    @Test
    void disconnectPlacement() throws IOException{
        build();
        gameManager.placeCreature(BUTTERFLY, 0,0);
        MoveResponse mr = gameManager.placeCreature(CreatureName.BUTTERFLY, 0, 2);
        assertEquals(MoveResult.MOVE_ERROR,mr.moveResult());
    }

    @Test
    void intrudingShareSpace() throws IOException{
        build();
        gameManager.placeCreature(GRASSHOPPER, 0,0);
        gameManager.placeCreature(BUTTERFLY,0,1);
        MoveResponse mr = gameManager.moveCreature(GRASSHOPPER,0,0,0,1);
        assertEquals(MoveResult.OK,mr.moveResult());
    }

    @Test
    void swapCheck() throws IOException{
        build();
        gameManager.placeCreature(CRAB, 0, 0);
        gameManager.placeCreature(CRAB, 0,1);
        MoveResponse mr = gameManager.moveCreature(CRAB,0,0,0,1);
        assertEquals(MoveResult.OK,mr.moveResult());
        assertEquals(PlayerName.BLUE,gameManager.getHexAt(0,1).getPlayerPiece());
    }

    @Test
    void runTest() throws IOException{
        build();
        gameManager.placeCreature(CreatureName.BUTTERFLY, 0, 0);//blue
        gameManager.placeCreature(CreatureName.BUTTERFLY, 0, 1);//red
        gameManager.placeCreature(CreatureName.DOVE, 0, -1);//blue
        gameManager.placeCreature(CreatureName.TURTLE, -1, 2);//red
        gameManager.placeCreature(CreatureName.TURTLE, -1, -1);//blue
        gameManager.placeCreature(CreatureName.DOVE, 0, 2);//red
        gameManager.placeCreature(CreatureName.DOVE, 0, -2);//blue
        gameManager.placeCreature(CreatureName.DOVE, 1, 1);//red
        gameManager.placeCreature(HUMMINGBIRD,1,-1);
        gameManager.placeCreature(HUMMINGBIRD, -1,1);
        MoveResponse mr = gameManager.moveCreature(HUMMINGBIRD,1, -1, 2,0);
        assertEquals(MoveResult.OK,mr.moveResult());
    }

    @Test
    void cantFly() throws IOException{
        setUp();
        gameManager.moveCreature(DOVE,0,-1,1,-1);
        gameManager.moveCreature(TURTLE,2,0,2,-1);
        gameManager.moveCreature(DOVE,0,-2,0,-1);
        gameManager.moveCreature(TURTLE,-1,2,-1,1);
        MoveResponse mr = gameManager.moveCreature(DOVE,1,-1,1,2);
        assertEquals(MoveResult.MOVE_ERROR,mr.moveResult());
    }

    @Test
    void fourthTurnButterfly() throws IOException{
        build();
        gameManager.placeCreature(CRAB, 0, 0);
        gameManager.placeCreature(CRAB, 0,1);
        gameManager.placeCreature(TURTLE, 1,-1);
        gameManager.placeCreature(TURTLE, -1, 2);
        gameManager.placeCreature(RABBIT,-1,0);
        gameManager.placeCreature(RABBIT,-1,1);
        MoveResponse mr = gameManager.placeCreature(DOVE,1,-2);
        assertEquals(MoveResult.MOVE_ERROR,mr.moveResult());
    }

    @Test
    void kamikazeSuccessTest() throws IOException{
        build();
        gameManager.placeCreature(SPIDER,0,0);
        gameManager.placeCreature(SPIDER, 0,1);
        MoveResponse mr =gameManager.moveCreature(SPIDER,0,0,0,1);
        assertEquals(MoveResult.OK,mr.moveResult());
    }

    @Test
    void kamikazeOneCreatureLeft() throws IOException{
        build();
        gameManager.placeCreature(SPIDER,0,0);
        gameManager.placeCreature(SPIDER, 0,1);
        gameManager.placeCreature(DOVE,1,0);
        MoveResponse mr =gameManager.moveCreature(SPIDER,0,1,1,0);
        assertEquals(MoveResult.OK,mr.moveResult());
    }

    @Test
    void kamikazeFail() throws IOException{
        build();
        gameManager.placeCreature(SPIDER,0,0);
        gameManager.placeCreature(SPIDER, 0,1);
        gameManager.placeCreature(DOVE,1,0);
        gameManager.placeCreature(DOVE, -1,2);
        MoveResponse mr =gameManager.moveCreature(SPIDER,0,0,0,1);
        assertEquals(MoveResult.MOVE_ERROR,mr.moveResult());
    }

    @Test
    void redKamiFail() throws IOException{
        build();
        gameManager.placeCreature(SPIDER,0,0);
        gameManager.placeCreature(SPIDER, 0,1);
        gameManager.placeCreature(BUTTERFLY,1,0);
        gameManager.placeCreature(BUTTERFLY, -1,2);
        gameManager.placeCreature(TURTLE,0,-1);
        MoveResponse mr =gameManager.moveCreature(SPIDER,0,1,1,0);
        assertEquals(MoveResult.MOVE_ERROR,mr.moveResult());
    }

    @Test
    void drawGame() throws IOException{
        build();
        gameManager.placeCreature(BUTTERFLY, 0, 0);
        gameManager.placeCreature(BUTTERFLY, 1, 0);
        gameManager.placeCreature(GRASSHOPPER, -1, 0);
        gameManager.placeCreature(GRASSHOPPER, 1, 1);
        gameManager.placeCreature(GRASSHOPPER, 0, -1);
        gameManager.placeCreature(GRASSHOPPER, 2, 0);
        gameManager.placeCreature(GRASSHOPPER, 0, 1);
        gameManager.placeCreature(GRASSHOPPER,-1,1);
        gameManager.placeCreature(GRASSHOPPER, 2, -1);
        MoveResponse mr = gameManager.placeCreature(GRASSHOPPER, 1, -1);
        assertEquals(MoveResult.DRAW,mr.moveResult());
    }
//Misc tests to fill out branch coverage
    @Test
    void canReachTest() throws IOException{
        build();
        gameManager.placeCreature(BUTTERFLY, 0, 0);
        gameManager.canReach(0,0,1,0);
        assertTrue(gameManager.canReach(0,0,1,0));
    }

    @Test
    void placeErrors() throws IOException{
        makeFirstMoves();
        MoveResponse mr = gameManager.placeCreature(BUTTERFLY, 5,5);
        assertEquals(new MoveResponse(MoveResult.MOVE_ERROR,"PLACEMENT DISCONNECTED"),mr);
        mr = gameManager.placeCreature(BUTTERFLY, 0,0);
        assertEquals(new MoveResponse(MoveResult.MOVE_ERROR,"SPOT NOT AVAILABLE"),mr);
        mr = gameManager.placeCreature(HORSE, 0,-1);
        assertEquals(new MoveResponse(MoveResult.MOVE_ERROR,"CREATURE DNE"),mr);
    }

    @Test
    void movementErrors() throws IOException{
        setUp();
        MoveResponse mr =gameManager.moveCreature(SPIDER,1,-1,0,0);
        assertEquals(new MoveResponse(MoveResult.MOVE_ERROR, "PIECE IS MISSING"),mr);
        mr =gameManager.moveCreature(BUTTERFLY,0,0,0,0);
        assertEquals(new MoveResponse(MoveResult.MOVE_ERROR, "CANNOT MOVE TO SAME SPOT"),mr);
        mr =gameManager.moveCreature(DOVE,0,-2,-1,-1);
        assertEquals(new MoveResponse(MoveResult.MOVE_ERROR, "SPOT IS OCCUPIED"),mr);
        mr =gameManager.moveCreature(DOVE,0,-2,0,5);
        assertEquals(new MoveResponse(MoveResult.MOVE_ERROR, "SPOT TOO FAR"),mr);
        mr =gameManager.moveCreature(BUTTERFLY,0,-2,-1,-1);
        assertEquals(new MoveResponse(MoveResult.MOVE_ERROR, "INCORRECT CREATURE MOVEMENT"),mr);

    }
}
