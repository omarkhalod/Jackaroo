package view;

import java.util.HashMap;

import engine.Game;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.player.Marble;
import model.player.Player;

public class MarbleView {
	public static HashMap<Marble,ImageView> mp=new HashMap<>();
	public static void setMarble(Game game,Scene scene) {
		String[] marbles= {"p0m0", "p0m1", "p0m2", "p0m3","p1m0", "p1m1", "p1m2", "p1m3","p2m0", "p2m1", "p2m2", "p2m3","p3m0", "p3m1", "p3m2", "p3m3"};
		int i=0;
		for(Player player:game.getPlayers()) {
			String color=""+player.getColour();
			for(Marble marble:player.getMarbles()) {
				Image img=new Image(MarbleView.class.getResourceAsStream("/view/resources/marbles/"+color+".png"));
				ImageView marbleView = (ImageView) scene.getRoot().lookup("#"+marbles[i++]);
				marbleView.setImage(img);
				mp.put(marble,marbleView);
			}
		}
	}
}
