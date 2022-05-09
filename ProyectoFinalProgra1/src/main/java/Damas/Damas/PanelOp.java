
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

public class PanelOp extends JPanel {
   private static final long serialVersionUID = -4763875452164030755L;

	
	private VentanaDamas window;
	
	
	private JButton restartBtn;
	
	
	private JComboBox<String> player1Opts;
	
	private JButton player1Btn;

	
	private JComboBox<String> player2Opts;
	private JButton player2Btn;
        private JButton regresar;
         private JButton Reportes;
          
	
	/**
	 * Creates a new option panel for the specified checkers window.
	 * 
	 * @param window	the window with the game of checkers to update.
	 */
	public PanelOp(VentanaDamas window) {
		super(new GridLayout(0, 1));
		
		this.window = window;
		
		// Initialize the components
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
	
	/*public void setNetworkWindowMessage(boolean forPlayer1, String msg) {
		if (forPlayer1) {
			this.player1Net.setMessage(msg);
		} else {
			this.player2Net.setMessage(msg);
		}
	}*/
	
	/*public NetworkWindow getNetworkWindow1() {
		return player1Net;
	}
	
	public NetworkWindow getNetworkWindow2() {
		return player2Net;
	}
	
	private void handleNetworkUpdate(NetworkWindow win, ActionEvent e) {
		
		if (win == null || window == null || e == null) {
			return;
		}
		
		// Get the info
		int srcPort = win.getSourcePort(), destPort = win.getDestinationPort();
		String destHost = win.getDestinationHost();
		boolean isPlayer1 = (win == player1Net);
		//Session s = (isPlayer1? window.getSession1() : window.getSession2());
		
		// Setting new port to listen on
		if (e.getID() == NetworkWindow.LISTEN_BUTTON) {
			
			// Validate the port
			if (srcPort < 1025 || srcPort > 65535) {
				win.setMessage("  Error: source port must be"
						+ " between 1025 and 65535. ");
				return;
			}
		/*	if (!ConnectionListener.available(srcPort)) {
				win.setMessage("  Error: source port " + srcPort+ " is not available.");
				return;
			}
			*/
			// Update the server if necessary
			/*if (s.getListener().getPort() != srcPort) {
				s.getListener().stopListening();
			}
			s.getListener().setPort(srcPort);
			s.getListener().listen();
			win.setMessage("  This client is listening on port " + srcPort);
			win.setCanUpdateListen(false);
			win.setCanUpdateConnect(true);
                        */
		//}
		
		// Try to connect
		//else if (e.getID() == NetworkWindow.CONNECT_BUTTON) {
			
			// Validate the port and host
			/*if (destPort < 1025 || destPort > 65535) {
				win.setMessage("  Error: destination port must be "
						+ "between 1025 and 65535. ");
				return;
			}
			if (destHost == null || destHost.isEmpty()) {
				destHost = "127.0.0.1";
			}
			
			// Connect to the proposed host
			Command connect = new Command(Command.COMMAND_CONNECT,
					win.getSourcePort() + "", isPlayer1? "1" : "0");
			String response = connect.send(destHost, destPort);
			
			// No response
			if (response.isEmpty()) {
				win.setMessage("  Error: could not connect to " + destHost +
						":" + destPort + ".");
			}
			
			// It was a valid client, but refused to connect
			else if (response.startsWith(CheckersNetworkHandler.RESPONSE_DENIED)) {
				String[] lines = response.split("\n");
				String errMsg = lines.length > 1? lines[1] : "";
				if (errMsg.isEmpty()) {
					win.setMessage("  Error: the other client refused to connect.");
				} else {
					win.setMessage("  " + errMsg);
				}
			}
			
			// The connection was accepted by the checkers client
			else if (response.startsWith(CheckersNetworkHandler.RESPONSE_ACCEPTED)){
				
				// Update the session
				s.setDestinationHost(destHost);
				s.setDestinationPort(destPort);
				win.setMessage("  Successfully started a session with " +
						destHost + ":" + destPort + ".");
				win.setCanUpdateConnect(false);
								
				// Update the SID
				String[] lines = response.split("\n");
				String sid = lines.length > 1? lines[1] : "";
				s.setSid(sid);
				
				// Get the new game state
				Command get = new Command(Command.COMMAND_GET, sid, null);
				response = get.send(destHost, destPort);
				lines = response.split("\n");
				String state = lines.length > 1? lines[1] : "";
				window.setGameState(state);
			}
			
			// General error, maybe the user tried a web server and
			// the response is an HTTP response
			else {
				win.setMessage("  Error: you tried to connect to a host and "
						+ "port that isn't running a checkers client.");
			}*/
		//}
	//}
	
	/**
	 * Gets a new instance of the type of player selected for the specified
	 * combo box.
	 * 
	 * @param playerOpts	the combo box with the player options.
	 * @return a new instance of a {@link model.Player} object that corresponds
	 * with the type of player selected.
	 */
	private static Jugador getPlayer(JComboBox<String> playerOpts) {
		
		Jugador player = new Humano();
		if (playerOpts == null) {
			return player;
		}
		
		// Determine the type
		String type = "" + playerOpts.getSelectedItem();
		if (type.equals("Computadora")) {
			player = new Computadora();
		} /*else if (type.equals("Network")) {
			player = new NetworkPlayer();
		}*/
		
		return player;
	}
	
	/**
	 * The {@code OptionListener} class responds to the components within the
	 * option panel when they are clicked/updated.
	 */
	private class OptionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			// No window to update
			if (window == null) {
				return;
			}
			
			Object src = e.getSource();

			// Handle the user action
			JButton btn = null;
			boolean isNetwork = false, isP1 = true;
			//Session s = null;
			if (src == restartBtn) {
				window.restart();
				//window.getBoard().updateNetwork();
			} else if (src == player1Opts) {
				Jugador player = getPlayer(player1Opts);
				window.setPlayer1(player);
				//isNetwork = (player instanceof NetworkPlayer);
				btn = player1Btn;
				//s = window.getSession1();
			} else if (src == player2Opts) {
				Jugador player = getPlayer(player2Opts);
				window.setPlayer2(player);
				//isNetwork = (player instanceof NetworkPlayer);
				btn = player2Btn;
				//s = window.getSession2();
				isP1 = false;
			} /*else if (src == player1Btn) {
				player1Net.setVisible(true);
			} else if (src == player2Btn) {
				player2Net.setVisible(true);
			}*/
			
			// Handle a network update
			//else if (src == player1Net || src == player2Net) {
				//handleNetworkUpdate((NetworkWindow) src, e);
			//}
			
			// Update UI
			if (btn != null) {
				
				// Disconnect if required
				//String sid = s.getSid();
				/*if (!isNetwork && btn.isVisible() &&
					//	sid != null && !sid.isEmpty()) {
					
					// Send the request
					//Command disconnect = new Command(
						//	Command.COMMAND_DISCONNECT, sid);
					//disconnect.send(
						//	s.getDestinationHost(), s.getDestinationPort());
					
					// Update the session
					//s.setSid(null);
					//NetworkWindow win = isP1? player1Net : player2Net;
					//win.setCanUpdateConnect(true);
				}*/
				
				// Update the UI
				btn.setVisible(isNetwork);
				btn.repaint();
			}
		}
	}
}
