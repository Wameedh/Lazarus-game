package lazarusgame.game.moveableObjects;

import lazarusgame.game.CollidableObject;
import lazarusgame.game.stationaryObjects.Stationary;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Cardboard extends Stationary {

    private boolean boxOnTop;
    private boolean boxUnderStatus;

    public Cardboard(int x, int y, BufferedImage image, boolean boxOnTop, int strength) {
        super(x, y, image,boxOnTop, strength);
        this.boxOnTop = boxOnTop;
        this.boxUnderStatus = false;
    }
    public boolean isThereABoxUnder(){
        return this.boxUnderStatus;
    }

    @Override
    public void checkCollision(CollidableObject c){

        if(getRectangle().intersects(c.getRectangle())) {
            Rectangle intersection = getRectangle().intersection(c.getRectangle());

            if (intersection.height < intersection.width && getY() < intersection.y) {
                //Down
                if (c instanceof Stationary) {

                    if (c.getStrength() >= getStrength()) {
                        setCollided(true);
                        ((Stationary) c).setBoxOnTop(true);
                    } else {
                        //Brake the box under
                        ((Stationary) c).setIsBrokenBox(true);
                    }

                }
            }

        }
        }


//    public void drawImage(Graphics2D g){
//        g.drawImage(getImage(), getX(), ((int) getY() + 1),null);
//    }
}
