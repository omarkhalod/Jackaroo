package model.card.standard;
import java.util.ArrayList;

import model.player.Marble;
import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.IllegalMovementException;
import exception.InvalidMarbleException;

public class Four extends Standard{
	public Four(String name, String description, Suit suit, BoardManager boardManager, GameManager
			gameManager){
		super(name, description, 4, suit, boardManager, gameManager);
	}
	
	public void act(ArrayList<Marble> marbles) throws ActionException,
	InvalidMarbleException {
		if (!this.validateMarbleColours(marbles))
			throw new InvalidMarbleException("Wrong marble colour");
		if(!this.validateMarbleSize(marbles))
			throw new InvalidMarbleException("Wrong marble size");
		boardManager.moveBy(marbles.get(0), -4, false);
	}
}
