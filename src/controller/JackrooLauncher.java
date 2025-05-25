package controller;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

import engine.Game;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;

public class JackrooLauncher extends Application{
	private Stage primaryStage;
	private Scene landingScene;
	private Scene gameScene;
	
    private static final String TITLE_TEXT = "Jackaroo";
    private static final Duration CHAR_DELAY = Duration.millis(150);
    private static final Duration STARTING_DELAY = Duration.seconds(1);
    private static final int BG_WIDTH = 1800;
    private static final int BG_HEIGHT = 900;
    private ImageView mediaView;
      
    private static JackrooLauncher instance;     // <-- NEW

    public JackrooLauncher() {                   // <-- NEW
        instance = this;
    }

    public static JackrooLauncher getInstance() {// <-- NEW
        return instance;
    }
    
    
    
    @Override
    public void start(Stage stage) throws Exception {
		this.primaryStage = stage;
		Parent landingRoot = createLandingRoot();
		
        landingScene = new Scene(landingRoot, BG_WIDTH, BG_HEIGHT);
        //Make video fill on resize
        landingScene.widthProperty().addListener((obs, oldW, newW) ->
            mediaView.setFitWidth(newW.doubleValue()));
        landingScene.heightProperty().addListener((obs, oldH, newH) ->
            mediaView.setFitHeight(newH.doubleValue()));
        landingScene.getStylesheets().add(
                getClass().getResource("app.css").toExternalForm()
            );
        
        stage.setTitle("Jackaroo Launcher");
        stage.centerOnScreen();
        stage.setFullScreen(true);
        stage.setScene(landingScene);
        stage.setMinWidth(500);
        stage.setMinHeight(700);
        stage.show();
    }
    
    
    private Parent createLandingRoot() {
        
        mediaView = new ImageView("/view/resources/gameplay/start.png");
        mediaView.setPreserveRatio(false);
        mediaView.setFitWidth(BG_WIDTH);
        mediaView.setFitHeight(BG_HEIGHT);

        // Typing effect title (maybe add diffrent types of timelines)
        Label titleLabel = new Label();
        Timeline typer = new Timeline(
            new KeyFrame(CHAR_DELAY, e -> {
                int next = titleLabel.getText().length();
                titleLabel.setText(titleLabel.getText() + TITLE_TEXT.charAt(next));
            })
        );
        //set cycle count and add a delay of 1 seccond
        typer.setCycleCount(TITLE_TEXT.length());
        typer.setDelay(STARTING_DELAY);
        typer.delayProperty();
        
        //play text
        typer.play();
        

        //Name input and button
        Label promptLabel = new Label("Enter your player name:");
        TextField nameField = new TextField();
        nameField.setPromptText("Your name…");
        
        //so they can be invisible
        nameField.setDisable(true);
        nameField.setOpacity(0);
        promptLabel.setOpacity(0);

        Button startButton = new Button("Start Game");
        startButton.setDisable(true);
    	startButton.setOpacity(0);
        startButton.setOnAction(e -> {
            String playerName = nameField.getText().trim();
            if (playerName.isEmpty()) {
            	popUp("Input a name first plz");
            } else {
                System.out.println("Starting game for player: " + playerName);
                switchToGame(playerName);
            }
        });

        
        VBox controls = new VBox(15, titleLabel, promptLabel, nameField, startButton);
        controls.setAlignment(Pos.CENTER);
        controls.getStyleClass().add("controls-box");
        
        //link to css file
        titleLabel.getStyleClass().add("title-label");
        promptLabel.getStyleClass().add("prompt-label");
        nameField.getStyleClass().add("rounded-text-field");
        startButton.getStyleClass().add("start-button");
        
        nameField.setOnKeyPressed(e -> {
        	startButton.setDisable(false);
        	startButton.setOpacity(1);
        	
        });

        
        FadeTransition fadeIn2 = new FadeTransition(Duration.seconds(1.2), promptLabel);
        fadeIn2.setFromValue(0);
        fadeIn2.setToValue(1);
        
        FadeTransition fadeIn1 = new FadeTransition(Duration.seconds(2), nameField);
        fadeIn1.setFromValue(0);
        fadeIn1.setToValue(1);
        
        
        fadeIn2.setOnFinished(e -> {
        	fadeIn1.play();
        });
        
        
        // when typing finishes, enable inputs *and* run fade:
        typer.setOnFinished(e -> {
            nameField.setDisable(false);
            startButton.setDisable(false);
            fadeIn2.play();
        });
        Pane xd=new Pane();
        xd.setMaxSize(Region.USE_PREF_SIZE,Region.USE_PREF_SIZE);
        xd.getChildren().add(mediaView);
        return new StackPane(xd, controls);
		
	}
    
    
    private void switchToGame(String playerName) {
		Main x = new Main();
		x.launcher = this;
		try {
			this.gameScene = x.start(playerName);
		} catch (Exception e) {
			System.out.println("this aint working here 2");
			e.printStackTrace();
		}
		
		this.primaryStage.setScene(gameScene);
		this.primaryStage.centerOnScreen();
		this.primaryStage.setFullScreen(true);
		this.primaryStage.setTitle("Jackaroo – Let’s Play!");
		this.primaryStage.show();
	}
    

    public int seven() {
    	return getSplitDistance(primaryStage);
    }
    public int getSplitDistance(Stage owner) {
        Stage dialog = new Stage();
        dialog.initOwner(owner);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Pick a Number");
        Label header = new Label("Choose the split distance");
        header.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        ObservableList<Integer> choices = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6);
        ComboBox<Integer> combo = new ComboBox<>(choices);
        combo.getSelectionModel().selectFirst();
        Button ok = new Button("OK");
        Button cancel = new Button("Cancel");
        HBox buttons = new HBox(10, ok, cancel);
        buttons.setAlignment(Pos.CENTER);
        VBox layout = new VBox(15, header, new Label("Number:"), combo, buttons);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        final int[] result = { CardController.splitDistance };
        ok.setDefaultButton(true);
        ok.setOnAction(e -> {
            result[0] = combo.getValue();
            dialog.close();
        });
        cancel.setCancelButton(true);
        cancel.setOnAction(e -> {
            dialog.close();
        });
        dialog.setScene(new Scene(layout));
        dialog.showAndWait();
        CardController.splitDistance = result[0];
        return CardController.splitDistance;
    }
	public void popUp(String message) {
	    Stage dialog = new Stage();
	    dialog.setTitle("Error");
	    dialog.initOwner(primaryStage);                         
	    dialog.initModality(Modality.WINDOW_MODAL);            
	    Label headerLabel = new Label("An exception occurred");
	    headerLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

	    Label messageLabel = new Label(message);
	    messageLabel.setWrapText(true);

	    Button okButton = new Button("OK");
	    okButton.setDefaultButton(true);
	    okButton.setOnAction(e -> dialog.close());

	    VBox layout = new VBox(10, headerLabel, messageLabel, okButton);
	    layout.setPadding(new Insets(15));
	    layout.setAlignment(Pos.CENTER);
	    Scene scene = new Scene(layout);
	    dialog.setScene(scene);
	    dialog.showAndWait();
	}
	
	public void endGame(Game game){
    	if(game.checkWin()!= null){
    		switchToWin(game);
    	}
    }
	
	public void switchToWin(Game game) {
	    	Scene winningScene = new Scene(WinningScene.createWinningRoot(game));
	    	
	    	this.primaryStage.setScene(winningScene);
			this.primaryStage.centerOnScreen();
			this.primaryStage.setFullScreen(true);
			this.primaryStage.setTitle("GG!");
			this.primaryStage.show();
		}
	
	
	public static void main(String[] args) {
        launch(args);
    }
}
