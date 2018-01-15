package net.marijn.senet.rules;

import net.marijn.senet.game.board.Board;
import net.marijn.senet.game.board.Square;
import net.marijn.senet.utils.Callback;

public class AttackRule extends Rule {

	/**
	 * You can't attack a pawn if there is two or more next to each other
	 * 
	 * @param board
	 */
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
		
		String pawn = newPlace.getPawn();
		if (!pawn.equals(".") && !pawn.equals(board.getPlayers().get(playerIndex).getPawn())) {
			Square leftOfNewPlace = null;
			Square rightOfNewPlace = null;
			
			if (newSquare != 1) leftOfNewPlace = board.getSquare(newSquare - 1);
			if (newSquare != 30) rightOfNewPlace = board.getSquare(newSquare + 1);
			
			if ((leftOfNewPlace != null && leftOfNewPlace.getPawn().equals(pawn)) || (rightOfNewPlace !=null && rightOfNewPlace.getPawn().equals(pawn))) {
				if (checkRun) System.out.println("Attack on safe piece: " + newSquare);
				callback.call(false);
				return;
			}
		}
		
		callback.call(true);		
	}

}
