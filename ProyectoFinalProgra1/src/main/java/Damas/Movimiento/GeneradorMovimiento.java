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
   
	public static List<Point> getMoves(Tabla board, Point start) {
		return getMoves(board, Tabla.toIndex(start));
	}
	
	
	public static List<Point> getMoves(Tabla board, int startIndex) {
		
		
		List<Point> endPoints = new ArrayList<>();
		if (board == null || !Tabla.isValidIndex(startIndex)) {
			return endPoints;
		}
		
		
		int id = board.get(startIndex);
		Point p = Tabla.toPoint(startIndex);
		addPoints(endPoints, p, id, 1);
		
		
		for (int i = 0; i < endPoints.size(); i ++) {
			Point end = endPoints.get(i);
			if (board.get(end.x, end.y) != Tabla.EMPTY) {
				endPoints.remove(i --);
			}
		}
		
		return endPoints;
	}
	
	
	 
	public static List<Point> getSkips(Tabla board, Point start) {
		return getSkips(board, Tabla.toIndex(start));
	}
	
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
	
	
	public static boolean isValidSkip(Tabla board,
			int startIndex, int endIndex) {
		
		if (board == null) {
			return false;
		}

		
		if (board.get(endIndex) != Tabla.EMPTY) {
			return false;
		}
		
		
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
	
	
	
	public static void addPoints(List<Point> points, Point p, int id, int delta) {
		
		
		boolean isKing = (id == Tabla.BLACK_KING || id == Tabla.WHITE_KING);
		if (isKing || id == Tabla.BLACK_CHECKER) {
			points.add(new Point(p.x + delta, p.y + delta));
			points.add(new Point(p.x - delta, p.y + delta));
		}
		
		
		if (isKing || id == Tabla.WHITE_CHECKER) {
			points.add(new Point(p.x + delta, p.y - delta));
			points.add(new Point(p.x - delta, p.y - delta));
		}
	}
    
}
