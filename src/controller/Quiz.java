package controller;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


public class Quiz extends Application {
	public static String pizza;
	public static String beverage;
	public static int cnt=1;
	@Override
	public void start(Stage primaryStage) {
		try {
			StackPane root = new StackPane();
			Scene scene = new Scene(root,600,600);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Quiz 3 Version 1");
			Button pizza1=new Button();
			pizza1.setText("Pizza1");
			Button pizza2=new Button();
			pizza2.setText("Pizza2");
			Button bev1=new Button();
			bev1.setText("Beverage1");
			Button bev2=new Button();
			bev2.setText("Beverage2");
			Button makeCombo=new Button();
			makeCombo.setText("Make Combo");
			HBox ok=new HBox(20);
			TextArea text=new TextArea();
			text.setMaxSize(Region.USE_PREF_SIZE,Region.USE_PREF_SIZE);
			text.setMinSize(Region.USE_PREF_SIZE,Region.USE_PREF_SIZE);
			text.setPrefSize(300,400);
			pizza1.setOnMouseClicked(e -> {
				pizza1.setDisable(true);
				pizza2.setDisable(false);
				pizza="Pizza1";
			});
			pizza2.setOnMouseClicked(e -> {
				pizza2.setDisable(true);
				pizza1.setDisable(false);
				pizza="Pizza2";
			});
			bev1.setOnMouseClicked(e -> {
				bev1.setDisable(true);
				bev2.setDisable(false);
				beverage="Beverage1";
			});
			bev2.setOnMouseClicked(e -> {
				bev2.setDisable(true);
				bev1.setDisable(false);
				beverage="Beverage2";
			});
			makeCombo.setOnMouseClicked(e -> {
				pizza1.setDisable(false);
				pizza2.setDisable(false);
				bev1.setDisable(false);
				bev2.setDisable(false);
				text.setText(text.getText()+'\n'+"Combo"+cnt++ +":\n"+pizza+"\n"+beverage);
			});
			ok.getChildren().add(pizza1);
			ok.getChildren().add(pizza2);
			ok.getChildren().add(bev1);
			ok.getChildren().add(bev2);
			ok.getChildren().add(makeCombo);
			ok.getChildren().add(text);
			root.getChildren().add(ok);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
