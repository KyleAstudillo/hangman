// game.java
//
//
package hangman;

import hangman.Networking.*;
import hangman.controller.DrawController;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Game implements GameActionEven {

	private static final Logger logger = LogManager.getLogger("Game");

	private String answer;
	private String tmpAnswer;
	private String[] letterAndPosArray;
	private String[] words;
	private int moves;
	private int totalMoves;
	private int index;
	private int copies;
	private final ReadOnlyObjectWrapper<GameStatus> gameStatus;
	private ObjectProperty<Boolean> gameState = new ReadOnlyObjectWrapper<Boolean>();
	private DrawController drawController;
	private NetworkHelper networkHelper;
	private World world = new World();
	private BoringClient client;
	private Thread thread;

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

	public Game(DrawController drawController, NetworkHelper networkHelper) {
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
		totalMoves = 0;
		gameState.setValue(false); // initial state
		this.drawController = drawController;
		this.networkHelper = networkHelper;
		world.addListener(this);
		if(networkHelper.getOnline()){
			client = new BoringClient(networkHelper, world);
		}
		createGameStatusBinding();
		thread = new Thread(client);
		thread.start();
	}

	@Override
	public synchronized void actionHappen() {
		logger.warn("!!!!!!!!!!!!!!!!!!!!!! Action Happen !!!!!!!!!!!!!!!!!!!!!!");
		Action action = world.getAction();
		switch (action.getActionTag()){
			case SEND:
				this.makeMove(action);
				break;
			case EXIT:
				this.reset(action);
				break;
			case RESTART:
				this.reset(action);
				break;
			default:
				break;
		}
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
				log("temporary answer is " + tmpAnswer);
				log("index is " + index);
				log("total bad moves done is " + moves);
				log("total moves done is " + totalMoves);
				if(check != null ) {
					return check;
				}

				if(tmpAnswer.trim().length() == 0 && totalMoves == 0){
					log("new game");
					return GameStatus.OPEN;
				}
				else if (index != -1){
					log("good guess");
					return GameStatus.GOOD_GUESS;
				}
				else if(index == -1 && totalMoves > 0){
					moves++;
					drawHangmanFrame();
					log("bad guess");
					return GameStatus.BAD_GUESS;
					//printHangman();
				}
				return GameStatus.BAD_GUESS;
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

	public String initializePlaceHolder(){
		String placeHolder = "";
		for(int i=0; i < answer.length() ;i++){
			placeHolder += "_ ";
		}
		return placeHolder;
	}

	public String updatePlaceHolder(String placeHolder){
		StringBuilder sb1 = new StringBuilder(placeHolder);
		for(int i=0; i < tmpAnswer.length();i++){
			if(tmpAnswer.charAt(i)!= (' ') && i == 0){
				sb1.setCharAt(i, tmpAnswer.charAt(i));
			}
			else if(tmpAnswer.charAt(i)!= (' ')){
				sb1.setCharAt(i*2, tmpAnswer.charAt(i));
			}
		}
		if(moves == numOfTries()){
			for(int i=0; i < answer.length();i++){
				if(answer.charAt(i)!= (' ') && i == 0){
					sb1.setCharAt(i, answer.charAt(i));
				}
				else if(answer.charAt(i)!= (' ')){
					sb1.setCharAt(i*2, answer.charAt(i));
				}
			}
		}
		/*
		if(index == 0 && index != -1) {
			sb1.setCharAt(index, answer.charAt(index));
		}else if (index != -1){sb1.setCharAt(index*2, answer.charAt(index));}
		*/
		return sb1.toString();
	}

	private void setRandomWord() {
		ArrayList<String> words = new ArrayList<>();
		int linecount = 0;
		try {
			BufferedReader bf = new BufferedReader(new FileReader("words.txt"));
			String line;
			while ((line = bf.readLine()) != null) {
				linecount++;
				words.add(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		int randomNum = ThreadLocalRandom.current().nextInt(0,  linecount + 1);

		/*for(String word:words){
			System.out.println(word);
		}*/
		log("in setRandomWord: ");
		//int idx = (int) (Math.random() * words.length);
		answer = words.get(randomNum);
		log("Word is " + answer);
		//answer = "apple";//words[idx].trim(); // remove new line character
	}

	private void setWord(Action action) {
		log("in setRandomWord: ");
		answer = action.getExtra();
		log("Word is " + answer);
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
		for(int i = 0; i < letterAndPosArray.length; i++) {
			if(letterAndPosArray[i].equals(input)) {
				copies++;
			}
		}
		int index;
		do{
			index = getValidIndex(input);
			if (index != -1) {
				StringBuilder sb = new StringBuilder(tmpAnswer);
				sb.setCharAt(index, input.charAt(0));
				tmpAnswer = sb.toString();
				copies--;
			}
		}while(copies != 0);
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

	//Server makeMove
	public void makeMove(Action action) {
		log("in SERVER makeMove: " + action.getExtra());
		totalMoves++;
		index = update(action.getExtra());
		// this will toggle the state of the game
		//gameState.setValue(!gameState.getValue());
		Task<Void> task = new Task<Void>() {

			@Override protected Void call() throws Exception {

				Platform.runLater(new Runnable() {
					@Override public void run() {
						gameState.setValue(!gameState.getValue());
					}
				});
				return null;
			}
		};
		task.run();
	}

	//ClientMake Move
	public void makeMove(String letter) {
		log("in CLIENT makeMove: " + letter);
		totalMoves++;
		if(networkHelper.getOnline()) {
			client.communicateActionOut(new Action(networkHelper.getUsername(),
					ActionTag.SEND, String.valueOf(letter.charAt(0))));
		}
		index = update(letter);
		// this will toggle the state of the game
		gameState.setValue(!gameState.getValue());
	}

	//Server Reset()
	public void reset(Action action) {
		setWord(action);
		prepTmpAnswer();
		prepLetterAndPosArray();
		moves = 0;
		gameState.setValue(false); // initial state
		createGameStatusBinding();
		drawHangmanFrame();
	}

	//Client Reset
	public void reset() {
		/*gameStatus.addListener(new ChangeListener<GameStatus>() {
			@Override
			public void changed(ObservableValue<? extends GameStatus> observable,
								GameStatus oldValue, GameStatus newValue) {
				if (gameStatus.get() != GameStatus.OPEN) {
					log("in Game: in changed");
					//currentPlayer.set(null);
				}
			}
		});*/

		setRandomWord();
		prepTmpAnswer();
		prepLetterAndPosArray();
		moves = 0;
		totalMoves = 0;
		gameState.setValue(false); // initial state
		//this.drawController = drawController;
		if(networkHelper.getOnline()) {
			client.communicateActionOut(new Action(networkHelper.getUsername(), ActionTag.RESTART, answer));
		}
		createGameStatusBinding();
		drawHangmanFrame();
	}

	public int numOfTries() {
		return 7;
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