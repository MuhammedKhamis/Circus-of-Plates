package model.save;

import java.util.ArrayList;

public class PlayersStacksData {

    private int rightStackSize;
    private int leftStackSize;
    private ArrayList<SaveShapeNode> rightHandshapes;
    private ArrayList<SaveShapeNode> leftHandshapes;

    public PlayersStacksData(ArrayList<SaveShapeNode> rightHandshapes, ArrayList<SaveShapeNode> leftHandshapes) {
        this.rightHandshapes = rightHandshapes;
        this.leftHandshapes = leftHandshapes;
        rightStackSize = rightHandshapes.size();
        leftStackSize = leftHandshapes.size();
    }

    public int getRightStackSize() {
        return rightStackSize;
    }

    public int getLeftStackSize() {
        return leftStackSize;
    }

    public ArrayList<SaveShapeNode> getRightHandshapes() {
        return rightHandshapes;
    }

    public ArrayList<SaveShapeNode> getlefttHandshapes() {
        return leftHandshapes;
    }

}
