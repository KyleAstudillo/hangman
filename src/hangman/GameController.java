package hangman;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class GameController {

	private static final Logger logger = LogManager.getLogger("Game");

    //Game Code
	private final ExecutorService executorService;
	private final Game game;

	
	public GameController(Game game) {
		this.game = game;
		executorService = Executors.newSingleThreadExecutor(new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				Thread thread = new Thread(r);
				thread.setDaemon(true);
				return thread;
			}
		});
	}

	@FXML
	private VBox board ;
	@FXML
	private Label statusLabel ;
	@FXML
	private Label userInputLabel ;
	@FXML
	private Label enterALetterLabel ;
	@FXML
	private TextField textField ;

    public void initialize() throws IOException {
		logger.info("in initialize");
		drawHangman();
		addTextBoxListener();
		setUpStatusLabelBindings();
		setUpUserInputLabelBinding();
	}

	private void addTextBoxListener() {
		textField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
				if(newValue.length() > 0) {
					System.out.print(newValue);
					game.makeMove(newValue);
					textField.clear();
				}
			}
		});
	}

	private void setUpStatusLabelBindings() {

        logger.info("in setUpStatusLabelBindings");
		statusLabel.textProperty().bind(Bindings.format("%s", game.gameStatusProperty()));
		enterALetterLabel.textProperty().bind(Bindings.format("%s", "Enter a letter:"));
		/*	Bindings.when(
					game.currentPlayerProperty().isNotNull()
			).then(
				Bindings.format("To play: %s", game.currentPlayerProperty())
			).otherwise(
				""
			)
		);
		*/
	}

	private void setUpUserInputLabelBinding(){
		int answerLength = game.getAnswer().length();
		String placeHolder = "";
		for(int i=0; i<answerLength ;i++){
			placeHolder += "_ ";
		}
		System.out.print(javafx.scene.text.Font.getFamilies());
		userInputLabel.textProperty().bind(Bindings.format("%s", placeHolder));
	}

	private void drawHangman() {

		Line line = new Line();
		line.setStartX(25.0f);
		line.setStartY(0.0f);
		line.setEndX(25.0f);
		line.setEndY(25.0f);

		Circle c = new Circle();
		c.setRadius(10);

		board.getChildren().add(line);
		board.getChildren().add(c);

	}
		
	@FXML 
	private void newHangman() {
		game.reset();
	}

	@FXML
	private void quit() {
		board.getScene().getWindow().hide();
	}

}