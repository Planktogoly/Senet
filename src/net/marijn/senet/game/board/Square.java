package net.marijn.senet.game.board;

public class Square {

	private String pion;
	
	public Square(String pion) {
		this.pion = pion;
	}
	
	public String getPion() {
		return pion;
	}
	
	public void setPion(String pion) {
		this.pion = pion;
	}
	
	public boolean isEmpty() {
		return ".".equals(pion);
	}
	
	
}
