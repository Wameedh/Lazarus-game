package lazarusgame.game.moveableObjects;

import java.awt.image.BufferedImage;

/**
 *
 * @author Wameedh Mohammed Ali
 */
public abstract class Moveable {

    private int x;
    private int y;

    private BufferedImage image;

    public Moveable(int x, int y, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.image = image;
    }


    /**********************************
                   SETTERS
     *********************************/

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }

    public void setImage(BufferedImage image){ this.image = image; }

    /**********************************
                  GETTERS
     *********************************/

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }


    public BufferedImage getImage(){ return image; }


}
