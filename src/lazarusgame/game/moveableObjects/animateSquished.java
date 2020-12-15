package lazarusgame.game.moveableObjects;


import java.awt.*;

/**
 *
 * @author saengduean8
 *  https://github.com/saengduean8/LazarusGame/tree/master/src/component
 */
public class animateSquished extends Animation {

    Image [] aniSquishedImage;
    int index;

    public animateSquished(int x, int y) {
        super(x, y);
        this.index = 0;
        this.aniSquishedImage = new Image[11];
        aniSquishedImage[0] = Toolkit.getDefaultToolkit().getImage("resources/lazarus/LazSquished/Background.gif");
        aniSquishedImage[1] = Toolkit.getDefaultToolkit().getImage("resources/lazarus/LazSquished/Frame 2.gif");
        aniSquishedImage[2] = Toolkit.getDefaultToolkit().getImage("resources/lazarus/LazSquished/Frame 3.gif");
        aniSquishedImage[3] = Toolkit.getDefaultToolkit().getImage("resources/lazarus/LazSquished/Frame 4.gif");
        aniSquishedImage[4] = Toolkit.getDefaultToolkit().getImage("resources/lazarus/LazSquished/Frame 5.gif");
        aniSquishedImage[5] = Toolkit.getDefaultToolkit().getImage("resources/lazarus/LazSquished/Frame 6.gif");
        aniSquishedImage[6] = Toolkit.getDefaultToolkit().getImage("resources/lazarus/LazSquished/Frame 7.gif");
        aniSquishedImage[7] = Toolkit.getDefaultToolkit().getImage("resources/lazarus/LazSquished/Frame 8.gif");
        aniSquishedImage[8] = Toolkit.getDefaultToolkit().getImage("resources/lazarus/LazSquished/Frame 9.gif");
        aniSquishedImage[9] = Toolkit.getDefaultToolkit().getImage("resources/lazarus/LazSquished/Frame 10.gif");
        aniSquishedImage[10] = Toolkit.getDefaultToolkit().getImage("resources/lazarus/LazSquished/Frame 11.gif");
    }

    public  Image nextImageOrNull(){
        if(index >= aniSquishedImage.length ) {
            return null;

        }
        Image returnImage = aniSquishedImage[index];
        index++;


        return returnImage;
    }

    public void updatePosition(Lazarus lazarus){ }

}
