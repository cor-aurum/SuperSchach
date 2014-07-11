package gui;

import javax.swing.*;
import java.awt.event.*;
/**
 * Write a description of class LeeresFeld here.
 * 
 * @author Felix Sch&uuml;tze 
 * @version (a version number or a date)
 */
public class Feld extends JButton
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SwingSchnittstelle spiel;
    private int posx;
    private int posy;
    public Feld(final SwingSchnittstelle spiel,final int x,final int y)
    {
        this.spiel=spiel;
        this.posx=x;
        this.posy=y;
        addActionListener(new 
            ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    spiel.klick(x,y);
                    
                    //klick.start();                    
                }
            });   
    }
    public int gebeInhalt()
    {
        return spiel.figur(posx,posy);
    }
}
