package net.marijn.senet.game.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import net.marijn.senet.game.Senet;
import net.marijn.senet.game.board.positions.TestPosition;
import net.marijn.senet.game.player.Player;
import net.marijn.senet.rules.AttackRule;
import net.marijn.senet.rules.BlockadeRule;
import net.marijn.senet.rules.CantBeAttackedRule;
import net.marijn.senet.rules.CantBeYourPawnRule;
import net.marijn.senet.rules.NeedsToBeYourPawnRule;
import net.marijn.senet.rules.NeedsToHaveAPawnRule;
import net.marijn.senet.rules.OutsideTheBoardRule;
import net.marijn.senet.rules.PitFallRule;
import net.marijn.senet.rules.Rule;
import net.marijn.senet.utils.Callback;

public class Board {

	private Senet senet;
	
	private ArrayList<Square> squares;
	private ArrayList<Rule> rules;

	private HashMap<Integer, TestPosition> testPositions;
	
	private boolean passesRules = true;
	
	public Board(Senet senet) {
		this.senet = senet;
		
		this.squares = new ArrayList<>();
		this.rules = new ArrayList<>();
		this.testPositions = new HashMap<>();
		
		rules.add(new NeedsToHaveAPawnRule(this));
		rules.add(new NeedsToBeYourPawnRule(this));
		rules.add(new OutsideTheBoardRule(this));
		rules.add(new AttackRule(this));
		rules.add(new BlockadeRule(this));
		rules.add(new PitFallRule(this));
		rules.add(new CantBeAttackedRule(this));
		rules.add(new CantBeYourPawnRule(this));
		
		initializeTestPositions();
		createBoard();
	}	
	
	
	
	/**
	 * Change the pawn from his old place to his new place 
	 * 
	 * @param playerIndex
	 * @param oldPlace
	 * @param newPlace
	 * @return if the pawn changed his position
	 */
	public boolean set(int playerIndex, int oldPlace, int newPlace) {		
		if (!checkIfSetIsCorrect(playerIndex, oldPlace, newPlace, true)) {
			return false;
		}
		
		Square oldSquare = getSquare(oldPlace);
		Square newSquare = getSquare(newPlace);
		
		String newpawn = newSquare.getPawn();
		String oldpawn = oldSquare.getPawn();
		
		newSquare.setPawn(oldpawn);
		oldSquare.setPawn(newpawn);
		
		if (newPlace == 30) {
			newSquare.setPawn(".");
		}
		
		if (!(newPlace == 27)) {
			print();
		}
		
		checkIfAPlayerWon(playerIndex);
		return true;
	}
	
	/**
	 * We calculate the best place for the computer to go to.
	 * 
	 * The best place is the farthest place a pawn can go.
	 * 
	 * @param playerIndex
	 * @param pointsThrown
	 * @return best place
	 */
	public int getBestSet(int playerIndex, int pointsThrown) {
		String pawn = getPlayers().get(playerIndex).getPawn();
		
		HashMap<Integer, Integer> answers = new HashMap<>();
		
		for (int i = 0; i < 30; i++) {
			if ((i + 1) + pointsThrown > 30 || (i + 1) + pointsThrown < 1) continue;
			
			Square square = squares.get(i);
			
			if (square.getPawn().equals(pawn)) {				
				if (checkIfSetIsCorrect(playerIndex, i + 1, (i + 1) + pointsThrown, false)) {
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
	
	/**
	 * Check if a player can set a pawn
	 * 
	 * @param playerIndex
	 * @param pointsThrown
	 * @return if a player can place a set
	 */
	public boolean checkifPlayerCanSetApawn(int playerIndex, int pointsThrown) {
		String pawn = getPlayers().get(playerIndex).getPawn();
		
		passesRules = true;	
		
		for (int i = 0; i < 30; i++) {
			if ((i + 1) + pointsThrown > 30 || (i + 1) + pointsThrown < 1) {
				passesRules = false;
				continue;
			}
			
			Square square = squares.get(i);
			
			if (square.getPawn().equals(pawn)) {				
				if (checkIfSetIsCorrect(playerIndex, i + 1, (i + 1) + pointsThrown, false)) break;	
			}
		}
		
		return passesRules;
	}

	/**
	 * Print the game board
	 */
	public void print() {
		System.out.println("+---------------------+");

		System.out.print("| ");
		for (int i = 0; i < squares.size() - 20; i++) {
			Square square = squares.get(i);

			System.out.print(square.getPawn() + " ");
		}
		
		System.out.print("|");
		System.out.println("");
		System.out.print("| ");

		for (int i = 19; i >= squares.size() - 20; i--) {
			Square square = squares.get(i);

			System.out.print(square.getPawn() + " ");
		}
		
		System.out.print("|");
		System.out.println("");
		System.out.print("| ");
		
		for (int i = 20; i < squares.size(); i++) {
			Square square = squares.get(i);

			System.out.print(square.getPawn() + " ");
		}

		System.out.print("|");
		System.out.println("");
		System.out.println("+---------------------+");
	}
	
	/**
	 * Create the board whether it is a test position or not
	 */
	public void createBoard() {
		squares.clear();
		
		int testPositionInt = senet.getTestPosition();
		
		TestPosition testPosition = null;
		
		if (testPositionInt >= 1) testPosition = testPositions.get(senet.getTestPosition());
		
		for (int i = 0; i < 30; i++) {
			String pawn = ".";

			if (testPosition != null) {
				String testpawn = testPosition.getpawnOnPlace(i);
				
				if (testpawn != null) pawn = testpawn;
			} else {
				if (i < 10 && i % 2 == 0)
					pawn = "O";
				else if (i < 10 && !(i % 2 == 0))
					pawn = "X";
			}
			squares.add(new Square(pawn));
		}
	}
	
	/**
	 * Check if the set a player wants to make is correct by the Senet rules.
	 * 
	 * @param playerIndex
	 * @param oldPlace
	 * @param newPlace
	 * @param checkRun
	 * @return if the set is correct by the Senet rules
	 */
	private boolean checkIfSetIsCorrect(int playerIndex, int oldPlace, int newPlace, boolean checkRun) {
		passesRules = true;	
		
		for (Rule rule : rules) {		
			if (checkRun && rule.getClass().getName().equalsIgnoreCase("net.marijn.senet.rules.PitFallRule")) continue;	
			if (!passesRules) continue;
			
			rule.run(new Callback<Boolean>() {
				
				@Override
				public void call(Boolean passed) {			
					if (!passed) {
						passesRules = false;
					}
				}
			}, playerIndex, oldPlace, newPlace, checkRun);
		}
		
		return passesRules;
	}
	
	/**
	 * Set the winner if there is one
	 * 
	 * @param playerIndex
	 */
	private void checkIfAPlayerWon(int playerIndex) {
		int whitesLeft = 0;
		int blacksLeft = 0;
		
		for (int i = 0; i < 30; i++ ) {
			Square square = getSquare(i + 1);
			
			if (square.getPawn().equals("O")) whitesLeft++;
			else if (square.getPawn().equals("X")) blacksLeft++;
		}
		
		if (whitesLeft == 0 || blacksLeft == 0) {
			senet.setWinner(getPlayers().get(playerIndex));
			senet.setLoser(getPlayers().get(playerIndex == 0 ? 1 : 0));
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
	
	public ArrayList<Player> getPlayers() {
		return senet.getPlayers();
	}
	
	public Square getSquare(int index) {
		int rightIndex = index - 1;
		
		return squares.get(rightIndex);
	}
}
