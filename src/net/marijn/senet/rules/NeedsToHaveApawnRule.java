package net.marijn.senet.rules;

import net.marijn.senet.game.board.Board;
import net.marijn.senet.utils.Callback;

public class NeedsToHaveApawnRule extends Rule {

	/**
	 * You need to select a place with a pawn on it
	 * 
	 * @param board
	 */
	public NeedsToHaveApawnRule(Board board) {
		super(board);
	}

	@Override
	public void run(Callback<Boolean> callback, int playerIndex, int oldSquare, int newSquare, boolean checkRun) {
		if (".".equals(board.getSquare(oldSquare).getPawn())) {
			if (checkRun) System.out.println("You don't have a piece on square " + oldSquare + ".");
			callback.call(false);
			return;
		}	
		
		callback.call(true);
	}

}
