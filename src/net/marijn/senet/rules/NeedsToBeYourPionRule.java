package net.marijn.senet.rules;

import net.marijn.senet.game.Player;
import net.marijn.senet.game.board.Board;
import net.marijn.senet.utils.Callback;

public class NeedsToBeYourPionRule extends Rule {

	public NeedsToBeYourPionRule(Board board) {
		super(board);
	}

	@Override
	public void run(Callback<Boolean> callback, int playerIndex, int oldSquare, int newSquare) {
		Player player = board.getPlayers().get(playerIndex);
		
		if (!player.getPion().equals(board.getSquare(oldSquare).getPion())) {
			System.out.println("This isn't your pion. You are " + player.getPion() + "!");
			callback.call(false);
			return;
		}
		
		callback.call(true);		
	}

}
