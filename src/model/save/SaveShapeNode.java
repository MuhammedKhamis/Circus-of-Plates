package model.save;

import javafx.scene.paint.Color;

public class SaveShapeNode {

    private String name;
    private Color color;
    private double x, y;

    public SaveShapeNode(String name, Color color, double x, double y) {
        this.name = name;
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Color getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

}
