package model.player;

import exception.*;
import java.util.ArrayList;
import model.Colour;
import model.card.Card;


public class Player {
	private final String name;
	private final Colour colour;
	private ArrayList<Card> hand;
	private final ArrayList<Marble> marbles;
	private Card selectedCard;
	private final ArrayList<Marble> selectedMarbles;
	
	public Player(String name, Colour colour){
		this.name = name;
		this.colour = colour;
		this.hand = new ArrayList<Card>();
		this.marbles = new ArrayList<Marble>();
		this.selectedMarbles = new ArrayList<Marble>();
		
		for (int i = 0; i < 4; i++) marbles.add(new Marble(colour));
		
		this.selectedCard = null;
		
	}

	public String getName() {
		return name;
	}

	public Colour getColour() {
		return colour;
	}

	public ArrayList<Card> getHand() {
		return hand;
	}
	
	public void setHand(ArrayList<Card> hand){
		this.hand=hand;
	}

	public ArrayList<Marble> getMarbles() {
		return marbles;
	}

	public Card getSelectedCard() {
		return selectedCard;
	}
	
	public void regainMarble(Marble marble){
		marbles.add(marble);
	}
	
	public Marble getOneMarble(){
		if (marbles.size() > 0)
			return marbles.get(0);
		return null;
	}
	public void removeMarble(Marble marble) {
		for(int i=0;i<marbles.size();i++) {
			if(marbles.get(i)==marble) {
				marbles.remove(i);
				break;
			}
		}
	}
	 public void selectCard(Card card) throws InvalidCardException{
		 if (hand.contains(card))
			 selectedCard = card;
		 else
			 throw new InvalidCardException();
	 }
	 
	 public void selectMarble(Marble marble) throws InvalidMarbleException{
		 for(Marble marb:selectedMarbles) {
			 if(marb==marble) return;
		 }
		 if (selectedMarbles.size() == 2) throw new InvalidMarbleException();
		 selectedMarbles.add(marble);
	 }
	 
	 public void deselectAll(){
		 selectedCard = null;
		 selectedMarbles.clear();
	 }
	 
	 public void play() throws GameException{
		 if(selectedCard==null) throw new InvalidCardException();
		 if(!selectedCard.validateMarbleColours(selectedMarbles))
			 throw new InvalidMarbleException("Wrong marble colour");
		 if(!selectedCard.validateMarbleSize(selectedMarbles))
			 throw new InvalidMarbleException("Wrong marble size");
		 selectedCard.act(selectedMarbles);
	 }
} 

