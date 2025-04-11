package model.card.standard;

import java.util.ArrayList;

import model.player.Marble;
import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.IllegalMovementException;
import exception.InvalidMarbleException;

public class King extends Standard{
	public King(String name, String description, Suit suit, BoardManager boardManager, GameManager
			gameManager){
		super(name, description, 13, suit, boardManager, gameManager);
	}
	
	public void act(ArrayList<Marble> marbles) throws ActionException,
	InvalidMarbleException {
		if (!this.validateMarbleColours(marbles) || !this.validateMarbleSize(marbles))
			throw new InvalidMarbleException("Wrong marble colour or size nigga");
		try{
			if (boardManager.getCellofMarble(marbles.get(0)) != null){
				boardManager.moveBy(marbles.get(0), 13, true);
			}
			else{
				gameManager.fieldMarble();
			}
		}
		catch(Exception e){
			throw new IllegalMovementException("Illegal movement exception nigga");
			}
	}
}