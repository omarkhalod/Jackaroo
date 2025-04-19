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
	public boolean validateMarbleSize(ArrayList<Marble> marbles) {
		return marbles.size() == 1|| marbles.size() == 0;
	}
	public void act(ArrayList<Marble> marbles) throws ActionException,
	InvalidMarbleException {
		if (marbles.size()==0)
			gameManager.fieldMarble();
		else
			boardManager.moveBy(marbles.get(0), 1, false);
	}
}
