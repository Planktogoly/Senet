package net.marijn.senet.rules;

import net.marijn.senet.game.board.Board;
import net.marijn.senet.game.player.Player;
import net.marijn.senet.utils.Callback;

public class NeedsToBeYourpawnRule extends Rule {

	/**
	 * You can only move a pawn that is yours
	 * 
	 * @param board
	 */
	public NeedsToBeYourpawnRule(Board board) {
		super(board);
	}

	@Override
	public void run(Callback<Boolean> callback, int playerIndex, int oldSquare, int newSquare, boolean checkRun) {
		Player player = board.getPlayers().get(playerIndex);
		
		if (!player.getPawn().equals(board.getSquare(oldSquare).getPawn())) {
			if (checkRun) System.out.println("You don't have a piece on square " + oldSquare + ". You are " + player.getPawn() + "!");
			callback.call(false);
			return;
		}
		
		callback.call(true);		
	}

}
