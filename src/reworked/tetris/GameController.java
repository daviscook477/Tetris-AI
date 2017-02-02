package reworked.tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import reworked.ai.AI;
import reworked.ai.AISim;

public class GameController {

	/*
	 * Is the game paused?
	 */
	private boolean paused = false;
	
	/*
	 * Is the game running?
	 */
	private boolean running = true;
	
	private Board board;
	
	public static final int ROWS = 20;
	public static final int COLUMNS = 10;
	public static final int DRAW_WIDTH = 20;
	public static final int DRAW_HEIGHT = 20;
	public static final int X_BASE = 100;
	public static final int Y_BASE = 50;
	public static final int DROP_ROWS = 2;
	
	private JFrame frame = new JFrame();
	@SuppressWarnings("serial")
	private JPanel panel = new JPanel() {
		@Override
		public void paintComponent(Graphics g) {
			Graphics2D g2D = (Graphics2D) g;
			ITile[] [] tiles = board.copyTiles();
			// DRAW OUTLINE
			g2D.setColor(Color.BLACK);
			g2D.draw(new Rectangle2D.Float(X_BASE, Y_BASE+DROP_ROWS*(DRAW_HEIGHT+1), (DRAW_WIDTH+1)*COLUMNS, (DRAW_HEIGHT+1)*ROWS));
			// ADD PLAYER TO TILE DRAWING.
			if (board.getPlayer() != null) {
				board.place(board.getPlayer(), tiles);
			}
			// DRAW OUTLINE OF WHERE THE BLOCK IS FALLING TO
			if (board.getPlayer() != null) {
				Position fallPos = board.projectTileGroup(board.getPlayer(), Direction.DOWN);
				if (fallPos != board.getPlayer().getPos()) {
					ITile[] [] draw = board.getPlayer().getConfiguration();
					for (int i = draw.length - 1; i >= 0; i--) {
						for (int j = 0; j < draw[0].length; j++) {
							if (draw[i] [j] != null) {
								draw[i] [j].draw(g2D, X_BASE + (j + fallPos.col)*(DRAW_WIDTH+1), Y_BASE + (board.getNumRows() + i - 1 - fallPos.row)*(DRAW_HEIGHT+1), DRAW_WIDTH, DRAW_HEIGHT, true);
							}
						}
					}
				}
			}
			// DRAW TILES
			for (int i = tiles.length - 1; i >= 0; i--) {
				for (int j = 0; j < tiles[0].length; j++) {
					if (tiles[i] [j] != null) {
						tiles[i] [j].draw(g2D, X_BASE + j*(DRAW_WIDTH+1), Y_BASE + (tiles.length - i - 1)*(DRAW_HEIGHT+1), DRAW_WIDTH, DRAW_HEIGHT, false);
					}
				}
			}
		}
	};
	
	/**
	 * Creates the game controller.
	 */
	public GameController() {
		// Set up the board and the JFrame.
		board = new Board(ROWS, COLUMNS);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(200+COLUMNS*DRAW_WIDTH+40, 200+ROWS*DRAW_HEIGHT+40);
		frame.setResizable(false);
		frame.setTitle("Tetris");
		frame.add(panel);
		frame.setVisible(true);
	}
	
	// TETRIS GAME THINGS
	
	private int linesCleared = 0;
	
	// The number of pieces known to the AI
	// basically its search depth - how many moves in advance
	// does it look
	public static final int PIECES_KNOWN = 2;

	/**
	 * Starts the game.
	 */
	public void startGame() {
		new Runnable() {
			@Override
			public void run() {
				
				// The AI for running the simulation.
				
				// The game is currently modified for running an AI simulation instead of player input.
				
				AI ai = new AI(new float[] {-1.0f,-.25f,10.0f,-20.0f});
				PieceGenerator pg = new PieceGenerator();
				
				//=========================MAIN GAME LOOP========================
			
				int count = 0;
				
				final int counts_per_down = 10;
				
				// Main game loop.
				while (running) {
		
					// Only do things while the game is not paused.
					if (!paused) {
						count++;

						// Only move down every counts_per_down game loops.
						if (count >= counts_per_down) {
							count = 0;
							AISim.newBoard(ai, board, pg, PIECES_KNOWN);
							frame.repaint();
						}
						
						// Check if the player block hit something:
						if (board.getPlayer() == null) {
							// Check for new rows being completed
							int[] full = board.getFullRows();
							if (full.length > 0) {
								// Delete the rows.
								board.clearRows(full);
								linesCleared += full.length;
							}
							
							// If the player lost then stop the game.
							if (board.isLost()) {
								System.out.println("YOU LOST!");
								System.out.println("LINES CLEARED: " + linesCleared);
								setPaused(true);
								break;
							}
							
						}
						System.out.println("lines: " + linesCleared);
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				
				//=====================END MAIN GAME LOOP==============================
				
			}
		}.run();
	}
	
	/**
	 * Sets if the game is paused.
	 * @param pause is the game paused.
	 */
	public void setPaused(boolean pause) {
		this.paused = pause;
	}
	
}
