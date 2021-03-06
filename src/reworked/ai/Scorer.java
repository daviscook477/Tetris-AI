package reworked.ai;

import reworked.tetris.Board;
import reworked.tetris.ITile;

/**
 * Class that has a bunch of static methods
 * for scoring the state of a board of tetris.
 * These scores are used as a heuristic for
 * selecting the "best" move by using the move
 * that results in a new board state that has the
 * lowest score.
 * @author Davis
 *
 */
public class Scorer {

	/**
	 * Get the heights of the each column in the board, i.e.
	 * how high the tiles are stacked in that column
	 * @param b the board
	 * @return array of heights
	 */
	public static int[] heights(Board b) {
		ITile[] [] tiles = b.getTiles();
		int[] heights = new int[b.getNumCols()];
		for (int i = 0; i < heights.length; i++) {
			heights[i] = 0;
		}
		// Iterate over all columns
		for (int j = 0; j < b.getNumCols(); j++) {
			// Start from the top of the column and go down.
			for (int i = b.getNumRows() - 1; i >= 0; i--) {
				// If the space is empty.
				if (tiles[i] [j] == null) {
					// Increase the known height of the column.
					heights[j] = heights[j] + 1;
				// If the space is full, the height is now known,
				// so break this loop.
				} else {
					break;
				}
			}
		}
		// That procedure actually found the inverse of the height.
		// Next, we invert it to get the actual height.
		for (int i = 0; i < heights.length; i++) {
			heights[i] = b.getNumRows() - heights[i];
		}
		return heights;
	}
	
	/**
	 * Gets average height of the board
	 * @param b the board
	 * @return average height
	 */
	public static float averageHeight(Board b) {
		int[] heights = heights(b);
		float total = 0;
		for (int i = 0; i < heights.length; i++) {
			total += heights[i];
		}
		return total / heights.length;
	}
	
	/**
	 * Gets total height of the board
	 * @param b the board
	 * @return total height
	 */
	public static int totalHeight(Board b) {
		int[] heights = heights(b);
		int total = 0;
		for (int i = 0; i < heights.length; i++) {
			total += heights[i];
		}
		return total;
	}
	
	
	public static int totalHeightWeightedByAltitude(Board b) {
		int[] heights = heights(b);
		int total = 0;
		for (int i = 0; i < heights.length; i++) {
			total += (heights[i])*(heights[i]+1)*(2*heights[i]+1)/6;
		}
		return total;
	}
	
	/**
	 * Gets the number of cells that are filled in the board
	 * @param b the board
	 * @return number of filled cells
	 */
	public static int numCells(Board b) {
		ITile[] [] tiles = b.getTiles();
		int num = 0;
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				if (tiles[i] [j] != null) {
					num++;
				}
			}
		}
		return num;
	}
	
	/**
	 * Gets the standard deviation of the heights of the columns in the board
	 * @param b the board
	 * @return standard deviation of board heights
	 */
	public static float standardDev(Board b) {
		int[] heights = heights(b);
		float avg = averageHeight(b);
		float sum = 0.0f;
		for (int i = 0; i < heights.length; i++) {
			sum += (heights[i]-avg)*(heights[i]-avg);
		}
		return (float) Math.sqrt(sum / (b.getNumCols() - 1));
	}
	
	/**
	 * Gets the total amount of tiles that are filled in above holes in the board
	 * @param b the board
	 * @return total numbers of tiles that are filled in above holes
	 */
	public static int numHeightAboveHoles(Board b) {
		int[] heights = new int[b.getNumCols()];
		for (int i = 0; i < heights.length; i++) {
			heights[i] = heightAboveHolePerColumn(b, i);
		}
		int sum = 0;
		for (int i = 0; i < heights.length; i++) {
			sum += heights[i];
		}
		return sum;
	}

	/**
	 * Gets the number of holes in the board.
	 * A hole is considered an empty tile that has filled in tiles above it
	 * @param b the board
	 * @return the numbers of holes
	 */
	public static int numHoles(Board b) {
		int holes = 0;
		for (int i = 0; i < b.getNumCols(); i++) {
			holes += numHolesPerColumn(b, i);
		}
		return holes;
	}
	
	public static int numColumnsWithHoles(Board b) {
		int cols = 0;
		for (int i = 0; i < b.getNumCols(); i++) {
			if (numHolesPerColumn(b, i) > 0) {
				cols++;
			}
		}
		return cols;
	}
	
	public static int heightAboveHolePerColumn(Board b, int col) {
		ITile[] [] tiles = b.getTiles();
		// If we're counting the height above a hole.
		boolean counting = false;
		int height = 0;
		// Start from the top of the column and go down.
		for (int i = b.getNumRows() - 1; i >= 0; i--) {
			// If there exists and actual tile, we must be counting and the number of height above the hole must increase by one.
			if (tiles[i] [col] != null) {
				counting = true;
				height++;
			}
			// If we are counting height above holes and we find the whole, we've found the total height.
			if (counting && tiles[i] [col] == null) {
				break;
			}
			// If this is the last iteration and no hole has been found.
			if (i - 1 < 0) {
				height = 0; // Reset the height to zero because there wasn't any hole to count height above.
			}
		}
		return height;
	}
	
	private static int numHolesPerColumn(Board b, int col) {
		// If we're counting the height above a hole.
		boolean counting = false;
		int holes = 0;
		ITile[] [] tiles = b.getTiles();
		// Start from the top of the column and go down.
		for (int i = b.getNumRows() - 1; i >= 0; i--) {
			// If there exists and actual tile, we must be counting and the number of height above the hole must increase by one.
			if (tiles[i] [col] != null) {
				counting = true;
			}
			// If we are counting height above holes and we find the whole, we've found the total height.
			if (counting && tiles[i] [col] == null) {
				holes++;
			}
		}
		return holes;
	}
	
	
	public static int fullRows(Board b) {
		return b.getFullRows().length;
	}
	
	
}
