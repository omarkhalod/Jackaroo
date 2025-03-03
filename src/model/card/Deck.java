package model.card;
import java.util.*;
import engine.GameManager;
import engine.board.BoardManager;
import model.card.standard.*;
import model.card.wild.*;
import java.io.*;
public class Deck {
	private static final String CARDS_FILE="Cards.csv";
	private static ArrayList<Card> cardsPool;
	public static void loadCardPool(BoardManager boardManager,GameManager gameManager) throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(CARDS_FILE))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int code=Integer.parseInt(values[0]);
                int frequency=Integer.parseInt(values[1]);
                String name=values[2];
                String description=values[3];
                Card current;
                if(code==14) {
                	current=new Burner(name,description,boardManager,gameManager);
                }else if(code==15) {
                	current=new Saver(name,description,boardManager,gameManager);
                }else {
                	int rank=Integer.parseInt(values[4]);
                	Suit suit;
                	if(values[5].equals("SPADE"))
                		suit=Suit.SPADE;
                	else if(values[5].equals("HEART"))
                		suit=Suit.HEART;
                	else if(values[5].equals("DIAMOND"))
                		suit=Suit.DIAMOND;
                	else
                		suit=Suit.CLUB;
                	if(code==1) {
                		current=new Ace(name,description,suit,boardManager,gameManager);
                	}else if(code==4) {
                		current=new Four(name,description,suit,boardManager,gameManager);
                	}else if(code==5) {
                		current=new Five(name,description,suit,boardManager,gameManager);
                	}else if(code==7) {
                		current=new Seven(name,description,suit,boardManager,gameManager);
                	}else if(code==10){
                		current=new Ten(name,description,suit,boardManager,gameManager);
                	}else if(code==11) {
                		current=new Jack(name,description,suit,boardManager,gameManager);
                	}else if(code==12) {
                		current=new Queen(name,description,suit,boardManager,gameManager);
                	}else if(code==13) {
                		current=new King(name,description,suit,boardManager,gameManager);
                	}else {
                		current=new Standard(name,description,rank,suit,boardManager,gameManager);
                	}
                }
                for(int i=0;i<frequency;i++) {
                	cardsPool.add(current);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	public static ArrayList<Card> drawCards(){
		ArrayList<Card> hand=new ArrayList<>();
		Collections.shuffle(cardsPool);
		for(int i=0;i<4;i++) {
			hand.add(cardsPool.get(0));
			cardsPool.remove(0);
		}
		return hand;
	}
}
