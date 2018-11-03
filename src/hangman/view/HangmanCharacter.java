package hangman.view;

import hangman.helpers.CustomColors;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Screen;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class HangmanCharacter {

    private static final Logger logger = LogManager.getLogger("HangmanCharacter");
    private float width = 0;
    private float height = 0;

    public HangmanCharacter(){
        logger.info("init constructor");
    }

    public void draw(Pane pane, int numMov, int mov, float width, float height){
        logger.info("draw method(numMov: " + numMov + ", mov: " + mov + ", width: " + width+", height: " + height + ")");
        this.width = width;
        this.height = height;
        switch (mov) {
            case 6:
                pane.getChildren().add(getLeftArm());
                break;
            case 5:
                pane.getChildren().add(getRightArm());
                break;
            case 4:
                pane.getChildren().add(getLeftLeg());
                break;
            case 3:
                pane.getChildren().add(getRightLeg());
                break;
            case 2:
                pane.getChildren().add(getBody());
                break;
            case 1:
                pane.getChildren().add(getHead());
                break;
            case 0:
                pane.getChildren().add(getNoose());
                break;
            default:
                break;
        }
    }

    public void setTranslation(){

    }

    public Circle getHead(){
        logger.info("getHead");
        Circle circle = new Circle(250, 100, 0);//head
        circle.setRadius(50);//radius of head
        circle.setStroke(Color.BLACK);//circle color
        circle.setFill(null);//makes the head empty(no brain haha)
        circle.setStrokeWidth(5);//sets the line thickness of circle (head)
        return circle;
    }

    public Line getNoose(){
        logger.info("getNoose");
        return drawLine(width/2.0f, 0.f, width/2.0f, 50.0f, "Green");
    }

    public Line getBody(){
        logger.info("getBody");
        return drawLine(width/2.0f, 250f, width/2.0f, 150f, "Red");
    }

    public Line getLeftArm(){
        logger.info("getLeftArm");
        return drawLine(width/2.0f, 200.0f, 200.0f, 170.0f, "Blue");
    }

    public Line getRightArm(){
        logger.info("getRightArm");
        return drawLine(width/2.0f, 200.0f, width/2.0f + 50.0f, 170.0f, "Yellow");
    }

    public Line getLeftLeg(){
        logger.info("getLeftLeg");
        return drawLine(width/2.0f, 250.0f,200.0f, 300.0f, "Blue");
    }

    public Line getRightLeg(){
        logger.info("getRightLeg");
        return drawLine(width/2.0f, 250.0f, width/2.0f + 50.0f, 300.0f, "Blue");
    }

    /*
    public Polygon getHat(){
        return drawLine(25.0f, 0.0f, 25.0f, 25.0f, "Blue");
    }
    */

    private Line drawLine(float startX, float startY, float endX, float endY, String color){
        Line line = new Line();
        line.setStartX(startX);
        line.setStartY(startY);
        line.setEndX(endX);
        line.setEndY(endY);
        line.setStrokeWidth(5.0f);
        line.setStyle("-fx-stroke:" + color);
        return line;
    }

}
