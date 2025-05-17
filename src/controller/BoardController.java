package controller;

import java.util.ArrayList;
import java.util.HashMap;

import engine.Game;
import engine.board.Cell;
import engine.board.SafeZone;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class BoardController {
	public static Pane overlay;
	public static ArrayList<Point2D> trackPositions = new ArrayList<>();
	public static ArrayList<ArrayList<Point2D>> safeZonePositions=new ArrayList<>();
	public static ArrayList<ArrayList<Point2D>> homeZonePositions=new ArrayList<>();
	public static HashMap<Cell,Point2D> positions = new HashMap<>();
	public static ImageView test=new ImageView("/view/resources/marbles/BLUE.png");
	public static void init() {
		trackPositions.add(new Point2D(377,263));
		double x=377;
		double y=263;
		for(int i=0;i<5;i++) {
			y-=20;
			trackPositions.add(new Point2D(x,y));
		}
		for(int i=0;i<6;i++) {
			x-=21;
			y-=(1.0/6.0);
			trackPositions.add(new Point2D(x,y));
		}
		for(int i=0;i<4;i++) {
			y-=20.75;
			trackPositions.add(new Point2D(x,y));
		}
		x=249;
		y=58.25;
		trackPositions.add(new Point2D(x,y));
		for(int i=0;i<5;i++) {
			x-=20;
			trackPositions.add(new Point2D(x,y));
		}
		for(int i=0;i<4;i++) {
			y-=25;
			trackPositions.add(new Point2D(x,y));
		}
		for(int i=0;i<5;i++) {
			x+=20;
			trackPositions.add(new Point2D(x,y));
		}
		for(int i=0;i<5;i++) {
			x+=0.2;
			y-=20.75;
			trackPositions.add(new Point2D(x,y));
		}
		for(int i=0;i<6;i++) {
			x+=(127.0/6.0);
			y-=(0.75/6.0);
			trackPositions.add(new Point2D(x,y));
		}
		for(int i=0;i<5;i++) {
			y-=20.1;
			trackPositions.add(new Point2D(x,y));
		}
		for(int i=0;i<4;i++) {
			x+=25;
			trackPositions.add(new Point2D(x,y));
		}
		for(int i=0;i<5;i++) {
			y+=20.1;
			trackPositions.add(new Point2D(x,y));
		}
		for(int i=0;i<6;i++) {
			x+=21.25;
			y+=0.125;
			trackPositions.add(new Point2D(x,y));
		}
		for(int i=0;i<5;i++) {
			x+=0.1;
			y+=20.8;
			trackPositions.add(new Point2D(x,y));
		}
		for(int i=0;i<5;i++) {
			x+=20;
			trackPositions.add(new Point2D(x,y));
		}
		for(int i=0;i<4;i++) {
			y+=25;
			trackPositions.add(new Point2D(x,y));
		}
		for(int i=0;i<5;i++) {
			x-=20;
			trackPositions.add(new Point2D(x,y));
		}
		for(int i=0;i<5;i++) {
			y+=21;
			trackPositions.add(new Point2D(x,y));
		}
		for(int i=0;i<6;i++) {
			y+=(1.0/6.0);
			x-=(128.0/6.0);
			trackPositions.add(new Point2D(x,y));
		}
		for(int i=0;i<5;i++) {
			y+=20;
			trackPositions.add(new Point2D(x,y));
		}
		for(int i=0;i<3;i++) {
			x-=25;
			trackPositions.add(new Point2D(x,y));
		}
		x=427;
		y=238;
		safeZonePositions.add(new ArrayList<>());
		safeZonePositions.get(0).add(new Point2D(x,y));
		for(int i=0;i<3;i++) {
			y-=25;
			safeZonePositions.get(0).add(new Point2D(x,y));
		}
		safeZonePositions.add(new ArrayList<>());
		x=680;
		y=8.75;
		safeZonePositions.get(1).add(new Point2D(x,y));
		for(int i=0;i<3;i++) {
			x-=25;
			safeZonePositions.get(1).add(new Point2D(x,y));
		}
		safeZonePositions.add(new ArrayList<>());
		x=427;
		y=-222;
		safeZonePositions.get(2).add(new Point2D(x,y));
		for(int i=0;i<3;i++) {
			y+=25;
			safeZonePositions.get(2).add(new Point2D(x,y));
		}
		safeZonePositions.add(new ArrayList<>());
		x=174;
		y=8.75;
		safeZonePositions.get(3).add(new Point2D(x,y));
		for(int i=0;i<3;i++) {
			x+=25;
			safeZonePositions.get(3).add(new Point2D(x,y));
		}
		homeZonePositions.add(new ArrayList<>());
		x=535;
		y=219.5;
		homeZonePositions.get(0).add(new Point2D(x,y));
		homeZonePositions.get(0).add(new Point2D(x+18,y-18));
		homeZonePositions.get(0).add(new Point2D(x+36,y));
		homeZonePositions.get(0).add(new Point2D(x+18,y+18));
		homeZonePositions.add(new ArrayList<>());
		x=648.5;
		y=-106;
		homeZonePositions.get(1).add(new Point2D(x,y));
		homeZonePositions.get(1).add(new Point2D(x+18,y-18));
		homeZonePositions.get(1).add(new Point2D(x+36,y));
		homeZonePositions.get(1).add(new Point2D(x+18,y+18));
		homeZonePositions.add(new ArrayList<>());
		x=284;
		y=-203;
		homeZonePositions.get(2).add(new Point2D(x,y));
		homeZonePositions.get(2).add(new Point2D(x+18,y-18));
		homeZonePositions.get(2).add(new Point2D(x+36,y));
		homeZonePositions.get(2).add(new Point2D(x+18,y+18));
		homeZonePositions.add(new ArrayList<>());
		x=171;
		y=120;
		homeZonePositions.get(3).add(new Point2D(x,y));
		homeZonePositions.get(3).add(new Point2D(x+18,y-18));
		homeZonePositions.get(3).add(new Point2D(x+36,y));
		homeZonePositions.get(3).add(new Point2D(x+18,y+18));
		Point2D offset=new Point2D(503,503);
		for(Point2D e:trackPositions) {
			e.add(offset);
		}
		for(ArrayList<Point2D> e:safeZonePositions) {
			for(Point2D p:e) {
				p.add(offset);
			}
		}
		for(ArrayList<Point2D> e:homeZonePositions) {
			for(Point2D p:e) {
				p.add(offset);
			}
		}
	}
	public static void setUp(StackPane root,Game game) {
		overlay=new Pane();
		overlay.setStyle("-fx-background-color: transparent;");
		overlay.setPickOnBounds(false);
		overlay.setMouseTransparent(true);
		root.getChildren().add(overlay);
		ArrayList<Cell> track= game.getBoard().getTrack();
		init();
		test.setScaleX(0.025);
		test.setScaleY(0.025);
		test.setX(377);
		test.setY(263);
		overlay.getChildren().add(test);
		for(int i=0;i<100;i++) {
			positions.put(track.get(i),trackPositions.get(i));
		}
		ArrayList<SafeZone> safeZones=game.getBoard().getSafeZones();
		for(int i=0;i<4;i++) {
			for(int j=0;j<4;j++) {
				positions.put(safeZones.get(i).getCells().get(j),safeZonePositions.get(i).get(j));
			}
		}
		
	}
}
