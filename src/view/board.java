package view;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class board extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Group root = new Group();
		Label l = new Label("Hello World!");
		TextField txt=new TextField();
		root.getChildren().add(l);
		root.getChildren().add(txt);
		txt.getOnKeyPressed();
		txt.clear();
		// Step 2: Create a Scene (using root [width + height])
		Scene s = new Scene(root, 1000, 800);
		// Step 3: Add the Scene to the Stage
		primaryStage.setScene(s);
		primaryStage.setTitle("First FX Application");
		// Step 4: Show the stage
		primaryStage.show();
		
	}
	public static void main(String[]args){
		// Step 5: Call launch() in main()
		launch(args);
	}
}
