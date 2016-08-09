package reworked.tetris;

/**
 * Interface used to represent a specific block i.e. polyomino.
 * The difference between a block and a tile group is the rotation ability.
 * @author Davis
 *
 */
public interface IBlock extends ITileGroup {

	/**
	 * Rotates the block clockwise.
	 */
	void rotateClockwise();
	
	/**
	 * Rotates the block counter clockwise.
	 */
	void rotateCounterClockwise();
	
	/**
	 * Sets the rotation state of the block.
	 * @param rs the new rotation state.
	 */
	void setRotation(RotateState rs);
	
}
