package com.superschach.superschach.spiel;

import java.io.File;
import java.io.InputStream;
import java.util.Locale;
import java.util.ResourceBundle;

import com.superschach.superschach.ki.Datenbank;
import com.superschach.superschach.ki.KI;

public abstract class AbstractGUI
{
	private Spiel spiel;
	private SpielThread spielThread; // =new SpielThread();
	private KIThread kiThread;
	private int x = 0;
	private int y = 0;
	private int kinummer = 0;
	private int kispieler = 0;
	private int kilevel = 3;
	public final static String VERSION = "2.2";

	public AbstractGUI()
	{
		spiel = new Spiel(this);
		// kiThread = new KIThread();
	}

	public static String meldung(String key)
	{
		try
		{
			return ResourceBundle.getBundle(
					"com.superschach.superschach.spiel.Ausgaben")
					.getString(key);
		} catch (Exception e)
		{
		}
		return ResourceBundle.getBundle(
				"com.superschach.superschach.spiel.Ausgaben", Locale.GERMAN)
				.getString(key);
	}

	public AbstractGUI(InputStream stream) throws Exception
	{
		laden(stream);
		// spielThread = new SpielThread();
		// kiThread = new KIThread();
	}

	public byte[] letzterZug()
	{
		return spiel.letzterZug();
	}

	public static String verzeichnis()
	{
		String userdir = "";
		if (System.getProperty("os.name").toLowerCase().indexOf("win") > -1)
			userdir = System.getenv("APPDATA") + File.separator;
		else
			userdir = System.getProperty("user.home") + File.separator;

		File dir = new File(userdir + ".super-schach");
		dir.mkdir();
		return userdir + ".super-schach" + File.separator;
	}

	// Methoden mit der die GUI auf das SPiel zugreifen kann
	/**
	 * Die Methode "klick" sagt dem Spiel, welches Feld gedr�ckt werden soll.
	 * Die Koordinaten werden als Integer �bergeben.
	 */
	public void klick(int klickx, int klicky)
	{
		if (sollThread())
		{
			this.x = klickx;
			this.y = klicky;
			// System.out.println(x + " " + y);
			if ((kiThread == null || (!kiThread.isAlive()) || (spielThread == null || !spielThread
					.isAlive())))
			{
				spielThread = new SpielThread();
				if (sollThread())
				{
					spielThread.start();
				}
			}
		} else
		{
			spiel.Entscheider(x, y);
		}
	}

	private class SpielThread extends Thread implements Runnable // Thread um
																	// die
																	// Bewegung
																	// sichtbar
																	// zu machen
	{
		public void run()
		{
			spiel.Entscheider(x, y);
		}
	}

	/**
	 * gibt die ID der Figur auf jedem Feld zur&uuml;ck. Das Vorzeichen
	 * bezeichnet hierbei den Spieler.
	 */
	public int figur(int x, int y)
	{
		return spiel.inhalt(x, y);
	}

	public void aufbauen()
	{
		spiel.startAufstellung();
	}

	public boolean zurueck() // Macht den letzten zug r�ckg�nig
	{
		return spiel.zugzurueck();
	}

	public void ki(int nummer, int spieler, int level) // 0 ist keine KI, sonst
														// sind die KIs
														// durchnummeriert
	{
		// spiel.waehleKI(nummer, spieler);
		this.kinummer = nummer;
		this.kispieler = spieler;
		this.kilevel = level;
		if ((kiThread == null || (!kiThread.isAlive()) || (spielThread == null || !spielThread
				.isAlive())))
		{
			kiThread = new KIThread();
			kiThread.start();
		} else
		{
			spiel.waehleKIohneStart(nummer, (byte) spieler, level);
		}
	}

	private class KIThread extends Thread implements Runnable // Thread um die
																// Bewegung
																// sichtbar zu
																// machen
	{
		public void run()
		{
			spiel.waehleKI(kinummer, (byte) kispieler, kilevel);
		}
	}

	public abstract boolean sollThread();

	public abstract void leaveLobby();

	public boolean ki(int i)
	{
		aktualisieren();
		return spiel.ki(i);
	}

	public boolean aktiviereKI(int k, byte spieler, int level)
	{
		return spiel.aktiviereKI(k, spieler, level);
	}

	public boolean aktiviereKI(KI ki, byte spieler, String name)
	{
		return spiel.aktiviereKI(ki, spieler, name);
	}

	public boolean logSpeichern(File f)
	{
		return spiel.logSpeichern(f);
	}

	public boolean Player0()
	{
		return spiel.Player0();
	}

	public int getXMax()
	{
		return spiel.XMax;
	}

	public int getYMax()
	{
		return spiel.YMax;
	}

	public boolean speichern(File f)
	{
		return spiel.speichern(f);
	}

	public void laden(InputStream stream) throws Exception
	{
		spiel = new Spiel(this, stream);
		aktualisieren();
		stirb(getGeworfen());
	}

	public int[] getGeworfen()
	{
		return spiel.getGeworfen();
	}

	public void uebersetzen(String s)
	{
		spiel.uebersetzen(s);
	}

	public boolean istKI(int player)
	{
		return spiel.istKI(player);
	}

	// Hoooks um befehle an die AbstractGUI zu senden
	public void farbe(int x, int y, int farbe)
	{
	}

	public abstract int figurMenu();// {return 4;} //Men� f�r Figurenauswahl
									// wenn man nen Bauern ans Zeil gebracht
									// hat. Wenn nicht gehookt dann wird
									// standartm��ig ne Dame erstellt

	public abstract void meldungAusgeben(String meldung);// {System.out.println(meldung);}
															// //Infofenster
															// soll angezeigt
															// werde, wenn nicht
															// gehookt, wird
															// System.out.println
															// verwendet

	public abstract void aktualisieren(int x, int y);// {}

	public abstract void aktualisieren();// {}

	public abstract void nachricht(String s);

	public void zugGemacht()
	{
	}

	public void blink()
	{
	}

	public void stirb(int typ, int x, int y)
	{
	};

	public void stirb(int[] geworfen)
	{
	}

	public void chaterhalten(String s)
	{
	}

	public void drehen()
	{
	}

	public File getFile()
	{
		return null;
	}

	public void resetFeld()
	{

	}

	public static String getVersion()
	{
		return VERSION;
	}

	public void herausforderung(long gegnerID, int herausforderungID)
	{

	}

	public void startDenken(String name)
	{
		meldungAusgeben(name + " denkt");
	}

	public void stopDenken(boolean dran)
	{
		meldungAusgeben("Du bist am Zug");
	}

	public void patt()
	{
		nachricht("Patt");
	}

	public void remis()
	{
		nachricht("Remis");
	}

	public void schach(int x, int y)
	{
		System.out.println("noch nicht �berschrieben");
		statusMeldungAusgeben("dran und steht im Schach");
	}

	public void statusMeldungAusgeben(String ereignis)
	{
		meldungAusgeben(spiel.name() + " ist " + ereignis);
	}

	public void matt(String name)
	{

	}

	public byte getStatus()
	{
		return spiel.getStatus();
	}

	public String getSpielName()
	{
		return spiel.getSpielName();
	}

	public void setSpielName(String s)
	{
		spiel.setSpielName(s);
	}

	public abstract String[] getLogin(boolean falsch);

	public String getSpielDefaultName()
	{
		return spiel.getSpielDefaultName();
	}

	public abstract void gegnerSpielVerlassen();

	public void gegnerAufgegeben()
	{
		meldungAusgeben(meldung("gegner_aufgeben"));
	}

	public void verbindungUnterbrochen()
	{
		meldungAusgeben(meldung("verbindungs_fehler"));
	}

	/**
	 * Herausforderungs Meldung schliessen/entfernen
	 * 
	 * @param herausforderungID
	 */
	public abstract void herausforderungAbbrechen(int herausforderungID);

}
