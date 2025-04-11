package model.card.standard;
import java.util.ArrayList;

import engine.GameManager;
import engine.board.BoardManager;
import exception.*;
import model.card.Card;
import model.player.Marble;


public class Standard extends Card{
	private final int rank;
	private final Suit suit;
	
	public Standard(String name, String description, int rank, Suit suit, BoardManager boardManager,
			GameManager gameManager){
		super(name, description, boardManager, gameManager);
		this.rank = rank;
		this.suit = suit;
	}
	
	public int getRank(){ 
		return this.rank; 
	}
	
	public Suit getSuit(){ 
		return this.suit;
	}

	@Override
	public void act(ArrayList<Marble> marbles) throws ActionException,
			InvalidMarbleException {
		if (!this.validateMarbleColours(marbles) || !this.validateMarbleSize(marbles))
			throw new InvalidMarbleException("Wrong marble colour or size nigga");
		try{
			boardManager.moveBy(marbles.get(0), rank, false);
		}
		catch(Exception e){
			throw new IllegalMovementException("Illegal movement exception nigga");
			}
	}
	
	
}
