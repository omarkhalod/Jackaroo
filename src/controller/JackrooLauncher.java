package controller;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

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
    private static final String VIDEO_PATH = Paths.get(System.getProperty("user.dir")).resolve("src").resolve("view").resolve("resources").resolve("gameplay").resolve("gameplay.mp4").toUri().toString();
    private MediaView mediaView;
      
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
        
        return new StackPane(mediaView, controls);
		
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
        // Prepare the dialog Stage
        Stage dialog = new Stage();
        dialog.initOwner(owner);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Pick a Number");

        // Header
        Label header = new Label("Choose the split distance");
        header.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        // Combo box with your choices
        ObservableList<Integer> choices = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6);
        ComboBox<Integer> combo = new ComboBox<>(choices);
        combo.getSelectionModel().selectFirst();

        // OK and Cancel buttons
        Button ok = new Button("OK");
        Button cancel = new Button("Cancel");
        HBox buttons = new HBox(10, ok, cancel);
        buttons.setAlignment(Pos.CENTER);

        // Layout everything
        VBox layout = new VBox(15, header, new Label("Number:"), combo, buttons);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        // Wire up button actions
        final int[] result = { CardController.splitDistance }; // fallback
        ok.setDefaultButton(true);
        ok.setOnAction(e -> {
            result[0] = combo.getValue();
            dialog.close();
        });
        cancel.setCancelButton(true);
        cancel.setOnAction(e -> {
            // leave result as-is
            dialog.close();
        });

        // Show dialog and wait
        dialog.setScene(new Scene(layout));
        dialog.showAndWait();

        // Store and return
        CardController.splitDistance = result[0];
        return CardController.splitDistance;
    }
	public void popUp(String message) {
	    // 1) Create a new, unimpressive Stage to act as your dialog
	    Stage dialog = new Stage();
	    dialog.setTitle("Error");
	    dialog.initOwner(primaryStage);                          // block only the primary stage
	    dialog.initModality(Modality.WINDOW_MODAL);              // WINDOW_MODAL = only owner is blocked

	    // 2) Build your “content”
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

	    // 3) Put it all in a Scene and show it
	    Scene scene = new Scene(layout);
	    dialog.setScene(scene);

	    // 4) Show and wait for the user to close
	    dialog.showAndWait();
	}
	
	
	
	
	public static void main(String[] args) {
        launch(args);
    }
}
