package controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.application.Platform;
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
import model.player.Marble;
import model.player.Player;
import view.CardView;
import view.MarbleView;
import engine.board.*;
import exception.GameException;
import exception.InvalidCardException;
import engine.*;

public class Main {
    private static Game game;
    private static HBox handPlayer, handCPU1, handCPU2, handCPU3;	
	private static Controller controller;
	public static JackrooLauncher launcher;
	
	
	public static boolean hasAnyValidMove(Game game) {
	    Player player = game.getCurrentPlayer();
	    ArrayList<Marble> marbles = player.getMarbles();
	    ArrayList<Card> cards = player.getHand();

	    for (Card card : cards) {
	        // Try all single-marble moves
	        for (Marble m : marbles) {
	            ArrayList<Marble> testMove = new ArrayList<>();
	            testMove.add(m);
	            try {
	                card.act(testMove);
	                return true;
	            } catch (Exception e) {
	                // Ignore invalid move
	            }
	        }

	        // Try all two-marble combos (if allowed by card type)
	        for (int i = 0; i < marbles.size(); i++) {
	            for (int j = i + 1; j < marbles.size(); j++) {
	                ArrayList<Marble> testMove = new ArrayList<>();
	                testMove.add(marbles.get(i));
	                testMove.add(marbles.get(j));
	                try {
	                    card.act(testMove);
	                    return true;
	                } catch (Exception e) {
	                    // Ignore invalid move
	                }
	            }
	        }
	    }

	    return false; // No card worked with any marble combo
	}

	
	public static void play(Game game,StackPane root) throws GameException {
	
		if(!hasAnyValidMove(game)){
			Card discard = game.getCurrentPlayer().getHand().remove(0);
			refreshUI();
			endTurn();
			playCPUTurnsSequentially();
			return;
		}
		if(CardController.selected.getName().equals("Seven")&&MarbleView.selectedMarbles.size()==2) {
			int split=launcher.seven();
			game.editSplitDistance(split);
			game.playPlayerTurn();
			MarbleController.moveMarble(game,MarbleView.selectedMarbles.get(0),BoardController.sevenFirstPath);
			MarbleController.moveMarble(game,MarbleView.selectedMarbles.get(1),BoardController.sevenSecondPath);
//			deselect(game);
			endTurn();
			playCPUTurnsSequentially();
			return;
		}
		game.playPlayerTurn();
		if(MarbleView.selectedMarbles.isEmpty()) {
			if(CardController.selected.getName().equals("Queen")) {
				CardController.discardCard(game,CardController.selected,root);
				CardController.discardCard(game,CardController.discarded,root);
//				deselect(game);
				endTurn();
				playCPUTurnsSequentially();
				return;
			}else
				MarbleController.fieldMarble(game);
			
		}else if(MarbleView.selectedMarbles.size()==1) {
			MarbleController.moveMarble(game,MarbleView.selectedMarbles.get(0),BoardController.fullPath);
		}else {	
			double x=MarbleView.selectedMarbles.get(0).getLayoutX();
			double y=MarbleView.selectedMarbles.get(0).getLayoutY();
			MarbleView.selectedMarbles.get(0).setLayoutX(MarbleView.selectedMarbles.get(1).getLayoutX());
			MarbleView.selectedMarbles.get(0).setLayoutY(MarbleView.selectedMarbles.get(1).getLayoutY());
			MarbleView.selectedMarbles.get(1).setLayoutX(x);
			MarbleView.selectedMarbles.get(1).setLayoutY(y);
		}
		CardController.discardCard(game,CardController.selected,root);
//		deselect(game);
		endTurn();
		playCPUTurnsSequentially();
	}
	private static void playCPUTurnsSequentially() {
	    if (game.getCurrentPlayer() != game.getPlayers().get(0)) {
	    	if(!game.canPlayTurn()){
	    		game.endPlayerTurn();
	    		refreshUI();
	    		playCPUTurnsSequentially();
	    	}
	    	else{
	    		System.out.println("Playing turn " + game.getCurrentPlayer().getName());
		    	PauseTransition pause = new PauseTransition(Duration.seconds(1));
		        pause.setOnFinished(event -> {
		            try {
//		            	Map<Marble, Cell> beforeMap = MarbleController.getMarblePositions(game);

		                game.playPlayerTurn(); // CPU makes move
		                Player current = game.getCurrentPlayer();
		                Card selectedCard = current.getSelectedCard();
		                ArrayList<Marble> selectedMarbles = current.getMarbles();
		                System.out.println(selectedMarbles);
		                
//		                double x=MarbleView.selectedMarbles.get(0).getLayoutX();
//		    			double y=MarbleView.selectedMarbles.get(0).getLayoutY();
//		    			MarbleView.selectedMarbles.get(0).setLayoutX(MarbleView.selectedMarbles.get(1).getLayoutX());
//		    			MarbleView.selectedMarbles.get(0).setLayoutY(MarbleView.selectedMarbles.get(1).getLayoutY());
//		    			MarbleView.selectedMarbles.get(1).setLayoutX(x);
//		    			MarbleView.selectedMarbles.get(1).setLayoutY(y);
		                
//		                System.out.println("Card played: " + game.getCurrentPlayer().getSelectedCard());
//		                game.endPlayerTurn();
		                endTurn();

		            } catch (GameException e) {
		                e.printStackTrace();
		            }
		            
		            // Continue with the next CPU if not back to human
		            refreshUI();
		            playCPUTurnsSequentially();
		        });
		        pause.play();
		    }
	    }
	    	
	}
	private static void refreshUI() {
	    updateHands();
	    deselect(game);

	    int current = game.getPlayers().indexOf(game.getCurrentPlayer());
	    if (controller != null) {
	        Platform.runLater(() -> controller.highlightCurrentPlayer(current));
	    }
	}
    public static void endTurn() {
        game.endPlayerTurn();
        updateHands();
        deselect(game);
        
        // Highlight current player
        int currentPlayerIndex = game.getPlayers().indexOf(game.getCurrentPlayer());
        if (controller != null) {
            Platform.runLater(() -> {
                controller.highlightCurrentPlayer(currentPlayerIndex);
            });
        }
    }
	private static void updateHands() {
        // Clear current hands
        handPlayer.getChildren().clear();
        handCPU1.getChildren().clear();
        handCPU2.getChildren().clear();
        handCPU3.getChildren().clear();
        
        // Update player hand (human)
		
//		handPlayer.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        for (Card c : game.getPlayers().get(0).getHand()) {
        	
            ImageView iv = CardView.mp.get(c);
            iv.setFitWidth(100);
            iv.setFitHeight(139);
//            iv.setEffect(glow);
            CardController.hoverOnCard(c, game);
            CardController.leaveCard(c, game);
            CardController.clickCard(c, game);
            handPlayer.getChildren().add(iv);
        }
        
        // Update CPU hands (showing backs)
        for (int i = 1; i < 4; i++) {
            for (Card c : game.getPlayers().get(i).getHand()) {
                ImageView iv = new ImageView("/view/resources/cards/back.png");
                CardView.mp.put(c, iv);
                iv.setFitWidth(100);
                iv.setFitHeight(139);
                (i==1 ? handCPU1 : i==2 ? handCPU2 : handCPU3).getChildren().add(iv);
            }
        }
        
  
    }
	public static void deselect(Game game) {
		Card temp=CardController.selected;
		CardController.selected=null;
		game.deselectAll();
        CardController.descLabel.setText("");
        CardController.nameLabel.setText("");
        CardController.suitLabel.setText("");
        CardController.rankLabel.setText("");
        if(temp!=null) {
        	ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), CardView.mp.get(temp));
	    	scaleDown.setToX(1.0);
	    	scaleDown.setToY(1.0);
	    	scaleDown.playFromStart();
        }
	    ArrayList<ImageView> selected=MarbleView.selectedMarbles;
	    DropShadow shadow=new DropShadow();
	    for(ImageView marble:selected) {
	    	ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), marble);
	    	scaleDown.setToX(1.0);
	    	scaleDown.setToY(1.0);
	    	scaleDown.playFromStart();
	    	marble.setEffect(shadow);
	    }
	    MarbleView.selectedMarbles.clear();
	}
	
	public Scene start(String playerName) throws Exception {
	    StackPane root = new StackPane();
	    root.setPadding(new Insets(20));
	    Rectangle2D bound=Screen.getPrimary().getVisualBounds();
	    double width=bound.getWidth();
	    double height=bound.getHeight();
	    Scene scene = new Scene(root, width, height);
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("Board.fxml"));
	    AnchorPane board = loader.load();
	    controller = loader.getController();
	    root.getChildren().add(board);
	    controller.setName(playerName);
	    game = new Game(playerName);
	    ArrayList<Card> playerHand = game.getPlayers().get(0).getHand();
	    CardView.hash(playerHand);
	    DropShadow glow = new DropShadow();

	    CardController.infoBox.getChildren().addAll(CardController.nameLabel,CardController.rankLabel,CardController.descLabel,CardController.suitLabel);
	    CardController.infoBox.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
	    root.getChildren().add(CardController.infoBox);
	    StackPane.setAlignment(CardController.infoBox,Pos.BOTTOM_LEFT);

	    handPlayer = new HBox(20);
	    handCPU1   = new HBox(20);
	    handCPU2   = new HBox(20);
	    handCPU3   = new HBox(20);

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
	    btnPlay.setOnAction(e -> { try { play(game,root); } catch (GameException ex) { ex.printStackTrace(); } });
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
	    BoardController.setUp(root, game);
	    return scene;
	}
}
