package hangman;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import hangman.controller.DrawController;
import javafx.beans.binding.Bindings;
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
	
	public GameController(Game game, DrawController drawController) {
		this.game = game;
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
		logger.info("in Drawing");
    	try{
    	    drawController.init(board);
			drawController.draw(game.numOfTries(), game.getMoves());
		}catch (Exception e){

		}
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