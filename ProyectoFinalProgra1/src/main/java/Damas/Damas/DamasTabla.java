
package Damas.Damas;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.Timer;
import Damas.Piezas.*;
import Damas.Movimiento.*;
public class DamasTabla  extends JButton  {
 
	/** Para inicio de la computadora. */
	private static final int TIMER_DELAY = 1000;
	
	/** Relleno de la tabla. */
	private static final int PADDING = 16;
	private Game game;
	private VentanaDamas window;
	
	/** Para la fichas negras. */
	private Jugador player1;
	
	/** Fichas Blancas. */
	private  Jugador player2;
	private Point selected;
	private boolean selectionValid;
	
	/** color de el cuadro . */
	private Color lightTile;

	/** color oscuro del cuadro. */
	private Color darkTile;
	
	
	private boolean isGameOver;
	
	/** control de movimientos computadora */
	private Timer timer;
	
	public DamasTabla(VentanaDamas window) {
		this(window, new Game(), null, null);
	}
	
	public DamasTabla(VentanaDamas window, Game game,
			Jugador player1, Jugador  player2) {
		
		
		super.setBorderPainted(false);
		super.setFocusPainted(false);
		super.setContentAreaFilled(false);
		super.setBackground(Color.LIGHT_GRAY);
		this.addActionListener(new ClickListener());
		
		
		this.game = (game == null)? new Game() : game;
		this.lightTile = Color.WHITE;
		this.darkTile = Color.BLACK;
		this.window = window;
		setPlayer1(player1);
		setPlayer2(player2);
	}
	
	/**
	 * actualizar los graficos.
	 */
	public void update() {
		runPlayer();
		this.isGameOver = game.isGameOver();
		repaint();
	}
	
	private void runPlayer() {
		
		
		Jugador player = getCurrentPlayer();
		
		this.timer = new Timer(TIMER_DELAY, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				getCurrentPlayer().updateGame(game);
				timer.stop();
				
				update();
			}
		});
		this.timer.start();
	}
	
	
	
	public synchronized boolean setGameState(boolean testValue,
			String newState, String expected) {
		
		
		if (testValue && !game.getGameState().equals(expected)) {
			return false;
		}
		
		
		this.game.setGameState(newState);
		repaint();
		
		return true;
	}
	
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		Game game = this.game.copy();
		
		// Hacer los calculos
		final int BOX_PADDING = 4;
		final int W = getWidth(), H = getHeight();
		final int DIM = W < H? W : H, BOX_SIZE = (DIM - 2 * PADDING) / 8;
		final int OFFSET_X = (W - BOX_SIZE * 8) / 2;
		final int OFFSET_Y = (H - BOX_SIZE * 8) / 2;
		final int CHECKER_SIZE = Math.max(0, BOX_SIZE - 2 * BOX_PADDING);
		
		// Draw checker board
		g.setColor(Color.BLACK);
		g.drawRect(OFFSET_X - 1, OFFSET_Y - 1, BOX_SIZE * 8 + 1, BOX_SIZE * 8 + 1);
		g.setColor(lightTile);
		g.fillRect(OFFSET_X, OFFSET_Y, BOX_SIZE * 8, BOX_SIZE * 8);
		g.setColor(darkTile);
		for (int y = 0; y < 8; y ++) {
			for (int x = (y + 1) % 2; x < 8; x += 2) {
				g.fillRect(OFFSET_X + x * BOX_SIZE, OFFSET_Y + y * BOX_SIZE,
						BOX_SIZE, BOX_SIZE);
			}
		}
		
		// Verificar si la casilla es valida
		if (Tabla.isValidPoint(selected)) {
			g.setColor(selectionValid? Color.GREEN : Color.RED);
			g.fillRect(OFFSET_X + selected.x * BOX_SIZE,
					OFFSET_Y + selected.y * BOX_SIZE,
					BOX_SIZE, BOX_SIZE);
		}
		
		// Dibujar las damas
		Tabla b = game.getBoard();
		for (int y = 0; y < 8; y ++) {
			int cy = OFFSET_Y + y * BOX_SIZE + BOX_PADDING;
			for (int x = (y + 1) % 2; x < 8; x += 2) {
				int id = b.get(x, y);
				
				// Empty, just skip
				if (id == Tabla.EMPTY) {
					continue;
				}
				
				int cx = OFFSET_X + x * BOX_SIZE + BOX_PADDING;
				
				// Black checker
				if (id == Tabla.BLACK_CHECKER) {
					g.setColor(Color.DARK_GRAY);
					g.fillOval(cx + 1, cy + 2, CHECKER_SIZE, CHECKER_SIZE);
					g.setColor(Color.LIGHT_GRAY);
					g.drawOval(cx + 1, cy + 2, CHECKER_SIZE, CHECKER_SIZE);
					g.setColor(Color.BLACK);
					g.fillOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
					g.setColor(Color.LIGHT_GRAY);
					g.drawOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
				}
				
				// rey negro
				else if (id == Tabla.BLACK_KING) {
					g.setColor(Color.DARK_GRAY);
					g.fillOval(cx + 1, cy + 2, CHECKER_SIZE, CHECKER_SIZE);
					g.setColor(Color.LIGHT_GRAY);
					g.drawOval(cx + 1, cy + 2, CHECKER_SIZE, CHECKER_SIZE);
					g.setColor(Color.DARK_GRAY);
					g.fillOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
					g.setColor(Color.LIGHT_GRAY);
					g.drawOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
					g.setColor(Color.BLACK);
					g.fillOval(cx - 1, cy - 2, CHECKER_SIZE, CHECKER_SIZE);
				}
				
				// rey blanco
				else if (id == Tabla.WHITE_CHECKER) {
					g.setColor(Color.LIGHT_GRAY);
					g.fillOval(cx + 1, cy + 2, CHECKER_SIZE, CHECKER_SIZE);
					g.setColor(Color.DARK_GRAY);
					g.drawOval(cx + 1, cy + 2, CHECKER_SIZE, CHECKER_SIZE);
					g.setColor(Color.WHITE);
					g.fillOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
					g.setColor(Color.DARK_GRAY);
					g.drawOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
				}
				
				// rey blanco
				else if (id == Tabla.WHITE_KING) {
					g.setColor(Color.LIGHT_GRAY);
					g.fillOval(cx + 1, cy + 2, CHECKER_SIZE, CHECKER_SIZE);
					g.setColor(Color.DARK_GRAY);
					g.drawOval(cx + 1, cy + 2, CHECKER_SIZE, CHECKER_SIZE);
					g.setColor(Color.LIGHT_GRAY);
					g.fillOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
					g.setColor(Color.DARK_GRAY);
					g.drawOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
					g.setColor(Color.WHITE);
					g.fillOval(cx - 1, cy - 2, CHECKER_SIZE, CHECKER_SIZE);
				}
				
				
				if (id == Tabla.BLACK_KING || id == Tabla.WHITE_KING) {
					g.setColor(new Color(255, 240,0));
					g.drawOval(cx - 1, cy - 2, CHECKER_SIZE, CHECKER_SIZE);
					g.drawOval(cx + 1, cy, CHECKER_SIZE - 4, CHECKER_SIZE - 4);
				}
			}
		}
		
		// dibuar el turno
		String msg = game.isP1Turn()? "TURNO NEGRAS " : "TRUNO BLANCAS";
		int width = g.getFontMetrics().stringWidth(msg);
		Color back = game.isP1Turn()? Color.BLACK : Color.WHITE;
		Color front = game.isP1Turn()? Color.WHITE : Color.BLACK;
		g.setColor(back);
		g.fillRect(W / 2 - width / 2 - 5, OFFSET_Y + 8 * BOX_SIZE + 2,
				width + 10, 15);
		g.setColor(front);
		g.drawString(msg, W / 2 - width / 2, OFFSET_Y + 8 * BOX_SIZE + 2 + 11);
		
		// Draw a game over sign
		if (isGameOver) {
			g.setFont(new Font("Arial", Font.BOLD, 20));
			msg = "Game Over!";
			width = g.getFontMetrics().stringWidth(msg);
			g.setColor(new Color(240, 240, 255));
			g.fillRoundRect(W / 2 - width / 2 - 5,
					OFFSET_Y + BOX_SIZE * 4 - 16,
					width + 10, 30, 10, 10);
			g.setColor(Color.RED);
			g.drawString(msg, W / 2 - width / 2, OFFSET_Y + BOX_SIZE * 4 + 7);
                        
                        
                        
                        
		}
                
               
	}
	
	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = (game == null)? new Game() : game;
	}

	public VentanaDamas getWindow() {
		return window;
	}

	public void setWindow(VentanaDamas window) {
		this.window = window;
	}

	public Jugador getPlayer1() {
		return player1;
	}

	public void setPlayer1(Jugador player1) {
		this.player1 = (player1 == null)? new Humano() : player1;
		if (game.isP1Turn() && !this.player1.esHumano()) {
			this.selected = null;
		}
	}

	public Jugador getPlayer2() {
		return player2;
	}

	public void setPlayer2(Jugador player2) {
		this.player2 = (player2 == null)? new Humano() : player2;
		if (!game.isP1Turn() && !this.player2.esHumano()) {
			this.selected = null;
		}
	}
	
	public Jugador getCurrentPlayer() {
		return game.isP1Turn()? player1 : player2;
	}

	public Color getLightTile() {
		return lightTile;
	}

	public void setLightTile(Color lightTile) {
		this.lightTile = (lightTile == null)? Color.WHITE : lightTile;
	}

	public Color getDarkTile() {
		return darkTile;
	}

	public void setDarkTile(Color darkTile) {
		this.darkTile = (darkTile == null)? Color.BLACK : darkTile;
	}

	/**
	 * se actualiza el punto y se intenta un movimiento si el último clic y este
*          ambos están en mosaicos negros.
	 */
	private void handleClick(int x, int y) {
		
		
		if (isGameOver || !getCurrentPlayer().esHumano()) {
			return;
		}
		
		Game copy = game.copy();
		
		// Determine what square (if any) was selected
		final int W = getWidth(), H = getHeight();
		final int DIM = W < H? W : H, BOX_SIZE = (DIM - 2 * PADDING) / 8;
		final int OFFSET_X = (W - BOX_SIZE * 8) / 2;
		final int OFFSET_Y = (H - BOX_SIZE * 8) / 2;
		x = (x - OFFSET_X) / BOX_SIZE;
		y = (y - OFFSET_Y) / BOX_SIZE;
		Point sel = new Point(x, y);
		
		// Determine if a move should be attempted
		if (Tabla.isValidPoint(sel) && Tabla.isValidPoint(selected)) {
			boolean change = copy.isP1Turn();
			String expected = copy.getGameState();
			boolean move = copy.move(selected, sel);
			boolean updated = (move?
					setGameState(true, copy.getGameState(), expected) : false);
			if (updated) {
				
			}
			change = (copy.isP1Turn() != change);
			this.selected = change? null : sel;
		} else {
			this.selected = sel;
		}
		
		// Check if the selection is valid
		this.selectionValid = isValidSelection(
				copy.getBoard(), copy.isP1Turn(), selected);
		
		update();
	}
	
	// verifica que sea el turno correcto del jugador
	private boolean isValidSelection(Tabla b, boolean isP1Turn, Point selected) {

		// Trivial cases
		int i = Tabla.toIndex(selected), id = b.get(i);
		if (id == Tabla.EMPTY || id == Tabla.INVALID) { // no checker here
			return false;
		} else if(isP1Turn ^ (id == Tabla.BLACK_CHECKER ||
				id == Tabla.BLACK_KING)) { // wrong checker
			return false;
		} else if (!GeneradorMovimiento.getSkips(b, i).isEmpty()) { // skip available
			return true;
		} else if (GeneradorMovimiento.getMoves(b, i).isEmpty()) { // no moves
			return false;
		}
		
		// Determina si es valido saltar sobre la ficha 
		List<Point> points = b.find(
				isP1Turn? Tabla.BLACK_CHECKER : Tabla.WHITE_CHECKER);
		points.addAll(b.find(
				isP1Turn? Tabla.BLACK_KING : Tabla.WHITE_KING));
		for (Point p : points) {
			int checker = Tabla.toIndex(p);
			if (checker == i) {
				continue;
			}
			if (!GeneradorMovimiento.getSkips(b, checker).isEmpty()) {
				return false;
			}
		}

		return true;
	}


	private class ClickListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			
			Point m = DamasTabla.this.getMousePosition();
			if (m != null) {
				handleClick(m.x, m.y);
			}
		}
	}
    
}
