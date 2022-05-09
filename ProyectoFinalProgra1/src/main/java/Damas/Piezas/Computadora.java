
package Damas.Piezas;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import Damas.Movimiento.*;

public class Computadora extends Jugador {
   
	private static final double WEIGHT_SKIP = 25;
	private static final double SKIP_ON_NEXT = 20;
	private static final double SAFE_SAFE = 5;
	private static final double SAFE_UNSAFE = -40;
	private static final double UNSAFE_SAFE = 40;
	private static final double UNSAFE_UNSAFE = -40;
	private static final double SAFE = 3;
	private static final double UNSAFE = -5;
	private static final double KING_FACTOR = 2;

	@Override
	public boolean esHumano() {
		return false;
	}

	@Override
	public void updateGame(Game game) {
		
		
		if (game == null || game.isGameOver()) {
			return;
		}
			
		
		Game copy = game.copy();
		List<Move> moves = getMoves(copy);

		
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
	
	
	private List<Move> getMoves(Game game) {
		
		
		if (game.getSkipIndex() >= 0) {
			
			List<Move> moves = new ArrayList<>();
			List<Point> skips = GeneradorMovimiento.getSkips(game.getBoard(),
					game.getSkipIndex());
			for (Point end : skips) {
				moves.add(new Move(game.getSkipIndex(), Tabla.toIndex(end)));
			}
			
			return moves;
		}
		
		List<Point> checkers = new ArrayList<>();
		Tabla b = game.getBoard();
		if (game.isP1Turn()) {
			checkers.addAll(b.find(Tabla.BLACK_CHECKER));
			checkers.addAll(b.find(Tabla.BLACK_KING));
		} else {
			checkers.addAll(b.find(Tabla.WHITE_CHECKER));
			checkers.addAll(b.find(Tabla.WHITE_KING));
		}
		

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
	
		
	private void getMoveWeight(Game game, Move m) {
		
		Point start = m.getStart(), end = m.getEnd();
		int startIndex = Tabla.toIndex(start), endIndex = Tabla.toIndex(end);
		Tabla b = game.getBoard();
		boolean changed = game.isP1Turn();
		boolean safeBefore = Movimiento.isSafe(b, start);
		int id = b.get(startIndex);
		boolean isKing = (id == Tabla.BLACK_KING || id == Tabla.WHITE_KING);
		
		
		m.changeWeight(getSafetyWeight(b, game.isP1Turn()));
		
		
		if (!game.move(m.getStartIndex(), m.getEndIndex())) {
			m.setWeight(Move.WEIGHT_INVALID);
			return;
		}
		b = game.getBoard();
		changed = (changed != game.isP1Turn());
		id = b.get(endIndex);
		isKing = (id == Tabla.BLACK_KING || id == Tabla.WHITE_KING);
		boolean safeAfter = true;
		
		
		if (changed) {
			safeAfter = Movimiento.isSafe(b, end);
			int depth = getSkipDepth(game, endIndex, !game.isP1Turn());
			if (safeAfter) {
				m.changeWeight(SKIP_ON_NEXT * depth * depth);
			} else {
				m.changeWeight(SKIP_ON_NEXT);
			}
		}
		

		else {
			int depth = getSkipDepth(game, startIndex, game.isP1Turn());
			m.changeWeight(WEIGHT_SKIP * depth * depth);
		}
		
		
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
	
	
	private double getSafetyWeight(Tabla b, boolean isBlack) {
		
		
		double weight = 0;
		List<Point> checkers = new ArrayList<>();
		if (isBlack) {
			checkers.addAll(b.find(Tabla.BLACK_CHECKER));
			checkers.addAll(b.find(Tabla.BLACK_KING));
		} else {
			checkers.addAll(b.find(Tabla.WHITE_CHECKER));
			checkers.addAll(b.find(Tabla.WHITE_KING));
		}
		
		
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
