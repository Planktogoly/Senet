package net.marijn.senet.game.board;

public class Square {

	private String pawn;
	
	public Square(String pawn) {
		this.pawn = pawn;
	}
	
	public String getPawn() {
		return pawn;
	}
	
	public void setPawn(String pawn) {
		this.pawn = pawn;
	}
	
	public boolean isEmpty() {
		return ".".equals(pawn);
	}
	
	
}
