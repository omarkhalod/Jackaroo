package model.card.standard;
import exception.*;
import java.util.ArrayList;

import model.player.Marble;
import engine.GameManager;
import engine.board.BoardManager;

public class Ten extends Standard{
	public Ten(String name, String description, Suit suit, BoardManager boardManager, GameManager
			gameManager){
		super(name, description, 10, suit, boardManager, gameManager);
	}
	
	public boolean validateMarbleSize(ArrayList<Marble> marbles) {
		return marbles.size() == 1 || marbles.size() ==0;
	}
	
	public boolean validateMarbleColours(ArrayList<Marble> marbles){
		if (marbles.size() == 0) return true;
		if (marbles.size() == 1) return marbles.get(0).getColour() == gameManager.getActivePlayerColour();
		return false;
	}
	public void act(ArrayList<Marble> marbles) throws ActionException,
	InvalidMarbleException {
		if (!this.validateMarbleColours(marbles) || !this.validateMarbleSize(marbles))
			throw new InvalidMarbleException("Wrong marble colour or size nigga");
		try{
			if (marbles.size() == 0) gameManager.discardCard(gameManager.getNextPlayerColour());
			else
				boardManager.moveBy(marbles.get(0), 10, false);
		}
		catch(Exception e){
			throw new IllegalMovementException("Illegal movement exception nigga");
			}
	}
}
