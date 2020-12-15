package lazarusgame.menus;

import lazarusgame.GameConstants;
import lazarusgame.Launcher;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class WonAllLevelsPanel extends JPanel {
    private BufferedImage menuBackground;
    private JButton exit;
    private Launcher lf;

    public WonAllLevelsPanel(Launcher lf) {
        this.lf = lf;
        try {
            menuBackground = ImageIO.read(this.getClass().getClassLoader().getResource("wonGameBackground.png"));
        } catch (IOException e) {
            System.out.println("Error cant read menu background");
            e.printStackTrace();
            System.exit(-3);
        }
        this.setBackground(Color.BLACK);
        this.setLayout(null);

        exit = new JButton("Exit");
        exit.setFont(new Font("Courier New", Font.BOLD ,24));
        exit.setBounds((GameConstants.START_MENU_SCREEN_WIDTH/2) - 75,(GameConstants.START_MENU_SCREEN_HEIGHT/2) + 50,175,50);
        exit.addActionListener((actionEvent -> {
            this.lf.closeGame();
        }));

        this.add(exit);

    }

    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.menuBackground,0,0,null);
    }
}

