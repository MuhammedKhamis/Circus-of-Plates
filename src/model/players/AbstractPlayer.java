package model.players;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import controller.PlateFetching;
import controller.PlateFetching.CheckResult;
import controller.ResourcesManager;
import controller.util.Enumrations.Players;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import logging.Logging;
import model.players.util.Observable;
import model.players.util.Observer;
import model.save.SaveShapeNode;
import model.shapes.interfaces.Shape;

public abstract class AbstractPlayer implements Observable {

    protected String imagePath;
    protected File imageFile;
    protected BufferedImage image;
    protected String imageName;
    protected float[][] stacksCenter;
    protected int[][] playerPosition;
    protected PlayersStack[] stacks;
    protected PlateFetching checker;
    protected ArrayList<Observer> observers;
    protected Players playerID;
    protected int score;
    protected ResourcesManager resourcesManager;

    public AbstractPlayer(final ResourcesManager resourcesManager) {
        stacks = new PlayersStack[2];
        initialize();
        checker = new PlateFetching(this);
        observers = new ArrayList<>();
        score = 0;
        this.resourcesManager = resourcesManager;
    }

    public void initialize() {
        for (int i = 0; i < stacks.length; i++) {
            stacks[i] = new PlayersStack(this, i);
            stacks[i].start();
        }

    }

    public ArrayList<SaveShapeNode> getStackList(final int index) {
        return stacks[index].getStackShape();
    }

    public void receivePlate(final int index, final Shape plate, final ImageView image) {
        Logging.info(String.valueOf(index));
        stacks[index].addShape(plate, image, index);

    }

    public BufferedImage getImage() {
        return image;
    }

    public float[][] getStacksCenters() {
        return stacksCenter;
    }

    public CheckResult check(final int x, final int y) {
        return checker.CheckMe(x, y);
    }

    public int[][] getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(final int[] position) {
        playerPosition[0][0] = position[0];
        playerPosition[0][1] = position[1];
    }

    public void setPoints(final double x, final double y, final float hight) {
        for (int i = 0; i < stacksCenter.length; i++) {
            for (int j = 0; j < stacksCenter[i].length; j++) {
                if (j == 0) {
                    stacksCenter[i][j] += x;

                } else {
                    stacksCenter[i][j] += (y + 4.5 * hight);
                }

            }
        }
    }

    public void updateHight(final float increment, final int index) {
        stacksCenter[index][1] -= increment;
    }

    public void move(final float DeltaX) {
        for (int i = 0; i < stacksCenter.length; i++) {
            stacksCenter[i][0] += DeltaX;
            Logging.info("stacksCenters : " + stacksCenter[i][1]);
        }
    }

    protected void loadImage() {
        imageFile = new File(imagePath);
        try {
            image = ImageIO.read(imageFile);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    protected void removeFromPane(final Node node) {
        resourcesManager.removePlate(node);
    }

    public void playerPosition(final int x, final int y) {
        playerPosition[0][0] = x;
        playerPosition[0][1] = y;
    }

    public void setPlayerID(final Players id) {
        playerID = id;
    }

    public int getScore() {
        return score;
    }

    public void incrementScore() {
        score++;
    }

    public int getStackHeight(final int index) {
        return stacks[index].getHeight();
    }

}
