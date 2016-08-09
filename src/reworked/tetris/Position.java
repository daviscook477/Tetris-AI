package reworked.tetris;

/**
 * Simple class for denoting a position in a matrix of rows and columns.
 * @author Davis
 *
 */
public class Position implements Cloneable {
	
	public Position(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public int row, col;
	
	public Position below(int dist) {
		return new Position(row - dist, col);
	}
	
	public Position above(int dist) {
		return new Position(row + dist, col);
	}
	
	public Position right(int dist) {
		return new Position(row, col + dist);
	}
	
	public Position left(int dist) {
		return new Position(row, col - dist);
	}
	
	@Override
	public Object clone() {
		return new Position(row, col);
	}
	
	@Override
	public String toString() {
		return "row: " + row + " col: " + col;
	}
	
}