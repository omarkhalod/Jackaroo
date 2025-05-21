package controller;

import java.nio.file.Paths;

import engine.Game;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class WinningScene {
	
    private static final Duration CHAR_DELAY = Duration.millis(150);
    private static final Duration STARTING_DELAY = Duration.seconds(1);
    private static final int BG_WIDTH = 1800;
    private static final int BG_HEIGHT = 900;
    private static final String VIDEO_PATH = Paths.get(System.getProperty("user.dir")).resolve("src").resolve("view").resolve("resources").resolve("gameplay").resolve("onepiece.mp4").toUri().toString();
    private static MediaView mediaView;
    
    
    public static Parent createWinningRoot(Game game) {
    	String TITLE_TEXT = game.checkWin() + " player won";

    	
    	// importing video
        Media media = new Media(VIDEO_PATH);
        MediaPlayer player = new MediaPlayer(media);
        ImageView chopper=new ImageView("/view/resources/gameplay/raf,360x360,075,t,fafafa_ca443f4786.png");
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
        titleLabel.setFont(new Font("Arial", 60));
        titleLabel.setStyle("-fx-text-fill: white;");
        
        //set cycle count and add a delay of 1 seccond
        typer.setCycleCount(TITLE_TEXT.length());
        typer.setDelay(STARTING_DELAY);
        typer.delayProperty();
        
        //play text
        typer.play();
        

        Button exitButton = new Button("Exit Game");
//      exitButton.setDisable(true);
    	exitButton.setOpacity(0);
        exitButton.setOnAction(e -> {
            Platform.exit();
        });
        
        
        exitButton.setFont(new Font("Arial", 24));
        exitButton.setStyle("-fx-text-fill: white;");
        exitButton.setStyle("-fx-background-color: gray;");

        
//        Button replayButton = new Button("Play a new game");
//        replayButton.setDisable(true);
//    	replayButton.setOpacity(0);
//        replayButton.setOnAction(e -> {
////            JackrooLauncher jackrooLauncher = new JackrooLauncher();
//        	JackrooLauncher.getInstance().showLandingScreen();
//        });
//        
//        
//        
//        replayButton.setFont(new Font("Arial", 24));
//        replayButton.setStyle("-fx-text-fill: white;");
//        replayButton.setStyle("-fx-background-color: gray;");
        
//        VBox controls = new VBox(15, titleLabel, exitButton, replayButton);
        VBox controls = new VBox(15, titleLabel, exitButton);
        controls.setAlignment(Pos.CENTER);
        controls.getStyleClass().add("controls-box");
        
        //link to css file
        titleLabel.getStyleClass().add("title-label");
        exitButton.getStyleClass().add("start-button");
//        replayButton.getStyleClass().add("start-button");

        
        FadeTransition fadeIn2 = new FadeTransition(Duration.seconds(1.2), exitButton);
        fadeIn2.setFromValue(0);
        fadeIn2.setToValue(1);
        
//        FadeTransition fadeIn1 = new FadeTransition(Duration.seconds(1.2), replayButton);
//        fadeIn1.setFromValue(0);
//        fadeIn1.setToValue(1);
//        
        
        
        // when typing finishes, enable inputs *and* run fade:
        typer.setOnFinished(e -> {
            exitButton.setDisable(false);
//            replayButton.setDisable(false);
            fadeIn2.play();
//            fadeIn1.play();
        });
        
        StackPane root = new StackPane();
        root.getChildren().add(mediaView);
        mediaView.fitWidthProperty().bind(root.widthProperty());
        mediaView.fitHeightProperty().bind(root.heightProperty());
        root.getChildren().add(chopper);
        chopper.fitWidthProperty().bind(root.widthProperty());
        chopper.fitHeightProperty().bind(root.heightProperty());
        root.getChildren().add(controls);
        return root;

        
		
	}
}
