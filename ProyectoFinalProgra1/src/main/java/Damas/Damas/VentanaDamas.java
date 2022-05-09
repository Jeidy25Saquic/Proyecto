
package Damas.Damas;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import Damas.Piezas.*;

public class VentanaDamas extends  JFrame{
   
	
	/*Ventana por defecto del juego. */
	public static final int DEFAULT_WIDTH = 500;
	public static final int DEFAULT_HEIGHT = 600;
	
	
	public static final String DEFAULT_TITLE = "JUEGO DE DAMAS";
	
	
	private DamasTabla board;
	
	private PanelOp opts;
	
	
	
	public VentanaDamas() {
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_TITLE);
	}
	
	public VentanaDamas (Jugador player1, Jugador player2) {
		this();
		setPlayer1(player1);
		setPlayer2(player2);
	}
	
	public VentanaDamas(int width, int height, String title) {
		
		// Setup the window
		super(title);
		super.setSize(width, height);
		super.setLocationByPlatform(true);
		
		// Setup the components
		JPanel layout = new JPanel(new BorderLayout());
		this.board = new DamasTabla(this);
		this.opts = new PanelOp(this);
		layout.add(board, BorderLayout.CENTER);
		layout.add(opts, BorderLayout.SOUTH);
		this.add(layout);
               
		layout.setBackground(Color.cyan);
		
	}
	
	public DamasTabla getBoard() {
		return board;
	}

	
	public void setPlayer1(Jugador player1) {
		this.board.setPlayer1(player1);
		this.board.update();
	}
	
	
	public void setPlayer2(Jugador player2) {
		this.board.setPlayer2(player2);
		this.board.update();
	}
	
	public void restart() {
		this.board.getGame().restart();
		this.board.update();
	}
	
	public void setGameState(String state) {
		this.board.getGame().setGameState(state);
	}
	
	
}
