
package Damas.Movimiento;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import Damas.Piezas.*;

public class Movimiento {
    
	public static boolean isValidMove(Game game,	int startIndex, int endIndex) {
		return game == null? false : isValidMove(game.getBoard(),
				game.isP1Turn(), startIndex, endIndex, game.getSkipIndex());
	}
	
	
	 
	public static boolean isValidMove(Tabla board, boolean isP1Turn,
			int startIndex, int endIndex, int skipIndex) {
		
		// Basic checks
		if (board == null || !Tabla.isValidIndex(startIndex) ||
				!Tabla.isValidIndex(endIndex)) {
			return false;
		} else if (startIndex == endIndex) {
			return false;
		} else if (Tabla.isValidIndex(skipIndex) && skipIndex != startIndex) {
			return false;
		}
		
		
		if (!validateIDs(board, isP1Turn, startIndex, endIndex)) {
			return false;
		} else if (!validateDistance(board, isP1Turn, startIndex, endIndex)) {
			return false;
		}
		
		
		return true;
	}
	
	
	private static boolean validateIDs(Tabla board, boolean isP1Turn,
			int startIndex, int endIndex) {
		
		
		if (board.get(endIndex) != Tabla.EMPTY) {
			return false;
		}
		
		
		int id = board.get(startIndex);
		if ((isP1Turn && id != Tabla.BLACK_CHECKER && id != Tabla.BLACK_KING)
				|| (!isP1Turn && id != Tabla.WHITE_CHECKER
				&& id != Tabla.WHITE_KING)) {
			return false;
		}
		
		
		Point middle = Tabla.middle(startIndex, endIndex);
		int midID = board.get(Tabla.toIndex(middle));
		if (midID != Tabla.INVALID && ((!isP1Turn &&
				midID != Tabla.BLACK_CHECKER && midID != Tabla.BLACK_KING) ||
				(isP1Turn && midID != Tabla.WHITE_CHECKER &&
				midID != Tabla.WHITE_KING))) {
			return false;
		}
		
		
		return true;
	}
	
	
	 
	private static boolean validateDistance(Tabla board, boolean isP1Turn,
			int startIndex, int endIndex) {
		
		
		Point start = Tabla.toPoint(startIndex);
		Point end = Tabla.toPoint(endIndex);
		int dx = end.x - start.x;
		int dy = end.y - start.y;
		if (Math.abs(dx) != Math.abs(dy) || Math.abs(dx) > 2 || dx == 0) {
			return false;
		}
		
		
		int id = board.get(startIndex);
		if ((id == Tabla.WHITE_CHECKER && dy > 0) ||
				(id == Tabla.BLACK_CHECKER && dy < 0)) {
			return false;
		}
		
		
		Point middle = Tabla.middle(startIndex, endIndex);
		int midID = board.get(Tabla.toIndex(middle));
		if (midID < 0) {
			
			
			List<Point> checkers;
			if (isP1Turn) {
				checkers = board.find(Tabla.BLACK_CHECKER);
				checkers.addAll(board.find(Tabla.BLACK_KING));
			} else {
				checkers = board.find(Tabla.WHITE_CHECKER);
				checkers.addAll(board.find(Tabla.WHITE_KING));
			}
			
			
			for (Point p : checkers) {
				int index = Tabla.toIndex(p);
				if (!GeneradorMovimiento.getSkips(board, index).isEmpty()) {
					return false;
				}
			}
		}
		
		
		return true;
	}
	
	
	public static boolean isSafe(Tabla board, Point checker) {
		
		
		if (board == null || checker == null) {
			return true;
		}
		int index = Tabla.toIndex(checker);
		if (index < 0) {
			return true;
		}
		int id = board.get(index);
		if (id == Tabla.EMPTY) {
			return true;
		}
		
		
		boolean isBlack = (id == Tabla.BLACK_CHECKER || id == Tabla.BLACK_KING);
		List<Point> check = new ArrayList<>();
		GeneradorMovimiento.addPoints(check, checker, Tabla.BLACK_KING, 1);
		for (Point p : check) {
			int start = Tabla.toIndex(p);
			int tid = board.get(start);
			
			
			if (tid == Tabla.EMPTY || tid == Tabla.INVALID) {
				continue;
			}
			
			boolean isWhite = (tid == Tabla.WHITE_CHECKER ||
					tid == Tabla.WHITE_KING);
			if (isBlack && !isWhite) {
				continue;
			}
			boolean isKing = (tid == Tabla.BLACK_KING || tid == Tabla.BLACK_KING);
			
			
			int dx = (checker.x - p.x) * 2;
			int dy = (checker.y - p.y) * 2;
			if (!isKing && (isWhite ^ (dy < 0))) {
				continue;
			}
			int endIndex = Tabla.toIndex(new Point(p.x + dx, p.y + dy));
			if (GeneradorMovimiento.isValidSkip(board, start, endIndex)) {
				return false;
			}
		}
		
		return true;
	}
}
