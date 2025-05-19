package view;
import java.util.ArrayList;
import java.util.HashMap;

import engine.Game;
import javafx.scene.image.ImageView;
import model.card.Card;
import model.card.standard.Standard;
import model.card.standard.Suit;

public class CardView {
	public static HashMap<Card,ImageView> mp=new HashMap<Card,ImageView>();
	public static boolean trap=false;
	public static String id(int rank,Suit suit) {
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
}
