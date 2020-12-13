package lazarusgame.menus;

import lazarusgame.GameConstants;
import lazarusgame.Launcher;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class EndGamePanel extends JPanel {

    private BufferedImage menuBackground;
    private JButton start;
    private JButton exit;
    private Launcher lf;

    public EndGamePanel(Launcher lf) {
        this.lf = lf;
        try {
            menuBackground = ImageIO.read(this.getClass().getClassLoader().getResource("Background.bmp"));
        } catch (IOException e) {
            System.out.println("Error cant read menu background");
            e.printStackTrace();
            System.exit(-3);
        }
        this.setBackground(Color.BLACK);
        this.setLayout(null);

        start = new JButton("Restart Game");
        start.setFont(new Font("Courier New", Font.BOLD ,24));
        start.setBounds((GameConstants.START_MENU_SCREEN_WIDTH/2) - 75,(GameConstants.START_MENU_SCREEN_HEIGHT/2) - 50,175,50);
        start.addActionListener((actionEvent -> {
            this.lf.setFrame("lazarusgame/game");
        }));


        exit = new JButton("Exit");
        exit.setFont(new Font("Courier New", Font.BOLD ,24));
        exit.setBounds((GameConstants.START_MENU_SCREEN_WIDTH/2) - 75,(GameConstants.START_MENU_SCREEN_HEIGHT/2) + 50,175,50);
        exit.addActionListener((actionEvent -> {
            this.lf.closeGame();
        }));


        this.add(start);
        this.add(exit);

    }

    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.menuBackground,0,0,null);
    }
}
