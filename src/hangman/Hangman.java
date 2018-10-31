package hangman;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Hangman extends Application {

	private static final Logger logger = LogManager.getLogger("Hangman");

	@Override
	public void start(final Stage primaryStage) throws IOException {
		logger.info("Starting Application");
		final Game game = new Game();
        logger.info("Loading Hangman.fxml");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Hangman.fxml"));
        logger.info("Setting Controller");
		loader.setController(new GameController(game));
		Parent root = loader.load();
		Scene scene = new Scene(root, 500, 500);
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
