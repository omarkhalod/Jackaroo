package controller;

import java.util.ArrayList;

import engine.Game;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import view.MarbleView;

public class MarbleController {
	static int cnt=0;
	public static void move(Game game) {
		ArrayList<ImageView> marbles=MarbleView.selectedMarbles;
		for(ImageView marble:marbles) {
			((StackPane)marble.getParent()).getChildren().remove(marble);
			BoardController.overlay.getChildren().add(marble);
			marble.setLayoutX(BoardController.trackPositions.get(cnt).getX());
			marble.setLayoutY(BoardController.trackPositions.get(cnt).getY());
			System.out.println(BoardController.trackPositions.get(cnt).getX());
			System.out.println(BoardController.trackPositions.get(cnt).getY());
			cnt++;
		}
	}
}
