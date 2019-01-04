package test;

import homework4.Creature;
import homework4.Game;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

public class GameTest {
    @Test
    public void gameTest(){
        Game game = new Game();
        for(int i=0;i<10;i++){
            assertTrue(game.findCreatureByName(String.valueOf(i))==null);
        }
    }
}
