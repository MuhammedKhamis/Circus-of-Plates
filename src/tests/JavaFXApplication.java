package tests;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

public class JavaFXApplication extends Application {
    
    public static void main(String[] args) {

        Application.launch(args);
    }

    public void start(Stage primaryStage) {

        primaryStage.setTitle("Simple Animation");
        
        Group root = new Group();
        Scene scene = new Scene(root, 512, 512);
        
        Image road = new Image ("http://hajsoftutorial.com/im/road.png");
        ImageView roadview = new ImageView(road);
        
        Image car = new Image("http://hajsoftutorial.com/im/car.png");
        ImageView carview = new ImageView(car);
        carview.setLayoutX(190);
        carview.setLayoutY(380);
        ImageView carview2 = new ImageView(car);
        carview2.setLayoutX(190+50);
        carview2.setLayoutY(380);
        
        TranslateTransition trtr = new TranslateTransition(Duration.millis(10000), carview);
        TranslateTransition tt = new TranslateTransition(Duration.millis(10000), carview2);
        trtr.setByY(-380);
        trtr.setCycleCount(Timeline.INDEFINITE);
        trtr.setAutoReverse(true);
        trtr.play();
        tt.setByY(-380);
        tt.setCycleCount(Timeline.INDEFINITE);
        tt.setAutoReverse(true);
        tt.play();
        
        root.getChildren().addAll(roadview,carview,carview2);
        primaryStage.setScene(scene);
        primaryStage.show();   
    }
}