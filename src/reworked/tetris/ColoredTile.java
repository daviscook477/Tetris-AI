package reworked.tetris;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public abstract class ColoredTile implements ITile {

	public abstract Color getColor();
	
	@Override
	public void draw(Graphics2D g2D, float x, float y, float width, float height, boolean transparent) {
		// Fill with color.
		Color c = getColor();
		int alpha;
		if (transparent) {
			alpha = 60;
		} else {
			alpha = 255;
		}
		g2D.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha));
		g2D.fill(new Rectangle2D.Float(x, y, width + 1, height + 1));
		// Outline.
		g2D.setColor(Color.BLACK);
		g2D.draw(new Rectangle2D.Float(x, y, width + 1, height + 1));
	}
	
	@Override
	public void create(Board b, Position pos) {
		// Do nothing on creation.
	}

	@Override
	public void tick(Board b, Position pos) {
		// Do nothing on ticks.
	}

	@Override
	public void destroy(Board b, Position pos) {
		// TODO: generate some neat colored particles on destruction.
	}

}
