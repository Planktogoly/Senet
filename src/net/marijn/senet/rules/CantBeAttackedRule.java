package net.marijn.senet.rules;

import net.marijn.senet.game.Player;
import net.marijn.senet.game.board.Board;
import net.marijn.senet.game.board.Square;
import net.marijn.senet.utils.Callback;

public class CantBeAttackedRule extends Rule {

	public CantBeAttackedRule(Board board) {
		super(board);
	}

	@Override
	public void run(Callback<Boolean> callback, int playerIndex, int oldSquare, int newSquare, boolean checkRun) {
		if (newSquare == 26 || newSquare == 28 || newSquare == 29) {
			Square newPlace = board.getSquare(newSquare);
			Player player = board.getPlayers().get(playerIndex);
			
			if (!".".equals(newPlace.getPion()) && !newPlace.getPion().equals(player.getPion())) {
				if (checkRun) System.out.println("You can't attack pions on this place!");
				callback.call(false);
				return;
			}		
			
			callback.call(true);
		}
		
	}

}
