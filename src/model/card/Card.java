package model.card;
import java.util.ArrayList;

import model.player.Marble;
import engine.GameManager;
import engine.board.BoardManager;
import engine.board.Cell;
import exception.*;

abstract public class Card {
	private final String name;
	private final String description;
	protected BoardManager boardManager;
	protected GameManager gameManager;
	
	public Card(String name, String description, BoardManager boardManager, GameManager gameManager){
		this.name = name;
		this.description = description;
		this.boardManager = boardManager;
		this.gameManager = gameManager;
	}
	
	public String getName(){
		return name;
	}
	
	public String getDescription(){
		return description;
	}
	
	public boolean validateMarbleSize(ArrayList<Marble> marbles) {
		return marbles.size() == 1;
	}
	
	public boolean validateMarbleColours(ArrayList<Marble> marbles){
		for (Marble marble: marbles){
			if (marble.getColour() != gameManager.getActivePlayerColour()) return false;
		}
		return true;
	}
	
	abstract public void act(ArrayList<Marble> marbles) throws ActionException,
	InvalidMarbleException;
	

	
}
