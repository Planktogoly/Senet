package net.marijn.senet;

import net.marijn.senet.game.Senet;

public class Main {
	
	private static Senet senet;
	
	public static void main(String[] args) {
		senet = new Senet();
		
		senet.play();		
	}

}
