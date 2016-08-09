package reworked.tetris;

import java.awt.Color;

public class ColoredColumnExplosionTile extends ColoredTile {

	@Override
	public Color getColor() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void create(Board b, Position pos) {
		int col = pos.col;
		int rows = b.getNumRows();
		for (int i = 0; i < rows; i++) {
			b.destroyTile(i, col);
		}
	}

}
