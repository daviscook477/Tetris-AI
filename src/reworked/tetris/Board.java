package reworked.tetris;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {

	/*
	 * The block being controlled by the player.
	 */
	private IBlock playerBlock = null;
	
	/*
	 * The stationary tiles that are already in place.
	 */
	private ITile[] [] stationaryTiles;
	
	/*
	 * The position from which new blocks are dropped.
	 */
	private Position dropBlocksFromHere;
	
	/**
	 * Create a new board.
	 * @param rows the amount of rows for the board.
	 * @param columns the amount of columns for the board.
	 */
	public Board(int rows, int columns) {
		stationaryTiles = new ITile[rows + GameController.DROP_ROWS] [columns];
		// Blocks will be dropped from top center.
		dropBlocksFromHere = new Position(rows - 1  + GameController.DROP_ROWS, columns / 2);
	}
	
	/**
	 * Spawns a new block for the player to control.
	 */
	public void spawnNewPlayerBlock() {
		IBlock b = RotatingPolyomino.randomPolyomino(dropBlocksFromHere);
		b.setPos(b.getPos().left(b.getColumns() / 2));
		playerBlock = b;
	}
	
	/**
	 * Spawns a new block for the player to control.
	 * @param type the type of block to spawns
	 */
	public void spawnNewPlayerBlock(String type) {
		IBlock b = new RotatingPolyomino(dropBlocksFromHere, type);
		b.setPos(b.getPos().left(b.getColumns() / 2));
		playerBlock = b;
	}
	
	/**
	 * Please don't modify the tile array.
	 */
	public ITile[] [] getTiles() {
		return stationaryTiles;
	}
	
	public int getNumRows() {
		return stationaryTiles.length;
	}
	
	public int getNumCols() {
		return stationaryTiles[0].length;
	}
	
	public boolean isLost() {
		boolean lost = false;
		for (int i = getNumRows() - 1; i > getNumRows() - 1 - GameController.DROP_ROWS; i--) {
			for (int j = 0; j < getNumCols(); j++) {
				if (stationaryTiles[i] [j] != null) {
					lost = true;
				}
			}
		}
		return lost;
	}
	
	public void destroyTile(int rows, int cols) {
		ITile t = stationaryTiles[rows] [cols];
		if (t != null) {
			t.destroy(this, new Position(rows, cols));
		}
		stationaryTiles[rows] [cols] = null;
	}
	
	public void setTile(int rows, int cols, ITile newT) {
		ITile t = stationaryTiles[rows] [cols];
		if (t != null) {
			t.destroy(this, new Position(rows, cols));
		}
		if (newT != null) {
			newT.create(this, new Position(rows, cols));
		}
		stationaryTiles[rows] [cols] = newT;
	}
	
	// TODO: implement careful rotation.
	public IBlock getPlayer() {
		return playerBlock;
	}
	
	/**
	 * Determines which rows are currently completely full of tiles.
	 * @return the row numbers of each row that is full in an array with the numbers in increasing order.
	 */
	public int[] getFullRows() {
		ArrayList<Integer> fullRows = new ArrayList<Integer>();
		for (int i = 0; i < stationaryTiles.length; i++) {
			boolean full = true;
			for (int j = 0; j < stationaryTiles[0].length; j++) {
				if (stationaryTiles[i] [j] == null) {
					full = false;
				}
			}
			if (full) {
				fullRows.add(i);
			}
		}
		int[] rowsFull = new int[fullRows.size()];
		for (int i = 0; i < fullRows.size(); i++) {
			rowsFull[i] = fullRows.get(i);
		}
		return rowsFull;
	}
	
	/**
	 * Clears the given rows and drops down the pieces above them.
	 * @param rowsToClear
	 */
	public void clearRows(int[] rowsToClear) {
		// Remove each row sequentially.
		for (int i = 0; i < rowsToClear.length; i++) {
			// Remove the row.
			List<ITile[]> rowsAsList = new ArrayList<ITile[]>(Arrays.asList(stationaryTiles));
			// Remove the row at the index specified by the rowsToClear array.
			// It must be modified by - i b/c i number of rows have been
			// deleted from the array underneath it already.
			int indexToRemove = rowsToClear[i]-i;
			for (int j = 0; j < getNumCols(); j++) {
				// Fire a destroy event for the cleared tiles.
				stationaryTiles[indexToRemove] [j].destroy(this, new Position(indexToRemove, j));
			}
			rowsAsList.remove(indexToRemove);
			rowsAsList.add(new ITile[getNumCols()]);
			stationaryTiles = rowsAsList.toArray(new ITile[] [] {});
			// TODO: make the blocks above fall down in some sane manner. (or don't).
		}
	}
	
	/**
	 * Returns the position that a tile group would be in if it went as far as possible in a direction.
	 * @param tg the tile group to project.
	 * @param dir the direction in which to project.
	 * @return the position that a tile group would be in if it went as far as possible in a direction.
	 */
	public Position projectTileGroup(ITileGroup tg, Direction dir) {
		boolean stop = false;
		int bestDist = 0;
		while (!stop) {
			boolean canMove = canMove(tg, dir, bestDist + 1);
			if (canMove) {
				bestDist++;
			} else {
				stop = true;
			}
		}
		Position pos = tg.getPos();
		Position newPos = null;
		if (dir == Direction.DOWN) {
			newPos = pos.below(bestDist);
		} else if (dir == Direction.UP) {
			newPos = pos.above(bestDist);
		} else if (dir == Direction.LEFT) {
			newPos = pos.left(bestDist);
		} else if (dir == Direction.RIGHT) {
			newPos = pos.right(bestDist);
		}
		return newPos;
	}
	
	/**
	 * Returns if a position would be inside the board.
	 * @param pos the position to check.
	 * @return if the positions would be inside the board.
	 */
	public boolean insideBounds(Position pos) {
		if (pos.row >= 0 && pos.col >= 0 && pos.row < stationaryTiles.length &&
			pos.col < stationaryTiles[0].length) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns if a position contains a tile.
	 * @param pos the position to check.
	 * @return if the position contains a tile.
	 */
	public boolean tileAt(Position pos) {
		if (!insideBounds(pos)) {
			return false;
		} else {
			return (stationaryTiles[pos.row] [pos.col] != null);
		}
	}
	
	/**
	 * Moves the player's block downwards.
	 */
	public void movePlayerDown() {
		if (playerBlock != null) {
			boolean placed = moveTileGroup(playerBlock, Direction.DOWN, 1, OnHit.STATIONARY);
			// If the block was placed.
			if (placed) {
				// Fire a creation event for each tile in it.
				ITile[][] tiles = playerBlock.getConfiguration();
				for (int i = 0; i < tiles.length; i++) {
					for (int j = 0; j < tiles[0].length; j++) {
						if (tiles[i] [j] != null) {
							tiles[i] [j].create(this, new Position(i, j));
						}
					}
				}
				playerBlock = null;
			}
		}
	}
	
	/**
	 * Can a tile group move in the direction a specified distance?
	 * @param tg the tile group that's movement will be checked.
	 * @param dir the direction in which the tile group's movement is being checked.
	 * @param dist the distance that the tile group desires to move.
	 * @return if the tile group can perform the movement.
	 */
	public boolean canMove(ITileGroup tg, Direction dir, int dist) {
		// Start by assuming that it can move.
		boolean can = true;
		ITile[] [] arrangement = tg.getConfiguration();
		// Check each tile in the arrangement to see if it would conflict with a tile
		// in the stationary list.
		for (int i = 0; i < arrangement.length; i++) {
			for (int j = 0; j < arrangement[0].length; j++) {
				// Only consider non-null tiles in the arrangement.
				if (arrangement[i] [j] != null) {
					Position topLeft = tg.getPos();
					Position pos = new Position(topLeft.row - i, topLeft.col + j);
					Position newPos = null;
					if (dir == Direction.DOWN) {
						newPos = pos.below(dist);
					} else if (dir == Direction.UP) {
						newPos = pos.above(dist);
					} else if (dir == Direction.LEFT) {
						newPos = pos.left(dist);
					} else if (dir == Direction.RIGHT) {
						newPos = pos.right(dist);
					}
					// If it can't move for any tile in the arrangement, then it cannot move.
					if (!insideBounds(newPos) || tileAt(newPos)) {
						can = false;
					}
				}
			}
		}
		return can;
	}
	
	/**
	 * Moves a tile group.
	 * @param tg the tile group to move.
	 * @param dir the direction in which to move it.
	 * @param dist the distance to move it.
	 * @param oh the desired reaction to the block reaching a collision.
	 * @return if the tile group was turned stationary.
	 */
	public boolean moveTileGroup(ITileGroup tg, Direction dir, int dist, OnHit oh) {
		boolean can = canMove(tg, dir, dist);
		// If the block can be moved all the way.
		if (can) {
			tg.move(dir, dist);
		// If the tile group cannot be moved all the way and
		// the tile group should become stationary on movement being impossible.
		} else if (oh == OnHit.STATIONARY){
			// Place the tiles in the stationary array.
			place(tg, stationaryTiles);
			return true;
		}
		return false;
	}
	
	/**
	 * Rotates a block clockwise or counterclockwise.
	 * @param block the block to rotate.
	 * @param clockwise rotate clockwise if true, counterclockwise if false.
	 * @return if the block successfully rotated.
	 */
	public boolean rotateBlock(IBlock block, boolean clockwise) {
		// Rotate the clock.
		if (clockwise) {
			block.rotateClockwise();
		} else {
			block.rotateCounterClockwise();
		}
		// If the block can exist in the rotated state.
		if (canMove(block, Direction.UP, 0)) {
			// Keep it that way and return true.
			return true;
		// If the block cannot exist in the rotated state,
		// rotate it back and then return false.
		} else {
			if (clockwise) {
				block.rotateCounterClockwise();
			} else {
				block.rotateClockwise();
			}
			return false;
		}
	}
	
	/**
	 * Places a tile group into the stationary array.
	 * @param tg the tile group to be placed.
	 * @param placeIn the tile array to place the group in.
	 */
	public void place(ITileGroup tg, ITile[] [] placeIn) {
		ITile[] [] arrangement = tg.getConfiguration();
		// Check each tile in the arrangement to place it.
		for (int i = 0; i < arrangement.length; i++) {
			for (int j = 0; j < arrangement[0].length; j++) {
				if (arrangement[i] [j] != null) {
					Position topLeft = tg.getPos();
					placeIn[topLeft.row - i] [topLeft.col + j] = arrangement[i] [j];
				}
			}
		}
	}
	
	/**
	 * Makes a copy of the tiles.
	 * @return a copy of the tiles.
	 */
	public ITile[] [] copyTiles() {
		ITile[] [] back = new ITile[stationaryTiles.length] [stationaryTiles[0].length];
		for (int i = 0; i < back.length; i++) {
			for (int j = 0; j < back[0].length; j++) {
				back[i] [j] = stationaryTiles[i] [j];
			}
		}
		return back;
	}
	
	@Override
	public String toString() {
		String ret = "";
		ITile[] [] back = copyTiles();
		if (playerBlock != null) {
			place(playerBlock, back);
		}
		for (int i = 0; i < back.length; i++) {
			for (int j = 0; j < back[0].length; j++) {
				if (back[i] [j] == null) {
					System.out.print("-");
				} else {
					System.out.print("a");
				}
			}
			System.out.println();
		}
		return ret;
	}
	
}