package hangman;

import java.io.IOException;

import hangman.Networking.NetworkHelper;
import hangman.controller.DrawController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Hangman extends Application {

	private static final Logger logger = LogManager.getLogger("Hangman");
	private boolean show = true;
	private int width = 500;
	private int height = 500;
	NetworkHelper networkHelper = new NetworkHelper("127.0.0.1", true, "Reed", 9003);

    public Hangman(boolean show){
        this.show = show;
        launch();
    }

    public Hangman(){
    }

	@Override
	public void start(final Stage primaryStage) throws IOException {
        DrawController drawController = new DrawController();
		logger.info("Starting Application");
		final Game game = new Game(drawController, networkHelper);
        logger.info("Loading Hangman.fxml");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Hangman.fxml"));
        logger.info("Setting Controller");
		loader.setController(new GameController(game, drawController));
		Parent root = loader.load();
		Scene scene = new Scene(root, width, height);
        logger.info("Loading Hangman.css");
		scene.getStylesheets().add(getClass().getResource("Hangman.css").toExternalForm());
		primaryStage.setScene(scene);
        logger.info("Presenting Application");
        primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
