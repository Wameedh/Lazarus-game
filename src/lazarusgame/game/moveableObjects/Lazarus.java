package lazarusgame.game.moveableObjects;


import lazarusgame.GameConstants;
import lazarusgame.game.CollisionControl;
import lazarusgame.game.SoundPlayer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * @author Wameedh Mohammed Ali
 */
public class Lazarus extends Moveable {

    private int lives;
    private final SoundPlayer squishedSP;
    private final SoundPlayer wonSP;
    private Animation currentAnimation;
    private final CollisionControl collisionControl;
    private boolean RightPressed, movingRight;
    private boolean LeftPressed, movingLeft;
    private int gameRunningStatus;


    public Lazarus(int x, int y, BufferedImage image, ArrayList<ArrayList<Integer>> map) {
        super(x, y, image);
        this.squishedSP = new SoundPlayer(2, "Squished.wav");
        this.wonSP = new SoundPlayer(2, "Button.wav");
        this.lives = 2;
        this.collisionControl = new CollisionControl(map);
        this.gameRunningStatus = GameConstants.GAME_IS_RUNNING;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
        this.movingRight = true;
    }

    void toggleLeftPressed() {
        this.LeftPressed = true;
        this.movingLeft = true;
    }

    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }


    public void update() {
        handleMovement();
        checkIfLazarusWon();
    }

    public void checkIfLazarusDied(boolean status) {
        if (status) {
            this.squishedSP.play();
            this.lives--;
            if (this.lives == 0) {
                this.gameRunningStatus = GameConstants.GAME_OVER;
            } else {
                setX(GameConstants.LAZARUS_RESPAWN_X);
                setY(GameConstants.LAZARUS_RESPAWN_Y);
            }
        }
    }

    private void checkIfLazarusWon() {

        if (collisionControl.validateLazarusToStopCollision(getX(), getY())) {
            this.wonSP.play();
            this.gameRunningStatus = GameConstants.GAME_WON;
        }

    }

    public void handleMovement() {

        int newX;
        // check if there is any boxes underneath the lazarus if not it will keep falling
        while (!collisionControl.validateLazarusCollision(getX(), getY() + GameConstants.BLOCK_SIZE)) {
            setY(getY() + 1);
        }

        if (this.RightPressed) {

            if (movingRight) {

                newX = getX() + GameConstants.BLOCK_SIZE;

                // lazarus can't move up if there is more than one box
                if (collisionControl.validateLazarusCollision(newX, getY() - GameConstants.BLOCK_SIZE)) {
                    movingRight = false;
                    return;
                }

                // if there is collision then lazarus moves up one box at a time
                if (collisionControl.validateLazarusCollision(newX, getY())) {
                    //jump right
                    currentAnimation = new animateJumpRight(getX(), getY()); // create jump right animation

                } else {
                    currentAnimation = new animateRight(getX(), getY()); // create move right animation

                }

            }
        }

        if (this.LeftPressed) {

            if (movingLeft) {

                newX = getX() - GameConstants.BLOCK_SIZE;

                if (collisionControl.validateLazarusCollision(newX, getY() - GameConstants.BLOCK_SIZE)) {
                    movingLeft = false;
                    return;
                }

                if (collisionControl.validateLazarusCollision(newX, getY())) {
                    // jump left
                    currentAnimation = new animateJumpLeft(getX(), getY());//create jump left animation

                } else {
                    currentAnimation = new animateLeft(getX(), getY()); // create move left animation
                }
            }
        }
    }


    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getGameStatus() {
        return this.gameRunningStatus;
    }

    public void setGameStatus(int status) {
        this.gameRunningStatus = status;
    }

    public int getLives() {
        return lives;
    }

    public void drawLazarus(Graphics2D g2) {

        if (this.currentAnimation != null) {

            //when there is animation to play
            Image image = currentAnimation.nextImageOrNull();
            if (image == null) {
                currentAnimation.updatePosition(this);
                currentAnimation = null;
            } else {
                if (collisionControl.validateLazarustoBoxesCollision(getX(), getY())) {
                    LeftPressed = false;
                    RightPressed = false;
                }
                g2.drawImage(image, getX(), getY(), GameConstants.BLOCK_SIZE, GameConstants.BLOCK_SIZE, null);

            }

        } else {
            g2.drawImage(getImage(), getX(), getY(), GameConstants.BLOCK_SIZE, GameConstants.BLOCK_SIZE, null);

        }
    }

}
