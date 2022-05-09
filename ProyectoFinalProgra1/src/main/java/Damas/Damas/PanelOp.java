
package Damas.Damas;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import Damas.Piezas.*;
import java.awt.BorderLayout;
import MenuPrincipal.MenuPrincipal;
public class PanelOp extends JPanel {
  

	
	private VentanaDamas window;
	
	
	private JButton restartBtn;
	
	
	private JComboBox<String> player1Opts;
	
	private JButton player1Btn;

	
	private JComboBox<String> player2Opts;
	private JButton player2Btn;
        private JButton regresar;
         private JButton Reportes;
          
	
	public PanelOp(VentanaDamas window) {
		super(new GridLayout(0, 1));
		
		this.window = window;
		
		
		OptionListener ol = new OptionListener();
		final String[] playerTypeOpts = {"Persona", "Computadora"};
		this.restartBtn = new JButton("Restart");
               restartBtn.setBackground(Color.green);
                this.regresar = new JButton("Regresar");
                this.Reportes = new JButton("Reportes");
		this.player1Opts = new JComboBox<>(playerTypeOpts);
		this.player2Opts = new JComboBox<>(playerTypeOpts);
		this.restartBtn.addActionListener(ol);
		this.player1Opts.addActionListener(ol);
		this.player2Opts.addActionListener(ol);
                
		JPanel top = new JPanel(new FlowLayout(FlowLayout.CENTER));// sirve para acomodar un espacio algun grafico 
		JPanel middle = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		this.player1Btn = new JButton("Set Connection");
		this.player1Btn.addActionListener(ol);
		this.player1Btn.setVisible(false);
		this.player2Btn = new JButton("Set Connection");
		this.player2Btn.addActionListener(ol);
		this.player2Btn.setVisible(false);
                regresar.setBackground(Color.blue);
		JPanel layout = new JPanel(new BorderLayout());
		 layout .setBackground(Color.green);
		top.add(restartBtn);
               
		middle.add(new JLabel("(Negro) Jugador 1: "));
		middle.add(player1Opts);
		middle.add(player1Btn);
		bottom.add(new JLabel("(Blanco) Jugador2: "));
		bottom.add(player2Opts);
		bottom.add(player2Btn);
                bottom.add(regresar);
                 bottom.add(Reportes);
		this.add(top);
		this.add(middle);
		this.add(bottom);
	}

	public VentanaDamas getWindow() {
		return window;
	}

	public void setWindow(VentanaDamas window) {
		this.window = window;
	}
	
	
	private static Jugador getPlayer(JComboBox<String> playerOpts) {
		
		Jugador player = new Humano();
		if (playerOpts == null) {
			return player;
		}
		
		
		String type = "" + playerOpts.getSelectedItem();
		if (type.equals("Computadora")) {
			player = new Computadora();
		} 
		
		return player;
	}
	
	
	private class OptionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			
			if (window == null) {
				return;
			}
			
			Object src = e.getSource();

			
			JButton btn = null;
			boolean isNetwork = false, isP1 = true;
			
			if (src == restartBtn) {
				window.restart();
				
			} else if (src == player1Opts) {
				Jugador player = getPlayer(player1Opts);
				window.setPlayer1(player);
				
				btn = player1Btn;
				
			} else if (src == player2Opts) {
				Jugador player = getPlayer(player2Opts);
				window.setPlayer2(player);
				
				btn = player2Btn;
				
				isP1 = false;
			} if(src== regresar){
				
				MenuPrincipal mn = new MenuPrincipal();
				mn.setVisible(true);
			}
                                
			if (btn 
                                
                                != null) {
				
				
				btn.setVisible(isNetwork);
				btn.repaint();
			}
		}
	}
}
