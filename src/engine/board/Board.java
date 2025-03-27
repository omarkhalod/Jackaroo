package engine.board;
import java.util.ArrayList;
import engine.*;
import java.util.Random;
import model.Colour;
import model.player.Marble;
import exception.*;
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
	private ArrayList<Cell> getSafeZone(Colour colour){
		for(SafeZone safeZone:safeZones) {
			if(safeZone.getColour()==colour) {
				return safeZone.getCells();
			}
		}  
		return null;
	}
	private int getPositionInPath(ArrayList<Cell> path, Marble marble) {
		int ind=0;
		for(Cell cell:path) {
			if(cell.getMarble()==marble) return ind;
			ind++;
		}
		return -1;
	}
	private int getBasePosition(Colour colour) {
		if (colour==Colour.RED) return 0;
		if (colour==Colour.GREEN) return 25;
		if (colour==Colour.BLUE) return 50;
		if (colour==Colour.YELLOW) return 75;
		return -1;
	}
	private int getEntryPosition(Colour colour) {
		if (colour==Colour.RED) return 98;
		if (colour==Colour.GREEN) return 23;
		if (colour==Colour.BLUE) return 48;
		if (colour==Colour.YELLOW) return 73;
		return -1;
	}
	private ArrayList<Cell> validateSteps(Marble marble, int steps) throws IllegalMovementException{
		Colour marbleColour=marble.getColour();
		Colour playerColour=gameManager.getActivePlayerColour();
		int trackIdx=getPositionInPath(track, marble);
		if(trackIdx!=-1) {
			int entry=getEntryPosition(marbleColour);
			int end=entry+4;
			if(steps==4) {
				
			}
		}else {
			int safeIdx=getPositionInPath(getSafeZone(marbleColour),marble);
			if(safeIdx==-1) throw new IllegalMovementException("Marble cannot be moved.");
			
		}
	}
	private void validatePath(Marble marble,ArrayList<Cell> fullPath,boolean destroy) throws IllegalMovementException {
		int cnt=0;
		ArrayList<Cell> safeZone=getSafeZone(marble.getColour());
		Cell target=fullPath.get(fullPath.size()-1);
		for(Cell cell:fullPath) {
			if(cell.getMarble()==marble) continue;
			if(cell.getMarble()!=null&&cell!=target) cnt++;
			if(cell.getMarble()!=null&&cell.getMarble().getColour()==marble.getColour()&&!destroy) 
				throw new IllegalMovementException("Inavlid move due to Self-Blocking");
			if(cell.getMarble()!=null&&cell.getMarble().getColour()==marble.getColour()&&safeZone.contains(cell)) 
				throw new IllegalMovementException("Inavlid move due to Self-Blocking");
			if(cnt>1&&!destroy) throw new IllegalMovementException("Inavlid move due to Path Blockage");
			if(safeZone.contains(cell)) {
				int entry=getEntryPosition(marble.getColour());
				if(track.get(entry).getMarble()!=null&&track.get(entry).getMarble()!=marble&&!destroy)
					throw new IllegalMovementException("Invalid move due to Safe Zone Entry");
			}
			if(cell.getMarble()!=null) {
				int base=getBasePosition(cell.getMarble().getColour());
				if(cell==track.get(base))
					throw new IllegalMovementException("Invalid move due to Base Cell Blockage");
			}
		}
	}
	private void move(Marble marble, ArrayList<Cell> fullPath, boolean destroy) throws IllegalDestroyException{
		Cell curr=fullPath.get(0);
		Cell target=fullPath.get(fullPath.size()-1);
		for(Cell cell:fullPath) {
			if(cell==curr) cell.setMarble(null);
			else {
				if(destroy) {
					if(cell.getMarble()!=null) {
						gameManager.sendHome(cell.getMarble());
						cell.setMarble(null);
					}
				}
				if(cell==target) {
					if(cell.getMarble()!=null) {
						gameManager.sendHome(cell.getMarble());
					}
					cell.setMarble(marble);
				}
			}
		}
		if(target.isTrap()) {
			gameManager.sendHome(target.getMarble());
			target.setMarble(null);
			target.setTrap(false);
			assignTrapCell();
		}
	}
	private void validateSwap(Marble marble_1, Marble marble_2) throws IllegalSwapException{
		if(!marbleInTrack(marble_1)||!marbleInTrack(marble_2)) throw new IllegalSwapException();
		Colour colour=gameManager.getActivePlayerColour();
		if(marble_1.getColour()==colour) {
			int base=getBasePosition(marble_2.getColour());
			if(track.get(base).getMarble()==marble_2)
				throw new IllegalSwapException();
		}else {
			int base=getBasePosition(marble_1.getColour());
			if(track.get(base).getMarble()==marble_1)
				throw new IllegalSwapException();
		}
	}
	private void validateDestroy(int positionInPath) throws IllegalDestroyException{
	}
	private void validateFielding(Cell occupiedBaseCell) throws CannotFieldException{
		Colour colour=gameManager.getActivePlayerColour();
		if(occupiedBaseCell.getMarble()!=null&&occupiedBaseCell.getMarble().getColour()==colour)
			throw new CannotFieldException();
	}
	private void validateSaving(int positionInSafeZone, int positionOnTrack) throws InvalidMarbleException{
		
	}
	public void moveBy(Marble marble, int steps, boolean destroy) throws IllegalMovementException, IllegalDestroyException{
		ArrayList<Cell> fullPath=validateSteps(marble,steps);
		validatePath(marble,fullPath,destroy);
		
	}
	public void destroyMarble(Marble marble) throws IllegalDestroyException{
		if(marble.getColour()!=gameManager.getActivePlayerColour()) {
			validateDestroy()
		}
		for(Cell cell:track) {
			if(cell.getMarble()==marble) {
				cell.setMarble(null);
				break;
			}
		}
		gameManager.sendHome(marble);
	}
	public void sendToBase(Marble marble) throws CannotFieldException,IllegalDestroyException{
		validateFielding();
	}
	public void sendToSafe(Marble marble) throws InvalidMarbleException{
		validateSaving();
	}
	public ArrayList<Marble> getActionableMarbles(){
		ArrayList<Marble> marbles=new ArrayList<>();
		Colour colour=gameManager.getActivePlayerColour();
		for(Cell cell:track) {
			Marble marble=cell.getMarble();
			if(marble!=null&&marble.getColour()==colour) {
				marbles.add(marble);
			}
		}
		ArrayList<Cell> safeZone=getSafeZone(colour);
		for(Cell cell:safeZone) {
			Marble marble=cell.getMarble();
			if(marble!=null) {
				marbles.add(marble);
			}
		}
		return marbles;
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
