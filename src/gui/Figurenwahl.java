package gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * Beschreiben Sie hier die Klasse Figurenwahl.
 * 
 * @author Felix Sch&uuml;tze
 * @version (eine Versionsnummer oder ein Datum)
 */
public class Figurenwahl extends JPanel implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel = new JPanel();
	private JButton[] knopf = new JButton[4];
	//boolean bild = true;
	private boolean warten = false;
	int wahl;
	GUI gUI;

	public Figurenwahl(GUI gUI, boolean weiss)
	{
		this.gUI = gUI;
		panel.setLayout(new GridLayout(2, 2));
		setSize(200, 200);
		add(panel);
		for (int i = 0; i < 4; i++)
		{
			knopf[i] = new JButton();
			if (i == 1 || i == 2)
			{
				// knopf[i].setBackground(Color.white);
				knopf[i].setIcon(new ImageIcon(this.getClass().getClassLoader()
						.getResource("gui/bilder/holz_hell.png")));
			} else
				// knopf[i].setBackground(Color.gray);
				knopf[i].setIcon(new ImageIcon(this.getClass().getClassLoader()
						.getResource("gui/bilder/holz_dunkel.png")));
			knopf[i].addActionListener(this);
			panel.add(knopf[i]);
		}
		String farbe = "weiss";
		if (!weiss)
		{
			farbe = "schwarz";
		}
		for(int i=0;i<4;i++)
		{
			BufferedImage newImg = new BufferedImage(100, 100,
					BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = newImg.createGraphics();
			g2d.drawImage(((ImageIcon) knopf[i].getIcon()).getImage(), 0, 0,null);
			switch(i)
			{
			case 0:
				g2d.drawImage(new ImageIcon(this.getClass().getClassLoader()
						.getResource("gui/bilder/turm_" + farbe + ".png"))
						.getImage(), 0, 0, 100, 100, null);
				break;
			case 1:
				g2d.drawImage(new ImageIcon(this.getClass().getClassLoader()
						.getResource("gui/bilder/dame_" + farbe + ".png"))
						.getImage(), 0, 0, 100, 100, null);
				break;
			case 2:
				g2d.drawImage(new ImageIcon(this.getClass().getClassLoader()
						.getResource("gui/bilder/springer_" + farbe + ".png"))
						.getImage(), 0, 0, 100, 100, null);
				break;
			case 3:
				g2d.drawImage(new ImageIcon(this.getClass().getClassLoader()
						.getResource("gui/bilder/laeufer_" + farbe + ".png"))
						.getImage(), 0, 0, 100, 100, null);
				break;
			}
			knopf[i].setIcon(new ImageIcon(newImg));
			knopf[i].setBorderPainted(false);
			knopf[i].setContentAreaFilled(false);
		}
		/*
		knopf[0].setIcon(new ImageIcon(this.getClass().getClassLoader()
				.getResource("gui/bilder/turm_" + farbe + ".png")));
		knopf[1].setIcon(new ImageIcon(this.getClass().getClassLoader()
				.getResource("gui/bilder/dame_" + farbe + ".png")));
		knopf[2].setIcon(new ImageIcon(this.getClass().getClassLoader()
				.getResource("gui/bilder/springer_" + farbe + ".png")));
		knopf[3].setIcon(new ImageIcon(this.getClass().getClassLoader()
				.getResource("gui/bilder/laeufer_" + farbe + ".png")));
				*/
		setVisible(true);
	}

	public void actionPerformed(ActionEvent ae)
	{
		for (int j = 0; j < 4; j++)
		{
			if (ae.getSource() == this.knopf[j])
			{
				if (j == 0)
				{
					wahl = 1;
				}
				if (j == 1)
				{
					wahl = 4;
				}
				if (j == 2)
				{
					wahl = 2;
				}
				if (j == 3)
				{
					wahl = 3;
				}
			}
		}
		warten = true;
		gUI.rG.remove(2);
		gUI.rG.setSelectedIndex(0);
		gUI.repaint();
	}

	public int wahl()
	{
		while (!warten)
			try
			{
				Thread.sleep(100);
			} catch (Exception e)
			{
			}
		return wahl;
	}
}
