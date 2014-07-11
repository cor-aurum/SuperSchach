package gui;

import javax.swing.JApplet;
import javax.swing.JLabel;

public class Browser extends JApplet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Browser()
	{
		add(new JLabel("Viel Spaﬂ beim Spiel!"));
		new Launcher();
	}
	
	public static void main(String[] args)
	{
		new Browser();
	}
}
