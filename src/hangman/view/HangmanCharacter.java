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
    private String hiddenColor = "aqua";


    Pane pane;
    //Nodes
    private Circle head;
    private Line body;
    private Line leftArm;
    private Line rightArm;
    private Line leftLeg;
    private Line rightLeg;
    private Line noose;
    private Line noose2;

    //Animation Translations
    private TranslateTransition transitionHead = new TranslateTransition();
    private TranslateTransition transitionbody = new TranslateTransition();
    private TranslateTransition transitionleftArm = new TranslateTransition();
    private TranslateTransition transitionrightArm = new TranslateTransition();
    private TranslateTransition transitionleftLeg = new TranslateTransition();
    private TranslateTransition transitionrightLeg = new TranslateTransition();
    private TranslateTransition transitionNoose = new TranslateTransition();




    public HangmanCharacter(){
        //logger.info("init constructor");

        //Hard Code
        width = 500;
        height = 500;

        //head
        head = new Circle(250, 100, 0);//head
        head.setRadius(50);//radius of head
        head.setStyle("-fx-stroke:" + hiddenColor);//circle color
        head.setFill(null);//makes the head empty(no brain haha)
        head.setStrokeWidth(5);//sets the line thickness of circle (head)

        //body parts
        noose = drawLine(width/2.0f, 0.f, width/2.0f, 50.0f, hiddenColor);
        noose2 = drawLine(width/2.0f, 0.f, width/2.0f, 50.0f, hiddenColor);
        body = drawLine(width/2.0f, 250f, width/2.0f, 150f, hiddenColor);
        leftArm = drawLine(width/2.0f, 200.0f, 200.0f, 170.0f, hiddenColor);
        rightArm = drawLine(width/2.0f, 200.0f, width/2.0f + 50.0f, 170.0f, hiddenColor);
        leftLeg = drawLine(width/2.0f, 250.0f,200.0f, 300.0f, hiddenColor);
        rightLeg = drawLine(width/2.0f, 250.0f, width/2.0f + 50.0f, 300.0f, hiddenColor);

        //Setup translations
        setTransition(transitionHead, head);
        setTransition(transitionbody, body);
        setTransition(transitionleftArm, leftArm);
        setTransition(transitionrightArm, rightArm);
        setTransition(transitionleftLeg, leftLeg);
        setTransition(transitionrightLeg, rightLeg);
        setTransition(transitionNoose, noose);
    }

    public void init(Pane pane){
        this.pane = pane;
        pane.getChildren().add(getNoose());
        pane.getChildren().add(getNoose2());
        pane.getChildren().add(getRightLeg());
        pane.getChildren().add(getLeftLeg());
        pane.getChildren().add(getRightArm());
        pane.getChildren().add(getLeftArm());
        pane.getChildren().add(getBody());
        pane.getChildren().add(getHead());
    }

    public void draw(int numMov, int mov, float width, float height){
        //logger.info("draw method(numMov: " + numMov + ", mov: " + mov + ", width: " + width+", height: " + height + ")");
        this.width = width;
        this.height = height;
        switch (mov) {
            case 7:
                getLeftArm().setStyle("-fx-stroke:" + color);
                playTranslation();
                break;
            case 6:
                getRightArm().setStyle("-fx-stroke:" + color);
                break;
            case 5:
                getLeftLeg().setStyle("-fx-stroke:" + color);
                break;
            case 4:
                getRightLeg().setStyle("-fx-stroke:" + color);
                break;
            case 3:
                getBody().setStyle("-fx-stroke:" + color);
                break;
            case 2:
                getHead().setStyle("-fx-stroke:" + color);
                break;
            case 1: //was working but stopped drawing but no that much of a problem
                getNoose().setStyle("-fx-stroke:" + color);
                getNoose2().setStyle("-fx-stroke:" + color);
                break;
            case 0:
                stopTranslation();
                getNoose().setStyle("-fx-stroke:" + hiddenColor);
                getNoose2().setStyle("-fx-stroke:" + hiddenColor);
                getLeftArm().setStyle("-fx-stroke:" + hiddenColor);
                getRightArm().setStyle("-fx-stroke:" + hiddenColor);
                getLeftLeg().setStyle("-fx-stroke:" + hiddenColor);
                getRightLeg().setStyle("-fx-stroke:" + hiddenColor);
                getBody().setStyle("-fx-stroke:" + hiddenColor);
                getHead().setStyle("-fx-stroke:" + hiddenColor);
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
        //logger.info("***** SET TRANSLATION");
        transitionHead.play();
        transitionbody.play();
        transitionleftArm.play();
        transitionrightArm.play();
        transitionleftLeg.play();
        transitionrightLeg.play();
        transitionNoose.play();
    }

    public void stopTranslation(){
        //logger.info("***** SET TRANSLATION");
        transitionHead.pause();
        transitionbody.pause();
        transitionleftArm.pause();
        transitionrightArm.pause();
        transitionleftLeg.pause();
        transitionrightLeg.pause();
        transitionNoose.pause();
    }

    public Circle getHead(){
        //logger.info("getHead");
        return head;
    }

    public Line getNoose(){
        //logger.info("getNoose");
        return noose;
    }
    public Line getNoose2(){
        //logger.info("getNoose2");
        return noose2;
    }

    public Line getBody(){
        //logger.info("getBody");
        return body;
    }

    public Line getLeftArm(){
        //logger.info("getLeftArm");
        return leftArm;
    }

    public Line getRightArm(){
        //logger.info("getRightArm");
        return rightArm;
    }

    public Line getLeftLeg(){
        //logger.info("getLeftLeg");
        return leftLeg;
    }

    public Line getRightLeg(){
        //logger.info("getRightLeg");
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
