package reworked.tetris;

/**
 * Group of tiles that has a position and a configuration.
 * The position is the top left corner of the configuration.
 * @author Davis
 *
 */
public interface ITileGroup {
	
	Position getPos();
	
	void setPos(Position newPos);
	
	void move(Direction dir, int dist);
	
	ITile[] [] getConfiguration();
	
	int getRows();
	
	int getColumns();
	
	/**
	 * Checks if a tile exists at the position.
	 * @param pos position in local coordinates.
	 * @return if there is a tile at the position.
	 */
	boolean tileAt(Position pos);

}
