package engine.board;
import java.util.ArrayList;
import java.util.Random;
import model.Colour;
import engine.GameManager;

public class Board implements BoardManager{
	private final GameManager gameManager;
	private final ArrayList<Cell> track;
	private final ArrayList<SafeZone> safeZones;
	private int splitDistance;
	
	public Board(ArrayList<Colour> colourOrder, GameManager gameManager){
		this.gameManager = gameManager;
		this.track = new ArrayList<Cell>();
		this.safeZones = new ArrayList<SafeZone>();
		this.splitDistance = 3;
		
		for (int i = 0; i < 100; i++) this.track.add(new Cell(CellType.NORMAL));
		
		for (int i = 0; i < 100; i += 25){
			this.track.get(i).setCellType(CellType.BASE);
			this.track.get(i + 23).setCellType(CellType.ENTRY);
		}
		
		for(int i = 0 ; i < 8 ; i++) assignTrapCell();
		
		for (int i = 0; i < 4; i++) safeZones.add(new SafeZone(colourOrder.get(i)));
		
	}
	
	private void assignTrapCell(){
		Random RNG = new Random();
		int idx=RNG.nextInt(100);
		while ((this.track.get(idx).getCellType() != CellType.NORMAL) || this.track.get(idx).isTrap()){
			idx = RNG.nextInt(100);
		}
		this.track.get(idx).setTrap(true);
	}
	public int getSplitDistance() {
		return this.splitDistance;
	}
	public void setSplitDistance(int splitDistance){
		this.splitDistance = splitDistance;
	}
	public ArrayList<Cell> getTrack(){
		return track;
	}
	public ArrayList<SafeZone> getSafeZones(){
		return safeZones;
	}

}
