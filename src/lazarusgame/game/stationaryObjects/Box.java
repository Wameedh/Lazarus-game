package lazarusgame.game.stationaryObjects;

import lazarusgame.GameConstants;

import java.awt.image.BufferedImage;


public class Box extends Stationary {


    public Box(int x, int y, BufferedImage image, int strength) {
        super(x, y, image, strength);

    }

    public void moveBoxDown() {
        assert getX() >= 0 : "X postion of box cannot be negative";
        assert getY() >= 0 : "Y postion of box cannot be negative";
        setY(getY() + 2);
    }

    public int getNextBoxDownPosition() {
        return getY() + GameConstants.BLOCK_SIZE;
    }


}
