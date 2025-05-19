package controller;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import engine.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;
import model.player.Marble;
import model.player.Player;
import view.MarbleView;

public class Controller implements Initializable{
	public ArrayList<ArrayList<ImageView>> playerMarbles = new ArrayList<>();	
	@FXML private ImageView p0m0, p0m1, p0m2, p0m3;
	@FXML private ImageView p1m0, p1m1, p1m2, p1m3;
	@FXML private ImageView p2m0, p2m1, p2m2, p2m3;
	@FXML private ImageView p3m0, p3m1, p3m2, p3m3;
	@FXML private Circle humanCircle, cpu1Circle, cpu2Circle, cpu3Circle;
	@FXML private Label name;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		ArrayList<ImageView> human = new ArrayList<>(Arrays.asList(p0m0, p0m1, p0m2, p0m3));
		ArrayList<ImageView> cpu1 = new ArrayList<>(Arrays.asList(p1m0, p1m1, p1m2, p1m3));
		ArrayList<ImageView> cpu2 = new ArrayList<>(Arrays.asList(p2m0, p2m1, p2m2, p2m3));
		ArrayList<ImageView> cpu3 = new ArrayList<>(Arrays.asList(p3m0, p3m1, p3m2, p3m3));
		
		playerMarbles.add(human);
		playerMarbles.add(cpu1);
		playerMarbles.add(cpu2);
		playerMarbles.add(cpu3);
		DropShadow shadow=new DropShadow();
		for (int i = 0; i < playerMarbles.size(); i++) {
	        for (ImageView marble : playerMarbles.get(i)) {
	            marble.setPickOnBounds(false);
	            marble.setEffect(shadow);
	            marble.setOnMouseEntered(e -> marble.setEffect(new Glow(0.8)));
	            marble.setOnMouseExited(e -> {
	            	if(!MarbleView.selectedMarbles.contains(marble))
	            		marble.setEffect(shadow);
	            });
	        }
	    }
		
	}
	 public void highlightCurrentPlayer(int playerIndex) { 
		humanCircle.setStroke(Color.BLACK);
		cpu1Circle.setStroke(Color.BLACK);
		cpu2Circle.setStroke(Color.BLACK);
		cpu3Circle.setStroke(Color.BLACK);
       
		// Highlight current player
		switch(playerIndex) {
			case 0: humanCircle.setStroke(Color.GOLD); break;
			case 1: cpu3Circle.setStroke(Color.GOLD); break;
			case 2: cpu2Circle.setStroke(Color.GOLD); break;
			case 3: cpu1Circle.setStroke(Color.GOLD); break;
		}
	 }
	 public void setName(String name) {
		 this.name.setText(name);
	 }
	 public void initColors(ArrayList<Color> order) {
		 humanCircle.setFill(order.get(0));
			humanCircle.setStrokeWidth(5);
			humanCircle.setStroke(Color.GOLD);
			cpu3Circle.setFill(order.get(1));
			cpu3Circle.setStrokeWidth(5);
			cpu2Circle.setFill(order.get(2));
			cpu2Circle.setStrokeWidth(5);
			cpu1Circle.setFill(order.get(3));
			cpu1Circle.setStrokeWidth(5);
	 }
}
