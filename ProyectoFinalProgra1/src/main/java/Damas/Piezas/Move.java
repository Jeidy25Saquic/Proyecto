
package Damas.Piezas;


import java.awt.Point;



public class Move {
	
	
	public static final double WEIGHT_INVALID = Double.NEGATIVE_INFINITY;

	
	private byte startIndex;
	
	
	private byte endIndex;
	
	
	private double weight;
	
	public Move(int startIndex, int endIndex) {
		setStartIndex(startIndex);
		setEndIndex(endIndex);
	}
	
	public Move(Point start, Point end) {
		setStartIndex(Tabla.toIndex(start));
		setEndIndex(Tabla.toIndex(end));
	}
	
	public int getStartIndex() {
		return startIndex;
	}
	
	public void setStartIndex(int startIndex) {
		this.startIndex = (byte) startIndex;
	}
	
	public int getEndIndex() {
		return endIndex;
	}
	
	public void setEndIndex(int endIndex) {
		this.endIndex = (byte) endIndex;
	}
	
	public Point getStart() {
		return Tabla.toPoint(startIndex);
	}
	
	public void setStart(Point start) {
		setStartIndex(Tabla.toIndex(start));
	}
	
	public Point getEnd() {
		return Tabla.toPoint(endIndex);
	}
	
	public void setEnd(Point end) {
		setEndIndex(Tabla.toIndex(end));
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	public void changeWeight(double delta) {
		this.weight += delta;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "[startIndex=" + startIndex + ", "
				+ "endIndex=" + endIndex + ", weight=" + weight + "]";
	}}