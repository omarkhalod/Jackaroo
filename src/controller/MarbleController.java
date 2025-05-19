package controller;

import java.util.ArrayList;

import engine.Game;
import engine.board.Cell;
import exception.CannotFieldException;
import exception.IllegalDestroyException;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import model.Colour;
import model.player.Marble;
import model.player.Player;
import view.MarbleView;

public class MarbleController {
	static int cnt=0;
	public static ArrayList<Marble> destroyed=new ArrayList<>();
	public static int id(Colour color,Game game) {
		int i=0;
		for(Player player:game.getPlayers()) {
			if(color==player.getColour()) {
				return i;
			}
			i++;
		}
		return -1;
	}
	public static void moveMarble(Game game,ImageView marbleView,ArrayList<Cell> fullPath) {
		double x=BoardController.positions.get(fullPath.get(0)).getX();
		double y=BoardController.positions.get(fullPath.get(0)).getY();
		int i=0;
		SequentialTransition seq = new SequentialTransition();
		boolean trap=false;
		for(Cell cell:fullPath) {
			if(i++==0) continue;
			double x0=BoardController.positions.get(cell).getX();
			double y0=BoardController.positions.get(cell).getY();
			TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5),marbleView);
			tt.setByX(x0 - x);
			tt.setByY(y0 - y);
			x=x0;
			y=y0;
			seq.getChildren().add(tt);
			if(cell.isTrap())
				trap=true;
			else
				trap=false;
		}
		if(trap) {
			destroyMarble(game,fullPath.get(0).getMarble(),x,y);
		}
		seq.play();
	}
	public static void fieldMarble(Game game) throws CannotFieldException, IllegalDestroyException {
		Colour colour=game.getActivePlayerColour();
		Cell base=game.getBoard().getTrack().get(game.getBoard().publicBasePosition(colour));
		Marble marble=base.getMarble();
		ImageView marbleView=MarbleView.mp.get(marble);
		if(marble==null) {
			System.out.println("NULL");
			System.out.println(game.getCurrentPlayer().getMarbles().size());
			System.out.println(game.getCurrentPlayer().getSelectedCard());
			System.out.println(game.getCurrentPlayer().getName());
		}
		Point2D old=new Point2D(marbleView.getLayoutX(),marbleView.getLayoutY());
		BoardController.emptyHomeCell.get(id(colour,game)).add(old);
		if(marbleView.getParent() instanceof StackPane) {
			((StackPane)marbleView.getParent()).getChildren().remove(marbleView);
			BoardController.overlay.getChildren().add(marbleView);
		}
		marbleView.setLayoutX(BoardController.positions.get(base).getX());
		marbleView.setLayoutY(BoardController.positions.get(base).getY());
	}
	public static void destroyMarble(Game game,Marble marble,double x0,double y0) {
		Colour colour=marble.getColour();
		Point2D target=BoardController.emptyHomeCell.get(id(colour,game)).remove(0);
		double x=target.getX();
		double y=target.getY();
		ImageView marb=MarbleView.mp.get(marble);
		marb.setLayoutX(x);
		marb.setLayoutY(y);
	}
}
