package controller;

import java.nio.file.Paths;
import java.util.ArrayList;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Colour;
import model.card.Card;
import model.card.Deck;
import model.card.standard.Ace;
import model.card.wild.Wild;
import model.player.Marble;
import model.player.Player;
import view.CardView;
import view.MarbleView;
import engine.board.*;
import exception.CannotFieldException;
import exception.GameException;
import exception.IllegalDestroyException;
import exception.InvalidCardException;
import exception.SplitOutOfRangeException;
import engine.*;

public class Main {
    private static Game game;
    private static HBox handPlayer, handCPU1, handCPU2, handCPU3;	
	public static Controller controller;
	public static JackrooLauncher launcher;
	public static ImageView firePit=new ImageView();
	private static VBox playerTurns=new VBox(5);
    private static Label currPlayerTurn=new Label();
    private static Label nextPlayerTurn=new Label();
    private static StackPane root;
    private static String TRAP_PATH = Paths.get(System.getProperty("user.dir")).resolve("src").resolve("view").resolve("resources").resolve("TRAP.png").toUri().toString();
    
    public static void showTrapFlash() {
        ImageView flash = new ImageView(TRAP_PATH);
        flash.setFitWidth(200);
        flash.setFitHeight(200);
        flash.setOpacity(0.8);  // tweak as desired
        Platform.runLater(() -> {
            root.getChildren().add(flash);
            PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
            pause.setOnFinished(evt -> {
                root.getChildren().remove(flash);
            });
            pause.play();
        });
    }
    

        
//    -----------------------------------------------------------------------------
    
   public static void reset(){
	   game = null;
   }
    
	public static void play(StackPane root) {
		if(game.checkWin()!=null) {
			launcher.endGame(game);
			return;
		}
		if(game.getPlayers().get(0).getSelectedCard().getName().equals("Seven")&&MarbleView.selectedMarbles.size()==2) {
			try {
				game.editSplitDistance(launcher.seven());
			} catch (SplitOutOfRangeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				launcher.popUp(e.getMessage());
			}
		}
		try {
			game.playPlayerTurn();
		} catch (GameException e) {
			launcher.popUp(e.getMessage());
			e.printStackTrace();
			if(game.getCurrentPlayer().getSelectedCard()!=null) {
				game.endPlayerTurn();
				updateBoard();
				deselect();
				updateHands();
				updateFirePit();
				PauseTransition pause = new PauseTransition(Duration.seconds(1));
			    pause.setOnFinished(evt -> {
			        playCPU(root,1);
			    });
			    pause.play();
			}
			return;
		}
		
		game.endPlayerTurn();
		updateBoard();
		deselect();
		updateHands();
		updateFirePit();
		if(CardView.trap) {
			CardView.trap=false;
			showTrapFlash();
		}
		PauseTransition pause = new PauseTransition(Duration.seconds(1));
	    pause.setOnFinished(evt -> {
	        playCPU(root,1);
	    });
	    pause.play();
	}
	public static void playCPU(StackPane root,int i){
		if(game.checkWin()!=null) {
			launcher.endGame(game);
			return;
		}
		currPlayerTurn.setText("Current Player: "+game.getCurrentPlayer().getName());
		nextPlayerTurn.setText("Next player: "+game.getPlayers().get(MarbleController.id(game.getNextPlayerColour(), game)).getName());
		if(i==4) {
			if(!game.canPlayTurn()) {
				game.endPlayerTurnNull();
				updateBoard();
				updateHands();
				updateFirePit();
				deselect();
				if(CardView.trap) {
					CardView.trap=false;
					showTrapFlash();
				}
				PauseTransition pause = new PauseTransition(Duration.seconds(1));
			    pause.setOnFinished(evt -> {
			        playCPU(root,1);
			    });
			    pause.play();
				return;
			}
			return;
		}
		
		if(!game.canPlayTurn()) {
			game.endPlayerTurnNull();
			updateBoard();
			updateHands();
			updateFirePit();
			deselect();
			if(CardView.trap) {
				CardView.trap=false;
				showTrapFlash();
			}
			PauseTransition pause = new PauseTransition(Duration.seconds(1));
		    pause.setOnFinished(evt -> {
		    	playCPU(root,i+1);
		    });
		    pause.play();
			return;
		}
		try {
			game.getPlayers().get(i).play();
		} catch (GameException e) {
			launcher.popUp(e.getMessage());
			e.printStackTrace();
		}
		game.endPlayerTurn();
		updateBoard();
		updateHands();
		updateFirePit();
		deselect();
		if(CardView.trap) {
			CardView.trap=false;
			showTrapFlash();
		}
		PauseTransition pause = new PauseTransition(Duration.seconds(1));
	    pause.setOnFinished(evt -> {
	    	playCPU(root,i+1);
	    });
	    pause.play();
	}
	public static void updateBoard() {
		for(Cell cell:game.getBoard().getTrack()) {
			if(cell.getMarble()!=null) {
				ImageView marb=MarbleView.mp.get(cell.getMarble());
				marb.setLayoutX(BoardController.positions.get(cell).getX());
				marb.setLayoutY(BoardController.positions.get(cell).getY());
			}
		}
		ArrayList<Marble> actionableMarbles=game.getBoard().getActionableMarbles();
		ArrayList<ArrayList<Point2D>> homeZones=BoardController.homeZonePositions;
		int[] free=new int[4];
		for(Marble marble:MarbleView.mp.keySet()) {
			if(actionableMarbles.contains(marble))
				continue;
			int id=MarbleController.id(marble.getColour(), game);
			double x=homeZones.get(id).get(free[id]).getX();
			double y=homeZones.get(id).get(free[id]).getY();
			free[id]++;
			MarbleView.mp.get(marble).setLayoutX(x);
			MarbleView.mp.get(marble).setLayoutY(y);
		}
		ArrayList<SafeZone> safeZones= game.getBoard().getSafeZones();
		for(SafeZone safeZone:safeZones) {
			for(Cell cell:safeZone.getCells()) {
				if(cell.getMarble()!=null) {
					ImageView marb=MarbleView.mp.get(cell.getMarble());
					marb.setLayoutX(BoardController.positions.get(cell).getX());
					marb.setLayoutY(BoardController.positions.get(cell).getY());
				}
			}
		}
	}
	public static void updateHands() {
	    ArrayList<Card> playerHand = game.getPlayers().get(0).getHand();
		handPlayer.getChildren().clear();
	    handCPU1.getChildren().clear();
	    handCPU2.getChildren().clear();
	    handCPU3.getChildren().clear();
	    DropShadow glow = new DropShadow();
	    CardView.hash(playerHand);
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
	}
	public static void updateFirePit() {
		firePit.setVisible(true);
		ArrayList<Card> firePitt=game.getFirePit();
		if(firePitt.isEmpty()) {
			firePit.setVisible(false);
			return;
		}
		CardView.hash(firePitt);
		for(Card card:firePitt) {
			ImageView cardView=CardView.mp.get(card);
			cardView.setOnMouseClicked(null);
			cardView.setOnMouseEntered(null);
			cardView.setOnMouseExited(null);
		}
		Card card=firePitt.get(firePitt.size()-1);
		firePit.setImage(CardView.mp.get(card).getImage());
	}
	public static void deselect() {
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
	public static Color colourToColor(Colour colour) {
		if(colour==Colour.RED)
			return Color.RED;
		if(colour==Colour.BLUE)
			return Color.BLUE;
		if(colour==Colour.GREEN)
			return Color.GREEN;
		return Color.YELLOW;
	}
	public Scene start(String playerName) throws Exception {
	    game = new Game(playerName);
		root = new StackPane();
	    firePit.setPreserveRatio(true);
	    firePit.setFitWidth(100);
        firePit.setFitHeight(139);
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
	    CardController.infoBox.getChildren().addAll(CardController.nameLabel,CardController.rankLabel,CardController.descLabel,CardController.suitLabel);
	    CardController.infoBox.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
	    root.getChildren().add(CardController.infoBox);
	    StackPane.setAlignment(CardController.infoBox,Pos.BOTTOM_LEFT);
	    handPlayer=new HBox(20);
	    handCPU1=new HBox(20);
	    handCPU2=new HBox(20);
	    handCPU3=new HBox(20);
	    handCPU1.setRotate(270);
	    handCPU2.setRotate(180);
	    handCPU3.setRotate(90);
	    updateHands();
	    root.getChildren().add(handPlayer);
	    StackPane.setAlignment(handPlayer, Pos.BOTTOM_CENTER);
	    root.getChildren().add(handCPU1);
	    StackPane.setAlignment(handCPU1, Pos.CENTER_LEFT);
	    root.getChildren().add(handCPU2);
	    StackPane.setAlignment(handCPU2, Pos.TOP_CENTER);
	    root.getChildren().add(handCPU3);
	    StackPane.setAlignment(handCPU3, Pos.CENTER_RIGHT);

	    Button btnPlay     = new Button("Play");
	    Button btnDeselect = new Button("Deselect All");
	    btnPlay.setOnAction(e -> { play(root); });
	    btnDeselect.setOnAction(e -> deselect());
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
	    root.getChildren().add(firePit);
	    StackPane.setAlignment(firePit,Pos.CENTER);
	    firePit.setPickOnBounds(false);
	    DropShadow shadow=new DropShadow();
	    firePit.setEffect(shadow);
	    updateBoard();
	    ArrayList<Color> order=new ArrayList<>();
	    for(Player player:game.getPlayers()) {
	    	order.add(colourToColor(player.getColour()));
	    }
	    currPlayerTurn.setText("Current player: "+playerName);
	    nextPlayerTurn.setText("Next Player: CPU 1");
	    StackPane.setAlignment(playerTurns,Pos.BOTTOM_RIGHT);
	    currPlayerTurn.setStyle(whiteText);
	    nextPlayerTurn.setStyle(whiteText);
	    currPlayerTurn.setWrapText(true);
	    nextPlayerTurn.setWrapText(true);
	    playerTurns.setStyle(
	    	    "-fx-background-color: rgba(30,30,30,0.85);" +
	    	    "-fx-background-radius: 10;" +
	    	    "-fx-padding: 10;" +
	    	    "-fx-border-color: white;" +
	    	    "-fx-border-width: 1;" +
	    	    "-fx-border-radius: 10;"
	    	);
	    playerTurns.getChildren().add(currPlayerTurn);
	    playerTurns.getChildren().add(nextPlayerTurn);
	    playerTurns.setPickOnBounds(false);
	    playerTurns.setPrefWidth(200);
	    playerTurns.setMinSize(Region.USE_PREF_SIZE,Region.USE_PREF_SIZE);
	    playerTurns.setMaxSize(Region.USE_PREF_SIZE,Region.USE_PREF_SIZE);
	    root.getChildren().add(playerTurns);
	    scene.setOnKeyPressed( event -> {
            if (event.getCode() == KeyCode.Q) {
                MarbleController.fieldMarble(game,game.getPlayers().get(0), launcher);
            }else if(event.getCode() == KeyCode.W) {
            	MarbleController.fieldMarble(game,game.getPlayers().get(1), launcher);
            }else if(event.getCode() == KeyCode.E) {
            	MarbleController.fieldMarble(game,game.getPlayers().get(2), launcher);
            }else if(event.getCode() == KeyCode.R) {
            	MarbleController.fieldMarble(game,game.getPlayers().get(3), launcher);
            }
        });
	    controller.setIcons(order);
	    return scene; // to test winning screen just replace "scene" with "switchToWin()"
	}
	

}
