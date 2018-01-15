package net.marijn.senet.game.dice;

public class Dice {
	
	/**
	 * Throw the dice of Senet
	 * 
	 * @return either 1, 2, 3, 4 or 6
	 */
	public static int throwSticks() {		
		int whiteSticks = 0;
		
		for (int i = 0; i < 4; i++) {
			if (Math.random() > 0.5) whiteSticks++;
		}
		
		if (whiteSticks == 1) return 1;
		else if (whiteSticks == 2) return 2;
		else if (whiteSticks == 3) return 3;
		else if (whiteSticks == 4) return 4;
		else return 6;		
	}

}
