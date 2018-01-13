package net.marijn.senet.game.board;

import java.util.ArrayList;

public class Board {

	private ArrayList<Square> squares;

	public Board() {
		this.squares = new ArrayList<>();

		for (int i = 0; i < 30; i++) {
			String pion = ".";

			if (i < 10 && i % 2 == 0)
				pion = "O";
			else if (i < 10 && !(i % 2 == 0))
				pion = "X";

			squares.add(new Square(pion));
		}
	}
	
	public Square getSquare(int index) {
		int rightIndex = index - 1;
		
		return squares.get(rightIndex);
	}
	
	public void set(Square oldPlace, Square newPlace) {
		String newPion = newPlace.getPion();
		String oldPion = oldPlace.getPion();
		
		newPlace.setPion(oldPion);
		oldPlace.setPion(newPion);
		
		print();
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
