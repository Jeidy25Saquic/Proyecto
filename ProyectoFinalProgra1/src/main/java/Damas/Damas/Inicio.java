
package Damas.Damas;

import javax.swing.UIManager;


public class Inicio extends javax.swing.JFrame {
    
		public Inicio(){
		
		try {
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		VentanaDamas window = new VentanaDamas();
		window.setDefaultCloseOperation(VentanaDamas.EXIT_ON_CLOSE);
		window.setVisible(true);
	}
}
