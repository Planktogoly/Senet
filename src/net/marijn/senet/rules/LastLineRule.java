package net.marijn.senet.rules;

import net.marijn.senet.game.board.Board;
import net.marijn.senet.game.board.Square;
import net.marijn.senet.game.player.Player;
import net.marijn.senet.utils.Callback;

public class LastLineRule extends Rule {

	/**
	 * You can't go to place 30 if there are pawns not in the last line
	 * 
	 * @param board
	 */
	public LastLineRule(Board board) {
		super(board);
	}

	@Override
	public void run(Callback<Boolean> callback, int playerIndex, int oldSquare, int newSquare, boolean checkRun) {
		if (newSquare == 30) {
			Player player = board.getPlayers().get(playerIndex);			
			String pawn = player.getPawn();
			
			boolean hasAllpawnsInLastLine = true;
			for (int i = 0; i < 19; i++) {
				Square square = board.getSquare(i);	
				
				if (square.getPawn().equals(pawn)) hasAllpawnsInLastLine = false;
			}
			
			if (!hasAllpawnsInLastLine) {
				callback.call(false);
				return;
			}
			
			callback.call(true);
		}		
	}

}
