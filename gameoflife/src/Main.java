import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.GameOfLife;
import view.config.ConfigPhaseView;
import view.config.ConfigPresenter;

/**
 * Kaï Everaerts
 * 1/02/2019
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        ConfigPhaseView view = new ConfigPhaseView();
        GameOfLife model= new GameOfLife();
        ConfigPresenter root = new ConfigPresenter(view, model);
        Scene scene = new Scene(view);
        primaryStage.getIcons().addAll(new Image("resources/logo.png"));
        primaryStage.setTitle("Game Of Life (Luca & Kai)");
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(800);
        primaryStage.setMinWidth(600);
        primaryStage.show();
    }
}
