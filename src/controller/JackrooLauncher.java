package controller;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
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
	
    private static final String TITLE_TEXT = "Jackroo";
    private static final Duration CHAR_DELAY = Duration.millis(150);
    private static final Duration STARTING_DELAY = Duration.seconds(1);
    private static final int BG_WIDTH = 1800;
    private static final int BG_HEIGHT = 900;
    private static final String VIDEO_PATH =JackrooLauncher.class.getClass().getResource("/view/resources/gameplay/gameplay.mp4").toExternalForm();
    private MediaView mediaView;
    
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

    public void restartGame() {
        primaryStage.setScene(landingScene);
        primaryStage.centerOnScreen();
        primaryStage.setFullScreen(true);
        primaryStage.setTitle("Jackaroo Launcher");
        primaryStage.show();
    }
    
    
    private Parent createLandingRoot() {
    	// importing video
        Media media = new Media(VIDEO_PATH);
        MediaPlayer player = new MediaPlayer(media);
        
        //video play forever
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.setAutoPlay(true);
        
        //so we can see the video and we remove the ratio so it can resize freely 
        mediaView = new MediaView(player);
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
//            	still does not work for some reason
//                new Alert(Alert.AlertType.WARNING,
//                          "Please enter a name before starting.",
//                          ButtonType.OK).showAndWait();
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
        
        return new StackPane(mediaView, controls);
		
	}
    
    
    private void switchToGame(String playerName) {
		Main x = new Main();
		x.launcher=this;
		try {
			this.gameScene = x.start(playerName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
		List<Integer> choices = Arrays.asList(1,2,3,4,5,6);
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(1, choices);
        dialog.initOwner(primaryStage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Pick a Number");
        dialog.setHeaderText("Choose the split distance");
        dialog.setContentText("Number:");
        Optional<Integer> result = dialog.showAndWait();
        result.ifPresent(number -> {
            CardController.splitDistance=number;
        });
        return CardController.splitDistance;
    }
	public void popUp(String message) {
		Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An exception occurred");
        alert.setContentText(message);
		alert.initOwner(primaryStage);
        // 2) Only block that window, not the whole application:
        alert.initModality(Modality.WINDOW_MODAL);
        alert.showAndWait();
	}
	
	
	public static void main(String[] args) {
        launch(args);
    }
}
