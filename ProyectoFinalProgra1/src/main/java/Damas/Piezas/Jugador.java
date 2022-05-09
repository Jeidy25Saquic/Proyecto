
package Damas.Piezas;

public abstract class Jugador {
    
	 
	public abstract boolean esHumano();
	
	public abstract void updateGame(Game game);
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "[Humano=" + esHumano() + "]";
	}
}
