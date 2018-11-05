package hangman.view;
/**
 * @author Kyle Astudillo
 * @date: 11/3/2018
 * @description: All variables for controlling the man Drawing
 */
import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class HangmanCharacter {

    private static final Logger logger = LogManager.getLogger("HangmanCharacter");

    //Tried to not hard code this
    private float width = 0;
    private float height = 0;
    private String color = "EE7AEB";


    //Nodes
    private Circle head;
    private Line body;
    private Line leftArm;
    private Line rightArm;
    private Line leftLeg;
    private Line rightLeg;
    private Line noose;

    //Animation Translations
    private TranslateTransition transitionHead = new TranslateTransition();
    private TranslateTransition transitionbody = new TranslateTransition();
    private TranslateTransition transitionleftArm = new TranslateTransition();
    private TranslateTransition transitionrightArm = new TranslateTransition();
    private TranslateTransition transitionleftLeg = new TranslateTransition();
    private TranslateTransition transitionrightLeg = new TranslateTransition();
    private TranslateTransition transitionNoose = new TranslateTransition();




    public HangmanCharacter(){
        logger.info("init constructor");

        //Hard Code
        width = 500;
        height = 500;

        //head
        head = new Circle(250, 100, 0);//head
        head.setRadius(50);//radius of head
        head.setStyle("-fx-stroke:" + color);//circle color
        head.setFill(null);//makes the head empty(no brain haha)
        head.setStrokeWidth(5);//sets the line thickness of circle (head)

        //body parts
        noose = drawLine(width/2.0f, -300.f, width/2.0f, 50.0f, color);
        body = drawLine(width/2.0f, 250f, width/2.0f, 150f, color);
        leftArm = drawLine(width/2.0f, 200.0f, 200.0f, 170.0f, color);
        rightArm = drawLine(width/2.0f, 200.0f, width/2.0f + 50.0f, 170.0f, color);
        leftLeg = drawLine(width/2.0f, 250.0f,200.0f, 300.0f, color);
        rightLeg = drawLine(width/2.0f, 250.0f, width/2.0f + 50.0f, 300.0f, color);

        //Setup translations
        setTransition(transitionHead, head);
        setTransition(transitionbody, body);
        setTransition(transitionleftArm, leftArm);
        setTransition(transitionrightArm, rightArm);
        setTransition(transitionleftLeg, leftLeg);
        setTransition(transitionrightLeg, rightLeg);
        setTransition(transitionNoose, noose);
    }

    public void draw(Pane pane, int numMov, int mov, float width, float height){
        logger.info("draw method(numMov: " + numMov + ", mov: " + mov + ", width: " + width+", height: " + height + ")");
        this.width = width;
        this.height = height;
        switch (mov) {
            case 6:
                pane.getChildren().add(getLeftArm());
                playTranslation();
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
            case 0: //was working but stopped drawing but no that much of a problem
                pane.getChildren().add(getNoose());
                break;
            default:
                break;
        }
    }

    public void setTransition(TranslateTransition transition, Node node){
        transition.setDuration(Duration.seconds(3.0));
        transition.setToX(0);
        transition.setToY(50);
        transition.setAutoReverse(true);
        transition.setCycleCount(Animation.INDEFINITE);
        transition.setNode(node);
    }

    public void playTranslation(){
        logger.info("***** SET TRANSLATION");
        transitionHead.play();
        transitionbody.play();
        transitionleftArm.play();
        transitionrightArm.play();
        transitionleftLeg.play();
        transitionrightLeg.play();
        transitionNoose.play();
    }

    public Circle getHead(){
        logger.info("getHead");
        return head;
    }

    public Line getNoose(){
        logger.info("getNoose");
        return noose;
    }

    public Line getBody(){
        logger.info("getBody");
        return body;
    }

    public Line getLeftArm(){
        logger.info("getLeftArm");
        return leftArm;
    }

    public Line getRightArm(){
        logger.info("getRightArm");
        return rightArm;
    }

    public Line getLeftLeg(){
        logger.info("getLeftLeg");
        return leftLeg;
    }

    public Line getRightLeg(){
        logger.info("getRightLeg");
        return rightLeg;
    }

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