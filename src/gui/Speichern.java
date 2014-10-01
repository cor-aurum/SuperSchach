package gui;

import java.io.File;

import spiel.Schnittstelle;

public class Speichern {
	private File verzeichnis;
	private GUI gUI;
	public Speichern(GUI gUI, String gegner)
	{
		this.gUI=gUI;
		verzeichnis=new File(Schnittstelle.verzeichnis()+File.separator+gegner);
		verzeichnis.mkdir();
		verzeichnis=new File(Schnittstelle.verzeichnis()+File.separator+gegner+File.separator+System.currentTimeMillis()+".schach");
	}
	
	public Speichern(GUI gUI, File f)
	{
		this.gUI=gUI;
		verzeichnis=f;
	}
	
	public void speichern()
	{
		gUI.spiel.speichern(verzeichnis);
	}
	
	public void loeschen()
	{
		verzeichnis.delete();
	}
}
