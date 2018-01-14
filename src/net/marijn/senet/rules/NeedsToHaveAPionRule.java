package net.marijn.senet.rules;

import net.marijn.senet.game.board.Board;
import net.marijn.senet.utils.Callback;

public class NeedsToHaveAPionRule extends Rule {

	public NeedsToHaveAPionRule(Board board) {
		super(board);
	}

	@Override
	public void run(Callback<Boolean> callback, int playerIndex, int oldSquare, int newSquare) {
		if (".".equals(board.getSquare(oldSquare).getPion())) {
			System.out.println("This place doesn't have pion!");
			callback.call(false);
			return;
		}	
		
		callback.call(true);
	}

}
