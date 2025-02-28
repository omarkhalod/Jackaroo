package engine;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import model.Colour;
import model.card.Card;
import model.player.CPU;
import model.player.Player;
import engine.board.Board;

public class Game implements GameManager{
	private final Board board;
	private final ArrayList<Player> players;
	private final ArrayList<Card> firePit;
	private int currentPlayerIndex;
	private int turn;
	
	public Game(String playerName) throws IOException{
		firePit = new ArrayList<Card>();
		ArrayList<Colour> colours = new ArrayList<Colour>();
		colours.add(Colour.BLUE);
		colours.add(Colour.RED);
		colours.add(Colour.YELLOW);
		colours.add(Colour.GREEN);
		Collections.shuffle(colours);
		this.board = new Board(colours, this);
		
		players = new ArrayList<Player>();
		players.add(new Player(playerName, colours.get(0)));
		players.add(new CPU("CPU 1", colours.get(1), board));
		players.add(new CPU("CPU 2", colours.get(2), board));
		players.add(new CPU("CPU 3", colours.get(3), board));
		
		// stopped at 4.21.2.5
		
	}
	
}
