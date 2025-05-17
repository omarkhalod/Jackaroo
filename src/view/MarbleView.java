package view;

import java.util.ArrayList;
import java.util.HashMap;

import engine.Game;
import exception.InvalidMarbleException;
import javafx.animation.ScaleTransition;
import javafx.scene.Scene;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.util.Duration;
import model.player.Marble;
import model.player.Player;

public class MarbleView {
	public static HashMap<Marble,ImageView> mp=new HashMap<>();
	public static ArrayList<ImageView> selectedMarbles=new ArrayList<>();
	public static void setMarble(Game game,Scene scene) {
		String[] marbles= {"p0m0", "p0m1", "p0m2", "p0m3","p1m0", "p1m1", "p1m2", "p1m3","p2m0", "p2m1", "p2m2", "p2m3","p3m0", "p3m1", "p3m2", "p3m3"};
		int i=0;
		
		for(Player player:game.getPlayers()) {
			String color=""+player.getColour();
			for(Marble marble:player.getMarbles()) {
				Image img=new Image(MarbleView.class.getResourceAsStream("/view/resources/marbles/"+color+".png"));
				ImageView marbleView = (ImageView) scene.getRoot().lookup("#"+marbles[i++]);
				marbleView.setImage(img);
				marbleView.setManaged(false);
				mp.put(marble,marbleView);
				ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), mp.get(marble));
			    scaleUp.setToX(1.2);
			    scaleUp.setToY(1.2);
			    Glow glow=new Glow(1.1);
				marbleView.setOnMouseClicked(e ->{
					boolean flag=false;
					if(e.getButton() == MouseButton.PRIMARY) {
					try {
						game.selectMarble(marble);
					} catch (InvalidMarbleException e1) {
						e1.printStackTrace();
						flag=true;
					}
					if(!flag) {
						selectedMarbles.add(marbleView);
						scaleUp.playFromStart();
						marbleView.setEffect(null);
						marbleView.setEffect(glow);
					}
					}
				});
			}
		}
	}
}
