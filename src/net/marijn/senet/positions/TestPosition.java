package net.marijn.senet.positions;

import java.util.HashMap;

public class TestPosition {
	
	private HashMap<Integer, String> board;
	
	public TestPosition() {
		board = new HashMap<>();		
	}
	
	public void addPosition(int place, String pion) {
		board.put(place, pion);
	}
	
	public String getPionOnPlace(int place) {
		return board.get(place);
	}
	
}
