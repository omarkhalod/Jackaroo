package model.card.wild;
import java.util.ArrayList;

import exception.*;
import model.player.Marble;
import engine.GameManager;
import engine.board.BoardManager;
import engine.board.CellType;
import exception.ActionException;
import exception.IllegalMovementException;
import exception.InvalidMarbleException;

public class Burner extends Wild{
	public Burner(String name, String description, BoardManager boardManager, 
			GameManager gameManager){
		super(name, description, boardManager, gameManager);
	}

	public boolean validateMarbleColours(ArrayList<Marble> marbles){
		if (marbles.size() == 1) return marbles.get(0).getColour() != gameManager.getActivePlayerColour();
		return false;
	}

	@Override
	public void act(ArrayList<Marble> marbles) throws ActionException,
	InvalidMarbleException {
		if (!this.validateMarbleColours(marbles))
			throw new InvalidMarbleException("Wrong marble colour");
		if(!this.validateMarbleSize(marbles))
			throw new InvalidMarbleException("Wrong marble size");
		boardManager.destroyMarble(marbles.get(0));
	}
}
