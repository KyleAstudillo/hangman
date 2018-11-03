import hangman.Hangman;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestGame {
    private static final Logger logger = LogManager.getLogger("TestGame");

    @Test
    public void TestWinGame(){
        Hangman hangman = new Hangman(false);
        assertNotNull("Not Null");
    }
}
