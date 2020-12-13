package lazarusgame.game.moveableObjects;


import lazarusgame.GameConstants;
import java.awt.*;

/**
 *
 * @author saengduean8
 *  https://github.com/saengduean8/LazarusGame/tree/master/src/component
 */
public class animateLeft extends Animation {

    Image [] aniLeftImage;
    int index;

    public animateLeft(int x, int y) {
        super(x, y);
        this.index = 0;
        this.aniLeftImage = new Image[7];
        aniLeftImage[0] = Toolkit.getDefaultToolkit().getImage("resources/lazarus/LazLeft/Background.gif");
        aniLeftImage[1] = Toolkit.getDefaultToolkit().getImage("resources/lazarus/LazLeft/Frame 2.gif");
        aniLeftImage[2] = Toolkit.getDefaultToolkit().getImage("resources/lazarus/LazLeft/Frame 3.gif");
        aniLeftImage[3] = Toolkit.getDefaultToolkit().getImage("resources/lazarus/LazLeft/Frame 4.gif");
        aniLeftImage[4] = Toolkit.getDefaultToolkit().getImage("resources/lazarus/LazLeft/Frame 5.gif");
        aniLeftImage[5] = Toolkit.getDefaultToolkit().getImage("resources/lazarus/LazLeft/Frame 6.gif");
        aniLeftImage[6] = Toolkit.getDefaultToolkit().getImage("resources/lazarus/LazLeft/Frame 7.gif");
    }

    public  Image nextImageOrNull(){
        if(index >= aniLeftImage.length ) {
            return null;

        }
        Image returnImage = aniLeftImage[index];
        index++;


        return returnImage;
    }

    public void updatePosition(Lazarus lazarus) {
        lazarus.setX(lazarus.getX() - GameConstants.BLOCK_SIZE);
    }


}


