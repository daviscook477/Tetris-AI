package reworked.tetris;

import java.awt.Graphics2D;

public interface ITile {
	
	/**
	 * Draws the tile.
	 * @param g2D graphics 2D with which to draw the tile
	 * @param x x-coordinate to begin drawing at.
	 * @param y y-coordinate to begin drawing at.
	 * @param width the width of the tile to draw.
	 * @param height the height of the tile to draw.
	 * @param transparent if the drawing should be done slightly transparent.
	 */
	void draw(Graphics2D g2D, float x, float y, float width, float height, boolean transparent);

	/**
	 * Called upon the creation of the tile.
	 * @param b the board state directly before the tile is placed in it.
	 */
	void create(Board b, Position pos);
	
	/**
	 * Called every game tick.
	 * @param b the current board state during this tick.
	 */
	void tick(Board b, Position pos);
	
	/**
	 * Called directly before this tile is destroyed.
	 * @param b the current board state directly before being destroyed.
	 */
	void destroy(Board b, Position pos);
	
}
