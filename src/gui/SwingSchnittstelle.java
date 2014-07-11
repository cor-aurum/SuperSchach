package gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.JFileChooser;

import spiel.Schnittstelle;

/**
 * Write a description of class SwingSchnittstelle here.
 * 
 * @author Felix Sch&uuml;tze
 * @version super
 */
public class SwingSchnittstelle extends Schnittstelle
{
	Brett brett;
	static SwingSchnittstelle schnittstelle;

	public SwingSchnittstelle(Brett brett)
	{
		this.brett = brett;
		schnittstelle = this;
	}

	public void zugGemacht()
	{
		brett.gUI.rG.zug();
	}

	public void aktualisieren()
	{
		brett.aktualisieren();
	}

	public void aktualisieren(int x, int y)
	{
		brett.aktualisieren(x, y);
	}

	public void meldungAusgeben(String s)
	{
		brett.gUI.rG.anzeige(s);
	}

	public void farbe(int x, int y, int farbe)
	{
		brett.farbe(x, y, farbe);
	}

	static public void eingabe(int x, int y)
	{
		SwingSchnittstelle.schnittstelle.klick(x, y);
	}

	public void blink()
	{
		brett.blink();
	}

	public boolean logSpeichern(gui.RechteGUI.MyFilter f)
	{
		boolean ret=false;
		JFileChooser chooser = new JFileChooser();
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileFilter(f);
		try
		{
			FileReader fr = new FileReader(Schnittstelle
					.verzeichnis() + "verzeichnis.v");

			BufferedReader br = new BufferedReader(fr);
			chooser.setCurrentDirectory(new File(br.readLine()));
			br.close();
		} catch (Exception e)
		{
		}
		int rueckgabeWert = chooser.showSaveDialog(null);
		if (rueckgabeWert == JFileChooser.APPROVE_OPTION)
		{
			ret=logSpeichern(new File((chooser.getSelectedFile().toString().indexOf(".slog")==-1)?chooser.getSelectedFile().toString()+".slog":chooser.getSelectedFile().toString()));
			
			try{
				FileWriter fw = new FileWriter(Schnittstelle.verzeichnis()
						+ "verzeichnis.v");
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(chooser.getSelectedFile().getParent().toString());
				bw.close();
				}
			catch(Exception e)
			{				
			}
		}
		return ret;
	}
	
	public void stirb(int typ)
	{
		brett.gUI.stirb(typ);
	}

	public void stirb(int[] geworfen)
	{
		brett.gUI.stirb(geworfen);
	}

	public void chaterhalten(String s) throws Exception
	{
		brett.gUI.rG.anzeige(s);
	}

	public int figurMenu()
	{
		Figurenwahl fW = new Figurenwahl(brett.gUI, Player0());
		brett.gUI.rG.addTab("Auswahl", fW);
		brett.gUI.rG.setSelectedIndex(2);
		brett.gUI.repaint();
		// fW.bild=Player0();
		return fW.wahl();
	}

	public void nachricht(String s)
	{
		brett.gUI.nachricht(s);
	}

	public File getFile()
	{
		return brett.gUI.rG.getFile();
	}
}
