package lazarusgame.game.moveableObjects;

import lazarusgame.GameConstants;

import java.awt.*;
/**
 *
 * @author saengduean8
 *  https://github.com/saengduean8/LazarusGame/tree/master/src/component
 */
public class animateJumpLeft extends Animation {


    Image [] aniJumpLeftImage;
    int index;

    public animateJumpLeft(int x, int y) {
        super(x, y);
        this.index = 0;
        this.aniJumpLeftImage = new Image[7];
        aniJumpLeftImage[0] = Toolkit.getDefaultToolkit().getImage("resources/lazarus/LazJumpLeft/Background.gif");
        aniJumpLeftImage[1] = Toolkit.getDefaultToolkit().getImage("resources/lazarus/LazJumpLeft/Frame 2.gif");
        aniJumpLeftImage[2] = Toolkit.getDefaultToolkit().getImage("resources/lazarus/LazJumpLeft/Frame 3.gif");
        aniJumpLeftImage[3] = Toolkit.getDefaultToolkit().getImage("resources/lazarus/LazJumpLeft/Frame 4.gif");
        aniJumpLeftImage[4] = Toolkit.getDefaultToolkit().getImage("resources/lazarus/LazJumpLeft/Frame 5.gif");
        aniJumpLeftImage[5] = Toolkit.getDefaultToolkit().getImage("resources/lazarus/LazJumpLeft/Frame 6.gif");
        aniJumpLeftImage[6] = Toolkit.getDefaultToolkit().getImage("resources/lazarus/LazJumpLeft/Frame 7.gif");
    }

    public  Image nextImageOrNull(){
        if(index >= aniJumpLeftImage.length ) {
            return null;

        }
        Image returnImage = aniJumpLeftImage[index];
        index++;


        return returnImage;
    }

    public void updatePosition(Lazarus lazarus) {
        lazarus.setY(lazarus.getY() - GameConstants.BLOCK_SIZE);
        lazarus.setX(lazarus.getX() - GameConstants.BLOCK_SIZE);
    }

}
