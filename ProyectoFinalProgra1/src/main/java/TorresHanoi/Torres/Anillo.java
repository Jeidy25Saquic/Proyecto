/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TorresHanoi.Torres;

import java.awt.Color;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class Anillo extends JPanel {
    /**
     * Constructor de la clase Anillo 
     */
    public Anillo() {
        Random rand = new Random();

        //tres colores bases
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        
        Color colorAnillo = new Color(r, g, b);
         //Línea 1
        Border bordejpanel = new TitledBorder(new BevelBorder(2));

        //Línea 2
        this.setBorder(bordejpanel); 
        this.setBackground(colorAnillo);
    }

}
