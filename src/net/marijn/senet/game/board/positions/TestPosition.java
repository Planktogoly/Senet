package net.marijn.senet.game.board.positions;

import java.util.HashMap;

public class TestPosition {
	
	private HashMap<Integer, String> board;
	
	public TestPosition() {
		board = new HashMap<>();		
	}
	
	public void addPosition(int place, String pawn) {
		board.put(place, pawn);
	}
	
	public String getpawnOnPlace(int place) {
		return board.get(place);
	}
	
}
