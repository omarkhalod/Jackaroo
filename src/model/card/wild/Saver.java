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
		if (!this.validateMarbleColours(marbles) || !this.validateMarbleSize(marbles))
			throw new InvalidMarbleException("Wrong marble colour or size nigga");
		try{
			if (boardManager.getCellofMarble(marbles.get(0)).getCellType() != CellType.SAFE)
				boardManager.sendToSafe(marbles.get(0));
			else throw new InvalidMarbleException("Wrong marble type nigga");
		}
		catch(Exception e){
			throw new IllegalMovementException("Illegal movement exception nigga");
			}
	}
	

	
}
