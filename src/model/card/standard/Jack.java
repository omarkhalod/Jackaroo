package model.card.standard;

import java.util.ArrayList;

import model.player.Marble;
import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.IllegalMovementException;
import exception.InvalidMarbleException;

public class Jack extends Standard{
	public Jack(String name, String description, Suit suit, BoardManager boardManager, GameManager
			gameManager){
		super(name, description, 11, suit, boardManager, gameManager);
	}
	
	public boolean validateMarbleSize(ArrayList<Marble> marbles) {
		return marbles.size() == 1 || marbles.size() ==2;
	}
	
	public boolean validateMarbleColours(ArrayList<Marble> marbles){
		if (marbles.size() == 1 && marbles.get(0).getColour() == gameManager.getActivePlayerColour()) return true;
		if (marbles.size() == 2 &&
			(marbles.get(0).getColour() == gameManager.getActivePlayerColour() ^
			marbles.get(1).getColour() == gameManager.getActivePlayerColour()))
			return true;
		
		return false;
	}
	
	public void act(ArrayList<Marble> marbles) throws ActionException,
	InvalidMarbleException {
		if (!this.validateMarbleColours(marbles))
			throw new InvalidMarbleException("Wrong marble colour");
		if(!this.validateMarbleSize(marbles))
			throw new InvalidMarbleException("Wrong marble size");
		if (marbles.size() == 1)
			boardManager.moveBy(marbles.get(0), 11, false);
		else
			boardManager.swap(marbles.get(0), marbles.get(1));
	}
}
