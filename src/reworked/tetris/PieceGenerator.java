package reworked.tetris;

import java.util.ArrayList;
import java.util.Random;

public class PieceGenerator {

	private ArrayList<String> upcoming;
	
	private ArrayList<String> upcoming2;
	
	public PieceGenerator() {
		upcoming = genUpcoming();
		upcoming2 = genUpcoming();
	}
	
	/**
	 * Generates a random ordering of the possible tetrominos as the next blocks to fall.
	 * @return
	 */
	private ArrayList<String> genUpcoming() {
		Random r = new Random();
		ArrayList<String> selected = new ArrayList<String>(RotatingPolyomino.tetrominos.length);
		ArrayList<Integer> possibles = new ArrayList<Integer>(RotatingPolyomino.tetrominos.length);
		for (int i = 0; i < RotatingPolyomino.tetrominos.length; i++) {
			possibles.add(i);
		}
		while (possibles.size() > 0) {
			int index = r.nextInt(possibles.size());
			selected.add(RotatingPolyomino.tetrominos[possibles.get(index)]);
			possibles.remove(index);
		}
		return selected;
	}
	
	/**
	 * Shows the next piece that is ahead places ahead. 0 returns the next piece.
	 * @param ahead
	 * @return
	 */
	public String nextPiece(int ahead) {
		if (ahead >= upcoming.size()) {
			return upcoming2.get(ahead - upcoming.size());
		}
		return upcoming.get(ahead);
	}
	
	public String takeNextPiece() {
		String ret = nextPiece(0);
		upcoming.remove(0);
		if (upcoming.size() <= 0) {
			upcoming = upcoming2;
			upcoming2 = genUpcoming();
		}
		return ret;
	}
	
}
