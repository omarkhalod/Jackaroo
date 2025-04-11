package engine;
import java.io.*;
import java.util.*;

import model.Colour;
import model.card.Card;
import model.card.Deck;
import model.player.CPU;
import model.player.Marble;
import model.player.Player;
import engine.board.Board;
import engine.board.Cell;
import engine.board.SafeZone;
import exception.*;
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
		Deck.loadCardPool(board, this);
		players = new ArrayList<Player>();
		players.add(new Player(playerName, colours.get(0)));
		players.add(new CPU("CPU 1", colours.get(1), board));
		players.add(new CPU("CPU 2", colours.get(2), board));
		players.add(new CPU("CPU 3", colours.get(3), board));
		for(Player player:players) {
			ArrayList<Card> hand=Deck.drawCards();
			player.setHand(hand);
		}
		currentPlayerIndex=0;
		turn=0;
	}
	public Board getBoard() {
		return board;
	}
	public ArrayList<Player> getPlayers(){
		return players;
	}
	public ArrayList<Card> getFirePit(){
		return firePit;
	}
//	-----------------------------------------------
	public void selectCard(Card card) throws InvalidCardException{
		players.get(currentPlayerIndex).selectCard(card);
	}
	
	public void selectMarble(Marble marble) throws InvalidMarbleException{
		players.get(currentPlayerIndex).selectMarble(marble);
	}
	
	public void deselectAll(){
		players.get(currentPlayerIndex).deselectAll();
	}
	
	public void editSplitDistance(int splitDistance) throws SplitOutOfRangeException{
		if (splitDistance<1 || splitDistance > 6) throw new SplitOutOfRangeException();
		board.setSplitDistance(splitDistance);
	}
	
	public boolean canPlayTurn(){
		return players.get(turn).getHand().size() != 0;
		// ^ should use turn walla current player index and what exactly to compare nigga
		// what is the difference between turn and currentplayerindex nigga
	}

	public void playPlayerTurn() throws GameException{
		if (!canPlayTurn()) throw new IllegalMovementException("Can't do nigga");
		// ^ what kind of exception to throw nigga
		
		try {
			players.get(turn).play();
		}
		catch (Exception e){
			throw e;
		}	
	}	
	
	public void endPlayerTurn(){
		firePit.add(players.get(currentPlayerIndex).getSelectedCard());
		players.get(currentPlayerIndex).getHand().remove(players.get(currentPlayerIndex).getSelectedCard());
		
		currentPlayerIndex = (currentPlayerIndex + 1) % 4;
		
		if (currentPlayerIndex == 0) turn = (turn + 1) % 4;
		
		if (turn == 0){
			for (Player player:players){
				player.getHand().addAll(Deck.drawCards());
			}
		}
		if (Deck.getPoolSize() < 4){
			Deck.refillPool(firePit);
			firePit.clear();
			
		}		
	}
	
	public Colour checkWin(){
			for (SafeZone safezone:board.getSafeZones()){
				boolean flag = true;
				for (int i = 0; i < 4; i++){
					if (safezone.getCells().get(i).getMarble() == null) flag = false;
				}
				if (flag) return safezone.getColour();
		}
			return null;
	}
	@Override
	public void sendHome(Marble marble) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void fieldMarble() throws CannotFieldException,
			IllegalDestroyException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void discardCard(Colour colour) throws CannotDiscardException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void discardCard() throws CannotDiscardException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Colour getActivePlayerColour() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Colour getNextPlayerColour() {
		// TODO Auto-generated method stub
		return null;
	}
}
