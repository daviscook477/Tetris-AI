package reworked.test;

import reworked.tetris.ITile;
import reworked.tetris.Board;
import reworked.tetris.GameController;

public class Main {
	public static void main(String[] args) {
		Board b = new Board(8, 10);
		b.spawnNewPlayerBlock();
		GameController gc = new GameController();
		gc.startGame();
	}
	
	public static void print(ITile[] [] arr) {
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr.length; j++) {
				if (arr[i] [j] == null) {
					System.out.print("-");
				} else {
					System.out.print("a");
				}
			}
			System.out.println();
		}
		System.out.println("===============================");
	}
}
