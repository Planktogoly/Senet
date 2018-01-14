package net.marijn.senet.rules;

import net.marijn.senet.game.board.Board;
import net.marijn.senet.game.board.Square;
import net.marijn.senet.utils.Callback;

public class AttackRule extends Rule {

	public AttackRule(Board board) {
		super(board);
	}

	@Override
	public void run(Callback<Boolean> callback, int playerIndex, int oldSquare, int newSquare) {
		Square newPlace = board.getSquare(newSquare);
		
		String pion = newPlace.getPion();
		if (!pion.equals(".") && !pion.equals(board.getPlayers().get(playerIndex).getPion())) {
			Square leftOfNewPlace = board.getSquare(newSquare - 1);
			Square rightOfNewPlace = board.getSquare(newSquare + 1);
			
			if (leftOfNewPlace.getPion().equals(pion) || rightOfNewPlace.getPion().equals(pion)) {
				System.out.println("Attack on safe piece: " + newSquare);
				callback.call(false);
				return;
			}
		}
		
		callback.call(true);		
	}

}
