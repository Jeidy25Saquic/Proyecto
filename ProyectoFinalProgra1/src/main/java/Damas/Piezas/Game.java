
package Damas.Piezas;
import java.awt.Point;
import java.util.List;

import Damas.Movimiento.*;

public class Game {
  
	private Tabla board;
	
	
	private boolean isP1Turn;
	
	
	private int skipIndex;
	
	public Game() {
		restart();
	}
	
	public Game(String state) {
		setGameState(state);
	}
	
	public Game(Tabla board, boolean isP1Turn, int skipIndex) {
		this.board = (board == null)? new Tabla() : board;
		this.isP1Turn = isP1Turn;
		this.skipIndex = skipIndex;
	}
	
	
	public Game copy() {
		Game g = new Game();
		g.board = board.copy();
		g.isP1Turn = isP1Turn;
		g.skipIndex = skipIndex;
		return g;
	}
	
	
	public void restart() {
		this.board = new Tabla();
		this.isP1Turn = true;
		this.skipIndex = -1;
	}
	
	
	public boolean move(Point start, Point end) {
		if (start == null || end == null) {
			return false;
		}
		return move(Tabla.toIndex(start), Tabla.toIndex(end));
	}
	
	
	public boolean move(int startIndex, int endIndex) {
		
		
		if (!Movimiento.isValidMove(this, startIndex, endIndex)) {
			return false;
		}
		
		
		Point middle = Tabla.middle(startIndex, endIndex);
		int midIndex = Tabla.toIndex(middle);
		this.board.set(endIndex, board.get(startIndex));
		this.board.set(midIndex, Tabla.EMPTY);
		this.board.set(startIndex, Tabla.EMPTY);
		
		
		Point end = Tabla.toPoint(endIndex);
		int id = board.get(endIndex);
		boolean switchTurn = false;
		if (end.y == 0 && id == Tabla.WHITE_CHECKER) {
			this.board.set(endIndex, Tabla.WHITE_KING);
			switchTurn = true;
		} else if (end.y == 7 && id == Tabla.BLACK_CHECKER) {
			this.board.set(endIndex, Tabla.BLACK_KING);
			switchTurn = true;
		}
		
		
		boolean midValid = Tabla.isValidIndex(midIndex);
		if (midValid) {
			this.skipIndex = endIndex;
		}
		if (!midValid || GeneradorMovimiento.getSkips(
				board.copy(), endIndex).isEmpty()) {
			switchTurn = true;
		}
		if (switchTurn) {
			this.isP1Turn = !isP1Turn;
			this.skipIndex = -1;
		}
		
		return true;
	}
	
	
	public Tabla getBoard() {
		return board.copy();
	}
	
	
	public boolean isGameOver() {

		// Ensure there is at least one of each checker
		List<Point> black = board.find(Tabla.BLACK_CHECKER);
		black.addAll(board.find(Tabla.BLACK_KING));
		if (black.isEmpty()) {
			return true;
		}
		List<Point> white = board.find(Tabla.WHITE_CHECKER);
		white.addAll(board.find(Tabla.WHITE_KING));
		if (white.isEmpty()) {
			return true;
		}
		
		// Check that the current player can move
		List<Point> test = isP1Turn? black : white;
		for (Point p : test) {
			int i = Tabla.toIndex(p);
			if (!GeneradorMovimiento.getMoves(board, i).isEmpty() ||
					!GeneradorMovimiento.getSkips(board, i).isEmpty()) {
				return false;
			}
		}
		
		// No moves
		return true;
	}
	
	public boolean isP1Turn() {
		return isP1Turn;
	}
	
	public void setP1Turn(boolean isP1Turn) {
		this.isP1Turn = isP1Turn;
	}
	
	public int getSkipIndex() {
		return skipIndex;
	}
	
	
	public String getGameState() {
		
		
		String state = "";
		for (int i = 0; i < 32; i ++) {
			state += "" + board.get(i);
		}
		
		
		state += (isP1Turn? "1" : "0");
		state += skipIndex;
		
		return state;
	}
	
	
	public void setGameState(String state) {
		
		restart();
		
		
		if (state == null || state.isEmpty()) {
			return;
		}
		
		
		int n = state.length();
		for (int i = 0; i < 32 && i < n; i ++) {
			try {
				int id = Integer.parseInt("" + state.charAt(i));
				this.board.set(i, id);
			} catch (NumberFormatException e) {}
		}
		
		
		if (n > 32) {
			this.isP1Turn = (state.charAt(32) == '1');
		}
		if (n > 33) {
			try {
				this.skipIndex = Integer.parseInt(state.substring(33));
			} catch (NumberFormatException e) {
				this.skipIndex = -1;
			}
		}
	}}