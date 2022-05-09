/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Damas.Movimiento;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import Damas.Piezas.*;
public class GeneradorMovimiento {
    /* Gets a list of move end-points for a given start index.
	 * 
	 * @param board	the board to look for available moves.
	 * @param start	the center index to look for moves around.
	 * @return the list of points such that the start to a given point
	 * represents a move available.
	 * @see {@link #getMoves(Board, int)}
	 */
	public static List<Point> getMoves(Tabla board, Point start) {
		return getMoves(board, Tabla.toIndex(start));
	}
	
	/**
	 * Gets a list of move end-points for a given start index.
	 * 
	 * @param board			the board to look for available moves.
	 * @param startIndex	the center index to look for moves around.
	 * @return the list of points such that the start to a given point
	 * represents a move available.
	 * @see {@link #getMoves(Board, Point)}
	 */
	public static List<Point> getMoves(Tabla board, int startIndex) {
		
		// Trivial cases
		List<Point> endPoints = new ArrayList<>();
		if (board == null || !Tabla.isValidIndex(startIndex)) {
			return endPoints;
		}
		
		// Determine possible points
		int id = board.get(startIndex);
		Point p = Tabla.toPoint(startIndex);
		addPoints(endPoints, p, id, 1);
		
		// Remove invalid points
		for (int i = 0; i < endPoints.size(); i ++) {
			Point end = endPoints.get(i);
			if (board.get(end.x, end.y) != Tabla.EMPTY) {
				endPoints.remove(i --);
			}
		}
		
		return endPoints;
	}
	
	/**
	 * Gets a list of skip end-points for a given starting point.
	 * 
	 * @param board	the board to look for available skips.
	 * @param start	the center index to look for skips around.
	 * @return the list of points such that the start to a given point
	 * represents a skip available.
	 * @see {@link #getSkips(Board, int)}
	 */
	public static List<Point> getSkips(Tabla board, Point start) {
		return getSkips(board, Tabla.toIndex(start));
	}
	
	/**
	 * Gets a list of skip end-points for a given start index.
	 * 
	 * @param board			the board to look for available skips.
	 * @param startIndex	the center index to look for skips around.
	 * @return the list of points such that the start to a given point
	 * represents a skip available.
	 * @see {@link #getSkips(Board, Point)}
	 */
	public static List<Point> getSkips(Tabla board, int startIndex) {
		
		// Trivial cases
		List<Point> endPoints = new ArrayList<>();
		if (board == null || !Tabla.isValidIndex(startIndex)) {
			return endPoints;
		}
		
		// Determine possible points
		int id = board.get(startIndex);
		Point p = Tabla.toPoint(startIndex);
		addPoints(endPoints, p, id, 2);
		
		// Remove invalid points
		for (int i = 0; i < endPoints.size(); i ++) {
			
			// Check that the skip is valid
			Point end = endPoints.get(i);
			if (!isValidSkip(board, startIndex, Tabla.toIndex(end))) {
				endPoints.remove(i --);
			}
		}

		return endPoints;
	}
	
	/**
	 * Checks if a skip is valid.
	 * 
	 * @param board			the board to check against.
	 * @param startIndex	the start index of the skip.
	 * @param endIndex		the end index of the skip.
	 * @return true if and only if the skip can be performed.
	 */
	public static boolean isValidSkip(Tabla board,
			int startIndex, int endIndex) {
		
		if (board == null) {
			return false;
		}

		// Check that end is empty
		if (board.get(endIndex) != Tabla.EMPTY) {
			return false;
		}
		
		// Check that middle is enemy
		int id = board.get(startIndex);
		int midID = board.get(Tabla.toIndex(Tabla.middle(startIndex, endIndex)));
		if (id == Tabla.INVALID || id == Tabla.EMPTY) {
			return false;
		} else if (midID == Tabla.INVALID || midID == Tabla.EMPTY) {
			return false;
		} else if ((midID == Tabla.BLACK_CHECKER || midID == Tabla.BLACK_KING)
				^ (id == Tabla.WHITE_CHECKER || id == Tabla.WHITE_KING)) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Adds points that could potentially result in moves/skips.
	 * 
	 * @param points	the list of points to add to.
	 * @param p			the center point.
	 * @param id		the ID at the center point.
	 * @param delta		the amount to add/subtract.
	 */
	public static void addPoints(List<Point> points, Point p, int id, int delta) {
		
		// Add points moving down
		boolean isKing = (id == Tabla.BLACK_KING || id == Tabla.WHITE_KING);
		if (isKing || id == Tabla.BLACK_CHECKER) {
			points.add(new Point(p.x + delta, p.y + delta));
			points.add(new Point(p.x - delta, p.y + delta));
		}
		
		// Add points moving up
		if (isKing || id == Tabla.WHITE_CHECKER) {
			points.add(new Point(p.x + delta, p.y - delta));
			points.add(new Point(p.x - delta, p.y - delta));
		}
	}
    
}
