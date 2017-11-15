package Example;

import ch.rs.reflectorgrid.ReflectorGrid;
import ch.rs.reflectorgrid.util.DefaultFieldNamingStrategy;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * A small Example main class...
 */
public class ExampleMain extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        ExampleObject exampleObject = new ExampleObject();
        ReflectorGrid reflectorGrid = new ReflectorGrid();
        reflectorGrid.setFieldNamingStrategy(DefaultFieldNamingStrategy.SPLIT_TO_CAPITALIZED_WORDS);

        GridPane gridPane = reflectorGrid.transfromIntoGrid(exampleObject);

        primaryStage.setScene(new Scene(gridPane));
        primaryStage.sizeToScene();
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> System.exit(0));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
