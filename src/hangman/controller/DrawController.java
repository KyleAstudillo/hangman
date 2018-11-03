package hangman.controller;

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

    public DrawController(){
        logger.info("in constructor");
    }

    public void init(VBox board){
        this.board = board;
    }

    public void draw(int numMov, int mov){
        logger.info("in draw");
        float width;
        float height;
        width = 500.0f;
        height = 500.0f;
        pane.setPrefWidth(width);
        pane.setPrefHeight(height);
        man.draw(pane, numMov, mov, width, height);
        if(!board.getChildren().contains(pane)) {
            board.getChildren().add(pane);
        }
    }
}
