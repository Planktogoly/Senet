package net.marijn.senet.rules;

import net.marijn.senet.game.board.Board;
import net.marijn.senet.utils.Callback;

public abstract class Rule {
	
	protected Board board;
	
	public Rule(Board board) {
		this.board = board;
	}
	
	public abstract void run(Callback<Boolean> callback, int playerIndex, int oldSquare, int newSquare, boolean checkRun);
	
}
