package net.marijn.senet.rules;

import net.marijn.senet.game.Player;
import net.marijn.senet.game.board.Board;
import net.marijn.senet.game.board.Square;
import net.marijn.senet.utils.Callback;

public class LastLineRule extends Rule {

	public LastLineRule(Board board) {
		super(board);
	}

	@Override
	public void run(Callback<Boolean> callback, int playerIndex, int oldSquare, int newSquare) {
		if (newSquare == 30) {
			Player player = board.getPlayers().get(playerIndex);			
			String pion = player.getPion();
			
			boolean hasAllPionsInLastLine = true;
			for (int i = 0; i < 19; i++) {
				Square square = board.getSquare(i);	
				
				if (square.getPion().equals(pion)) hasAllPionsInLastLine = false;
			}
			
			if (!hasAllPionsInLastLine) {
				callback.call(false);
				return;
			}
			
			callback.call(true);
		}		
	}

}
