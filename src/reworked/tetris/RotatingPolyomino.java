package reworked.tetris;

import java.awt.Color;
import java.util.HashMap;
import java.util.Random;

public class RotatingPolyomino implements IBlock {

	public static final RotateState DEFAULT_ROTATION = RotateState.DEG0;

	
	public static ITile[] [] reverseArrayRows(ITile[] [] arr) {
		ITile[] [] newArr = new ITile[arr.length] [arr[0].length];
		for (int i = 0; i < newArr.length; i++) {
			for (int j = 0; j < newArr[0].length; j++) {
				newArr[i] [j] = arr[arr.length - 1 - i] [j];
			}
		}
		return newArr;
	}
	
	private Position pos;
	
	private RotateState rotateState;
	
	private Polyomino p;
	
	private static HashMap<String, Polyomino> polyominos = new HashMap<String, Polyomino>();
	
	public static String[] tetrominos = new String[] {"I", "O", "T", "J", "L", "S", "Z"};
	
	// Define the tiles that make up each polyomino.
	
	/**
	 * Quick interface for a function that generates tiles.
	 * @author Davis
	 *
	 */
	public interface TileGen {
		ITile genTile();
	}
	
	/**
	 * Returns the tile that composes the I polyomino.
	 * @return the tile that composes the I polyomino.
	 */
	public static TileGen iGen = new TileGen() {
		public ITile genTile() {
			return new ColoredTile() {
				@Override
				public Color getColor() {
					return Color.decode("#00FDFF");
				}
			};
		}
	};
	
	/**
	 * Returns the tile that composes the O polyomino.
	 * @return the tile that composes the O polyomino.
	 */
	private static TileGen oGen = new TileGen() {
		public ITile genTile() {
			return new ColoredTile() {
				@Override
				public Color getColor() {
					return Color.decode("#FFFF00");
				}
			};
		}
	};
	
	/**
	 * Returns the tile that composes the T polyomino.
	 * @return the tile that composes the T polyomino.
	 */
	private static TileGen tGen = new TileGen() {
		public ITile genTile() {
			return new ColoredTile() {
				@Override
				public Color getColor() {
					return Color.decode("#FF00FF");
				}
			};
		}
	};
	
	/**
	 * Returns the tile that composes the J polyomino.
	 * @return the tile that composes the J polyomino.
	 */
	private static TileGen jGen = new TileGen() {
		public ITile genTile() {
			return new ColoredTile() {
				@Override
				public Color getColor() {
					return Color.decode("#0000FF");
				}
			};
		}
	};
	
	/**
	 * Returns the tile that composes the L polyomino.
	 * @return the tile that composes the L polyomino.
	 */
	private static TileGen lGen = new TileGen() {
		public ITile genTile() {
			return new ColoredTile() {
				@Override
				public Color getColor() {
					return Color.decode("#FF8000");
				}
			};
		}
	};
	
	/**
	 * Returns the tile that composes the S polyomino.
	 * @return the tile that composes the S polyomino.
	 */
	private static TileGen sGen = new TileGen() {
		public ITile genTile() {
			return new ColoredTile() {
				@Override
				public Color getColor() {
					return Color.decode("#00FF00");
				}
			};
		}
	};
	
	/**
	 * Returns the tile that composes the Z polyomino.
	 * @return the tile that composes the Z polyomino.
	 */
	private static TileGen zGen = new TileGen() {
		public ITile genTile() {
			return new ColoredTile() {
				@Override
				public Color getColor() {
					return Color.decode("#FF0000");
				}
			};
		}
	};
	
	/**
	 * tilesForSimpleArray. Helper method for making the polyomino generation easier.
	 * @param tGen
	 * @param simp
	 * @return
	 */
	private static ITile[] [] tfsa(TileGen tGen, int[] [] simp) {
		ITile[] [] ret = new ITile[simp.length] [simp[0].length];
		for (int i = 0; i < simp.length; i++) {
			for (int j = 0; j < simp[0].length; j++) {
				if (simp[i] [j] != 0) {
					ret[i] [j] = tGen.genTile();
				} else {
					ret[i] [j] = null;
				}
			}
		}
		return ret; //reverseArrayRows(ret);
	}
	
	// Define all of tetris's polyominos.
	
	static {
		polyominos.put("I", new Polyomino(
			tfsa(iGen, new int[] [] {
				{0,0,0,0},
				{1,1,1,1},
				{0,0,0,0},
				{0,0,0,0}
			}),
			tfsa(iGen, new int[] [] {
				{0,0,1,0},
				{0,0,1,0},
				{0,0,1,0},
				{0,0,1,0}
			}),
			tfsa(iGen, new int[] [] {
				{0,0,0,0},
				{0,0,0,0},
				{1,1,1,1},
				{0,0,0,0}
			}),
			tfsa(iGen, new int[] [] {
				{0,0,1,0},
				{0,0,1,0},
				{0,0,1,0},
				{0,0,1,0}
			})
		));
		polyominos.put("O", new Polyomino(
			tfsa(oGen, new int[] [] {
				{1,1},
				{1,1}
			}),
			tfsa(oGen, new int[] [] {
				{1,1},
				{1,1}
			}),
			tfsa(oGen, new int[] [] {
				{1,1},
				{1,1}
			}),
			tfsa(oGen, new int[] [] {
				{1,1},
				{1,1}
			})
		));
		polyominos.put("T", new Polyomino(
			tfsa(tGen, new int[] [] {
				{0,1,0},
				{1,1,1},
				{0,0,0}
			}),
			tfsa(tGen, new int[] [] {
				{0,1,0},
				{0,1,1},
				{0,1,0}
			}),
			tfsa(tGen, new int[] [] {
				{0,0,0},
				{1,1,1},
				{0,1,0}
			}),
			tfsa(tGen, new int[] [] {
				{0,1,0},
				{1,1,0},
				{0,1,0}
			})
		));
		polyominos.put("J", new Polyomino(
			tfsa(jGen, new int[] [] {
				{1,0,0},
				{1,1,1},
				{0,0,0}
			}),
			tfsa(jGen, new int[] [] {
				{0,1,1},
				{0,1,0},
				{0,1,0}
			}),
			tfsa(jGen, new int[] [] {
				{0,0,0},
				{1,1,1},
				{0,0,1}
			}),
			tfsa(jGen, new int[] [] {
				{0,1,0},
				{0,1,0},
				{1,1,0}
			})
		));
		polyominos.put("L", new Polyomino(
			tfsa(lGen, new int[] [] {
				{0,0,1},
				{1,1,1},
				{0,0,0}
			}),
			tfsa(lGen, new int[] [] {
				{0,1,0},
				{0,1,0},
				{0,1,1}
			}),
			tfsa(lGen, new int[] [] {
				{0,0,0},
				{1,1,1},
				{1,0,0}
			}),
			tfsa(lGen, new int[] [] {
				{1,1,0},
				{0,1,0},
				{0,1,0}
			})
		));
		polyominos.put("S", new Polyomino(
			tfsa(sGen, new int[] [] {
				{0,1,1},
				{1,1,0},
				{0,0,0}
			}),
			tfsa(sGen, new int[] [] {
				{0,1,0},
				{0,1,1},
				{0,0,1}
			}),
			tfsa(sGen, new int[] [] {
				{0,0,0},
				{0,1,1},
				{1,1,0}
			}),
			tfsa(sGen, new int[] [] {
				{1,0,0},
				{1,1,0},
				{0,1,0}
			})
		));
		polyominos.put("Z", new Polyomino(
				tfsa(zGen, new int[] [] {
					{1,1,0},
					{0,1,1},
					{0,0,0}
				}),
				tfsa(zGen, new int[] [] {
					{0,0,1},
					{0,1,1},
					{0,1,0}
				}),
				tfsa(zGen, new int[] [] {
					{0,0,0},
					{1,1,0},
					{0,1,1}
				}),
				tfsa(zGen, new int[] [] {
					{0,1,0},
					{1,1,0},
					{1,0,0}
				})
			));
	}
	
	/**
	 * Creates a random polyomino at the specified position.
	 * @param pos the position to create it at.
	 * @return the random polyomino.
	 */
	public static RotatingPolyomino randomPolyomino(Position pos) {
		Random r = new Random();
		int index = r.nextInt(RotatingPolyomino.tetrominos.length);
		String polyominoName = RotatingPolyomino.tetrominos[index];
		return new RotatingPolyomino(pos, polyominoName);
	}
	
	public RotatingPolyomino(Position pos, String polyomino) {
		this.pos = pos;
		this.rotateState = DEFAULT_ROTATION;
		this.p = RotatingPolyomino.polyominos.get(polyomino);
	}
	
	@Override
	public Position getPos() {
		return pos;
	}

	@Override
	public void setPos(Position newPos) {
		pos = newPos;
	}

	@Override
	public void move(Direction dir, int dist) {
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
		pos = newPos;
	}

	@Override
	public ITile[][] getConfiguration() {
		// Ignore break statement b/c each case returns.
		switch(rotateState) {
		case DEG0:
			return p.getArr0();
		case DEG90:
			return p.getArr90();
		case DEG180:
			return p.getArr180();
		case DEG270:
			return p.getArr270();
		}
		return null;
	}
	
	@Override
	public int getRows() {
		return getConfiguration().length;
	}
	
	@Override
	public int getColumns() {
		return getConfiguration()[0].length;
	}

	@Override
	public boolean tileAt(Position pos) {
		return (getConfiguration()[pos.row] [pos.col] != null);
	}

	@Override
	public void rotateClockwise() {
		switch(rotateState) {
		case DEG0:
			rotateState = RotateState.DEG90;
			break;
		case DEG90:
			rotateState = RotateState.DEG180;
			break;
		case DEG180:
			rotateState = RotateState.DEG270;
			break;
		case DEG270:
			rotateState = RotateState.DEG0;
			break;
		}
	}

	@Override
	public void rotateCounterClockwise() {
		switch(rotateState) {
		case DEG0:
			rotateState = RotateState.DEG270;
			break;
		case DEG90:
			rotateState = RotateState.DEG0;
			break;
		case DEG180:
			rotateState = RotateState.DEG90;
			break;
		case DEG270:
			rotateState = RotateState.DEG180;
			break;
		}
	}
	
	@Override
	public void setRotation(RotateState rs) {
		this.rotateState = rs;
	}
	
}
