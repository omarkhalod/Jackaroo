package controller;

import java.util.ArrayList;
import java.util.HashMap;

import engine.Game;
import exception.InvalidCardException;
import javafx.animation.ScaleTransition;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import model.card.Card;
import model.card.standard.Standard;
import model.card.standard.Suit;

public class CardController {
	public static HashMap<Card,ImageView> mp=new HashMap<Card,ImageView>();
	private static String id(int rank,Suit suit) {
		String out=""+rank;
		if(suit==Suit.CLUB)
			out+='c';
		else if(suit==Suit.HEART)
			out+='h';
		else if(suit==Suit.DIAMOND)
			out+='d';
		else
			out+='s';
		return out;
	}
	public static void hash(ArrayList<Card> hand) {
		for(Card card:hand) {
			if(card instanceof Standard) {
				Standard cardn=(Standard) card;
				ImageView img=new ImageView("/view/resources/cards/"+id(cardn.getRank(),cardn.getSuit())+".png");
				img.setSmooth(true);
				img.setPreserveRatio(true);
				mp.put(card,img);
			}else {
				ImageView img=new ImageView("/view/resources/cards/joker.png");
				img.setSmooth(true);
				img.setPreserveRatio(true);
				mp.put(card,img);
			}
		}
	}
    public static final Label descLabel = new Label();
    public static final Label suitLabel = new Label();
    public static final Label nameLabel = new Label();
    public static final Label rankLabel = new Label();
    public static final VBox infoBox = new VBox(5);
    public static Card selected=null;
	public static void hoverOnCard(Card card,Game game) {
		ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), mp.get(card));
	    scaleUp.setToX(1.1);
	    scaleUp.setToY(1.1);
		mp.get(card).setOnMouseEntered(e -> {
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
		ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), mp.get(card));
	    scaleDown.setToX(1.0);
	    scaleDown.setToY(1.0);
		mp.get(card).setOnMouseExited(e -> {
			if(selected!=null) return;
            descLabel.setText("");
            nameLabel.setText("");
            suitLabel.setText("");
            rankLabel.setText("");
            scaleDown.playFromStart();
        });
	}
	public static void clickCard(Card card,Game game) {
		ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), mp.get(card));
	    scaleUp.setToX(1.2);
	    scaleUp.setToY(1.2);
	    ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), mp.get(card));
	    scaleDown.setToX(1.0);
	    scaleDown.setToY(1.0);
		mp.get(card).setOnMouseClicked(e -> {
			if(e.getButton() == MouseButton.PRIMARY) {
			try {
				if(selected!=null) return;
				game.selectCard(card);
				scaleUp.playFromStart();
				selected=card;
			} catch (InvalidCardException e1) {
				e1.printStackTrace();
			}
			}else if(e.getButton()== MouseButton.SECONDARY) {
				if(card!=selected) return;
				game.deselectAll();
				selected=null;
				scaleDown.playFromStart();
			}
		});
	}
}
