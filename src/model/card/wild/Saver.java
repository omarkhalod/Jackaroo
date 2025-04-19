package model.card.wild;

import java.util.ArrayList;

import model.player.Marble;
import exception.*;
import engine.GameManager;
import engine.board.BoardManager;
import engine.board.CellType;

public class Saver extends Wild{
	public Saver(String name, String description, BoardManager boardManager, 
			GameManager gameManager){
		super(name, description, boardManager, gameManager);
	}
	
	@Override
	public void act(ArrayList<Marble> marbles) throws ActionException,
			InvalidMarbleException {
		boardManager.sendToSafe(marbles.get(0));
	}
}
