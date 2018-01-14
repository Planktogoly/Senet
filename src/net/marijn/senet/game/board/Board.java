package net.marijn.senet.game.board;

import java.util.ArrayList;
import java.util.HashMap;

import net.marijn.senet.game.Player;
import net.marijn.senet.game.Senet;
import net.marijn.senet.positions.TestPosition;
import net.marijn.senet.rules.AttackRule;
import net.marijn.senet.rules.BlockadeRule;
import net.marijn.senet.rules.CantBeAttackedRule;
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

	private HashMap<Integer, TestPosition> testPositions;
	
	public Board(Senet senet) {
		this.senet = senet;
		
		this.squares = new ArrayList<>();
		this.rules = new ArrayList<>();
		this.testPositions = new HashMap<>();
		
		rules.add(new NeedsToHaveAPionRule(this));
		rules.add(new NeedsToBeYourPionRule(this));
		rules.add(new OutsideTheBoardRule(this));
		rules.add(new AttackRule(this));
		rules.add(new BlockadeRule(this));
		rules.add(new PitFallRule(this));
		rules.add(new CantBeAttackedRule(this));
		
		initializeTestPositions();
		createBoard();
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
			if (!passesRules) return false;
			
			rule.run(new Callback<Boolean>() {
				
				@Override
				public void call(Boolean passed) {
					if (!passed) {
						passesRules = false;
					}
				}
			}, playerIndex, oldPlace, newPlace);
		}
		
		Square oldSquare = getSquare(oldPlace);
		Square newSquare = getSquare(newPlace);
		
		String newPion = newSquare.getPion();
		String oldPion = oldSquare.getPion();
		
		newSquare.setPion(oldPion);
		oldSquare.setPion(newPion);
		
		if (newPlace == 30) {
			newSquare.setPion(".");
		}
		
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
	
	public void createBoard() {
		squares.clear();
		
		int testPositionInt = senet.getTestPosition();
		
		TestPosition testPosition = null;
		
		if (testPositionInt >= 1) testPosition = testPositions.get(senet.getTestPosition());
		
		for (int i = 0; i < 30; i++) {
			String pion = ".";

			if (testPosition != null) {
				String testPion = testPosition.getPionOnPlace(i);
				
				if (testPion != null) pion = testPion;
			} else {
				if (i < 10 && i % 2 == 0)
					pion = "O";
				else if (i < 10 && !(i % 2 == 0))
					pion = "X";
			}
			squares.add(new Square(pion));
		}
	}

	private void initializeTestPositions() {
		TestPosition testPosition1 = new TestPosition();
		testPosition1.addPosition(0, "X");
		testPosition1.addPosition(1, "O");
		testPosition1.addPosition(3, "O");
		testPosition1.addPosition(4, "O");
		testPosition1.addPosition(5, "X");
		testPosition1.addPosition(9, "O");
		testPosition1.addPosition(11, "O");
		testPosition1.addPosition(13, "O");
		testPosition1.addPosition(15, "X");
		testPosition1.addPosition(16, "O");
		testPosition1.addPosition(17, "O");
		testPosition1.addPosition(19, "O");
		testPosition1.addPosition(20, "O");
		testPosition1.addPosition(22, "X");
		testPosition1.addPosition(23, "O");
		testPosition1.addPosition(24, "O");
		testPosition1.addPosition(25, "O");
		
		testPositions.put(1, testPosition1);
		
		TestPosition testPosition2 = new TestPosition();
		testPosition2.addPosition(11, "X");
		testPosition2.addPosition(21, "O");
		testPosition2.addPosition(22, "O");
		testPosition2.addPosition(23, "O");
		testPosition2.addPosition(28, "X");
		
		testPositions.put(2, testPosition2);
		
		TestPosition testPosition3 = new TestPosition();
		testPosition3.addPosition(5, "O");
		testPosition3.addPosition(12, "X");
		testPosition3.addPosition(17, "O");
		testPosition3.addPosition(21, "O");
		testPosition3.addPosition(24, "X");
		testPosition3.addPosition(25, "X");
		testPosition3.addPosition(27, "X");
		testPosition3.addPosition(28, "X");
		
		testPositions.put(3, testPosition3);
	}
	
}
