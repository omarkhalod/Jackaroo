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
		players.get(currentPlayerIndex).regainMarble(marble);
		
		try {
			board.destroyMarble(marble);
		} catch (IllegalDestroyException e) {
			e.printStackTrace();
		}
		
	}
	@Override
	public void fieldMarble() throws CannotFieldException,
			IllegalDestroyException {
		Marble marble = players.get(currentPlayerIndex).getOneMarble();
		if(marble == null) throw new CannotFieldException("Can't do nigga YOU GOT NO MARBLESS ON THE FIELD");
		//fielding the marble assuming its in the home zone into the base cell
		board.sendToBase(marble);
		//Cant change the player marble array so i wrote this weh 5alas
		//90% sure this will cause an error as destroymarble dose not check home zone
		board.destroyMarble(marble);
		
		
	}
	@Override
	public void discardCard(Colour colour) throws CannotDiscardException {
		//get index of the player with colour colour
		int idx = -1;
		for(int i = 0; i < players.size(); i++) {
			if(players.get(i).getColour().equals(colour)) {
				idx = i;
				break;
			}
		}
		Player curPlayer = players.get(idx);
		ArrayList<Card> hand = curPlayer.getHand();
		
		//remove random something
		if (!hand.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(hand.size());
            hand.remove(randomIndex);
        } else {
            throw new CannotDiscardException("You got no cards nigga");
        }
		//Update the hand in the players class
		curPlayer.setHand(hand);
		
	}
	@Override
	public void discardCard() throws CannotDiscardException {
		//select random player
		Random random = new Random();
        int randomIndex = random.nextInt(players.size());
        //Might have to check if the random players hand is not empty
        discardCard(players.get(randomIndex).getColour());
	}
	@Override
	public Colour getActivePlayerColour() {
		return players.get(currentPlayerIndex).getColour();
		
	}
	@Override
	public Colour getNextPlayerColour() {
		int next_idx = (currentPlayerIndex + 1) % players.size();
		return players.get(next_idx).getColour();
	}
}
