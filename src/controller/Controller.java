package controller;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;

public class Controller implements Initializable{
	private ArrayList<ArrayList<Sphere>> playerMarbles = new ArrayList<>();
	
	
	@FXML private Sphere p0m0, p0m1, p0m2, p0m3;
	@FXML private Sphere p1m0, p1m1, p1m2, p1m3;
	@FXML private Sphere p2m0, p2m1, p2m2, p2m3;
	@FXML private Sphere p3m0, p3m1, p3m2, p3m3;
	
	@FXML private Circle humanCircle, cpu1Circle, cpu2Circle, cpu3Circle;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		ArrayList<Sphere> human = new ArrayList<>(Arrays.asList(p0m0, p0m1, p0m2, p0m3));
		ArrayList<Sphere> cpu1 = new ArrayList<>(Arrays.asList(p1m0, p1m1, p1m2, p1m3));
		ArrayList<Sphere> cpu2 = new ArrayList<>(Arrays.asList(p2m0, p2m1, p2m2, p2m3));
		ArrayList<Sphere> cpu3 = new ArrayList<>(Arrays.asList(p3m0, p3m1, p3m2, p3m3));
		
		playerMarbles.add(human);
		playerMarbles.add(cpu1);
		playerMarbles.add(cpu2);
		playerMarbles.add(cpu3);
		
		Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW};
		
		for (int i = 0; i < playerMarbles.size(); i++) {
	        for (Sphere marble : playerMarbles.get(i)) {
	            PhongMaterial material = new PhongMaterial();
	            material.setDiffuseColor(colors[i]);
	            marble.setMaterial(material);;
	        }
	    }
		
		humanCircle.setFill(Color.RED);
		cpu1Circle.setFill(Color.GREEN);
		cpu2Circle.setFill(Color.BLUE);
		cpu3Circle.setFill(Color.YELLOW);
	}
	
	
	
}
