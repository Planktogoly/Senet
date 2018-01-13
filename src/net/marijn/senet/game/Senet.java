package net.marijn.senet.game;

import java.util.ArrayList;
import java.util.Scanner;

import net.marijn.senet.game.board.Board;
import net.marijn.senet.game.dice.Dice;

public class Senet {

	private ArrayList<Player> players;

	private Board board;

	private boolean testGame;
	private int testPosition;

	private Scanner scanner;

	public Senet() {
		this.players = new ArrayList<>();
		this.board = new Board();

		this.scanner = new Scanner(System.in);
	}

	public void play() {
		System.out.println("Welcome to Senet!");

		while (testGame == false && testPosition == 0) {
			System.out.println("Would you like to start a normal game (0) or a test position (1-3)? ->");
			setTestGame(scanner.next());
			scanner.nextLine();
		}

		System.out.println("Enter the name of the first player:");
		addPlayer(scanner.next());
		scanner.nextLine();
		System.out.println("Enter the name of the second player:");
		addPlayer(scanner.next());
		scanner.nextLine();

		boolean rightPoints = false;
		int playerIndex = 0;

		while (!rightPoints) {
			int thrownSticks = Dice.throwSticks();

			System.out.println(players.get(playerIndex).getName() + " has thrown " + thrownSticks);

			if (thrownSticks == 1)
				rightPoints = true;
			else {
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
		playerIndex = playerIndex == 0 ? 1 : 0;

		Player player = players.get(playerIndex);

		player.setPion("O");

		board.set(board.getSquare(10), board.getSquare(11));

		System.out.println(player.getName() + " (" + player.getPion() + "), press <ENTER> to throw the dice");
		scanner.nextLine();

		int sticks = Dice.throwSticks();
		System.out.println(player.getName() + " (" + player.getPion() + "), you have thrown " + sticks);
		board.set(board.getSquare(9), board.getSquare(9 + sticks));
	}

	private void addPlayer(String name) {
		Player player = new Player(name);

		players.add(player);
	}

	private void setTestGame(String rawAnswer) {
		int answer;

		try {
			answer = Integer.valueOf(rawAnswer);
		} catch (NumberFormatException exception) {
			return;
		}

		if (answer == 0) {
			testGame = false;
			testPosition = -1;
		} else if (answer <= 3) {
			testGame = true;
			testPosition = answer;
		}
	}

}
