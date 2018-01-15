package net.marijn.senet.rules;

import net.marijn.senet.game.board.Board;
import net.marijn.senet.game.board.Square;
import net.marijn.senet.utils.Callback;

public class BlockadeRule extends Rule {

	public BlockadeRule(Board board) {
		super(board);
	}

	@Override
	public void run(Callback<Boolean> callback, int playerIndex, int oldSquare, int newSquare, boolean checkRun) {
		String enemyPion = board.getPlayers().get(playerIndex == 0 ? 1 : 0).getPion();
		
		int count = 0;
		for (int i = oldSquare + 1; i < newSquare; i++) {
			if (i > 30) continue;
			if (count == 3) break;
			
			Square placeOnBoard = board.getSquare(i);

			if (count > 0 && placeOnBoard.getPion().equals(".")) break;
			
			if (placeOnBoard.getPion().equals(enemyPion)) {
				count++;
			}
		}
		
		if (count == 3) {
			if (checkRun) System.out.println("Attempt to jump over blockade");
			callback.call(false);
			return;
		}	
		
		callback.call(true);
	}

}
