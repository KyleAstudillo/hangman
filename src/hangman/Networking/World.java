package hangman.Networking;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.*;

public class World {

    private static final Logger logger = LogManager.getLogger("World");

    private List<GameActionEven> listeners = new ArrayList<GameActionEven>();
    private Queue<Action> allActions = new LinkedList<>();

    public synchronized void addAction(Action action){
        logger.info("addAction");
        allActions.add(action);
        for (GameActionEven gameActionEven : listeners){
            gameActionEven.actionHappen();
        }
    }

    public synchronized Action getAction() {
        logger.info("getAction");
        return allActions.poll();
    }

    public void addListener(GameActionEven toAdd) {
        logger.info("addListener");
        listeners.add(toAdd);
    }
}
