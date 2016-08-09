package reworked.ai;

import reworked.tetris.Board;
import reworked.tetris.Direction;
import reworked.tetris.IBlock;
import reworked.tetris.ITile;
import reworked.tetris.PieceGenerator;
import reworked.tetris.Position;
import reworked.tetris.RotateState;
import reworked.tetris.RotatingPolyomino;

public class AISim {

	public static final int NUM_OF_90_DEGREE_ROTATIONS = 4;
	
	public static void placeInBoard(int cols, int rotateState, Board pb, IBlock toPlace) {
		IBlock toCheck = toPlace;
		// Set up the block to check.
		toCheck.setPos(new Position(pb.getNumRows() - 1, cols));
		switch(rotateState) {
		case 0:
			toCheck.setRotation(RotateState.DEG0);
			break;
		case 1:
			toCheck.setRotation(RotateState.DEG90);
			break;
		case 2:
			toCheck.setRotation(RotateState.DEG180);
			break;
		case 3:
			toCheck.setRotation(RotateState.DEG270);
			break;
		}
		Position newPos = pb.projectTileGroup(toCheck, Direction.DOWN);
		toCheck.setPos(newPos);
		pb.place(toCheck, pb.getTiles());
	}
	
	public static void removeFromBoard(int cols, int rotateState, Board pb, IBlock toPlace) {
		IBlock toCheck = toPlace;
		ITile[] [] arrangement = toCheck.getConfiguration();
		// Check each tile in the arrangement to place it.
		for (int i = 0; i < arrangement.length; i++) {
			for (int j = 0; j < arrangement[0].length; j++) {
				if (arrangement[i] [j] != null) {
					Position topLeft = toCheck.getPos();
					pb.setTile(topLeft.row - i, topLeft.col + j, null);
				}
			}
		}
	}
	
	public static Object[] getBest(AI ai, Board pb, IBlock[] toPlaces, int levels) {
		IBlock toPlace = toPlaces[toPlaces.length - levels];
		int bestCol = 0;
		int bestRotate = 0;
		Float bestScore = null;
		for (int i = 0 - toPlace.getConfiguration()[0].length; i < pb.getNumCols(); i++) {
			for (int j = 0; j < NUM_OF_90_DEGREE_ROTATIONS; j++) {
				if (valid(i, j, pb, toPlace)) {
					if (levels > 1) {
						// Place the current one in the board at the specified spot.
						placeInBoard(i, j, pb, toPlace);
						// Recursively calculate what's best.
						Object[] bests = getBest(ai, pb, toPlaces, levels - 1);
						// If it has a better score than the current one, then keep it.
						float bestScoreR = (float) bests[2];
						if (bestScore == null || bestScoreR > bestScore) {
							bestCol = i;
							bestRotate = j;
							bestScore = bestScoreR;
						}
						// Remove the one from the board.
						removeFromBoard(i, j, pb, toPlace);
					// If we're at the base case, just score the board. It will have all of the pieces from the non-base cases.
					} else {
						float caseScore = score(i, j, ai, pb, toPlace);
						if (bestScore == null || caseScore > bestScore) {
							bestScore = caseScore;
							bestCol = i;
							bestRotate = j;
						}
					}
				}
			}
		}
		Object[] ret = new Object[3];
		ret[0] = bestCol;
		ret[1] = bestRotate;
		ret[2] = bestScore;
		return ret;
	}
	
	/**
	 * Calculates the new board state given and ai an the previous board state.
	 * @param ai the ai to calculate the new state with.
	 * @param pb the board's state previously.
	 * @param pg the piece generator used for getting the next piece in the sequence.
	 * @return the new board state.
	 */
	public static void newBoard(AI ai, Board pb, PieceGenerator pg, int knownPieces) {
		// Generate a new piece to place in the board.
		//IBlock toPlace = new RotatingPolyomino(null, pg.takeNextPiece());
		// Generate the next number of known pieces.
		IBlock[] toPlaces = new IBlock[knownPieces];
		// Pull the first piece from the generator and remove it permanently b/c it will be placed this step.
		toPlaces[0] = new RotatingPolyomino(null, pg.takeNextPiece());
		// The rest of the pieces are just going to be looked at, not pulled
		// b/c they will be placed at another step.
		for (int i = 1; i < toPlaces.length; i++) {
			toPlaces[i] = new RotatingPolyomino(null, pg.nextPiece(i-1));
		}
		// Get the best position recursively.
		Object[] bests = getBest(ai, pb, toPlaces, knownPieces);
		int bestCol = (int) bests[0];
		int bestRotate = (int) bests[1];
		toPlaces[0].setPos(new Position(pb.getNumRows() - 1, bestCol));
		switch(bestRotate) {
		case 0:
			toPlaces[0].setRotation(RotateState.DEG0);
			break;
		case 1:
			toPlaces[0].setRotation(RotateState.DEG90);
			break;
		case 2:
			toPlaces[0].setRotation(RotateState.DEG180);
			break;
		case 3:
			toPlaces[0].setRotation(RotateState.DEG270);
			break;
		}
		Position newPos = pb.projectTileGroup(toPlaces[0], Direction.DOWN);
		toPlaces[0].setPos(newPos);
		pb.place(toPlaces[0], pb.getTiles());
		//System.out.println("best move score: " + bests[2]);
		/*
		// Starting best positions.
		int bestCol = 0;
		int bestRotate = 0;
		float bestScore = score(bestCol, bestRotate, ai, pb, toPlace);
		for (int i = 0 - toPlace.getConfiguration()[0].length; i < pb.getNumCols(); i++) {
			for (int j = 0; j < NUM_OF_90_DEGREE_ROTATIONS; j++) {
				if (valid(i, j, pb, toPlace)) {
					float caseScore = score(i, j, ai, pb, toPlace);
					if (caseScore > bestScore) {
						bestScore = caseScore;
						bestCol = i;
						bestRotate = j;
					}
				}
			}
		}
		toPlace.setPos(new Position(pb.getNumRows() - 1, bestCol));
		switch(bestRotate) {
		case 0:
			toPlace.setRotation(RotateState.DEG0);
			break;
		case 1:
			toPlace.setRotation(RotateState.DEG90);
			break;
		case 2:
			toPlace.setRotation(RotateState.DEG180);
			break;
		case 3:
			toPlace.setRotation(RotateState.DEG270);
			break;
		}
		Position newPos = pb.projectTileGroup(toPlace, Direction.DOWN);
		toPlace.setPos(newPos);
		pb.place(toPlace, pb.getTiles());*/
	}
	
	private static boolean valid(int col, int rotate, Board pb, IBlock toPlace) {
		IBlock toCheck = toPlace;
		// Set up the block to check.
		toCheck.setPos(new Position(pb.getNumRows() - 1, col));
		switch(rotate) {
		case 0:
			toCheck.setRotation(RotateState.DEG0);
			break;
		case 1:
			toCheck.setRotation(RotateState.DEG90);
			break;
		case 2:
			toCheck.setRotation(RotateState.DEG180);
			break;
		case 3:
			toCheck.setRotation(RotateState.DEG270);
			break;
		}
		if (pb.canMove(toCheck, Direction.UP, 0)) {
			return true;
		}
		return false;
	}
	
	private static float score(int col, int rotate, AI ai, Board pb, IBlock toPlace) {
		IBlock toCheck = toPlace;
		// Set up the block to check.
		toCheck.setPos(new Position(pb.getNumRows() - 1, col));
		switch(rotate) {
		case 0:
			toCheck.setRotation(RotateState.DEG0);
			break;
		case 1:
			toCheck.setRotation(RotateState.DEG90);
			break;
		case 2:
			toCheck.setRotation(RotateState.DEG180);
			break;
		case 3:
			toCheck.setRotation(RotateState.DEG270);
			break;
		}
		Position newPos = pb.projectTileGroup(toCheck, Direction.DOWN);
		toCheck.setPos(newPos);
		pb.place(toCheck, pb.getTiles());
		float score = ai.scoreOf(pb);
		ITile[] [] arrangement = toCheck.getConfiguration();
		// Check each tile in the arrangement to place it. <- that doesn't make sense. What this really does is removes the tiles from the board.
		for (int i = 0; i < arrangement.length; i++) {
			for (int j = 0; j < arrangement[0].length; j++) {
				if (arrangement[i] [j] != null) {
					Position topLeft = toCheck.getPos();
					pb.setTile(topLeft.row - i, topLeft.col + j, null);
				}
			}
		}
		return score;
	}
	
	
}
