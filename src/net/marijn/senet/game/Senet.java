package net.marijn.senet.game;

import java.util.ArrayList;
import java.util.Scanner;

import net.marijn.senet.game.board.Board;
import net.marijn.senet.game.dice.Dice;
import net.marijn.senet.game.player.Player;
import net.marijn.senet.utils.Utils;

public class Senet {
	
	private ArrayList<Player> players;

	private Board board;

	private GameMode gameMode;
	
	private boolean testGame;
	private int testPosition;

	private Scanner scanner;
	
	private int playerIndex;
	private Player winner;

	public Senet() {
		this.players = new ArrayList<>();
		this.board = new Board(this);

		this.scanner = new Scanner(System.in);
		
		this.playerIndex = 0;
	}

	public void play() {
		System.out.println("Welcome to Senet!");
		System.out.println("<------------------------->");
		
		setupSenet();
		
		startGame();

		while (winner == null) {
			playTurn();
		}		
		
		System.out.println("<------------------------->");
		System.out.println(winner.getName() + " has won the game!");
	}
	
	private void setupSenet() {
		while (!testGame && testPosition == 0) {
			System.out.println("Would you like to start a normal game (0) or start on a test position (1-3)?");
			setTestGame(scanner.next());
			scanner.nextLine();
		}
		
		while (gameMode == null) {
			System.out.println("Do you want to play singleplayer(1) or multiplayer(2)?");
			int answer = Utils.isAnswerANumber(scanner.nextLine());
			
			if (answer == 1) gameMode = GameMode.SINGEPLAYER;
			else if (answer == 2) gameMode = GameMode.MULTIPLAYER; 
		}
		
		if (gameMode == GameMode.MULTIPLAYER) {
			boolean correctName = false;
			while (!correctName) {
				System.out.println("Enter the name of the first player:");
				String input = scanner.nextLine();
				
				if (input.isEmpty()) {
					System.out.println("Your name can't be empty!");
					continue;
				}
				
				addPlayer(input);
				correctName = true;
			}
			
			correctName = false;
			while (!correctName) {
				System.out.println("Enter the name of the second player:");
				String input = scanner.nextLine();
				
				if (input.isEmpty()) {
					System.out.println("Your name can't be empty!");
					continue;			
				}
				
				String enemyName = players.get(0).getName();
				
				if (input.equals(enemyName)) {
					System.out.println("Player '" +enemyName + "' already exists!");
					continue;
				}
				
				addPlayer(input);
				correctName = true;			
			}
		} else {
			boolean correctName = false;
			while (!correctName) {
				System.out.println("Enter your player name:");
				String input = scanner.nextLine();
				
				if (input.isEmpty()) {
					System.out.println("Your name can't be empty!");
					continue;
				}
				if (input.equals("Computer")) {
					System.out.println("Player 'Computer' already exists!");
					continue;
				}
				
				addPlayer(input);
				correctName = true;
			}
			
			addPlayer("Computer");
		}
	}
	
	private void startGame() {
		boolean rightPoints = false;
		while (!rightPoints) {
			int thrownSticks = Dice.throwSticks();

			System.out.println(players.get(playerIndex).getName() + " has thrown " + thrownSticks);

			if (thrownSticks == 1) {
				rightPoints = true;
			} else {
				playerIndex = playerIndex == 0 ? 1 : 0;

				// Wait 1 second. We dont want it to be instant!
				Utils.sleep(1000L);
			}
		}

		System.out.println(players.get(playerIndex).getName() + " starts the game!");

		players.get(playerIndex).setPion("X");
		
		if (testPosition == -1) {
			board.set(playerIndex, 10, 11, 0);
			
			playerIndex = playerIndex == 0 ? 1 : 0;

			Player player = players.get(playerIndex);

			player.setPion("O");
			
			if (player.getName().equalsIgnoreCase("Computer")) {
				int thrownPoints = Dice.throwSticks();
				System.out.println(player.getName() + " (" + player.getPion() + "), you have thrown " + thrownPoints);
				board.set(playerIndex, 9, 9 + thrownPoints, thrownPoints);
				playerIndex = playerIndex == 0 ? 1 : 0;				
			} else {
				System.out.println(player.getName() + " (" + player.getPion() + "), press <ENTER> to throw the dice");
				scanner.nextLine();

				int thrownPoints = Dice.throwSticks();
				System.out.println(player.getName() + " (" + player.getPion() + "), you have thrown " + thrownPoints);
				board.set(playerIndex, 9, 9 + thrownPoints, thrownPoints);
				playerIndex = playerIndex == 0 ? 1 : 0;
			}
		} else {
			playerIndex = playerIndex == 0 ? 1 : 0;
			Player player = players.get(playerIndex);
			player.setPion("O");
			
			board.createBoard();
			board.print();
		}
	}
	
	private void playTurn() {
		Player player = players.get(playerIndex);
		
		if (player.getName().equals("Computer")) {
			int pointsThrown = Dice.throwSticks();
			System.out.println(player.getName() + " (" + player.getPion() + "), you have thrown " + pointsThrown);
			
			if (!board.checkifPlayerCanSetAPion(playerIndex, pointsThrown)) {
				System.out.println("The computer can't set a pion! Checking for a backwards turn...");
				
				if (!board.checkifPlayerCanSetAPion(playerIndex, -pointsThrown)) {
					System.out.println("The computer can't set a pion!");
				} else {
					int answer = board.getBestSet(playerIndex, -pointsThrown);
					
					board.set(playerIndex, answer, answer - pointsThrown, -pointsThrown);
				}			
			} else {
				int answer = board.getBestSet(playerIndex, pointsThrown);
				
				System.out.println(player.getName() + " (" + player.getPion() + "), which piece do you want to move? " + answer);
				
				board.set(playerIndex, answer, answer + pointsThrown, pointsThrown);
				
				// Wait 2 seconds. We dont want it to be instant!
				Utils.sleep(2000L);
			}
			
			if (pointsThrown == 2 || pointsThrown == 3) playerIndex = playerIndex == 0 ? 1 : 0;			
			return;
		}		
		
		System.out.println(player.getName() + " (" + player.getPion() + "), press <ENTER> to throw the dice");
		scanner.nextLine();

		int pointsThrown = Dice.throwSticks();
		System.out.println(player.getName() + " (" + player.getPion() + "), you have thrown " + pointsThrown);
		
		if (!board.checkifPlayerCanSetAPion(playerIndex, pointsThrown)) {
			System.out.println("You can't set a pion! Checking for a backwards turn...");
			
			if (!board.checkifPlayerCanSetAPion(playerIndex, -pointsThrown)) {
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
					
					if (board.set(playerIndex, answer, answer - pointsThrown, -pointsThrown)) rightAnswer = true;
				}
			}			
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
				
				if (board.set(playerIndex, answer, answer + pointsThrown, pointsThrown)) rightAnswer = true;
			}
		}
		
		if (pointsThrown == 2 || pointsThrown == 3) playerIndex = playerIndex == 0 ? 1 : 0;
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
	
	private void addPlayer(String name) {
		Player player = new Player(name);

		players.add(player);
	}
	
	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	public void setWinner(Player player) {
		this.winner = player;
	}
	
	public int getTestPosition() {
		return testPosition;
	}
}
