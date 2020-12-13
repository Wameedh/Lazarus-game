package lazarusgame.game.stationaryObjects;


import java.awt.*;
import java.awt.image.BufferedImage;


/**
 *
 * @author Wameedh Mohammed Ali
 */
public abstract class Stationary {

    private int x;
    private int y;



    private BufferedImage image; // This Image could be anything depending on which class is extending this abstract class

    private int strength;

    public Stationary(int x, int y, BufferedImage image, int strength) {
        this.x = x;
        this.y = y;
        this.image = image;
        this.strength = strength;
    }

    /*********************************
                   SETTERS
     *******************************/

    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }

    /*********************************
                  GETTERS
     *******************************/

    public int getStrength(){ return this.strength; }


    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }


    public void drawImage(Graphics g){
        g.drawImage(image,x,y,null);
    }




}
