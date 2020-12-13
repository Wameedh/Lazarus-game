package lazarusgame.game;

import lazarusgame.GameConstants;
import lazarusgame.game.stationaryObjects.Box;

import java.util.ArrayList;

/**
 *
 * @author saengduean8
 *  https://github.com/saengduean8/LazarusGame/tree/master/src/component
 *
 *  WITH SOME MODIFCATIONS TO THE CODE DONE BY ME, WAMEEDH MOHAMMED ALI
 */

public class CollisionControl {

    private ArrayList<ArrayList<Integer>> map;

    public CollisionControl(ArrayList<ArrayList<Integer>> map) {
        this.map = map;
    }

    public boolean validateLazarusCollision(int newX, int newY) {
        return validateLazarusToWallCollision(newX, newY) || validateLazarustoBoxesCollision(newX, newY) || validateLazarusToBoundaryCollision(newX);
    }

    private boolean validateLazarusToBoundaryCollision(int newX){
        return (newX < 0 || newX == GameConstants.SCREEN_WIDTH);
    }

    private boolean validateLazarusToWallCollision(int newX, int newY){
        int value = getMapping(newX, newY);
        return value == 6;
    }

    public boolean validateLazarustoBoxesCollision(int newX, int newY){
        int value = getMapping(newX, newY);
        return value == 1 || value == 2 || value == 3 || value == 4;
    }

    /**
     * Box -> Map
     */
    public int getMapping(int newX, int newY) {
        int boxX = newX / GameConstants.BLOCK_SIZE;
        int boxY = newY / GameConstants.BLOCK_SIZE;

        Integer value = map.get(boxY).get(boxX);
        return value;
    }

    public boolean validateBoxToWallCollision(Box box) {
        int newX = box.getX();
        int newY = box.getNextBoxDownPosition();

        int value = getMapping(newX, newY);

        return value == 6;
    }

    public boolean validateBoxToBoxCollision(Box box) {
        int newX = box.getX();
        int newY = box.getNextBoxDownPosition();

        int value = getMapping(newX, newY);

        if(value == 1 || value == 2 || value == 3 || value == 4) {
            return true;
        }
        return false;
    }

    public boolean validateLazarusToStopCollision(int lazX, int lazY) {
        int value = getMapping(lazX, lazY);
        return value == 5;
    }
}