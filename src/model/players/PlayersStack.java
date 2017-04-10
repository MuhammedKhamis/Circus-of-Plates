package model.players;

import java.util.ArrayList;
import java.util.Stack;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import model.save.SaveShapeNode;
import model.shapes.interfaces.Shape;

/**
 * General Comments : - The blue comments are used to documentation. - The green
 * comments indicates about something want to be add to the Class.
 * ---------------------------------------------------------------
 *
 * This class are used to control the stack of the players, and notify the
 * ScoreManager to increase the score.
 *
 *
 */

public class PlayersStack implements Runnable {

    /**
     * Max number of similarity we want to increase the score.
     */
    private final int similarity = 3;
    /**
     * Stack that save the names of the shapes that should be distinct.
     */
    private final Stack<Shape> plates;
    /**
     * Array List that save the names of the shapes that should be distinct.
     */
    private final ArrayList<ImageView> images;
    /**
     * Which player this object belongs to.
     */
    private final AbstractPlayer player;
    /**
     * Score controller that should be modified if we found match.
     */
    // private final ScoreManager manager;
    /**
     * Thread for stack
     */
    private Thread thread;
    /**
     * to determin Which stack
     */
    private final int index;
    public PlayersStack(final AbstractPlayer player, final int index) {
        plates = new Stack<>();
        images = new ArrayList<>();
        this.player = player;
        // this.manager = controller;
        this.index = index;
    }

    /**
     * Add the new shape to the stack ,then check if we found match or not.
     *
     * @param plate
     *            the plate to put in the Stack.
     */
    public void addShape(final Shape plate, final ImageView image, final int indexOfStack) {
        plates.push(plate);
        images.add(image);
        checkStack();

    }

    public ArrayList<SaveShapeNode> getStackShape() {
        ArrayList<Shape> tmp = new ArrayList<>();
        ArrayList<SaveShapeNode> wanted = new ArrayList<>();
        while (!plates.empty()) {
            tmp.add(plates.pop());
        }
        for (int i = tmp.size() - 1; i >= 0; i--) {
            wanted.add(new SaveShapeNode(tmp.get(i).getClass().getSimpleName(), tmp.get(i).getColor(),
                    images.get(tmp.size() - 1 - i).getX(), images.get(tmp.size() - 1 - i).getY()));
            plates.add(tmp.get(i));
        }
        return wanted;
    }

    public int getHeight() {
        return images.size();
    }

    /**
     * This method is used to put the items we want to check about them in an
     * array.
     *
     */
    private void checkStack() {
        if (plates.size() >= similarity) {
            final Shape[] platesToCheck = new Shape[similarity];
            for (int i = 0; i < similarity; i++) {
                platesToCheck[i] = plates.pop();
            }
            final boolean allSimilar = checkSimilarity(platesToCheck);
            
            if (allSimilar) {
            	
                for (int i = 0; i < similarity; i++) {
                    plates.pop();
                    final ImageView im = images.remove(images.size() - 1);
                    //im.setVisible(false);
                    Platform.runLater(() -> player.removeFromPane(im));
                }
                updateScore();
            }
        }
    }

    /**
     * This method is used to see it the items in the array are similar or not
     *
     * @param platesToCheck
     *            array we want to compare its items
     * @return true -> the items are all similar false -> at least one item is
     *         not similar
     */
    private boolean checkSimilarity(final Shape[] platesToCheck) {
        boolean allSimilar = true;
        final Color comparator = platesToCheck[0].getColor();
        for (int i = similarity - 1; i >= 0; i--) {
            if (!platesToCheck[i].getColor().equals(comparator)) {
                allSimilar = false;
            }
            plates.push(platesToCheck[i]);
        }
        return allSimilar;
    }

    /**
     * modify the Score manager to update the score of the current player.
     */
    private void updateScore() {
        player.incrementScore();
        player.notifyObserver();
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(this);
        }
        thread.setDaemon(true);
        thread.start();

    }

    private void drawStack() {
        final int[][] position = player.getPlayerPosition();

        int leftHight = 0;
        int RightHight = 0;

        for (int i = 0; i < images.size(); i++) {
            if (index == 0) {
            	final int index = i;
            	final int finalHight = leftHight;
                Platform.runLater(() -> images.get(index).setX(position[0][0] + 110));
                Platform.runLater(() -> images.get(index).setY(520 - (50 * finalHight)));
                leftHight++;
            } else {
            	final int index = i;
            	final int finalHight = RightHight;
            	Platform.runLater(() -> images.get(index).setX(position[0][0] - 15));
                Platform.runLater(() -> images.get(index).setY(555 - (50 * finalHight)));;
                RightHight++;
            }

        }
    }

    @Override
    public synchronized void run() {
        // TODO Auto-generated method stub
        while (true) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    drawStack();
                }
            });
            try {
                Thread.sleep(100);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
