package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.UnaryOperator;

import engine.Game;
import exception.InvalidCardException;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;
import model.card.Card;
import model.card.standard.Standard;
import model.card.standard.Suit;
import view.CardView;
import view.MarbleView;

public class CardController {
    public static final Label descLabel = new Label();
    public static final Label suitLabel = new Label();
    public static final Label nameLabel = new Label();
    public static final Label rankLabel = new Label();
    public static final VBox infoBox = new VBox(5);
    public static Card selected=null;
    public static int splitDistance=0;
	public static void hoverOnCard(Card card,Game game) {
		ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), CardView.mp.get(card));
	    scaleUp.setToX(1.1);
	    scaleUp.setToY(1.1);
		CardView.mp.get(card).setOnMouseEntered(e -> {
			if(selected!=null) return;
			scaleUp.playFromStart();
	    	descLabel.setText("Description: " + card.getDescription());
	    	nameLabel.setText("Name: " + card.getName());
	    	if(card instanceof Standard) {
	    		Standard cardn=(Standard) card;
	    		suitLabel.setText("Suit: " + cardn.getSuit());
	    		rankLabel.setText("Rank: " + cardn.getRank());
	    	}
	    	
        });
	}
	public static void leaveCard(Card card,Game game) {
		ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), CardView.mp.get(card));
	    scaleDown.setToX(1.0);
	    scaleDown.setToY(1.0);
		CardView.mp.get(card).setOnMouseExited(e -> {
			if(selected!=null) return;
            descLabel.setText("");
            nameLabel.setText("");
            suitLabel.setText("");
            rankLabel.setText("");
            scaleDown.playFromStart();
        });
	}
	public static void clickCard(Card card,Game game) {
		ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), CardView.mp.get(card));
	    scaleUp.setToX(1.2);
	    scaleUp.setToY(1.2);
	    ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), CardView.mp.get(card));
	    scaleDown.setToX(1.0);
	    scaleDown.setToY(1.0);
		CardView.mp.get(card).setOnMouseClicked(e -> {
			if(e.getButton() == MouseButton.PRIMARY) {
			try {
				if(selected!=null) return;
				game.selectCard(card);
				scaleUp.playFromStart();
				selected=card;
			} catch (InvalidCardException e1) {
				e1.printStackTrace();
			}
			}
		});
	}
}
