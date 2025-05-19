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
}
