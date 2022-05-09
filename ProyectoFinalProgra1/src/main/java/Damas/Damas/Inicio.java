
package Damas.Damas;

import javax.swing.UIManager;


public class Inicio extends javax.swing.JFrame {
    
		public Inicio(){
		//Set the look and feel to the OS look and feel
		try {
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Create a window to display the checkers game
		VentanaDamas window = new VentanaDamas();
		window.setDefaultCloseOperation(VentanaDamas.EXIT_ON_CLOSE);
		window.setVisible(true);
	}
}
