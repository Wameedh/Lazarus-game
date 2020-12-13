/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazarusgame.game;

import lazarusgame.GameConstants;
import lazarusgame.Launcher;
import lazarusgame.game.moveableObjects.Lazarus;
import lazarusgame.game.moveableObjects.LazarusControl;
import lazarusgame.game.moveableObjects.TimerTaskForBoxSpawning;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

import java.util.Timer;


import static javax.imageio.ImageIO.read;

/**
 * @author anthony-pc
 */
public class TRE extends JPanel implements Runnable {

    private boolean gameOver;

    private TimerTaskForBoxSpawning timerTaskForBoxSpawning;
    private Map map;
    private Lazarus lazarus;
    private int level = 1;

    private Launcher lf;


    private BufferedImage lazarusImg;

    private BufferedImage cardboardImg;
    private BufferedImage woodBoxImg;
    private BufferedImage metalBoxImg;
    private BufferedImage stoneBoxImg;
    private BufferedImage powerButtonImg;

    private BufferedImage bg;

    private BufferedImage wall;

    private JFrame jf;

    public TRE(Launcher lf) {
        this.lf = lf;
    }

    @Override
    public void run() {
        try {
            this.resetGame();
            Timer timer = new Timer();
            this.timerTaskForBoxSpawning = new TimerTaskForBoxSpawning(map, lazarus);
            timer.schedule(timerTaskForBoxSpawning, 0, 2000);
            while (!gameOver) {
                this.repaint();   // redraw game
                update();
                Thread.sleep(12);
            }
            //this.resetGame();
        } catch (InterruptedException ignored) {
            System.out.println(ignored);
        }
    }

    /**
     * Reset game to its initial state.
     */
    public void resetGame() {
        this.lazarus.setX(GameConstants.LAZARUS_RESPAWN_X);
        this.lazarus.setY(GameConstants.LAZARUS_RESPAWN_Y);
        this.lazarus.setLives(2);
        this.lazarus.setGameStatus(GameConstants.GAME_IS_RUNNING);
        map.gameRest();
        map.readMap(level);
        this.gameOver = false;

    }

    /**
     * Load all resources for Tank Wars Game. Set all Game Objects to their
     * initial state as well.
     */
    public void gameInitialize() {

        this.gameOver = false;

        try {
            /*
             * note class loaders read files from the out folder (build folder in Netbeans) and not the
             * current working directory.
             */
            lazarusImg = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("Lazarus_stand.png")));
            cardboardImg = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("CardBox.gif")));
            woodBoxImg = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("WoodBox.gif")));
            metalBoxImg = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("MetalBox.gif")));
            stoneBoxImg = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("StoneBox.gif")));
            powerButtonImg = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("Button.gif")));
            bg = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("Background.bmp")));

            wall = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("Wall.gif")));


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        this.map = new Map(wall, cardboardImg, woodBoxImg, metalBoxImg, stoneBoxImg, powerButtonImg);
        lazarus = new Lazarus(200, 200, lazarusImg, 0, map.getMap());

        LazarusControl lazarusControl = new LazarusControl(lazarus, KeyEvent.VK_A, KeyEvent.VK_D);

        this.lf.getJf().addKeyListener(lazarusControl);
        map.readMap(level);


    }

    private void update() {
        map.moveBoxes();
        lazarus.update();
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);
        map.mapUpdate();

        // Set the background and draw the map
        this.setBackground(Color.GRAY);

        g2.drawImage(this.bg, 0, 0, null);

        map.renderMap(g2);
        map.drawImage(g2);

        if (timerTaskForBoxSpawning.getFirstBox() == 1) {
            g2.drawImage(this.cardboardImg, 0, 520, null);
        } else if (timerTaskForBoxSpawning.getFirstBox() == 2) {
            g2.drawImage(this.woodBoxImg, 0, 520, null);
        } else if (timerTaskForBoxSpawning.getFirstBox() == 3) {
            g2.drawImage(this.metalBoxImg, 0, 520, null);
        } else if (timerTaskForBoxSpawning.getFirstBox() == 4) {
            g2.drawImage(this.stoneBoxImg, 0, 520, null);
        }

        // Draw lazarus
        this.lazarus.drawLazarus(g2);

        // print lives and hp
        g2.setColor(Color.orange);
        g2.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        g2.drawString("LIVES:", 15, 35);

        for (int i = 0; i < lazarus.getLives(); i++) {

            if (i == 0) {
                g2.scale(0.9, 0.9);
                g2.drawImage(lazarusImg, 90, 8, null);
            } else {
                g2.drawImage(lazarusImg, 80 + (20 + lazarusImg.getWidth()) * i, 8, null);
            }

        }


        if (lazarus.getLives() == 0) {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
            g2.setColor(Color.white);
            g2.setFont(new Font("TimesRoman", Font.PLAIN, 50));
            g2.drawString("YOU HAVE LOST!!", (GameConstants.SCREEN_WIDTH / 4), GameConstants.SCREEN_HEIGHT / 2);
            this.gameOver = true;
        }

        if (lazarus.getGameStatus() == GameConstants.GAME_OVER) {

            this.gameOver = true;
            timerTaskForBoxSpawning.cancel();
            this.lf.setFrame("end");
        }

        if (lazarus.getGameStatus() == GameConstants.GAME_WON) {

            if ((level + 1) > GameConstants.MAX_NUMBER_OF_LEVELS) {
                this.gameOver = true;
                this.lf.setFrame("won");
            } else {
                level++;
                this.gameOver = true;
                timerTaskForBoxSpawning.cancel();
                this.lf.setFrame("lazarusgame/game");

            }
        }

    }

}
