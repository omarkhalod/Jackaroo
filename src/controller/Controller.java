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
	@FXML private Label name,cepu1,cepu2,cepu3;
	@FXML private ImageView omori,kel,aubrey,hero;
	@FXML private ImageView b0,b1,b2,b3;
	@FXML private ImageView boardImage,backgroundImage;
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
		name.setStyle(
			    "-fx-text-fill: white;" +
			    "-fx-font-weight: bold;" +
			    "-fx-effect: dropshadow(one-pass-box, black, 1, 0.0, 0, 0);"
			);
		cepu1.setStyle(
			    "-fx-text-fill: white;" +
			    "-fx-font-weight: bold;" +
			    "-fx-effect: dropshadow(one-pass-box, black, 1, 0.0, 0, 0);"
			);
		cepu2.setStyle(
			    "-fx-text-fill: white;" +
			    "-fx-font-weight: bold;" +
			    "-fx-effect: dropshadow(one-pass-box, black, 1, 0.0, 0, 0);"
			);
		cepu3.setStyle(
			    "-fx-text-fill: white;" +
			    "-fx-font-weight: bold;" +
			    "-fx-effect: dropshadow(one-pass-box, black, 1, 0.0, 0, 0);"
			);
	}

	 public void setName(String name) {
		 this.name.setText(name);
	 }
	 public void setIcons(ArrayList<Color> order) {
		 Image omo=new Image(MarbleView.class.getResourceAsStream("/view/resources/gameplay/omori.gif"));
		 Image aub=new Image(MarbleView.class.getResourceAsStream("/view/resources/gameplay/aubrey.gif"));
		 Image kel=new Image(MarbleView.class.getResourceAsStream("/view/resources/gameplay/kel.gif"));
		 Image her=new Image(MarbleView.class.getResourceAsStream("/view/resources/gameplay/hero.gif"));
		 Image blue=new Image(MarbleView.class.getResourceAsStream("/view/resources/gameplay/border-Photoroom.png"));
		 Image red=new Image(MarbleView.class.getResourceAsStream("/view/resources/gameplay/BORDER RED.png"));
		 Image green=new Image(MarbleView.class.getResourceAsStream("/view/resources/gameplay/border green.png"));
		 Image yellow=new Image(MarbleView.class.getResourceAsStream("/view/resources/gameplay/border yellow.png"));
		 Image back=new Image(MarbleView.class.getResourceAsStream("/view/resources/gameplay/background.png"));
		 Image boar=new Image(MarbleView.class.getResourceAsStream("/view/resources/gameplay/download.png"));
		 omori.setImage(omo);
		 this.kel.setImage(kel);
		 aubrey.setImage(aub);
		 hero.setImage(her);
		 backgroundImage.setImage(boar);
		 boardImage.setImage(back);
		 ImageView[] tst= {b0,b1,b2,b3};
		 for(int i=0;i<4;i++) {
			 if(order.get(i)==Color.RED) {
				 tst[i].setImage(red);
			 }else if(order.get(i)==Color.BLUE) {
				 tst[i].setImage(blue);
			 }else if(order.get(i)==Color.GREEN) {
				 tst[i].setImage(green);
			 }else
				 tst[i].setImage(yellow);
		 }
	 }
}
