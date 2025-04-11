package model.card.standard;

import java.util.ArrayList;

import exception.*;
import model.player.Marble;
import engine.GameManager;
import engine.board.BoardManager;

public class Ace extends Standard{
	
	public Ace(String name, String description, Suit suit, BoardManager boardManager, GameManager
			gameManager){
		super(name, description, 1, suit, boardManager, gameManager);
	}
	
	public void act(ArrayList<Marble> marbles) throws ActionException,
	InvalidMarbleException {
		if (!this.validateMarbleColours(marbles) || !this.validateMarbleSize(marbles))
			throw new InvalidMarbleException("Wrong marble colour or size nigga");
		try{
			if (boardManager.getCellofMarble(marbles.get(0)) == null)
				gameManager.fieldMarble();
			else boardManager.moveBy(marbles.get(0), 1, false);
		}
		catch(Exception e){
			throw new IllegalMovementException("Illegal movement exception nigga");
			}
}
}
