package hangman.controller;
/**
 * @author Kyle Astudillo
 * @date: 11/3/2018
 * @description: Controller for drawing objects to the screen
 */
import hangman.view.HangmanCharacter;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class DrawController {

    private static final Logger logger = LogManager.getLogger("DrawController");

    HangmanCharacter man = new HangmanCharacter();
    Pane pane = new Pane();
    VBox board;
    private float width;
    private float height;

    public DrawController(){
        logger.info("in constructor");
    }

    //init is called after GameController has initialized but before the viewport is init
    public void init(VBox board){
        this.board = board;
        man.init(pane);
    }

    public void draw(int numMov, int mov){
        logger.info("in draw");

        //Attempted not to hard code this but it was hard because
        //this method is called before viewport init
        width = 500.0f;
        height = 500.0f;
        pane.setPrefWidth(width);
        pane.setPrefHeight(height);
        man.draw(numMov, mov, width, height);
        if(!board.getChildren().contains(pane)) {
            board.getChildren().add(pane);
        }
    }
}
