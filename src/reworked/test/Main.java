package reworked.test;

import reworked.tetris.Board;
import reworked.tetris.GameController;

public class Main {
	public static void main(String[] args) {
		Board b = new Board(8, 10);
		b.spawnNewPlayerBlock();
		GameController gc = new GameController();
		gc.startGame(); // starts the AI simulation
	}
}
