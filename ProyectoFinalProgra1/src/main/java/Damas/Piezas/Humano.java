/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Damas.Piezas;

public class Humano extends Jugador{
    
    public boolean esHumano() {
		return true;
	}

	/**
	 * Performs no updates on the game. As human players can interact with the
	 * user interface to update the game.
	 */
	@Override
	public void updateGame(Game game) {}
}
