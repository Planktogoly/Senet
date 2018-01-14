package net.marijn.senet.rules;

import net.marijn.senet.game.board.Board;
import net.marijn.senet.game.board.Square;
import net.marijn.senet.utils.Callback;

public class AttackRule extends Rule {

	public AttackRule(Board board) {
		super(board);
	}

	@Override
	public void run(Callback<Boolean> callback, int playerIndex, int oldSquare, int newSquare, boolean checkRun) {
		if ((newSquare > 30) || (newSquare < 1)) {
			callback.call(true);
			return;
		}
		
		Square newPlace = board.getSquare(newSquare);
		
		String pion = newPlace.getPion();
		if (!pion.equals(".") && !pion.equals(board.getPlayers().get(playerIndex).getPion())) {
			Square leftOfNewPlace = null;
			Square rightOfNewPlace = null;
			
			if (newSquare != 1) leftOfNewPlace = board.getSquare(newSquare - 1);
			if (newSquare != 30) rightOfNewPlace = board.getSquare(newSquare + 1);
			
			if ((leftOfNewPlace != null && leftOfNewPlace.getPion().equals(pion)) || (rightOfNewPlace !=null && rightOfNewPlace.getPion().equals(pion))) {
				if (checkRun) System.out.println("Attack on safe piece: " + newSquare);
				callback.call(false);
				return;
			}
		}
		
		callback.call(true);		
	}

}
