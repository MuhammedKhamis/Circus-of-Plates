package controller;

import java.util.BitSet;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import model.players.AbstractPlayer;

public class PlayersMovement extends ImageView implements Runnable {

    private final Pane fPane;
    private final AbstractPlayer player1;
    private final AbstractPlayer player2;
    private final ImageView player1Image;
    private final ImageView player2Image;
    private Thread thread;
    private final BitSet keyboardBitSet = new BitSet();
    private static final int KEYBOARD_MOVEMENT_DELTA = 25;

    public PlayersMovement(final Pane fPane, final ResourcesManager resourcesManager, final ImageView player1Image,
            final ImageView player2Imgae) {
        this.fPane = fPane;
        this.player1 = resourcesManager.getFirstPlayer();
        this.player2 = resourcesManager.getSecondPlayer();
        this.player1Image = player1Image;
        this.player2Image = player2Imgae;
    }

    public void start() {
        final Thread t = new Thread() {
            @Override
            public void run() {
                while (true) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            CheckPause(fPane);
                            if (!Paused.getState()) {
                                Platform.runLater(() -> move(fPane, (player1Image), (player2Image)));
                            }
                        }
                    });
                    try {
                        Thread.sleep(500);
                    } catch (final InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        };
        t.setDaemon(true);
        t.start();

    }

    private void move(final Pane pane, final ImageView player1, final ImageView player2) {

        pane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(final KeyEvent event) {

                keyboardBitSet.set(event.getCode().ordinal(), true);
                for (final KeyCode keyCode : KeyCode.values()) {
                    if (keyboardBitSet.get(keyCode.ordinal())) {
                        if (keyCode == KeyCode.P) {
                            Paused.changeState();
                        }
                        if (keyCode == KeyCode.S){
                            MainController mainController = new MainController();
                            mainController.setSave();
                        }
                        
                        if (!Paused.getState()) {
                            if (keyCode == KeyCode.RIGHT && player1.getX() < 1050) {
                                player1.setX(player1.getX() + KEYBOARD_MOVEMENT_DELTA);
                                PlayersMovement.this.player1.playerPosition((int) player1.getX(), (int) player1.getY());
                            }
                            if (keyCode == KeyCode.LEFT && player1.getX() > 0) {
                                player1.setX(player1.getX() - KEYBOARD_MOVEMENT_DELTA);
                                PlayersMovement.this.player1.playerPosition((int) player1.getX(), (int) player1.getY());
                            }
                            if (keyCode == KeyCode.D && player2.getX() < 1050) {
                                player2.setX(player2.getX() + KEYBOARD_MOVEMENT_DELTA);
                                PlayersMovement.this.player2.playerPosition((int) player2.getX(), (int) player2.getY());
                            }
                            if (keyCode == KeyCode.A && player2.getX() > 0) {
                                player2.setX(player2.getX() - KEYBOARD_MOVEMENT_DELTA);
                                PlayersMovement.this.player2.playerPosition((int) player2.getX(), (int) player2.getY());
                            }
                        }
                    }
                }

            }

        });

        pane.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(final KeyEvent event) {
                keyboardBitSet.set(event.getCode().ordinal(), false);
            }
        });

        pane.setFocusTraversable(true);

    }

    @Override
    public void run() {
        thread.setDaemon(true);
        Platform.runLater(thread);

    }

    public void CheckPause(final Pane pane) {
        pane.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {

        });
    }

}