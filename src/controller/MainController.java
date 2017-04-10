package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import controller.util.Enumrations.Players;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import logging.Logging;
import model.gamestates.GameState;
import model.gamestates.PausedState;
import model.gamestates.Player1WinState;
import model.gamestates.Player2WinsState;
import model.gamestates.TiedState;
import model.players.AbstractPlayer;
import model.save.PlayersStacksData;
import model.save.SaveShapeNode;
import model.save.SavedStates;
import model.save.Snapshot;
import model.shapes.interfaces.Shape;
import model.shapes.util.PlatesFactory;
import util.DimensionsConstants;

public class MainController {

    @FXML
    private Pane paneFXid;

    @FXML
    private ImageView imageView;

    @FXML
    private Label counter;

    @FXML
    private Label score1;

    @FXML
    private Label score2;

    @FXML
    private Label scoreValue1;

    @FXML
    private Label scoreValue2;

    @FXML
    private Label pauseLabel;

    private ResourcesManager resourcesManager;
    private ScoreManager scoreManager;
    private AbstractPlayer player1, player2;
    private Integer countingNumbers = 60;
    private boolean halfSecond = true;
    private boolean initialize = true;
    private int difficulty;
    private ImageView pauseImage;
    private ImageView saveImage;
    private ImageView player1Imgae;
    private ImageView player2Imgae;

    @FXML
    public void initialize() {

        // Calling Appropriate Mangers
        resourcesManager = new ResourcesManager(paneFXid);
        scoreManager = ScoreManager.getInstance();

        // Load Background Images
        loadImages();

        // Load Players
        final ImageView player1 = createP1();
        final ImageView player2 = createP2();
        player1Imgae = player1;
        player2Imgae = player2;
        // Make Players Movable
        move(paneFXid, player1, player2);

        // Set Labels with appropriate data
        setLabels();

        // Make Labels following updates
        updateLabels();

    }

    private void loadImages() {
        // Background...
        String backgroundPath = System.getProperty("user.dir") + File.separator + "Resources" + File.separator
                + "wallpaper.jpg" + File.separator;
        backgroundPath = new File(backgroundPath).toURI().toString();
        final Image backgroundImage = new Image(backgroundPath);
        imageView.setImage(backgroundImage);
        imageView.setFitWidth(DimensionsConstants.XBoundary);
        imageView.setFitHeight(DimensionsConstants.YBoundary);

        // Shelf...
        String shelfPath = System.getProperty("user.dir") + File.separator + "Resources" + File.separator + "shelf.png"
                + File.separator;
        shelfPath = new File(shelfPath).toURI().toString();
        final Image shelfImage = new Image(shelfPath);

        // Left Shelf...
        final ImageView leftShelf = new ImageView(shelfImage);
        leftShelf.setX(-50);
        leftShelf.setY(-120);
        leftShelf.setFitWidth(470);
        paneFXid.getChildren().add(leftShelf);

        // Right Shelf...
        final ImageView RightShelf = new ImageView(shelfImage);
        RightShelf.setX(830);
        RightShelf.setY(-120);
        RightShelf.setFitWidth(420);
        paneFXid.getChildren().add(RightShelf);

        // Buttons...
        // Pause
        String pausePath = System.getProperty("user.dir") + File.separator + "Resources" + File.separator + "pause.png"
                + File.separator;
        pausePath = new File(pausePath).toURI().toString();
        final Image pause = new Image(pausePath);

        pauseImage = new ImageView(pause);
        pauseImage.setX(0);
        pauseImage.setY(0);
        pauseImage.setFitWidth(50);
        pauseImage.setFitHeight(50);
        paneFXid.getChildren().add(pauseImage);

        // Save
        String savePath = System.getProperty("user.dir") + File.separator + "Resources" + File.separator + "save.png"
                + File.separator;
        savePath = new File(savePath).toURI().toString();
        final Image save = new Image(savePath);

        saveImage = new ImageView(save);
        saveImage.setX(50);
        saveImage.setY(5);
        saveImage.setFitWidth(40);
        saveImage.setFitHeight(40);
        paneFXid.getChildren().add(saveImage);

        setPauseAction();
        setSaveAction();

    }

    private void setPauseAction() {
        pauseImage.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(final MouseEvent event) {
                Paused.changeState();
            }
        });

    }

    private void setSaveAction() {
        saveImage.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(final MouseEvent event) {

                setSave();

            }
        });
    }

    public void setSave() {
        Paused.setState(true);

        // File Chooser
        final FileChooser fileChooser = new FileChooser();

        // Set extension filter
        final FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");

        fileChooser.getExtensionFilters().addAll(extFilter);

        // Show save file dialog
        try {
            final File path = fileChooser.showSaveDialog(null);

            final String pathString = path.toString();
            final File f = new File(pathString);
            final String fileName = f.getName().substring(0, f.getName().indexOf('.'));
            final String absoulutePath = (pathString.substring(0, pathString.indexOf(fileName)));
            // Getting Scores
            final int len = Players.values().length;
            final int[] scores = new int[len];
            for (final Players t : Players.values()) {
                scores[t.ordinal()] = scoreManager.getScore(t);
            }
            // Getting Players for positions and StackLists
            final AbstractPlayer p1 = resourcesManager.getFirstPlayer();
            final AbstractPlayer p2 = resourcesManager.getSecondPlayer();
            final SavedStates savedStates = new SavedStates(scores, countingNumbers, difficulty,
                    p1.getPlayerPosition()[0], p2.getPlayerPosition()[0]);
            final PlayersStacksData[] data = new PlayersStacksData[len];
            data[0] = new PlayersStacksData(p1.getStackList(0), p1.getStackList(1));
            data[1] = new PlayersStacksData(p2.getStackList(0), p2.getStackList(1));
            final Snapshot save = new Snapshot(savedStates, data);
            save.saveShot(absoulutePath, fileName);
            // Snapshot load = new Snapshot();
            // load.LoadDate(System.getProperty("user.dir"), "test");
        } catch (Exception e) {
            // Do nothing...
        }
    }

    private ImageView createP1() {

        player1 = resourcesManager.getFirstPlayer();
        player1.addObserver(scoreManager);
        final BufferedImage image = player1.getImage();
        final ImageView imageView = convertImage(image);
        imageView.setFitHeight(150);
        imageView.setFitWidth(150);
        imageView.setX(400);// 400
        imageView.setY(520);// 520
        player1.setPoints(imageView.getX(), imageView.getY(), 150);
        paneFXid.getChildren().add(imageView);
        return imageView;
    }

    private ImageView createP2() {
        player2 = resourcesManager.getSecondPlayer();
        player2.addObserver(scoreManager);
        final BufferedImage image = player2.getImage();
        final ImageView imageView = convertImage(image);
        imageView.setFitHeight(150);
        imageView.setFitWidth(150);
        imageView.setX(100);
        imageView.setY(520);
        player2.setPoints(imageView.getX(), imageView.getY(), 150);
        paneFXid.getChildren().add(imageView);
        return imageView;
    }

    public AbstractPlayer getPlayer1() {
        return player1;
    }

    public AbstractPlayer getPlayer2() {
        return player2;
    }

    public void setTimeAfterLoad(final int elapsedTime) {
        countingNumbers = elapsedTime;
    }

    public ScoreManager getScoreManager() {
        return scoreManager;
    }

    private void move(final Pane pane, final ImageView player1Image, final ImageView player2Image) {
        final PlayersMovement playersMovement = new PlayersMovement(pane, resourcesManager, player1Image, player2Image);
        playersMovement.start();
    }

    private void setLabels() {

        // Pause Label...
        pauseLabel.setText("");
        pauseLabel.setFont(new Font(40));
        pauseLabel.setLayoutX(510);
        pauseLabel.setLayoutY(105);

        // Counter Label...
        counter.setText(countingNumbers.toString());
        counter.setFont(new Font(60));
        counter.setLayoutX(590);
        counter.setLayoutY(35);

        // Player1 Label...
        score1.setText("Player 1");
        score1.setFont(new Font(60));
        score1.setLayoutX(235);
        score1.setLayoutY(125);

        scoreValue1.setText("0");
        scoreValue1.setFont(new Font(60));
        scoreValue1.setLayoutX(310);
        scoreValue1.setLayoutY(200);

        // Player2 Label...
        score2.setText("Player 2");
        score2.setFont(new Font(60));
        score2.setLayoutX(800);
        score2.setLayoutY(125);

        scoreValue2.setText("0");
        scoreValue2.setFont(new Font(60));
        scoreValue2.setLayoutX(875);
        scoreValue2.setLayoutY(200);
    }

    // Setting Stars initially
    private void generateStars() {

        final ShapesMovements shape = new ShapesMovements(paneFXid, resourcesManager, difficulty);
        shape.start("Shapes Movement Thread");

    }

    private void updateLabels() {

        final Timeline oneSecond = new Timeline(new KeyFrame(Duration.seconds(0.5), new EventHandler<ActionEvent>() {

            @Override
            public void handle(final ActionEvent event) {
                if (!Paused.getState()) {

                    if (initialize) {
                        Logging.info(String.valueOf(difficulty));
                        generateStars();
                        initialize = false;
                    }

                    // Setting Time Label
                    if (!halfSecond) {
                        countingNumbers--;

                        if (countingNumbers >= 0) {
                            counter.setText(countingNumbers.toString());
                        } else if (countingNumbers == -1) {
                            Paused.changeState();
                            terminate();
                        }
                    }
                    halfSecond = !halfSecond;

                    // Setting Score Label
                    final Integer x = scoreManager.getScore(Players.player1);
                    final Integer y = scoreManager.getScore(Players.player2);

                    scoreValue1.setText(x.toString());
                    scoreValue2.setText(y.toString());

                    pauseLabel.setText("");
                } else {
                    final GameState state = new PausedState();
                    pauseLabel.setText(state.printProperMessage());
                }
            }
        }));
        oneSecond.setCycleCount(Timeline.INDEFINITE);
        oneSecond.play();

    }

    private void terminate() {
        paneFXid.getChildren().clear();
        paneFXid.getChildren().add(imageView);
        final Label l = new Label();
        l.setText("");
        l.setFont(new Font(60));
        l.setLayoutX(480);
        l.setLayoutY(35);
        paneFXid.getChildren().add(l);

        final int x = scoreManager.getScore(Players.player1);
        final int y = scoreManager.getScore(Players.player2);

        GameState state;
        if (x > y) {
            state = new Player1WinState();
        } else if (x < y) {
            state = new Player2WinsState();
        } else {
            state = new TiedState();
        }
        l.setText(state.printProperMessage());
    }

    public void setDifficulty(final int level) {
        difficulty = level;
    }

    public int getDifficulty() {
        return difficulty;
    }

    private ImageView convertImage(final BufferedImage image) {
        final Image imageF = SwingFXUtils.toFXImage(image, null);

        final ImageView dispaly = new ImageView(imageF);

        return dispaly;
    }

    public void loadStates(final SavedStates savedStates, final PlayersStacksData[] data) {
        setTimeAfterLoad(savedStates.getElapsedTime());
        player1Imgae.setX(savedStates.getP(0)[0]);
        player1Imgae.setY(savedStates.getP(0)[1]);
        player2Imgae.setX(savedStates.getP(1)[0]);
        player2Imgae.setY(savedStates.getP(1)[1]);
        for (int i = 0; i < savedStates.getScores()[0]; i++){
            player1.incrementScore();
            scoreManager.update(0);
        }
        for (int i = 0; i < savedStates.getScores()[1]; i++){
            player2.incrementScore();
            scoreManager.update(1);
        }
        fetchLoadedPlates(data);
    }

    private void fetchLoadedPlates(final PlayersStacksData[] data) {
        ArrayList<SaveShapeNode> savedPlates = data[0].getlefttHandshapes();
        for (final SaveShapeNode savedPlate : savedPlates) {
            final Shape plate = PlatesFactory.getInstance().getShape(savedPlate.getName());
            final ImageView imageView = convertImage(plate.getImage());
            imageView.setFitHeight(DimensionsConstants.IMAGE_SIZE);
            imageView.setFitWidth(DimensionsConstants.IMAGE_SIZE);
            paneFXid.getChildren().add(imageView);
            player1.receivePlate(1, plate, imageView);
        }
        savedPlates = data[0].getRightHandshapes();
        for (final SaveShapeNode savedPlate : savedPlates) {
            final Shape plate = PlatesFactory.getInstance().getShape(savedPlate.getName());
            final ImageView imageView = convertImage(plate.getImage());
            imageView.setFitHeight(DimensionsConstants.IMAGE_SIZE);
            imageView.setFitWidth(DimensionsConstants.IMAGE_SIZE);
            Platform.runLater(() -> paneFXid.getChildren().add(imageView));
            player1.receivePlate(0, plate, imageView);
        }
        savedPlates = data[1].getlefttHandshapes();
        for (final SaveShapeNode savedPlate : savedPlates) {
            final Shape plate = PlatesFactory.getInstance().getShape(savedPlate.getName());
            final ImageView imageView = convertImage(plate.getImage());
            imageView.setFitHeight(DimensionsConstants.IMAGE_SIZE);
            imageView.setFitWidth(DimensionsConstants.IMAGE_SIZE);
            Platform.runLater(() -> paneFXid.getChildren().add(imageView));
            player1.receivePlate(1, plate, imageView);
        }
        savedPlates = data[1].getRightHandshapes();
        for (final SaveShapeNode savedPlate : savedPlates) {
            final Shape plate = PlatesFactory.getInstance().getShape(savedPlate.getName());
            final ImageView imageView = convertImage(plate.getImage());
            imageView.setFitHeight(DimensionsConstants.IMAGE_SIZE);
            imageView.setFitWidth(DimensionsConstants.IMAGE_SIZE);
            Platform.runLater(() -> paneFXid.getChildren().add(imageView));
            player2.receivePlate(0, plate, convertImage(plate.getImage()));
        }
    }

}