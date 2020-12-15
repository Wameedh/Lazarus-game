package lazarusgame.game;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import lazarusgame.GameConstants;
import lazarusgame.game.stationaryObjects.Box;

public class Map {
    int lazarusStrength = GameConstants.LAZARUS_STRENGTH;
    int wallStrength = GameConstants.WALL_STRENGTH;
    int cardboardBoxStrength = GameConstants.CARDBOARD_BOX_STRENGTH;
    int woodBoxStrength = GameConstants.WOOD_BOX_STRENGTH;
    int metalBoxStrength = GameConstants.METAL_BOX_STRENGTH;
    int stoneBoxStrength = GameConstants.STONE_BOX_STRENGTH;
    int powerButtonStrength = GameConstants.POWER_BUTTON_BOX_STRENGTH;

    private final BufferedImage wallImg;
    private final BufferedImage powerButtonImg;
    private final BufferedImage cardboardImg;
    private final BufferedImage woodBoxImg;
    private final BufferedImage metalBoxImg;
    private final BufferedImage stoneBoxImg;
    private final SoundPlayer wallSP;
    private final SoundPlayer crushSP;
    private final CollisionControl collisionControl;

    private boolean lazarusStatus;

    private final ArrayList<Box> allWalls;

    private final CopyOnWriteArrayList<Box> allBoxes;
    // Get the collection to be removed
    ArrayList<Box> checkList;

    private final ArrayList<ArrayList<Integer>> map = new ArrayList<>();

    public Map(BufferedImage wallImg, BufferedImage cardboardImg, BufferedImage woodBoxImg, BufferedImage metalBoxImg, BufferedImage stoneBoxImg, BufferedImage powerButtonImg) {
        lazarusStatus = false;
        this.wallImg = wallImg;
        this.cardboardImg = cardboardImg;
        this.woodBoxImg = woodBoxImg;
        this.metalBoxImg = metalBoxImg;
        this.stoneBoxImg = stoneBoxImg;
        this.powerButtonImg = powerButtonImg;
        this.collisionControl = new CollisionControl(map);
        allBoxes = new CopyOnWriteArrayList<>();
        checkList = new ArrayList<>();
        allWalls = new ArrayList<>();
        this.wallSP = new SoundPlayer(2, "Wall.wav");
        this.crushSP = new SoundPlayer(2, "Crush.wav");
    }

    public void readMap(int level) {        //read initial map, put into 2d array
        String file = "resources/" + "level_" + level + ".txt";
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                if (currentLine.isEmpty())
                    continue;
                ArrayList<Integer> rows = new ArrayList<>();
                String[] values = currentLine.trim().split(" ");
                for (String str : values) {
                    if (!str.isEmpty()) {
                        int num = Integer.parseInt(str);
                        rows.add(num);
                    }
                }
                map.add(rows);
            }
        } catch (IOException ignored) {
        }
    }

    public ArrayList<ArrayList<Integer>> getMap() {
        return this.map;
    }

    public int getBoxFromMap(int x, int y) {
        int i = x / GameConstants.BLOCK_SIZE;
        int j = y / GameConstants.BLOCK_SIZE;

        return map.get(j).get(i);
    }

    public void mapUpdate() {    //print current map from 2d array
        allWalls.clear();

        int tileWidth = wallImg.getWidth();
        int tileHeight = wallImg.getHeight();

        int width = map.get(0).size();
        int height = map.size();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                if (map.get(j).get(i) == wallStrength) {
                    allWalls.add(new Box(i * tileWidth, j * tileHeight, wallImg, wallStrength));
                }
                if (map.get(j).get(i) == powerButtonStrength) {
                    allWalls.add(new Box(i * tileWidth, j * tileHeight, powerButtonImg, powerButtonStrength));
                }
            }
        }
    }

    public void clearLazarusPreviousLocationFormMap() {
        //Stating from the bottom of the map is more efficient
        for (int row = GameConstants.MAX_NUMBER_OF_ROWS - 1; row >= 0; row--) {
            for (int col = 0; col < GameConstants.MAX_NUMBER_OF_COLUMNS; col++) {
                int value = map.get(row).get(col);
                if (value == lazarusStrength) {
                    map.get(row).set(col, 0);
                    break;
                }
            }
        }
    }

    public void updateLazarusLocationOnTheMap(int lazarusX, int lazarusY) {
        clearLazarusPreviousLocationFormMap();
        map.get(lazarusY / GameConstants.BLOCK_SIZE).set(lazarusX / GameConstants.BLOCK_SIZE, lazarusStrength);
    }

    public void moveBoxes() {
        Iterator<Box> itr = allBoxes.iterator();
        Box box;
        while (itr.hasNext()) {
            box = itr.next();
            if (collisionControl.validateBoxToLazarusCollision(box)) {
                this.lazarusStatus = true;
            } else if (collisionControl.validateBoxToWallCollision(box)) {
                this.wallSP.play();
                map.get(box.getY() / GameConstants.BLOCK_SIZE).set(box.getX() / GameConstants.BLOCK_SIZE, box.getStrength());
                checkList.add(box);
            } else if (collisionControl.validateBoxToBoxCollision(box)) {
                // If there is box to box collision there are three possiblilities
                // 1. Heavy box (Priority higher) is on top of light box (Priority lower)
                // 2. Light box (Priority lower) is on top of heavy box (Priority higher)
                // 3. Both boxes of same type (same priority lower)
                stopBoxToBoxOnCollision(box, itr);
            } else {
                box.moveBoxDown();
            }
            this.allBoxes.removeAll(checkList);
            checkList.clear();
        }

    }


    private void stopBoxToBoxOnCollision(Box currentBox, Iterator<Box> itr) {
        int newX = currentBox.getX();
        int newY = currentBox.getNextBoxDownPosition();

        int bottomBoxType = getBoxFromMap(newX, newY);
        // Get box priorities
        int currentBoxPriority = currentBox.getStrength();

        if (bottomBoxType >= currentBoxPriority) {
            // Dont break bottom box and stop current box
            this.wallSP.play();
            map.get(currentBox.getY() / GameConstants.BLOCK_SIZE).set(currentBox.getX() / GameConstants.BLOCK_SIZE, currentBoxPriority);
            checkList.add(currentBox);
        } else {
            // Break bottom box
            this.crushSP.play();
            map.get(newY / GameConstants.BLOCK_SIZE).set(newX / GameConstants.BLOCK_SIZE, 0);
        }
    }


    public synchronized void updateMapWithNewWall(int strength, int x) {

        if (strength == cardboardBoxStrength) {
            this.allBoxes.add(new Box(x, 0, cardboardImg, strength));
        } else if (strength == woodBoxStrength) {
            this.allBoxes.add(new Box(x, 0, woodBoxImg, strength));
        } else if (strength == metalBoxStrength) {
            this.allBoxes.add(new Box(x, 0, metalBoxImg, strength));
        } else if (strength == stoneBoxStrength) {
            this.allBoxes.add(new Box(x, 0, stoneBoxImg, strength));
        }
    }


    public void renderMap(Graphics2D g2) {

        for (int row = 0; row < GameConstants.MAX_NUMBER_OF_ROWS; row++) {
            for (int col = 0; col < GameConstants.MAX_NUMBER_OF_COLUMNS; col++) {
                int value = map.get(row).get(col);
                int y = row * GameConstants.BLOCK_SIZE;
                int x = col * GameConstants.BLOCK_SIZE;

                if (value == wallStrength) {
                    g2.drawImage(wallImg, x, y, GameConstants.BLOCK_SIZE, GameConstants.BLOCK_SIZE, null);
                } else if (value == powerButtonStrength) {
                    g2.drawImage(powerButtonImg, x, y, GameConstants.BLOCK_SIZE, GameConstants.BLOCK_SIZE, null);
                } else if (value == cardboardBoxStrength) {
                    g2.drawImage(cardboardImg, x, y, GameConstants.BLOCK_SIZE, GameConstants.BLOCK_SIZE, null);
                } else if (value == woodBoxStrength) {
                    g2.drawImage(woodBoxImg, x, y, GameConstants.BLOCK_SIZE, GameConstants.BLOCK_SIZE, null);
                } else if (value == metalBoxStrength) {
                    g2.drawImage(metalBoxImg, x, y, GameConstants.BLOCK_SIZE, GameConstants.BLOCK_SIZE, null);
                } else if (value == stoneBoxStrength) {
                    g2.drawImage(stoneBoxImg, x, y, GameConstants.BLOCK_SIZE, GameConstants.BLOCK_SIZE, null);
                }
            }
        }
    }

    public void drawImage(Graphics2D g2) {
        for (Box allWall : allBoxes) {
            allWall.drawImage(g2);
        }
    }

    public boolean getLazarusStatus() {
        return this.lazarusStatus;
    }

    public void setLazarusStatus(boolean status) {
        this.lazarusStatus = status;
    }

    public void gameRest() {
        allBoxes.clear();
        allWalls.clear();
        map.clear();
    }

}
