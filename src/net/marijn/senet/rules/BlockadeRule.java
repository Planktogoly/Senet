package net.marijn.senet.rules;

import net.marijn.senet.game.board.Board;
import net.marijn.senet.game.board.Square;
import net.marijn.senet.utils.Callback;

public class BlockadeRule extends Rule {

	public BlockadeRule(Board board) {
		super(board);
	}

	@Override
	public void run(Callback<Boolean> callback, int playerIndex, int oldSquare, int newSquare) {
		String enemyPion = board.getPlayers().get(playerIndex == 0 ? 1 : 0).getPion();
		
		int count = 0;
		for (int i = oldSquare + 1; i < ((oldSquare + 1) + 3); i++) {
			if (i > 30) continue;
			
			Square placeOnBoard = board.getSquare(i);
			
			if (placeOnBoard.getPion().equals(enemyPion)) {
				count++;
			}
		}
		
		if (count == 3) {
			System.out.println("Attempt to jump over blockade");
			callback.call(false);
			return;
		}	
		
		callback.call(true);
	}

}
