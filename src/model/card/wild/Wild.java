package model.card.wild;
import java.util.ArrayList;

import engine.GameManager;
import engine.board.BoardManager;
import model.card.Card;
import model.player.Marble;

abstract public class Wild extends Card{
	public Wild(String name, String description, BoardManager boardManager, 
			GameManager gameManager){
		super(name, description, boardManager, gameManager);
		
	}
}
