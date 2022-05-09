/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Damas.Piezas;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import Damas.Movimiento.*;

public class Computadora extends Jugador {
    /** The weight of being able to skip. */
	private static final double WEIGHT_SKIP = 25;
	
	/** The weight of being able to skip on next turn. */
	private static final double SKIP_ON_NEXT = 20;
	
	/** The weight associated with being safe then safe before and after. */
	private static final double SAFE_SAFE = 5;

	/** The weight associated with being safe then unsafe before and after. */
	private static final double SAFE_UNSAFE = -40;

	/** The weight associated with being unsafe then safe before and after. */
	private static final double UNSAFE_SAFE = 40;

	/** The weight associated with being unsafe then unsafe before and after. */
	private static final double UNSAFE_UNSAFE = -40;
	
	/** The weight of a checker being safe. */
	private static final double SAFE = 3;
	
	/** The weight of a checker being unsafe. */
	private static final double UNSAFE = -5;
	
	/** The factor used to multiply some weights when the checker being
	 * observed is a king. */
	private static final double KING_FACTOR = 2;
	/* ------------ */

	@Override
	public boolean esHumano() {
		return false;
	}

	@Override
	public void updateGame(Game game) {
		
		// Nothing to do
		if (game == null || game.isGameOver()) {
			return;
		}
			
		// Get the available moves
		Game copy = game.copy();
		List<Move> moves = getMoves(copy);

		// Determine which one is the best
		int n = moves.size(), count = 1;
		double bestWeight = Move.WEIGHT_INVALID;
		for (int i = 0; i < n; i ++) {
			Move m = moves.get(i);
			getMoveWeight(copy.copy(), m);
			if (m.getWeight() > bestWeight) {
				count = 1;
				bestWeight = m.getWeight();
			} else if (m.getWeight() == bestWeight) {
				count ++;
			}
		}

		// Randomly select a move
		int move = ((int) (Math.random() * count)) % count;
		for (int i = 0; i < n; i ++) {
			Move m = moves.get(i);
			if (bestWeight == m.getWeight()) {
				if (move == 0) {
					game.move(m.getStartIndex(), m.getEndIndex());
				} else {
					move --;
				}
			}
		}
	}
	
	/**
	 * Gets all the available moves and skips for the current player.
	 * 
	 * @param game	the current game state.
	 * @return a list of valid moves that the player can make.
	 */
	private List<Move> getMoves(Game game) {
		
		// The next move needs to be a skip
		if (game.getSkipIndex() >= 0) {
			
			List<Move> moves = new ArrayList<>();
			List<Point> skips = GeneradorMovimiento.getSkips(game.getBoard(),
					game.getSkipIndex());
			for (Point end : skips) {
				moves.add(new Move(game.getSkipIndex(), Tabla.toIndex(end)));
			}
			
			return moves;
		}
		
		// Get the checkers
		List<Point> checkers = new ArrayList<>();
		Tabla b = game.getBoard();
		if (game.isP1Turn()) {
			checkers.addAll(b.find(Tabla.BLACK_CHECKER));
			checkers.addAll(b.find(Tabla.BLACK_KING));
		} else {
			checkers.addAll(b.find(Tabla.WHITE_CHECKER));
			checkers.addAll(b.find(Tabla.WHITE_KING));
		}
		
		// Determine if there are any skips
		List<Move> moves = new ArrayList<>();
		for (Point checker : checkers) {
			int index = Tabla.toIndex(checker);
			List<Point> skips = GeneradorMovimiento.getSkips(b, index);
			for (Point end : skips) {
				Move m = new Move(index, Tabla.toIndex(end));
				m.changeWeight(WEIGHT_SKIP);
				moves.add(m);
			}
		}
		
		// If there are no skips, add the regular moves
		if (moves.isEmpty()) {
			for (Point checker : checkers) {
				int index = Tabla.toIndex(checker);
				List<Point> movesEnds = GeneradorMovimiento.getMoves(b, index);
				for (Point end : movesEnds) {
					moves.add(new Move(index, Tabla.toIndex(end)));
				}
			}
		}
		
		return moves;
	}
	
	/**
	 * Gets the number of skips that can be made in one turn from a given start
	 * index.
	 * 
	 * @param game			the game state to check against.
	 * @param startIndex	the start index of the skips.
	 * @param isP1Turn		the original player turn flag.
	 * @return the maximum number of skips available from the given point.
	 */
	private int getSkipDepth(Game game, int startIndex, boolean isP1Turn) {
		
		// Trivial case
		if (isP1Turn != game.isP1Turn()) {
			return 0;
		}
		
		// Recursively get the depth
		List<Point> skips = GeneradorMovimiento.getSkips(game.getBoard(), startIndex);
		int depth = 0;
		for (Point end : skips) {
			int endIndex = Tabla.toIndex(end);
			game.move(startIndex, endIndex);
			int testDepth = getSkipDepth(game, endIndex, isP1Turn);
			if (testDepth > depth) {
				depth = testDepth;
			}
		}
		
		return depth + (skips.isEmpty()? 0 : 1);
	}
	
	/**
	 * Determines the weight of a move based on a number of factors (e.g. how
	 * safe the checker is before/after, whether it can take an opponents
	 * checker after, etc).
	 * 
	 * @param game	the current game state.
	 * @param m		the move to test.
	 */
	private void getMoveWeight(Game game, Move m) {
		
		Point start = m.getStart(), end = m.getEnd();
		int startIndex = Tabla.toIndex(start), endIndex = Tabla.toIndex(end);
		Tabla b = game.getBoard();
		boolean changed = game.isP1Turn();
		boolean safeBefore = Movimiento.isSafe(b, start);
		int id = b.get(startIndex);
		boolean isKing = (id == Tabla.BLACK_KING || id == Tabla.WHITE_KING);
		
		// Set the initial weight
		m.changeWeight(getSafetyWeight(b, game.isP1Turn()));
		
		// Make the move
		if (!game.move(m.getStartIndex(), m.getEndIndex())) {
			m.setWeight(Move.WEIGHT_INVALID);
			return;
		}
		b = game.getBoard();
		changed = (changed != game.isP1Turn());
		id = b.get(endIndex);
		isKing = (id == Tabla.BLACK_KING || id == Tabla.WHITE_KING);
		boolean safeAfter = true;
		
		// Determine if a skip could be made on next move
		if (changed) {
			safeAfter = Movimiento.isSafe(b, end);
			int depth = getSkipDepth(game, endIndex, !game.isP1Turn());
			if (safeAfter) {
				m.changeWeight(SKIP_ON_NEXT * depth * depth);
			} else {
				m.changeWeight(SKIP_ON_NEXT);
			}
		}
		
		// Check how many more skips are available
		else {
			int depth = getSkipDepth(game, startIndex, game.isP1Turn());
			m.changeWeight(WEIGHT_SKIP * depth * depth);
		}
		
		// Add the weight appropriate to how safe the checker is
		if (safeBefore && safeAfter) {
			m.changeWeight(SAFE_SAFE);
		} else if (!safeBefore && safeAfter) {
			m.changeWeight(UNSAFE_SAFE);
		} else if (safeBefore && !safeAfter) {
			m.changeWeight(SAFE_UNSAFE * (isKing? KING_FACTOR : 1));
		} else {
			m.changeWeight(UNSAFE_UNSAFE);
		}
		m.changeWeight(getSafetyWeight(b,
				changed? !game.isP1Turn() : game.isP1Turn()));
	}
	
	/**
	 * Calculates the 'safety' state of the game for the player specified. The
	 * player has 'safe' and 'unsafe' checkers, which respectively, cannot and
	 * can be skipped by the opponent in the next turn.
	 * 
	 * @param b			the board state to check against.
	 * @param isBlack	the flag indicating if black checkers should be observed.
	 * @return the weight corresponding to how safe the player's checkers are.
	 */
	private double getSafetyWeight(Tabla b, boolean isBlack) {
		
		// Get the checkers
		double weight = 0;
		List<Point> checkers = new ArrayList<>();
		if (isBlack) {
			checkers.addAll(b.find(Tabla.BLACK_CHECKER));
			checkers.addAll(b.find(Tabla.BLACK_KING));
		} else {
			checkers.addAll(b.find(Tabla.WHITE_CHECKER));
			checkers.addAll(b.find(Tabla.WHITE_KING));
		}
		
		// Determine conditions for each checker
		for (Point checker : checkers) {
			int index = Tabla.toIndex(checker);
			int id = b.get(index);
			boolean isKing = (id == Tabla.BLACK_KING || id == Tabla.WHITE_KING);
			if (Movimiento.isSafe(b, checker)) {
				weight += SAFE;
			} else {
				weight += UNSAFE * (isKing? KING_FACTOR : 1);
			}
		}
		
		return weight;
	}
    
}
