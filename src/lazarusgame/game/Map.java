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

   // private BufferedImage bg;

    private BufferedImage wallImg;
    private BufferedImage powerButtonImg;
    private BufferedImage cardboardImg;
    private BufferedImage woodBoxImg;
    private BufferedImage metalBoxImg;
    private BufferedImage stoneBoxImg;

    private CollisionControl collisionControl;


    private ArrayList<Box> allWalls;

    private CopyOnWriteArrayList<Box> allBoxes;
    // Get the collection to be removed
    ArrayList<Box> checkList;

    private ArrayList<ArrayList<Integer>> map = new ArrayList<>();

    public Map( BufferedImage wallImg, BufferedImage cardboardImg, BufferedImage woodBoxImg, BufferedImage metalBoxImg, BufferedImage stoneBoxImg, BufferedImage powerButtonImg) {

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


    }

    public void readMap(int level) {        //read initial map, put into 2d array
        String file = "resources/"+"level_"+level+".txt";
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
        } catch (IOException e) {
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

    public boolean checkObjectInMap(int x, int y, int width, int height) {
        int i = x / width;
        int j = y / height;

        if (j != 0) {
            return map.get(j - 1).get(i) == 1;
        }
        return false;
    }

    public void mapUpdate() {    //print current map from 2d array
        allWalls.clear();

        int tileWidth = wallImg.getWidth();
        int tileHeight = wallImg.getHeight();

        int width = map.get(0).size();
        int height = map.size();


        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                if (map.get(j).get(i) == 6) {
                    allWalls.add(new Box(i * tileWidth, j * tileHeight, wallImg,  6));
                }
                if (map.get(j).get(i) == 5) {
                    allWalls.add(new Box(i * tileWidth, j * tileHeight, powerButtonImg,  5));
                }
//                if (map.get(j).get(i) == 1) {
//                    this.allWalls.add(new Box(i * tileWidth, j * tileHeight, cardboardImg, false, 1));
//                   // setNewYForBoxMovement(i, j, 1);
//                }
//                if (map.get(j).get(i) == 2) {
//                    this.allWalls.add(new Box(i * tileWidth, j * tileHeight, woodBoxImg, false, 2));
//                   // setNewYForBoxMovement(i, j, 2);
//                }
//                if (map.get(j).get(i) == 3) {
//                    this.allWalls.add(new Box(i * tileWidth, j * tileHeight, metalBoxImg, false, 3));
//                   // setNewYForBoxMovement(i, j, 3);
//                }
//                if (map.get(j).get(i) == 4) {
//                    this.allWalls.add(new Box(i * tileWidth, j * tileHeight, stoneBoxImg, false, 4));
//                    //setNewYForBoxMovement(i, j, 4);
//                }
            }
        }
    }


    private void setNewYForBoxMovement(int x, int y, int value, Box box) {
        map.get(y).set(x, 0);
        map.get(y).set(x, value);
//        Integer value1 = map.get(y + 1).get(x);
//        if (value1 != 6) {
//            map.get(y).set(x, 0);
//            map.get(y + 1).set(x, value);
//        }
    }

    public void moveBoxes() {
            Iterator<Box> itr = allBoxes.iterator();
            Box box;
            while (itr.hasNext()) {
                box = itr.next();
                if (collisionControl.validateBoxToWallCollision(box)) {
                    map.get(box.getY() / GameConstants.BLOCK_SIZE).set(box.getX() / GameConstants.BLOCK_SIZE, box.getStrength());

                    // itr.remove();
                    checkList.add(box);
                } else if (collisionControl.validateBoxToBoxCollision(box)) {
                    // If there is box to box collision there are three possiblilities
                    // 1. Heavy box (Priority higher) is on top of light box (Priority lower)
                    // 2. Light box (Priority lower) is on top of heavy box (Priority higher)
                    // 3. Both boxes of same type (same priority lower)
                    stopBoxToBoxOnCollision(box, itr);
                } else {
                    //map.get(box.getY() / GameConstants.BLOCK_SIZE).set(box.getX() / GameConstants.BLOCK_SIZE, 0);
                    box.moveBoxDown();
                   // map.get(box.getY() / GameConstants.BLOCK_SIZE).set(box.getX() / GameConstants.BLOCK_SIZE, box.getStrength());
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
            map.get(currentBox.getY() / GameConstants.BLOCK_SIZE).set(currentBox.getX() / GameConstants.BLOCK_SIZE, currentBoxPriority);
            checkList.add(currentBox);
        } else {
            // Break bottom box
            map.get(newY / GameConstants.BLOCK_SIZE).set(newX / GameConstants.BLOCK_SIZE, 0);
        }
    }


    public synchronized void updateMapWithNewWall(int strength, int x) {

        if (strength == 1) {
           this.allBoxes.add(new Box(x, 0, cardboardImg, strength));

        } else if (strength == 2) {
           this.allBoxes.add(new Box(x, 0, woodBoxImg, strength));
        } else if (strength == 3) {
            this.allBoxes.add(new Box(x, 0, metalBoxImg, strength));

        } else if (strength == 4) {
           this.allBoxes.add(new Box(x, 0, stoneBoxImg, strength));

        }
    }


    public void renderMap(Graphics2D g2) {

        for (int row = 0; row < GameConstants.MAX_NUMBER_OF_ROWS; row++) {
            for (int col = 0; col < GameConstants.MAX_NUMBER_OF_COLUMNS; col++) {
                int value = map.get(row).get(col);
                int y = row * GameConstants.BLOCK_SIZE;
                int x = col * GameConstants.BLOCK_SIZE;
                if (value == 6) {

                    g2.drawImage(wallImg, x, y, GameConstants.BLOCK_SIZE, GameConstants.BLOCK_SIZE, null);
                    continue;
                }
                if (value == 5) {
                    g2.drawImage(powerButtonImg, x, y, GameConstants.BLOCK_SIZE, GameConstants.BLOCK_SIZE, null);
                    continue;
                }


                    if(value == 1) {
                        g2.drawImage(cardboardImg, x, y, GameConstants.BLOCK_SIZE, GameConstants.BLOCK_SIZE, null);
                    } else if(value == 2) {
                        g2.drawImage(woodBoxImg, x, y, GameConstants.BLOCK_SIZE, GameConstants.BLOCK_SIZE, null);
                    } else if(value == 3) {
                        g2.drawImage(metalBoxImg, x, y, GameConstants.BLOCK_SIZE, GameConstants.BLOCK_SIZE, null);
                    } else if(value == 4) {
                        g2.drawImage(stoneBoxImg, x, y, GameConstants.BLOCK_SIZE, GameConstants.BLOCK_SIZE, null);
                    }
            }
        }
    }

    public void drawImage(Graphics2D g2) {

//if(allBoxesTEST != null) {
//            int width = allBoxesTEST[0].length;
//            int height = allBoxesTEST.length;
//            Box allWall;
//
//            for (int i = 0; i < height; i++) {
//                for (int j = 0; j < width; j++) {
//                    if (allBoxesTEST[i][j] != null) {
//                        allWall = allBoxesTEST[i][j];
//                        int value = allWall.getStrength();
//                        if (collisionControl.validateBoxToWallCollision(allWall) || collisionControl.validateBoxToBoxCollision(allWall)) {
//                            allWall.setCollided(true);
//                            setNewYForBoxMovement(allWall.getX() / cardboardImg.getWidth(), allWall.getY() / cardboardImg.getHeight(), value);
//                        } else {
//
//                        }
//                        if (value == 1) {
//                            //g2.drawImage(cardboardImg, allWall.getX(), allWall.getY(), GameConstants.BLOCK_SIZE, GameConstants.BLOCK_SIZE, null);
//                            allWall.drawImage(g2);
//                            if(!allWall.getCollided()) {
//                                allBoxesTEST[i][j+1] = allWall;
//                            }
//                            //setNewYForBoxMovement(allWall.getX()/cardboardImg.getWidth(),allWall.getY()/cardboardImg.getHeight(),value);
//                        } else if (value == 2) {
//                            //g2.drawImage(woodBoxImg, allWall.getX(), allWall.getY(), GameConstants.BLOCK_SIZE, GameConstants.BLOCK_SIZE, null);
//                            allWall.drawImage(g2);
//                            if(!allWall.getCollided()) {
//                                allBoxesTEST[i][j+1] = allWall;
//                            }
//                            //setNewYForBoxMovement(allWall.getX()/woodBoxImg.getWidth(),allWall.getY()/woodBoxImg.getHeight(),value);
//                        } else if (value == 3) {
//                            // g2.drawImage(metalBoxImg, allWall.getX(), allWall.getY(), GameConstants.BLOCK_SIZE, GameConstants.BLOCK_SIZE, null);
//                            allWall.drawImage(g2);
//                            if(!allWall.getCollided()) {
//                                allBoxesTEST[i][j+1] = allWall;
//                            }
//                            // setNewYForBoxMovement(allWall.getX()/metalBoxImg.getWidth(),allWall.getY()/metalBoxImg.getHeight(),value);
//                        } else if (value == 4) {
//                            //g2.drawImage(stoneBoxImg, allWall.getX(), allWall.getY(), GameConstants.BLOCK_SIZE, GameConstants.BLOCK_SIZE, null);
//                            allWall.drawImage(g2);
//                            if(!allWall.getCollided()) {
//                                allBoxesTEST[i][j+1] = allWall;
//                            }
//                            //  setNewYForBoxMovement(allWall.getX()/stoneBoxImg.getWidth(),allWall.getY()/stoneBoxImg.getHeight(),value);
//                        }
//
//                    }
//                }
//            }
//        }
       // Box box = null;
            for (Box allWall : allBoxes) {
                allWall.drawImage(g2);
//                int value = allWall.getStrength();
//
//                if (collisionControl.validateBoxToWallCollision(allWall)) {
//                    allWall.setCollided(true);
//                    setNewYForBoxMovement(allWall.getX() / cardboardImg.getWidth(), allWall.getY() / cardboardImg.getHeight(), value, allWall);
//                } else if (collisionControl.validateBoxToBoxCollision(allWall)) {
//                    handleCollision(allWall);
//                    if (allWall.getCollided()) {
//                        setNewYForBoxMovement(allWall.getX() / cardboardImg.getWidth(), allWall.getY() / cardboardImg.getHeight(), value, allWall);
//                    }
//                }
//            if (value == 1) {
//                //g2.drawImage(cardboardImg, allWall.getX(), allWall.getY(), GameConstants.BLOCK_SIZE, GameConstants.BLOCK_SIZE, null);
//                allWall.drawImage(g2);
//                //setNewYForBoxMovement(allWall.getX()/cardboardImg.getWidth(),allWall.getY()/cardboardImg.getHeight(),value);
//            } else if (value == 2) {
//                //g2.drawImage(woodBoxImg, allWall.getX(), allWall.getY(), GameConstants.BLOCK_SIZE, GameConstants.BLOCK_SIZE, null);
//                allWall.drawImage(g2);
//                //setNewYForBoxMovement(allWall.getX()/woodBoxImg.getWidth(),allWall.getY()/woodBoxImg.getHeight(),value);
//            } else if (value == 3) {
//               // g2.drawImage(metalBoxImg, allWall.getX(), allWall.getY(), GameConstants.BLOCK_SIZE, GameConstants.BLOCK_SIZE, null);
//                allWall.drawImage(g2);
//               // setNewYForBoxMovement(allWall.getX()/metalBoxImg.getWidth(),allWall.getY()/metalBoxImg.getHeight(),value);
//            } else if (value == 4) {
//                //g2.drawImage(stoneBoxImg, allWall.getX(), allWall.getY(), GameConstants.BLOCK_SIZE, GameConstants.BLOCK_SIZE, null);
//                allWall.drawImage(g2);
//              //  setNewYForBoxMovement(allWall.getX()/stoneBoxImg.getWidth(),allWall.getY()/stoneBoxImg.getHeight(),value);
//            }
//                if (!allWall.getIsBrokenBox()) {
//                    allWall.drawImage(g2);
                //}
//                else {
//                    // box = allWall;
//                    this.allBoxes.remove(allWall);
//
//                }
            }

//        if(box != null) {
//            this.allBoxes.remove(box);
//        }
    }

    public void gameRest(){
        allBoxes.clear();
        allWalls.clear();
        map.clear();
    }

}
