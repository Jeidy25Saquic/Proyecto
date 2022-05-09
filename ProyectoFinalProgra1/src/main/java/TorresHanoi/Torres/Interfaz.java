
package TorresHanoi.Torres;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

public class Interfaz extends javax.swing.JFrame {
    Juego juego;

    /**
     * Creates new form VentanaPrincipal
     */
    public Interfaz() {
        try {
            juego = new Juego();

            initComponents();
            
            this.setLocationRelativeTo(null);
            this.setVisible(true);
            juego.setBounds(0, 0, 999, 500);
            juego.setVisible(true);
            this.add(juego);

            
        } catch (IOException ex) {
            Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setTitle(" TORRES DE HANOI");
        setResizable(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 999, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
}

