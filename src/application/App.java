package application;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logging.Logging;
import model.levels.HighSpeed;
import model.levels.LowSpeed;
import model.levels.MediumSpeed;
import model.levels.util.LevelSpeedStrategy;
import model.save.PlayersStacksData;
import model.save.SavedStates;
import model.save.Snapshot;
import model.shapes.dynamicloading.Loader;

public class App extends Application {

    public Stage stage;
    private SavedStates savedStates;
    private PlayersStacksData[] data;

    private static final Font FONT = Font.font("", FontWeight.BOLD, 18);

    private VBox menuBox;
    private int currentItem = 0;

    private final ScheduledExecutorService bgThread = Executors.newSingleThreadScheduledExecutor();

    private Parent createContent() {
        final Pane root = new Pane();
        root.setPrefSize(900, 600);

        final Rectangle bg = new Rectangle(900, 600);

        final HBox hbox = new HBox(15);
        hbox.setTranslateX(120);
        hbox.setTranslateY(50);

        final MenuItem easyItem = new MenuItem("EASY");
        easyItem.setOnActivate(() -> loadGameScene(30, false));

        final MenuItem mediumItem = new MenuItem("MEDIUM");
        mediumItem.setOnActivate(() -> loadGameScene(15, false));

        final MenuItem hardItem = new MenuItem("HARD");
        hardItem.setOnActivate(() -> loadGameScene(5, false));

        final MenuItem load = new MenuItem("LOAD");
        load.setOnActivate(() -> loadGame());

        final MenuItem itemExit = new MenuItem("EXIT");
        itemExit.setOnActivate(() -> System.exit(0));

        menuBox = new VBox(10, easyItem, mediumItem, hardItem, load, itemExit);
        menuBox.setAlignment(Pos.TOP_CENTER);
        menuBox.setTranslateX(360);
        menuBox.setTranslateY(300);

        final Text about = new Text("Circus of Plates");
        about.setTranslateX(50);
        about.setTranslateY(500);
        about.setFill(Color.WHITE);
        about.setFont(FONT);
        about.setOpacity(0.2);

        getMenuItem(0).setActive(true);

        root.getChildren().addAll(bg, hbox, menuBox, about);

        String pausePath = System.getProperty("user.dir") + File.separator + "Resources" + File.separator + "circus.png"
                + File.separator;
        pausePath = new File(pausePath).toURI().toString();
        final Image pause = new Image(pausePath);

        final ImageView pauseImage = new ImageView(pause);
        pauseImage.setX(290);
        pauseImage.setY(20);
        pauseImage.setFitWidth(250);
        pauseImage.setFitHeight(250);
        root.getChildren().add(pauseImage);

        return root;
    }

    private MenuItem getMenuItem(final int index) {
        return (MenuItem) menuBox.getChildren().get(index);
    }

    private static class MenuItem extends HBox {
        private final TriCircle c1 = new TriCircle(), c2 = new TriCircle();
        private final Text text;
        private Runnable script;

        public MenuItem(final String name) {
            super(15);
            setAlignment(Pos.CENTER);

            text = new Text(name);
            text.setFont(FONT);
            text.setEffect(new GaussianBlur(2));

            getChildren().addAll(c1, text, c2);
            setActive(false);
            setOnActivate(() -> Logging.info(name + " activated"));
        }

        public void setActive(final boolean b) {
            c1.setVisible(b);
            c2.setVisible(b);
            text.setFill(b ? Color.WHITE : Color.GREY);
        }

        public void setOnActivate(final Runnable r) {
            script = r;
        }

        public void activate() {
            if (script != null)
                script.run();
        }
    }

    private static class TriCircle extends Parent {
        public TriCircle() {
            final Shape shape1 = Shape.subtract(new Circle(5), new Circle(2));
            shape1.setFill(Color.WHITE);

            final Shape shape2 = Shape.subtract(new Circle(5), new Circle(2));
            shape2.setFill(Color.WHITE);
            shape2.setTranslateX(5);

            final Shape shape3 = Shape.subtract(new Circle(5), new Circle(2));
            shape3.setFill(Color.WHITE);
            shape3.setTranslateX(2.5);
            shape3.setTranslateY(-5);

            getChildren().addAll(shape1, shape2, shape3);

            setEffect(new GaussianBlur(2));
        }
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        loadMenuScene(primaryStage);
    }

    public static void main(final String[] args) {
        launch(args);
    }

    public void loadMenuScene(final Stage primaryStage) {
        currentItem = 0;
        stage = primaryStage;
        final Scene scene = new Scene(createContent());
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.UP) {
                if (currentItem > 0) {
                    getMenuItem(currentItem).setActive(false);
                    getMenuItem(--currentItem).setActive(true);
                }
            }

            if (event.getCode() == KeyCode.DOWN) {
                if (currentItem < menuBox.getChildren().size() - 1) {
                    getMenuItem(currentItem).setActive(false);
                    getMenuItem(++currentItem).setActive(true);
                }
            }

            if (event.getCode() == KeyCode.ENTER) {
                getMenuItem(currentItem).activate();
            }
        });
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> {
            bgThread.shutdownNow();
        });
        primaryStage.show();
    }

    public void loadGameScene(final int level, final boolean isLoading) {

        final Button b = new Button("BACK");
        b.setLayoutX(300);
        b.setLayoutY(300);
        b.setOnAction(e -> loadMenuScene(stage));
        final Loader loader = Loader.getInstance();
        final String path = System.getProperty("user.dir") + File.separator + "bin" + File.separator + "model"
                + File.separator + "shapes";
        final File file = new File(path);
        for (File f : file.listFiles()) {
            loader.invokeClassMethod(f);
        }

        Parent root = null;
        try {
            final FXMLLoader newLoader = new FXMLLoader(getClass().getResource("/view/GameDesign.fxml"));
            root = (Parent) newLoader.load();
            final MainController controller = newLoader.<MainController> getController();

            LevelSpeedStrategy strategy;

            if (level == 30) {
                strategy = new LowSpeed(controller);
            } else if (level == 15) {
                strategy = new MediumSpeed(controller);
            } else {
                strategy = new HighSpeed(controller);
            }

            strategy.start();

            if (isLoading) {
                // controller.setTimeAfterLoad(savedStates.getElapsedTime());
                // controller.getPlayer1().setPlayerPosition(savedStates.getP(0));
                // controller.getPlayer2().setPlayerPosition(savedStates.getP(1));
                controller.loadStates(savedStates, data);
            }

        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        stage.setTitle("Game");
        stage.setScene(new Scene(root));
        stage.setMinWidth(1200);
        stage.setMinHeight(700);
        stage.setX(0);
        stage.setY(0);
        stage.setResizable(false);
        stage.show();

    }

    private void loadGame() {
        try {
            final FileChooser fileChooser = new FileChooser();
            final File path = fileChooser.showOpenDialog(null);
            // Show save file dialog
            final String pathString = path.toString();
            final File f = new File(pathString);
            final String fileName = f.getName().substring(0, f.getName().indexOf('.'));
            final String absoulutePath = (pathString.substring(0, pathString.indexOf(fileName)));
            final Snapshot snapshot = new Snapshot();
            snapshot.LoadDate(absoulutePath, fileName);
            savedStates = snapshot.getGameState();
            data = new PlayersStacksData[2];
            data[0] = snapshot.getDate(0);
            data[1] = snapshot.getDate(1);
            loadGameScene(savedStates.getDiff(), true);
        } catch (Exception e) {
            Logging.fatal(e.getMessage());
        }
    }

}