package model.card.standard;
import java.util.ArrayList;

import model.player.Marble;
import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.IllegalMovementException;
import exception.InvalidMarbleException;

public class Five extends Standard{
	public Five(String name, String description, Suit suit, BoardManager boardManager, GameManager
			gameManager){
		super(name, description, 5, suit, boardManager, gameManager);
	}
	
	public boolean validateMarbleColours(ArrayList<Marble> marbles){
		return true;
	}
	
	public void act(ArrayList<Marble> marbles) throws ActionException,
	InvalidMarbleException {
		if (!this.validateMarbleColours(marbles) || !this.validateMarbleSize(marbles))
			throw new InvalidMarbleException("Wrong marble colour or size nigga");
		try{
			boardManager.moveBy(marbles.get(0), 5, false);
		}
		catch(Exception e){
			throw new IllegalMovementException("Illegal movement exception nigga");
			}
	}
}
