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
    void levelTwoTests() throws IOException{
        setUp();
        gameManager.moveCreature(TURTLE,-1,-1,-1,0);
        MoveResponse mr = gameManager.moveCreature(DOVE,0,-2,0,3);
        assertEquals(MoveResult.OK,mr.moveResult());
        assertEquals(MoveResult.MOVE_ERROR, gameManager.moveCreature(BUTTERFLY,0,0,1,-1));
        gameManager=null;
        setUp();
        assertEquals(MoveResult.MOVE_ERROR, gameManager.moveCreature(DOVE,0,-1,0,3));
    }

}
