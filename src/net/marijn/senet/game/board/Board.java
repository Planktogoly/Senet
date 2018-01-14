package net.marijn.senet.game.board;

import java.util.ArrayList;

import net.marijn.senet.game.Player;
import net.marijn.senet.game.Senet;
import net.marijn.senet.rules.AttackRule;
import net.marijn.senet.rules.BlockadeRule;
import net.marijn.senet.rules.NeedsToBeYourPionRule;
import net.marijn.senet.rules.NeedsToHaveAPionRule;
import net.marijn.senet.rules.OutsideTheBoardRule;
import net.marijn.senet.rules.PitFallRule;
import net.marijn.senet.rules.Rule;
import net.marijn.senet.utils.Callback;

public class Board {

	private Senet senet;
	
	private ArrayList<Square> squares;
	private ArrayList<Rule> rules;

	public Board(Senet senet) {
		this.senet = senet;
		
		this.squares = new ArrayList<>();
		this.rules = new ArrayList<>();
		
		rules.add(new NeedsToHaveAPionRule(this));
		rules.add(new NeedsToBeYourPionRule(this));
		rules.add(new OutsideTheBoardRule(this));
		rules.add(new AttackRule(this));
		rules.add(new BlockadeRule(this));
		rules.add(new PitFallRule(this));

		for (int i = 0; i < 30; i++) {
			String pion = ".";

			if (i < 10 && i % 2 == 0)
				pion = "O";
			else if (i < 10 && !(i % 2 == 0))
				pion = "X";

			squares.add(new Square(pion));
		}
	}	
	
	public ArrayList<Player> getPlayers() {
		return senet.getPlayers();
	}
	
	public Square getSquare(int index) {
		int rightIndex = index - 1;
		
		return squares.get(rightIndex);
	}
	
	private boolean passesRules = true;
	
	public boolean set(int playerIndex, int oldPlace, int newPlace) {
		passesRules = true;
		
		for (Rule rule : rules) {			
			rule.run(new Callback<Boolean>() {
				
				@Override
				public void call(Boolean passed) {
					if (!passed) passesRules = false;
				}
			}, playerIndex, oldPlace, newPlace);
		}
		
		if (!passesRules) return false;
		
		Square oldSquare = getSquare(oldPlace);
		Square newSquare = getSquare(newPlace);
		
		String newPion = newSquare.getPion();
		String oldPion = oldSquare.getPion();
		
		newSquare.setPion(oldPion);
		oldSquare.setPion(newPion);
		
		print();
		return true;
	}

	public void print() {
		System.out.println("+---------------------+");

		System.out.print("| ");
		for (int i = 0; i < squares.size() - 20; i++) {
			Square square = squares.get(i);

			System.out.print(square.getPion() + " ");
		}
		
		System.out.print("|");
		System.out.println("");
		System.out.print("| ");

		for (int i = 19; i >= squares.size() - 20; i--) {
			Square square = squares.get(i);

			System.out.print(square.getPion() + " ");
		}
		
		System.out.print("|");
		System.out.println("");
		System.out.print("| ");
		
		for (int i = 20; i < squares.size(); i++) {
			Square square = squares.get(i);

			System.out.print(square.getPion() + " ");
		}

		System.out.print("|");
		System.out.println("");
		System.out.println("+---------------------+");
	}

}
