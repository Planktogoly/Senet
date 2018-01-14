package net.marijn.senet.game;

import java.util.ArrayList;
import java.util.Scanner;

import net.marijn.senet.game.board.Board;
import net.marijn.senet.game.board.Square;
import net.marijn.senet.game.dice.Dice;
import net.marijn.senet.utils.Utils;

public class Senet {

	private ArrayList<Player> players;

	private Board board;

	private boolean testGame;
	private int testPosition;

	private Scanner scanner;
	
	private int playerIndex = 0;
	private Player winner = null;

	public Senet() {
		this.players = new ArrayList<>();
		this.board = new Board(this);

		this.scanner = new Scanner(System.in);
	}
	
	public int getTestPosition() {
		return testPosition;
	}

	public void play() {
		System.out.println("Welcome to Senet!");

		while (testGame == false && testPosition == 0) {
			System.out.println("Would you like to start a normal game (0) or a test position (1-3)?");
			setTestGame(scanner.next());
			scanner.nextLine();
		}

		System.out.println("Enter the name of the first player:");
		addPlayer(scanner.nextLine());
		System.out.println("Enter the name of the second player:");
		
		boolean notTheSameName = false;
		while (!notTheSameName) {
			String name = scanner.nextLine();
			
			if (!name.equals(players.get(0).getName())) {
				notTheSameName = true;
				addPlayer(name);
				continue;
			}
			
			System.out.println("You cannot have the same name!");
			System.out.println("Enter the name of the second player:");
		}

		boolean rightPoints = false;
		while (!rightPoints) {
			int thrownSticks = Dice.throwSticks();

			System.out.println(players.get(playerIndex).getName() + " has thrown " + thrownSticks);

			if (thrownSticks == 1) {
				rightPoints = true;
			} else {
				playerIndex = playerIndex == 0 ? 1 : 0;

				// Wait 1 second so it is not instant
				try {
					Thread.sleep(500L);
				} catch (InterruptedException e) {
					System.out.println("ERROR: Restart the game!");
				}
			}
		}

		System.out.println(players.get(playerIndex).getName() + " starts the game!");

		players.get(playerIndex).setPion("X");
		
		if (testPosition == -1) {
			board.set(playerIndex, 10, 11, 0);
			
			playerIndex = playerIndex == 0 ? 1 : 0;

			Player player = players.get(playerIndex);

			player.setPion("O");

			System.out.println(player.getName() + " (" + player.getPion() + "), press <ENTER> to throw the dice");
			scanner.nextLine();

			int sticks = Dice.throwSticks();
			System.out.println(player.getName() + " (" + player.getPion() + "), you have thrown " + sticks);
			board.set(playerIndex, 9, 9 + sticks, sticks);
			playerIndex = playerIndex == 0 ? 1 : 0;
		} else {
			playerIndex = playerIndex == 0 ? 1 : 0;
			Player player = players.get(playerIndex);
			player.setPion("O");
			
			board.createBoard();
			board.print();
		}

		while (winner == null) {
			playerTurn();
		}		
		
		System.out.println(winner.getName() + " has won the game!");
	}

	private void addPlayer(String name) {
		Player player = new Player(name);

		players.add(player);
	}

	private void setTestGame(String rawAnswer) {
		int answer = Utils.isAnswerANumber(rawAnswer);

		if (answer == 0) {
			testGame = false;
			testPosition = -1;
		} else if (answer >= 0 && answer <= 3) {
			testGame = true;
			testPosition = answer;
		} else if (answer < 0) {
			return;
		}
	}
	
	private void playerTurn() {
		Player player = players.get(playerIndex);
		
		System.out.println(player.getName() + " (" + player.getPion() + "), press <ENTER> to throw the dice");
		scanner.nextLine();

		int sticks = Dice.throwSticks();
		System.out.println(player.getName() + " (" + player.getPion() + "), you have thrown " + sticks);
		
		if (!board.checkifPlayerCanSetAPion(playerIndex)) {
			System.out.println("You can't set a pion!");
		} else {
			boolean rightAnswer = false;
			while (!rightAnswer) {
				System.out.println(player.getName() + " (" + player.getPion() + "), which piece do you want to move?");
				String rawAnswer = scanner.nextLine();
				
				int answer = Utils.isAnswerANumber(rawAnswer);
				if (answer == -1) continue;
				
				if (answer <= 0 || answer > 30) {
					System.out.println("The piece place needs to be higher than zero and lower than thirty!");
					continue;
				}
				
				if (board.set(playerIndex, answer, answer + sticks, sticks)) rightAnswer = true;
				
				checkIfSomeoneWon();
			}
		}
		
		if (sticks == 2 || sticks == 3) playerIndex = playerIndex == 0 ? 1 : 0;
	}
	
	public void checkIfSomeoneWon() {
		int whitesLeft = 0;
		int blacksLeft = 0;
		
		for (int i = 0; i < 30; i++ ) {
			Square square = board.getSquare(i + 1);
			
			if (square.getPion().equals("O")) whitesLeft++;
			else if (square.getPion().equals("X")) blacksLeft++;
		}
		
		if (whitesLeft == 0 || blacksLeft == 0) {
			winner = players.get(playerIndex);
		}
	}
	
	public ArrayList<Player> getPlayers() {
		return players;
	}
}
