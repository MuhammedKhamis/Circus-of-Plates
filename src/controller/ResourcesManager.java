package controller;

import controller.util.StackRemover;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import model.players.AbstractPlayer;
import model.players.Player1;
import model.players.Player2;
import model.shapes.interfaces.Shape;
import model.shapes.pool.PlatesPool;

public class ResourcesManager {

    private final PlatesPool platesPool;
    private final AbstractPlayer player1;
    private final AbstractPlayer player2;
    private final StackRemover remover;

    public ResourcesManager(final Pane pane) {
        platesPool = new PlatesPool();
        player1 = new Player1(this);
        player2 = new Player2(this);
        remover = new StackRemover();
        setPane(pane);
    }

    private void setPane(final Pane pane) {
    	remover.setPane(pane);
    }

    public void removePlate(final Node node) {
    	remover.remove(node);
    }

    public Shape getPlate() {
        return platesPool.getPlate();
    }

    public void returnPlate(final Shape finishedShape) {
        platesPool.returnPlate(finishedShape);
    }

    public AbstractPlayer getFirstPlayer() {
        return player1;
    }

    public AbstractPlayer getSecondPlayer() {
        return player2;
    }
}
