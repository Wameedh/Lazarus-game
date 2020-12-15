/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazarusgame.game.moveableObjects;


import lazarusgame.game.SoundPlayer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


/**
 *
 * @author anthony-pc
 */
public class LazarusControl implements KeyListener {

   // private AudioPlayer playMusic;
    private final SoundPlayer sp;
    private final Lazarus lazarus;
    private final int right;
    private final int left;


    public LazarusControl(Lazarus lazarus, int left, int right) {
        this.lazarus = lazarus;
        this.right = right;
        this.left = left;
        //playMusic = new AudioPlayer( "resources/Move.wav");
        this.sp = new SoundPlayer(2,"Move.wav");
    }

    @Override
    public void keyTyped(KeyEvent ke) {

    }

    @Override
    public void keyPressed(KeyEvent ke) {
        int keyPressed = ke.getKeyCode();

        if (keyPressed == left) {
            //playMusic.play();
            sp.play();
            this.lazarus.toggleLeftPressed();
        }
        if (keyPressed == right) {
            //playMusic.play();
            sp.play();
            this.lazarus.toggleRightPressed();
        }


    }

    @Override
    public void keyReleased(KeyEvent ke) {
        int keyReleased = ke.getKeyCode();

        if (keyReleased  == left) {
            this.lazarus.unToggleLeftPressed();
        }
        if (keyReleased  == right) {
            this.lazarus.unToggleRightPressed();
        }

    }
}
