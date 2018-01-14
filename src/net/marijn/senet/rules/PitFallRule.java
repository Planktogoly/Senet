package net.marijn.senet.rules;

import net.marijn.senet.game.board.Board;
import net.marijn.senet.game.board.Square;
import net.marijn.senet.utils.Callback;

public class PitFallRule extends Rule {

	public PitFallRule(Board board) {
		super(board);
	}

	@Override
	public void run(Callback<Boolean> callback, int playerIndex, int oldSquare, int newSquare) {
		if (newSquare == 27) {
			System.out.println("You fell in a pitfall!");
			
			for (int i = 0; i < 30; i++) {
				Square newPlaceSquare = board.getSquare(i + 1);
				
				if (!newPlaceSquare.isEmpty()) continue;
				
				board.set(playerIndex, oldSquare, i + 1);
				callback.call(true);
				return;
			}
			callback.call(false);
		}
		
	}

}
