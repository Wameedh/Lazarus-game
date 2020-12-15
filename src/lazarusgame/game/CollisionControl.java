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

    int lazarusStrength = GameConstants.LAZARUS_STRENGTH;
    int wallStrength = GameConstants.WALL_STRENGTH;
    int cardboardBoxStrength = GameConstants.CARDBOARD_BOX_STRENGTH;
    int woodBoxStrength = GameConstants.WOOD_BOX_STRENGTH;
    int metalBoxStrength = GameConstants.METAL_BOX_STRENGTH;
    int stoneBoxStrength = GameConstants.STONE_BOX_STRENGTH;
    int powerButtonStrength = GameConstants.POWER_BUTTON_BOX_STRENGTH;

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
        return value == wallStrength;
    }

    public boolean validateLazarustoBoxesCollision(int newX, int newY){
        int value = getMapping(newX, newY);
        return value == cardboardBoxStrength || value == woodBoxStrength || value == metalBoxStrength || value == stoneBoxStrength;
    }

    /**
     * Box -> Map
     */
    public int getMapping(int newX, int newY) {
        int boxX = newX / GameConstants.BLOCK_SIZE;
        int boxY = newY / GameConstants.BLOCK_SIZE;

        return map.get(boxY).get(boxX);
    }

    public boolean validateBoxToWallCollision(Box box) {
        int newX = box.getX();
        int newY = box.getNextBoxDownPosition();

        int value = getMapping(newX, newY);

        return value == wallStrength;
    }

    public boolean validateBoxToBoxCollision(Box box) {
        int newX = box.getX();
        int newY = box.getNextBoxDownPosition();

        int value = getMapping(newX, newY);
        return value == cardboardBoxStrength || value == woodBoxStrength || value == metalBoxStrength || value == stoneBoxStrength;
    }
    public boolean validateBoxToLazarusCollision(Box box) {
        int newX = box.getX();
        int newY = box.getNextBoxDownPosition();

        int value = getMapping(newX, newY);
        return value == lazarusStrength;
    }

    public boolean validateLazarusToStopCollision(int lazX, int lazY) {
        int value = getMapping(lazX, lazY);
        return value == powerButtonStrength;
    }
}