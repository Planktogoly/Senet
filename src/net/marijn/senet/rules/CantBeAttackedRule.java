package net.marijn.senet.rules;

import net.marijn.senet.game.board.Board;
import net.marijn.senet.game.board.Square;
import net.marijn.senet.game.player.Player;
import net.marijn.senet.utils.Callback;

public class CantBeAttackedRule extends Rule {

	/**
	 * You can't attack a pawn on the place 26, 28 and 29
	 * 
	 * @param board
	 */
	public CantBeAttackedRule(Board board) {
		super(board);
	}

	@Override
	public void run(Callback<Boolean> callback, int playerIndex, int oldSquare, int newSquare, boolean checkRun) {
		if (newSquare == 26 || newSquare == 28 || newSquare == 29) {
			Square newPlace = board.getSquare(newSquare);
			Player player = board.getPlayers().get(playerIndex);
			
			if (!".".equals(newPlace.getPawn()) && !newPlace.getPawn().equals(player.getPawn())) {
				if (checkRun) System.out.println("You can't attack pawns on this place!");
				callback.call(false);
				return;
			}		
			
			callback.call(true);
		}
		
	}

}
