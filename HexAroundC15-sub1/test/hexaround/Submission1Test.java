package hexaround;

import hexaround.config.GameConfiguration;
import hexaround.config.HexAroundConfigurationMaker;
import hexaround.game.*;
import hexaround.required.CreatureProperty;
import org.junit.jupiter.api.*;

import java.io.*;

import static hexaround.required.CreatureName.*;
import static org.junit.jupiter.api.Assertions.*;

public class Submission1Test {
    HexAroundFirstSubmission gameManager = null;


    void build() throws IOException {
        String hgcFile = "HexAroundC15-sub1/testConfigurations/FirstConfiguration.hgc";
        this.gameManager =
                (HexAroundFirstSubmission) HexAroundGameBuilder.buildGameManager(
                        "HexAroundC15-sub1/testConfigurations/FirstConfiguration.hgc");
    }

    @Test
    void firstTest() throws IOException {
        build();
        gameManager.placeCreature(GRASSHOPPER, 5, 42);
        assertEquals(GRASSHOPPER,gameManager.getCreatureAt(5, 42));
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
        assertTrue(gameManager.isOccupied(5,42));
    }

    @Test
    void propertyTestFalse() throws IOException{
        build();
        gameManager.placeCreature(GRASSHOPPER, 5, 42);
        assertFalse(gameManager.hasProperty(5,42, CreatureProperty.WALKING));
    }

    @Test
    void propertyTestTrue() throws IOException{
        build();
        gameManager.placeCreature(GRASSHOPPER, 5, 42);
        assertTrue(gameManager.hasProperty(5,42, CreatureProperty.JUMPING));
        assertTrue(gameManager.hasProperty(5,42, CreatureProperty.INTRUDING));
    }

    @Test
    void canReachFalse() throws IOException{
        build();
        gameManager.placeCreature(GRASSHOPPER, 5, 42);
        assertFalse(gameManager.canReach(5,42,5,50));
    }

    @Test
    void canReachTrue() throws IOException{
        build();
        gameManager.placeCreature(GRASSHOPPER, 5, 42);
        assertTrue(gameManager.canReach(5,42,5,45));
    }


}
