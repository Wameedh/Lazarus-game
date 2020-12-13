/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazarusgame.game.moveableObjects;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


/**
 *
 * @author anthony-pc
 */
public class LazarusControl implements KeyListener {

    private Lazarus lazarus;
    private final int right;
    private final int left;


    public LazarusControl(Lazarus lazarus, int left, int right) {
        this.lazarus = lazarus;
        this.right = right;
        this.left = left;

    }

    @Override
    public void keyTyped(KeyEvent ke) {

    }

    @Override
    public void keyPressed(KeyEvent ke) {
        int keyPressed = ke.getKeyCode();

        if (keyPressed == left) {
            this.lazarus.toggleLeftPressed();
        }
        if (keyPressed == right) {
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
