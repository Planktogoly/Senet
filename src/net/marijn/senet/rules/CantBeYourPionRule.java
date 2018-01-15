package net.marijn.senet.rules;

import net.marijn.senet.game.board.Board;
import net.marijn.senet.game.player.Player;
import net.marijn.senet.utils.Callback;

public class CantBeYourPionRule extends Rule {

	public CantBeYourPionRule(Board board) {
		super(board);
	}

	@Override
	public void run(Callback<Boolean> callback, int playerIndex, int oldSquare, int newSquare, boolean checkRun) {
		Player player = board.getPlayers().get(playerIndex);
		
		
/*		System.out.println(board.getSquare(newSquare).getPion() + newSquare);*/
		if (player.getPion().equals(board.getSquare(newSquare).getPion())) {
			if (checkRun) System.out.println("You can't attack your own pion!");
			callback.call(false);
			return;
		}
		
		callback.call(true);			
	}

}
