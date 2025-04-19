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
	public boolean validateMarbleSize(ArrayList<Marble> marbles) {
		return marbles.size() == 1|| marbles.size( )== 0;
	}
	public void act(ArrayList<Marble> marbles) throws ActionException,
	InvalidMarbleException {
		if (marbles.size()==1)
			boardManager.moveBy(marbles.get(0), 13, true);
		else
			gameManager.fieldMarble();
	}
}