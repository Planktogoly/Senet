package net.marijn.senet.game.player;

public class Player {
	
	private String name;
	
	private String pawn;
	
	private int turns = 0;
	
	public Player(String name) {
		this.name = name;
	}
	
	public String getPawn() {
		return pawn;
	}
	
	public void setPawn(String pawn) {
		this.pawn = pawn;
	}
	
	public String getName() {
		return name;
	}
	
	public void addTurn() {
		turns++;
	}
	
	public int getTurns() {
		return turns;
	}
}
