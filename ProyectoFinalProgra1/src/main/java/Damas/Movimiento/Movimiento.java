/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Damas.Movimiento;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import Damas.Piezas.*;

public class Movimiento {
    /**
	 * Determines if the specified move is valid based on the rules of checkers.
	 * 
	 * @param game			the game to check against.
	 * @param startIndex	the start index of the move.
	 * @param endIndex		the end index of the move.
	 * @return true if the move is legal according to the rules of checkers.
	 * @see {@link #isValidMove(Board, boolean, int, int, int)}
	 */
	public static boolean isValidMove(Game game,	int startIndex, int endIndex) {
		return game == null? false : isValidMove(game.getBoard(),
				game.isP1Turn(), startIndex, endIndex, game.getSkipIndex());
	}
	
	/**
	 * Determines if the specified move is valid based on the rules of checkers.
	 * 
	 * @param board			the current board to check against.
	 * @param isP1Turn		the flag indicating if it is player 1's turn.
	 * @param startIndex	the start index of the move.
	 * @param endIndex		the end index of the move.
	 * @param skipIndex		the index of the last skip this turn.
	 * @return true if the move is legal according to the rules of checkers.
	 * @see {@link #isValidMove(Game, int, int)}
	 */
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
		
		// Perform the tests to validate the move
		if (!validateIDs(board, isP1Turn, startIndex, endIndex)) {
			return false;
		} else if (!validateDistance(board, isP1Turn, startIndex, endIndex)) {
			return false;
		}
		
		// Passed all tests
		return true;
	}
	
	/**
	 * Validates all ID related values for the start, end, and middle (if the
	 * move is a skip).
	 * 
	 * @param board			the current board to check against.
	 * @param isP1Turn		the flag indicating if it is player 1's turn.
	 * @param startIndex	the start index of the move.
	 * @param endIndex		the end index of the move.
	 * @return true if and only if all IDs are valid.
	 */
	private static boolean validateIDs(Tabla board, boolean isP1Turn,
			int startIndex, int endIndex) {
		
		// Check if end is clear
		if (board.get(endIndex) != Tabla.EMPTY) {
			return false;
		}
		
		// Check if proper ID
		int id = board.get(startIndex);
		if ((isP1Turn && id != Tabla.BLACK_CHECKER && id != Tabla.BLACK_KING)
				|| (!isP1Turn && id != Tabla.WHITE_CHECKER
				&& id != Tabla.WHITE_KING)) {
			return false;
		}
		
		// Check the middle
		Point middle = Tabla.middle(startIndex, endIndex);
		int midID = board.get(Tabla.toIndex(middle));
		if (midID != Tabla.INVALID && ((!isP1Turn &&
				midID != Tabla.BLACK_CHECKER && midID != Tabla.BLACK_KING) ||
				(isP1Turn && midID != Tabla.WHITE_CHECKER &&
				midID != Tabla.WHITE_KING))) {
			return false;
		}
		
		// Passed all tests
		return true;
	}
	
	/**
	 * Checks that the move is diagonal and magnitude 1 or 2 in the correct
	 * direction. If the magnitude is not 2 (i.e. not a skip), it checks that
	 * no skips are available by other checkers of the same player.
	 * 
	 * @param board			the current board to check against.
	 * @param isP1Turn		the flag indicating if it is player 1's turn.
	 * @param startIndex	the start index of the move.
	 * @param endIndex		the end index of the move.
	 * @return true if and only if the move distance is valid.
	 */
	private static boolean validateDistance(Tabla board, boolean isP1Turn,
			int startIndex, int endIndex) {
		
		// Check that it was a diagonal move
		Point start = Tabla.toPoint(startIndex);
		Point end = Tabla.toPoint(endIndex);
		int dx = end.x - start.x;
		int dy = end.y - start.y;
		if (Math.abs(dx) != Math.abs(dy) || Math.abs(dx) > 2 || dx == 0) {
			return false;
		}
		
		// Check that it was in the right direction
		int id = board.get(startIndex);
		if ((id == Tabla.WHITE_CHECKER && dy > 0) ||
				(id == Tabla.BLACK_CHECKER && dy < 0)) {
			return false;
		}
		
		// Check that if this is not a skip, there are none available
		Point middle = Tabla.middle(startIndex, endIndex);
		int midID = board.get(Tabla.toIndex(middle));
		if (midID < 0) {
			
			// Get the correct checkers
			List<Point> checkers;
			if (isP1Turn) {
				checkers = board.find(Tabla.BLACK_CHECKER);
				checkers.addAll(board.find(Tabla.BLACK_KING));
			} else {
				checkers = board.find(Tabla.WHITE_CHECKER);
				checkers.addAll(board.find(Tabla.WHITE_KING));
			}
			
			// Check if any of them have a skip available
			for (Point p : checkers) {
				int index = Tabla.toIndex(p);
				if (!GeneradorMovimiento.getSkips(board, index).isEmpty()) {
					return false;
				}
			}
		}
		
		// Passed all tests
		return true;
	}
	
	/**
	 * Checks if the specified checker is safe (i.e. the opponent cannot skip
	 * the checker).
	 * 
	 * @param board		the current board state.
	 * @param checker	the point where the test checker is located at.
	 * @return true if and only if the checker at the point is safe.
	 */
	public static boolean isSafe(Tabla board, Point checker) {
		
		// Trivial cases
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
		
		// Determine if it can be skipped
		boolean isBlack = (id == Tabla.BLACK_CHECKER || id == Tabla.BLACK_KING);
		List<Point> check = new ArrayList<>();
		GeneradorMovimiento.addPoints(check, checker, Tabla.BLACK_KING, 1);
		for (Point p : check) {
			int start = Tabla.toIndex(p);
			int tid = board.get(start);
			
			// Nothing here
			if (tid == Tabla.EMPTY || tid == Tabla.INVALID) {
				continue;
			}
			
			// Check ID
			boolean isWhite = (tid == Tabla.WHITE_CHECKER ||
					tid == Tabla.WHITE_KING);
			if (isBlack && !isWhite) {
				continue;
			}
			boolean isKing = (tid == Tabla.BLACK_KING || tid == Tabla.BLACK_KING);
			
			// Determine if valid skip direction
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
