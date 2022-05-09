/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TorresHanoi.Torres;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;

public class  Torre extends JPanel{
     /**
     * Constructor de la clase torre
     */
    public Torre() {
        this.setLayout(null);
    }

    /**
     * Metodo que dibuja la torre en el panel
     * @param g 
     */
    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        this.setBackground(Color.white);

        g.setColor(Color.red);
        
        //base
        g.fillRect(10, 270, 200,5);
        
        //asta
        g.fillRect(110, 30, 5, 240);
        
    }
}
