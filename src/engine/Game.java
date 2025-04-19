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
		if(players.get(currentPlayerIndex).getHand().size()!=4-turn)
			return false;
		return true;
	}

	public void playPlayerTurn() throws GameException{
		players.get(currentPlayerIndex).play();
	}	
	public void endPlayerTurn(){
		firePit.add(players.get(currentPlayerIndex).getSelectedCard());
		players.get(currentPlayerIndex).getHand().remove(players.get(currentPlayerIndex).getSelectedCard());
		players.get(currentPlayerIndex).deselectAll();
		currentPlayerIndex = (currentPlayerIndex + 1) % 4;
		if (currentPlayerIndex == 0) turn++;
		if (turn == 4){
			turn=0;
			for (Player player:players){
				if (Deck.getPoolSize()<4){
					Deck.refillPool(firePit);
					firePit.clear();
				}
				player.setHand(Deck.drawCards());
			}
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
		for(Player p:players) {
			if(p.getColour()==marble.getColour())
				p.regainMarble(marble);
		}
	}
	@Override
	public void fieldMarble() throws CannotFieldException,
			IllegalDestroyException {
		Marble marble = players.get(currentPlayerIndex).getOneMarble();
		if(marble == null) throw new CannotFieldException();
		board.sendToBase(marble);
		players.get(currentPlayerIndex).removeMarble(marble);
	}
	@Override
	public void discardCard(Colour colour) throws CannotDiscardException {
		Player curPlayer=null;
		for(Player player:players) {
			if(player.getColour()==colour) {
				curPlayer=player;
			}
		}
		ArrayList<Card> hand = curPlayer.getHand();
		if (!hand.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(hand.size());
            hand.remove(randomIndex);
        } else {
            throw new CannotDiscardException();
        }
		curPlayer.setHand(hand);
		
	}
	@Override
	public void discardCard() throws CannotDiscardException {
		Random random = new Random();
        int randomIndex = random.nextInt(players.size());
        while(randomIndex==currentPlayerIndex) randomIndex=random.nextInt(players.size());
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
