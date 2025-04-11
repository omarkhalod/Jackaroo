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
		marbles.add(marble); // ya nigga, can marble be anything, anywhere?
	}
	
	public Marble getOneMarble(){
		if (marbles.size() > 0)
			return marbles.get(0);
		return null;
	}
	
	 public void selectCard(Card card) throws InvalidCardException{
		 if (hand.contains(card))
			 selectedCard = card;
		 
		 else throw new InvalidCardException("invalid card nigga");
	 }
	 
	 public void selectMarble(Marble marble) throws InvalidMarbleException{
		 if (selectedMarbles.size() == 2) throw new InvalidMarbleException("kteer awy greedy nigga");
		 selectedMarbles.add(marble);
	 }
	 
	 public void deselectAll(){
		 selectedCard = null;
		 selectedMarbles.clear();
	 }
	 
	 public void play() throws GameException{
		 try{
			 selectedCard.act(selectedMarbles);
		 }
		 
		 catch(Exception e){
			 throw e;
		 }
	 }
} 

