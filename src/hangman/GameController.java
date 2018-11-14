//	Game Controller
//
//
package hangman;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import hangman.controller.DrawController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class GameController {

	private static final Logger logger = LogManager.getLogger("GameController");

	//Game Code
	private final ExecutorService executorService;
	private final Game game;
	public String placeHolder;
	public SimpleStringProperty ssp;

	public GameController(Game game, DrawController drawController) {
		this.game = game;
		this.game.init(this);
		executorService = Executors.newSingleThreadExecutor(new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				Thread thread = new Thread(r);
				thread.setDaemon(true);
				return thread;
			}
		});
		this.drawController = drawController;
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


	//Controllers
	private DrawController drawController;

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
				if(!newValue.isEmpty()) {
					String testValue = newValue.substring(oldValue.length());
					boolean test = false;
					for (int i = 0; i < oldValue.length() && !oldValue.equals(""); i++) {
						if (testValue.charAt(0) == oldValue.charAt(i)) {
							test = true;
							break;
						}
					}
					if (newValue.length() > 0 && newValue.length() > oldValue.length() && test == false) {
                        game.makeMove(testValue);
                        // updates placeholder when textfield changes
                        placeHolder = game.updatePlaceHolder(placeHolder);
                        // rebinds it to the placeholder with _ replaced with letter
                        userInputLabel.textProperty().bindBidirectional(new SimpleStringProperty(placeHolder));
                        //textField.clear();

                    }
				}

			}
		});
	}

	private void setUpStatusLabelBindings() {

		logger.info("in setUpStatusLabelBindings");
		statusLabel.textProperty().bind(Bindings.format("%s", game.gameStatusProperty()));
		enterALetterLabel.textProperty().bind(Bindings.format("%s", "Enter a letter:"));

		/*
		Bindings.when(
					game.currentPlayerProperty().isNotNull()
			).then(
				Bindings.format("To play: %s", game.currentPlayerProperty())
			).otherwise(
				""
			)
		);
		*/
	}

	private void setUpUserInputLabelBinding(){ // sets the placeholder to have equal number of _ as letters in answer
		placeHolder = game.initializePlaceHolder();
		ssp = new SimpleStringProperty(placeHolder);
		userInputLabel.textProperty().bindBidirectional(ssp);
	}

	/**
	 * drawHangman objects before the secreen is displayed!
	 * and Set initial variables within the DrawCrontroller Class
	 */
	private void drawHangman() {
		logger.info("in Drawing");
		try{
			drawController.init(board);
			drawController.draw(game.numOfTries(), game.getMoves());
		}catch (Exception e){

		}
	}

	@FXML
	public void newHangman() { // resets the game and starts the game with a new placeholder fro the word
		game.reset();
		setUpUserInputLabelBinding();
		textField.clear();
	}

    public void serverNewHangman() { // resets the game and starts the game with a new placeholder fro the word
        setUpUserInputLabelBinding();
        textField.clear();
    }

	@FXML
	private void quit() {
		board.getScene().getWindow().hide();
	}

}