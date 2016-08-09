package reworked.ai;

import reworked.tetris.Board;

public class AI {

	private float[] weights;
	
	public AI(float[] weights) {
		this.weights = weights;
	}
	
	/**
	 * Returns the score of a board.
	 * @param b the board to score.
	 * @return this ai's scoring of the board.
	 */
	public float scoreOf(Board b) {
		float score = 0;
		score += Scorer.totalHeightWeightedByAltitude(b) * weights[0];
		score += Scorer.standardDev(b) * weights[1];
		score += Scorer.fullRows(b) * weights[2];
		score += Scorer.numHeightAboveHoles(b) * weights[3];
		return score;
	}
	
}
