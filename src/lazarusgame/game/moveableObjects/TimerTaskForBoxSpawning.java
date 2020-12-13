package lazarusgame.game.moveableObjects;

import lazarusgame.GameConstants;
import lazarusgame.game.Map;

import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;
/**
 *
 * @author Wameedh Mohammed Ali
 */

public class TimerTaskForBoxSpawning extends TimerTask {

    Random rand = new Random();

    private ArrayList<Integer> boxesQueue = new ArrayList<>();
    private Map map;
    private Lazarus lazarus;
    private int spawnXOfANewBox;

    public TimerTaskForBoxSpawning(Map map, Lazarus lazarus) {
        this.map = map;
        this.lazarus = lazarus;

        this.boxesQueue.add(rand.nextInt(4) + 1);
    }


    public int getFirstBox(){
        return boxesQueue.get(0);
    }

        public void run() {

            double x = (lazarus.getX() / 40.0);

            if (lazarus.getX() < GameConstants.SCREEN_WIDTH / 2) {
                spawnXOfANewBox = (int) Math.ceil(x);
            } else {
                spawnXOfANewBox = (int) Math.floor(x);
            }
            boxesQueue.add(rand.nextInt(4) + 1);
            map.updateMapWithNewWall(boxesQueue.get(0), spawnXOfANewBox * GameConstants.BLOCK_SIZE);
            boxesQueue.remove(0);
        }
}