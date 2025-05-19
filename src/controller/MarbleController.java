package controller;

import java.util.ArrayList;

import engine.Game;
import engine.board.Cell;
import exception.CannotFieldException;
import exception.GameException;
import exception.IllegalDestroyException;
import exception.InvalidCardException;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import model.Colour;
import model.card.Card;
import model.card.standard.King;
import model.player.CPU;
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
	public static void fieldMarble(Game game,Player player,JackrooLauncher launcher) {
		try {
			game.getBoard().sendToBase(player.getOneMarble());
		} catch (CannotFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalDestroyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Main.updateBoard();
	}
}
