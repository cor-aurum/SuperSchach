package gui;

import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Das SpielBrett
 * 
 * @author Felix Sch&uuml;tze, Johann Keller, Jan Hofmeier
 * @version (super-pre-alpha Milestone 00000000.1)
 */
public class Brett extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public SwingSchnittstelle spiel;
	private Feld[][] aKnopf;
	private JPanel[] panel;
	public GUI gUI;
	private boolean farbe = true;

	int breite = 50;
	int hoehe = 50;

	// BufferedImage a;

	public Brett(GUI gUI)
	{
		// System.out.println("Checkpoint 3");
		this.gUI = gUI;
		spiel = new SwingSchnittstelle(this);
		setLayout(new GridLayout(8, 1));
		spielfeldBauen(true);
		groesse();
		aktualisieren();
		this.addComponentListener(new ComponentAdapter()
		{
			public void componentResized(ComponentEvent e)
			{
				aktualisieren();
			}
		});

		// spiel.aufbauen();
		/*
		 * try { a = ImageIO.read(new File("gui/bilder/turm_schwarz.png")); }
		 * catch (Exception e) { }
		 */
		// icon=new TrayIcon();
		// System.out.println("Checkpoint 4");
	}

	private void groesse()
	{
		try
		{
			if (aKnopf[0][0].getSize().width > 0
					&& aKnopf[0][0].getSize().height > 0)
			{
				breite = aKnopf[0][0].getSize().width;
				hoehe = aKnopf[0][0].getSize().height;
			}
		} catch (Exception e)
		{
		}
	}

	public void spielfeldLeeren()
	{
		removeAll();
	}

	public boolean farbe()
	{
		return farbe;
	}

	public void setzeFarbe()
	{
		farbe = !farbe;
	}

	public void spielfeldBauen(boolean dreh)
	{
		aKnopf = new Feld[spiel.getXMax() + 1][spiel.getYMax() + 1];
		panel = new JPanel[spiel.getYMax() + 1];
		setLayout(new GridLayout(spiel.getYMax() + 1, 1));
		if (dreh)
		{
			for (int a = spiel.getYMax(); a > -1; a--)
			{
				panel[a] = new JPanel();
				add(panel[a]);
				panel[a].setLayout(new GridLayout(1, spiel.getXMax() + 1));
			}
			for (int a = 0; a < spiel.getYMax() + 1; a++)
			{
				for (int b = 0; b < spiel.getXMax() + 1; b++)
				{
					aKnopf[b][a] = new Feld(spiel, b, a);
					panel[a].add(aKnopf[b][a]);
				}
			}
		} else
		{
			for (int a = 0; a < spiel.getYMax() + 1; a++)
			{
				panel[a] = new JPanel();
				add(panel[a]);
				panel[a].setLayout(new GridLayout(1, spiel.getXMax() + 1));
			}
			for (int a = 0; a < spiel.getYMax() + 1; a++)
			{
				for (int b = spiel.getXMax(); b > -1; b--)
				{
					aKnopf[b][a] = new Feld(spiel, b, a);
					panel[a].add(aKnopf[b][a]);
				}
			}
		}

		aktualisieren();
		repaint();
	}

	public void meldungAusgeben(final String s)
	{
		gUI.rG.anzeige(s);
	}

	public void aktualisieren()
	{
		
		for (int x = 0; x < aKnopf.length; x++)
			for (int y = 0; y < aKnopf[x].length; y++)
			{
				muster(x, y);
				aktualisierenFigur(x, y);
			}
	}

	public void klick(int x, int y)
	{
		spiel.klick(x, y);
	}

	public void aufbauen()
	{
		spiel.aufbauen();
		// aktualisieren();
		aktualisieren();
	}

	public boolean zurueck()
	{
		boolean ret = spiel.zurueck();
		aktualisieren();
		return ret;
	}

	public void farbe(int x, int y, int farbe)
	{
		//gUI.stirb(spiel.getGestorben());
		if(this.farbe)
		{
			muster(x, y);
			BufferedImage newImg = new BufferedImage(breite, hoehe,
					BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = newImg.createGraphics();
			g2d.drawImage(((ImageIcon) aKnopf[x][y].getIcon()).getImage(), 0,
					0, null);
			switch (farbe)
			{
			case 3:
				g2d.drawImage(new ImageIcon(this.getClass().getClassLoader()
						.getResource("gui/bilder/gruen.png")).getImage(), 0, 0,
						breite, hoehe, null);
				break;
			case 4:
				g2d.drawImage(new ImageIcon(this.getClass().getClassLoader()
						.getResource("gui/bilder/rot.png")).getImage(), 0, 0,
						breite, hoehe, null);
				break;
			case 5:
				g2d.drawImage(new ImageIcon(this.getClass().getClassLoader()
						.getResource("gui/bilder/gelb.png")).getImage(), 0, 0,
						breite, hoehe, null);
				break;
			}
			aKnopf[x][y].setIcon(new ImageIcon(newImg));
			repaint();
			aktualisierenFigur(x, y);
		}
	}

	/*
	 * public void farbe(int x, int y, int farbe) { switch (farbe) { case 3:
	 * aKnopf[x][y].setBackground(Color.green);
	 * aKnopf[x][y].setBorderPainted(true); break; case 4:
	 * aKnopf[x][y].setBackground(Color.red);
	 * aKnopf[x][y].setBorderPainted(true); break; case 5:
	 * aKnopf[x][y].setBackground(Color.yellow);
	 * aKnopf[x][y].setBorderPainted(true); break; } }
	 */

	/*
	 * private void restoreMuster() { for (int a = 0; a < spiel.getYMax() + 1;
	 * a++) { for (int b = 0; b < spiel.getXMax() + 1; b++) { muster(b,a);
	 * //aktualisieren(b,a); } } }
	 */

	private void muster(int b, int a)
	{
		if (((a & 1) != (b & 1)))
		{
			// aKnopf[b][a].setBackground(Color.white);
			aKnopf[b][a].setIcon(new ImageIcon(this.getClass().getClassLoader()
					.getResource("gui/bilder/holz_hell.png")));
		} else
		{
			// aKnopf[b][a].setBackground(Color.gray);
			aKnopf[b][a].setIcon(new ImageIcon(this.getClass().getClassLoader()
					.getResource("gui/bilder/holz_dunkel.png")));
			aKnopf[b][a].setBorderPainted(false);

		}
	}

	public void aktualisieren(int x, int y)
	{
		muster(x, y);
		aktualisierenFigur(x, y);
	}

	private void aktualisierenFigur(int x, int y)
	{
		groesse();
		BufferedImage newImg = new BufferedImage(breite, hoehe,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = newImg.createGraphics();

		int bild = aKnopf[x][y].gebeInhalt(); // switch w�r besser... N�
		String farbe = "weiss";
		if (bild < 0)
		{
			farbe = "schwarz";
			bild = bild * (-1);
		}
		g2d.drawImage(((ImageIcon) aKnopf[x][y].getIcon()).getImage(), 0, 0,
				null);
		switch (bild)
		{
		case 1:
			g2d.drawImage(new ImageIcon(this.getClass().getClassLoader()
					.getResource("gui/bilder/turm_" + farbe + ".png"))
					.getImage(), 0, 0, breite, hoehe, null);
			break;
		case 4:
			g2d.drawImage(new ImageIcon(this.getClass().getClassLoader()
					.getResource("gui/bilder/springer_" + farbe + ".png"))
					.getImage(), 0, 0, breite, hoehe, null);
			break;
		case 2:
			g2d.drawImage(new ImageIcon(this.getClass().getClassLoader()
					.getResource("gui/bilder/laeufer_" + farbe + ".png"))
					.getImage(), 0, 0, breite, hoehe, null);
			break;
		case 3:
			g2d.drawImage(new ImageIcon(this.getClass().getClassLoader()
					.getResource("gui/bilder/dame_" + farbe + ".png"))
					.getImage(), 0, 0, breite, hoehe, null);
			break;
		case 16:
			g2d.drawImage(new ImageIcon(this.getClass().getClassLoader()
					.getResource("gui/bilder/koenig_" + farbe + ".png"))
					.getImage(), 0, 0, breite, hoehe, null);
			break;
		case 8:
			g2d.drawImage(new ImageIcon(this.getClass().getClassLoader()
					.getResource("gui/bilder/bauer_" + farbe + ".png"))
					.getImage(), 0, 0, breite, hoehe, null);
			break;
		default:
			// aKnopf[x][y].setIcon(new ImageIcon(""));
			break;
		}
		aKnopf[x][y].setIcon(new ImageIcon(newImg));
		aKnopf[x][y].setBorderPainted(false);
		validate();
		/*
		 * switch (bild) { case 1: aKnopf[x][y].setIcon(new
		 * ImageIcon(this.getClass().getClassLoader()
		 * .getResource("gui/bilder/turm_" + farbe + ".png"))); break; case 4:
		 * aKnopf[x][y].setIcon(new ImageIcon(this.getClass().getClassLoader()
		 * .getResource("gui/bilder/springer_" + farbe + ".png"))); break; case
		 * 2: aKnopf[x][y].setIcon(new
		 * ImageIcon(this.getClass().getClassLoader()
		 * .getResource("gui/bilder/laeufer_" + farbe + ".png"))); break; case
		 * 3: aKnopf[x][y].setIcon(new
		 * ImageIcon(this.getClass().getClassLoader()
		 * .getResource("gui/bilder/dame_" + farbe + ".png"))); break; case 16:
		 * aKnopf[x][y].setIcon(new ImageIcon(this.getClass().getClassLoader()
		 * .getResource("gui/bilder/koenig_" + farbe + ".png"))); break; case 8:
		 * aKnopf[x][y].setIcon(new ImageIcon(this.getClass().getClassLoader()
		 * .getResource("gui/bilder/bauer_" + farbe + ".png"))); break; default:
		 * aKnopf[x][y].setIcon(new ImageIcon("")); break; }
		 */
		setVisible(true);
		// gUI.TrayIcon.displayMessage();#
	}

	public void blink()
	{
		/*
		 * Blink bLINK=new Blink(gUI); if(!bLINK.fokus()) bLINK.start();
		 * 
		 * JOptionPane.showMessageDialog(null,
		 * "Sie haben eine Nachricht erhalten", "Nachricht",
		 * JOptionPane.WARNING_MESSAGE);
		 */
	}
}