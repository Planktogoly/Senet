package net.marijn.senet.rules;

import net.marijn.senet.game.board.Board;
import net.marijn.senet.game.player.Player;
import net.marijn.senet.utils.Callback;

public class CantBeYourpawnRule extends Rule {

	/**
	 * You can't attack your own pawn
	 * 
	 * @param board
	 */
	public CantBeYourpawnRule(Board board) {
		super(board);
	}

	@Override
	public void run(Callback<Boolean> callback, int playerIndex, int oldSquare, int newSquare, boolean checkRun) {
		Player player = board.getPlayers().get(playerIndex);
		
		if (player.getPawn().equals(board.getSquare(newSquare).getPawn())) {
			if (checkRun) System.out.println("You can't attack your own pawn!");
			callback.call(false);
			return;
		}
		
		callback.call(true);			
	}

}
