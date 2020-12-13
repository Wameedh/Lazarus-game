package lazarusgame.game.moveableObjects;


import java.awt.*;

/**
 *
 * @author saengduean8
 *  https://github.com/saengduean8/LazarusGame/tree/master/src/component
 */

public abstract class Animation {

    public float x;
    public float y;

    public Animation(int x, int y){
        this.x = x;
        this.y = y;

    }

    public abstract Image nextImageOrNull();

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public abstract void updatePosition(Lazarus lazarus);
}
