package hangman;

import hangman.controller.DrawController;
import javafx.beans.Observable;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Game {

	private static final Logger logger = LogManager.getLogger("Game");

	private String answer;
	private String tmpAnswer;
	private String[] letterAndPosArray;
	private String[] words;
	private int moves;
	private int index;
	private final ReadOnlyObjectWrapper<GameStatus> gameStatus;
	private ObjectProperty<Boolean> gameState = new ReadOnlyObjectWrapper<Boolean>();
	private DrawController drawController;

	public enum GameStatus {
		GAME_OVER {
			@Override
			public String toString() {
				return "Game over!";
			}
		},
		BAD_GUESS {
			@Override
			public String toString() { return "Bad guess..."; }
		},
		GOOD_GUESS {
			@Override
			public String toString() {
				return "Good guess!";
			}
		},
		WON {
			@Override
			public String toString() {
				return "You won!";
			}
		},
		OPEN {
			@Override
			public String toString() {
				return "Game on, let's go!";
			}
		}
	}

	public Game(DrawController drawController) {
		gameStatus = new ReadOnlyObjectWrapper<GameStatus>(this, "gameStatus", GameStatus.OPEN);
		gameStatus.addListener(new ChangeListener<GameStatus>() {
			@Override
			public void changed(ObservableValue<? extends GameStatus> observable,
								GameStatus oldValue, GameStatus newValue) {
				if (gameStatus.get() != GameStatus.OPEN) {
					log("in Game: in changed");
					//currentPlayer.set(null);
				}
			}
		});
		setRandomWord();
		prepTmpAnswer();
		prepLetterAndPosArray();
		moves = 0;
		gameState.setValue(false); // initial state
        this.drawController = drawController;
		createGameStatusBinding();
	}

	private void createGameStatusBinding() {
		List<Observable> allObservableThings = new ArrayList<>();
		ObjectBinding<GameStatus> gameStatusBinding = new ObjectBinding<GameStatus>() {
			{
				super.bind(gameState);
			}
			@Override
			public GameStatus computeValue() {
				log("in computeValue");
				GameStatus check = checkForWinner(index);
				if(check != null ) {
					return check;
				}

				if(tmpAnswer.trim().length() == 0){
					log("new game");
					return GameStatus.OPEN;
				}
				else if (index != -1){
					log("good guess");
					return GameStatus.GOOD_GUESS;
				}
				else {
					moves++;
					log("bad guess");
					return GameStatus.BAD_GUESS;
					//printHangman();
				}
			}
		};
		gameStatus.bind(gameStatusBinding);
	}

	public ReadOnlyObjectProperty<GameStatus> gameStatusProperty() {
		return gameStatus.getReadOnlyProperty();
	}
	public GameStatus getGameStatus() {
		return gameStatus.get();
	}

	private void setRandomWord() {
        log("in setRandomWord: ");
		//int idx = (int) (Math.random() * words.length);
		answer = "apple";//words[idx].trim(); // remove new line character
	}

	private void prepTmpAnswer() {
        log("in PrepTempAnswer: ");
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < answer.length(); i++) {
			sb.append(" ");
		}
		tmpAnswer = sb.toString();
	}

	private void prepLetterAndPosArray() {
        log("in prepLetterAndPosArray: ");
		letterAndPosArray = new String[answer.length()];
		for(int i = 0; i < answer.length(); i++) {
			letterAndPosArray[i] = answer.substring(i,i+1);
		}
	}

	private int getValidIndex(String input) {
        log("in getValidIndex: ");
		int index = -1;
		for(int i = 0; i < letterAndPosArray.length; i++) {
			if(letterAndPosArray[i].equals(input)) {
				index = i;
				letterAndPosArray[i] = "";
				break;
			}
		}
		return index;
	}

	private int update(String input) {
        log("in update: ");
		int index = getValidIndex(input);
		if(index != -1) {
			StringBuilder sb = new StringBuilder(tmpAnswer);
			sb.setCharAt(index, input.charAt(0));
			tmpAnswer = sb.toString();
		}
		return index;
	}

	private void drawHangmanFrame() {
        log("in DrawHangmanFrame");
        try {
            log("*** Drawing");
            if(this.numOfTries() >= this.getMoves()) {
                drawController.draw(this.numOfTries(), this.getMoves());
            }
        } catch (Exception e){
            log("*** Drawing Error!!!!");
            log(e.toString());
        }
    }

	public void makeMove(String letter) {
		log("in makeMove: " + letter);
		index = update(letter);
		// this will toggle the state of the game
		gameState.setValue(!gameState.getValue());
	}

	public void reset() {
		gameStatus.addListener(new ChangeListener<GameStatus>() {
			@Override
			public void changed(ObservableValue<? extends GameStatus> observable,
								GameStatus oldValue, GameStatus newValue) {
				if (gameStatus.get() != GameStatus.OPEN) {
					log("in Game: in changed");
					//currentPlayer.set(null);
				}
			}
		});
		setRandomWord();
		prepTmpAnswer();
		prepLetterAndPosArray();
		moves = 0;
		gameState.setValue(false); // initial state
		this.drawController = drawController;
		createGameStatusBinding();
	}

	public int numOfTries() {
		return 6; // TODO, fix me
	}
	public int getMoves(){return moves;}

	public static void log(String s) {
        logger.info(s);
	}

	public String getAnswer(){
		return answer;
	}

	private GameStatus checkForWinner(int status) {
		log("in checkForWinner");
		drawHangmanFrame();
		if(tmpAnswer.equals(answer)) {
			log("won");
			return GameStatus.WON;
		}
		else if(moves == numOfTries()) {
			log("game over");
			return GameStatus.GAME_OVER;
		}
		else {
			return null;
		}

	}
}
