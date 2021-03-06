package net.marijn.senet.rules;

import net.marijn.senet.game.board.Board;
import net.marijn.senet.utils.Callback;

public class OutsideTheBoardRule extends Rule {

	/**
	 * You can't move your pawn outside the board
	 * 
	 * @param board
	 */
	public OutsideTheBoardRule(Board board) {
		super(board);
	}

	@Override
	public void run(Callback<Boolean> callback, int playerIndex, int oldSquare, int newSquare, boolean checkRun) {
		if (newSquare > 30) {
			if (checkRun) System.out.println("You can't move a pawn outside of the board!");
			callback.call(false);
			return;
		}
		
		callback.call(true);
	}

}
