package net.marijn.senet.game.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import net.marijn.senet.game.Player;
import net.marijn.senet.game.Senet;
import net.marijn.senet.game.board.positions.TestPosition;
import net.marijn.senet.rules.AttackRule;
import net.marijn.senet.rules.BlockadeRule;
import net.marijn.senet.rules.CantBeAttackedRule;
import net.marijn.senet.rules.CantBeYourPionRule;
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
		rules.add(new CantBeYourPionRule(this));
		
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
	
	public boolean set(int playerIndex, int oldPlace, int newPlace, int sticks) {
		passesRules = true;	
		
		for (Rule rule : rules) {		
			if (!passesRules) continue;
			
			rule.run(new Callback<Boolean>() {
				
				@Override
				public void call(Boolean passed) {
					if (!passed) {
						passesRules = false;
					}
				}
			}, playerIndex, oldPlace, newPlace, true);
		}
		
		if (!passesRules) {
			return false;
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
		
		if (!(newPlace == 27)) {
			print();
		}
		return true;
	}
	
	public int getBestSet(int playerIndex, int pointsThrown) {
		String pion = getPlayers().get(playerIndex).getPion();
		
		HashMap<Integer, Integer> answers = new HashMap<>();
		
		for (int i = 0; i < 30; i++) {
			if ((i + 1) + pointsThrown > 30 || (i + 1) + pointsThrown < 1) continue;
			
			Square square = squares.get(i);
			
			if (square.getPion().equals(pion)) {
				passesRules = true;
				
				for (Rule rule : rules) {	
					if (rule.getClass().getName().equalsIgnoreCase("net.marijn.senet.rules.PitFallRule")) continue;
					if (!passesRules) break;
					
					rule.run(new Callback<Boolean>() {
						
						@Override
						public void call(Boolean passed) {
							passesRules = passed;
						}
					}, playerIndex, i + 1, (i + 1) + pointsThrown, false);
				}
				
				if (passesRules) {
					answers.put(i + 1, i + 1 + pointsThrown);
				} 				
			}
		}
		
		int bestAnswer = 0;
		int farestSet = 0;
		
		for (Entry<Integer, Integer> entry : answers.entrySet()) {
			if (entry.getValue() > farestSet) {
				bestAnswer = entry.getKey();
				farestSet = entry.getValue();
			}
		}		
		
		return bestAnswer;
	}
	
	public boolean checkifPlayerCanSetAPion(int playerIndex, int pointsThrown) {
		String pion = getPlayers().get(playerIndex).getPion();
		
		passesRules = true;	
		
		for (int i = 0; i < 30; i++) {
			if ((i + 1) + pointsThrown > 30 || (i + 1) + pointsThrown < 1) continue;
			
			Square square = squares.get(i);
			
			if (square.getPion().equals(pion)) {
				passesRules = true;
				
				for (Rule rule : rules) {	
					if (rule.getClass().getName().equalsIgnoreCase("net.marijn.senet.rules.PitFallRule")) continue;
					if (!passesRules) break;
					
					rule.run(new Callback<Boolean>() {
						
						@Override
						public void call(Boolean passed) {
							passesRules = passed;
						}
					}, playerIndex, i + 1, (i + 1) + pointsThrown, false);
				}
				
				if (passesRules) {
					break;
				} 				
			}
		}
		
		return passesRules;
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
