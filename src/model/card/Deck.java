package model.card;
import java.util.*;
import java.util.regex.*;
import engine.GameManager;
import engine.board.BoardManager;
import model.card.standard.*;
import model.card.wild.*;
import java.io.*;
public class Deck {
	private static final String CARDS_FILE="Cards.csv";
	private static ArrayList<Card> cardsPool;
	public static void loadCardPool(BoardManager boardManager,GameManager gameManager) throws IOException {
		cardsPool=new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(CARDS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
            	List<String> values = new ArrayList<>();
                Matcher matcher = Pattern.compile("\"([^\"]*)\"|([^,]+)").matcher(line);
                while (matcher.find()) {
                    if (matcher.group(1) != null) {
                        values.add(matcher.group(1));
                    } else {
                        values.add(matcher.group(2).trim());
                    }
                }
                int code=Integer.parseInt(values.get(0));
                int frequency=Integer.parseInt(values.get(1));
                String name=values.get(2);
                String description=values.get(3);
                for(int i=0;i<frequency;i++) {
                	if(code==14) {
                		cardsPool.add(new Burner(name,description,boardManager,gameManager));
                	}else if(code==15) {
                		cardsPool.add(new Saver(name,description,boardManager,gameManager));
                	}else {
                		int rank=Integer.parseInt(values.get(4));
                		Suit suit;
                		if(values.get(5).equals("SPADE"))
                			suit=Suit.SPADE;
                		else if(values.get(5).equals("HEART"))
                			suit=Suit.HEART;
                		else if(values.get(5).equals("DIAMOND"))
                			suit=Suit.DIAMOND;
                		else
                			suit=Suit.CLUB;
                		if(code==1) {
                			cardsPool.add(new Ace(name,description,suit,boardManager,gameManager));
                		}else if(code==4) {
                			cardsPool.add(new Four(name,description,suit,boardManager,gameManager));
                		}else if(code==5) {
                			cardsPool.add(new Five(name,description,suit,boardManager,gameManager));
                		}else if(code==7) {
                			cardsPool.add(new Seven(name,description,suit,boardManager,gameManager));
                		}else if(code==10){
                			cardsPool.add(new Ten(name,description,suit,boardManager,gameManager));
                		}else if(code==11) {
                			cardsPool.add(new Jack(name,description,suit,boardManager,gameManager));
                		}else if(code==12) {
                			cardsPool.add(new Queen(name,description,suit,boardManager,gameManager));
                		}else if(code==13) {
                			cardsPool.add(new King(name,description,suit,boardManager,gameManager));
                		}else {
                			cardsPool.add(new Standard(name,description,rank,suit,boardManager,gameManager));
                		}
                	}
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	public static ArrayList<Card> drawCards(){
		ArrayList<Card> hand=new ArrayList<>();
		Collections.shuffle(cardsPool);
		for(int i=0;i<4&&!cardsPool.isEmpty();i++) {
			hand.add(cardsPool.get(0));
			cardsPool.remove(0);
		}
		return hand;
	}
}
