
import ch.rs.reflectorgrid.ReflectorGrid;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ricardo
 */
public class Main extends Application{
    
    static ReflectorGrid grid = new ReflectorGrid();
    
    public static void main(String[] args){
        
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Logic Editor");
        Scene scene = new Scene(grid.turnObjectIntoGrid(new ExampleClass()));
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
}
