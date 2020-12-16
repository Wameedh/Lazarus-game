package lazarusgame.game.moveableObjects;

import lazarusgame.GameConstants;
import lazarusgame.game.Map;

import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;

/**
 * @author Wameedh Mohammed Ali
 */

public class TimerTaskForBoxSpawning extends TimerTask {

    Random rand = new Random();

    private final ArrayList<Integer> boxesQueue = new ArrayList<>();
    private final Map map;
    private final Lazarus lazarus;

    public TimerTaskForBoxSpawning(Map map, Lazarus lazarus) {
        this.map = map;
        this.lazarus = lazarus;
        this.boxesQueue.add(getRandomNumber());
    }


    public int getFirstBox() {
        return boxesQueue.get(0);
    }

    private int getRandomNumber() {
        // returns random number between 2-5 inclusive
        return rand.nextInt(4) + 2;
    }

    @Override
    public void run() {

        double x = (lazarus.getX() / 40.0);
        int spawnXOfANewBox;
        if (lazarus.getX() < GameConstants.SCREEN_WIDTH / 2) {
            spawnXOfANewBox = (int) Math.ceil(x);
        } else {
            spawnXOfANewBox = (int) Math.floor(x);
        }
        boxesQueue.add(getRandomNumber());
        map.updateMapWithNewWall(boxesQueue.get(0), spawnXOfANewBox * GameConstants.BLOCK_SIZE);
        boxesQueue.remove(0);
    }
}