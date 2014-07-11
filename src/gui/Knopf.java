package gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Beschreiben Sie hier die Klasse JButton.
 * 
 * @author Felix Sch&uuml;tze 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class Knopf extends JButton
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Knopf(String name,int f)
    {
        this(name);
        if((f&1)==0)
        setBackground(Color.white);
        else
        setBackground(Color.white);
    }
    
    public Knopf(int f)
    {
        this("",f);
    }
    
    public Knopf()
    {
        this("");
    }

    public Knopf(String name)
    {
        super();
        setIcon(new ImageIcon(this.getClass().getClassLoader().getResource("gui/bilder/papier.png")));
        setText(name);
        Font font = new Font(name,2,18);
        setFont(font);
        setHorizontalTextPosition(JButton.CENTER);
        setVerticalTextPosition(JButton.CENTER);
        //setBorderPainted(false);
        //setContentAreaFilled(false);
        //Color color = new Color(((int)(Math.random()*256)),((int)(Math.random()*256)),((int)(Math.random()*256)));
        //setBackground(color);
    }
}
