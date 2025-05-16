package controller;

import java.util.ArrayList;

import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.card.Card;
import model.card.Deck;
import model.card.standard.Ace;
import model.player.Player;
import view.CardView;
import view.MarbleView;
import engine.board.*;
import exception.GameException;
import exception.InvalidCardException;
import engine.*;

public class Main extends Application {
	public static void play(Game game) throws GameException {
		game.playPlayerTurn();
	}
	public static void deselect(Game game) {
		if(CardController.selected==null) return;
		Card temp=CardController.selected;
		CardController.selected=null;
		game.deselectAll();
        CardController.descLabel.setText("");
        CardController.nameLabel.setText("");
        CardController.suitLabel.setText("");
        CardController.rankLabel.setText("");
		ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), CardView.mp.get(temp));
	    scaleDown.setToX(1.0);
	    scaleDown.setToY(1.0);
	    scaleDown.playFromStart();
	}
	@Override
	public void start(Stage stage) throws Exception {
	    StackPane root = new StackPane();
	    root.setPadding(new Insets(20));
	    Rectangle2D bound=Screen.getPrimary().getVisualBounds();
	    double width=bound.getWidth();
	    double height=bound.getHeight();
	    Scene scene = new Scene(root, width, height);
	    stage.setFullScreen(true);
	    AnchorPane board = FXMLLoader.load(getClass().getResource("Board.fxml"));
	    root.getChildren().add(board);
	    Game game = new Game("Osos");
	    ArrayList<Card> playerHand = game.getPlayers().get(0).getHand();
	    CardView.hash(playerHand);
	    DropShadow glow = new DropShadow();

	    CardController.infoBox.getChildren().addAll(CardController.nameLabel,CardController.rankLabel,CardController.descLabel,CardController.suitLabel);
	    CardController.infoBox.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
	    root.getChildren().add(CardController.infoBox);
	    StackPane.setAlignment(CardController.infoBox,Pos.BOTTOM_LEFT);

	    HBox handPlayer = new HBox(20);
	    HBox handCPU1   = new HBox(20);
	    HBox handCPU2   = new HBox(20);
	    HBox handCPU3   = new HBox(20);

	    handCPU1.setRotate(90);
	    handCPU2.setRotate(180);
	    handCPU3.setRotate(270);

	    for (Card c : playerHand) {
	        ImageView iv = CardView.mp.get(c);
	        iv.setFitWidth(100);
	        iv.setFitHeight(139);
	        iv.setEffect(glow);
	        CardController.hoverOnCard(c, game);
	        CardController.leaveCard(c, game);
	        CardController.clickCard(c, game);
	        handPlayer.getChildren().add(iv);
	    }
	    handPlayer.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
	    for (int i = 1; i < 4; i++) {
	        for (Card c : game.getPlayers().get(i).getHand()) {
	            ImageView iv = new ImageView("/view/resources/cards/back.png");
	            CardView.mp.put(c, iv);
	            iv.setFitWidth(100);
	            iv.setFitHeight(139);
	            iv.setEffect(glow);
	            (i==1 ? handCPU1 : i==2 ? handCPU2 : handCPU3).getChildren().add(iv);
	        }
	    }
	    handCPU1.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
	    handCPU2.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
	    handCPU3.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
	    handPlayer.setPickOnBounds(false);
	    handCPU1.setPickOnBounds(false);
	    handCPU2.setPickOnBounds(false);
	    handCPU3.setPickOnBounds(false);
	    root.getChildren().add(handPlayer);
	    StackPane.setAlignment(handPlayer, Pos.BOTTOM_CENTER);
	    root.getChildren().add(handCPU1);
	    StackPane.setAlignment(handCPU1, Pos.CENTER_RIGHT);
	    root.getChildren().add(handCPU2);
	    StackPane.setAlignment(handCPU2, Pos.TOP_CENTER);
	    root.getChildren().add(handCPU3);
	    StackPane.setAlignment(handCPU3, Pos.CENTER_LEFT);

	    Button btnPlay     = new Button("Play");
	    Button btnDeselect = new Button("Deselect All");
	    btnPlay.setOnAction(e -> { try { play(game); } catch (GameException ex) { ex.printStackTrace(); } });
	    btnDeselect.setOnAction(e -> deselect(game));
	    btnPlay.setPickOnBounds(false);
	    btnDeselect.setPickOnBounds(false);
	    HBox buttonRow = new HBox(20, btnPlay, btnDeselect);
	    buttonRow.setAlignment(Pos.CENTER);
	    buttonRow.setPickOnBounds(false);
	    VBox bottomBar = new VBox(10, buttonRow, handPlayer);
	    bottomBar.setAlignment(Pos.BOTTOM_CENTER);
	    bottomBar.setPadding(new Insets(10));
	    VBox.setMargin(handPlayer, new Insets(20, 0, 0, 0)); 
	    bottomBar.setPickOnBounds(false);
	    root.getChildren().add(bottomBar);
	    StackPane.setAlignment(bottomBar, Pos.BOTTOM_CENTER);
	    CardController.descLabel .setWrapText(true);
	    CardController.nameLabel .setWrapText(true);
	    CardController.rankLabel .setWrapText(true);
	    CardController.suitLabel .setWrapText(true);
	    String whiteText = "-fx-text-fill: white;";
	    CardController.descLabel .setStyle(whiteText);
	    CardController.nameLabel .setStyle(whiteText);
	    CardController.rankLabel .setStyle(whiteText);
	    CardController.suitLabel .setStyle(whiteText);
	    CardController.infoBox.setPrefWidth(200);
	    CardController.infoBox.setStyle(
	    	    "-fx-background-color: rgba(30,30,30,0.85);" +
	    	    "-fx-background-radius: 10;" +
	    	    "-fx-padding: 10;" +
	    	    "-fx-border-color: white;" +
	    	    "-fx-border-width: 1;" +
	    	    "-fx-border-radius: 10;"
	    	);
	    MarbleView.setMarble(game, scene);
	    stage.setScene(scene);
	    stage.setTitle("Resizable Card-Marble Game");
	    stage.show();
	}
	public static void main(String[] args) {
		launch(args);
	}
}
