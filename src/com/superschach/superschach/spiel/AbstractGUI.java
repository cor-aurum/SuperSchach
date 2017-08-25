package com.superschach.superschach.spiel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.superschach.superschach.ki.KI;
import com.superschach.superschach.server.Server;

public abstract class AbstractGUI {
	private Spiel spiel;
	private SpielThread spielThread; // =new SpielThread();
	private KIThread kiThread;
	private int x = 0;
	private int y = 0;
	// private int kinummer = 0;
	// private int kispieler = 0;
	// private String kilevel = "3";
	public final static String VERSION = "2.2";
	private Logger logger = Logger.getLogger(AbstractGUI.class);
	private static Properties props = new Properties();
	private static Properties ausga = new Properties();

	public AbstractGUI() {
		this(false);
	}

	public AbstractGUI(boolean b) {
		if (b) {
			try {
				new Server();
				logger.info("Server gestartet");
			} catch (SQLException e) {
				logger.error(e.getSQLState());
				logger.error(e.getMessage());
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				logger.error(e.getMessage());
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		} else {
			spiel = new Spiel(this);
		}
		FileInputStream in;
		try {
			if (!new File(verzeichnis() + "Settings.properties").exists()) {
				createProp("Settings");
			}
			in = new FileInputStream(verzeichnis() + "Settings.properties");
			props.load(in);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (!new File(verzeichnis() + "Ausgaben.properties").exists()) {
				createProp("Ausgaben");
			}
			in = new FileInputStream(verzeichnis() + "Ausgaben.properties");
			ausga.load(in);
			in.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void createProp(String s) {
		Path target = Paths.get(verzeichnis(), s + ".properties");
		if (!Files.exists(target)) {
			InputStream source = getClass()
					.getResourceAsStream("/com/superschach/superschach/spiel/" + s + ".properties");
			try {
				Files.copy(source, target);
			} catch (IOException ex) {
				logger.error("Konnte Properties nicht kopieren.");
			} finally {
				try {
					source.close();
				} catch (IOException ex) {
					logger.error("Konnte InputStream nicht schließen.");
				}
			}
		}
	}

	public static String meldung(String key) {
		try {
			if (!ausga.getProperty(key).isEmpty())
				return ausga.getProperty(key);
			else
				try {
					return aktAusJar(key, "Ausgaben", ausga);
				} catch (IOException ex) {
					return key;
				}
		} catch (Exception e) {
			try {
				return aktAusJar(key, "Ausgaben", ausga);
			} catch (IOException ex) {
				return key;
			}
		}
	}

	public static String prop(String key) {
		try {
			if (!props.getProperty(key).isEmpty())
				return props.getProperty(key);
			else
				try {
					return aktAusJar(key, "Settings", props);
				} catch (IOException ex) {
					return key;
				}
		} catch (Exception e) {
			try {
				return aktAusJar(key, "Settings", props);
			} catch (IOException ex) {
				return key;
			}
		}
	}

	/**
	 * Aktualisiert die Properties Datei um einen noch nicht vorhandenen Key aus der
	 * Version in der Jar
	 * 
	 * @param key
	 * @param s
	 * @param p
	 * @return
	 * @throws IOException
	 */
	private static String aktAusJar(String key, String s, Properties p) throws IOException {
		Properties alt = new Properties();
		InputStream in = AbstractGUI.class
				.getResourceAsStream("/com/superschach/superschach/spiel/" + s + ".properties");
		alt.load(in);
		in.close();
		String str = alt.getProperty(key);
		p.setProperty(key, str);
		FileOutputStream out = new FileOutputStream(verzeichnis() + s + ".properties");
		p.store(out, null);
		return str;
	}

	public AbstractGUI(InputStream stream) throws Exception {
		laden(stream);
		// spielThread = new SpielThread();
		// kiThread = new KIThread();
	}

	public byte[] letzterZug() {
		return spiel.letzterZug();
	}

	public static String verzeichnis() {
		String userdir = "";
		if (System.getProperty("os.name").toLowerCase().indexOf("win") > -1)
			userdir = System.getenv("APPDATA") + File.separator;
		else
			userdir = System.getProperty("user.home") + File.separator;

		File dir = new File(userdir + ".super-schach");
		dir.mkdir();
		return userdir + ".super-schach" + File.separator;
	}

	// Methoden mit der die GUI auf das Spiel zugreifen kann
	/**
	 * Die Methode "klick" sagt dem Spiel, welches Feld gedrückt werden soll. Die
	 * Koordinaten werden als Integer übergeben.
	 */
	public void klick(int klickx, int klicky) {
		if (sollThread()) {
			this.x = klickx;
			this.y = klicky;
			// System.out.println(x + " " + y);
			if ((kiThread == null || (!kiThread.isAlive()) || (spielThread == null || !spielThread.isAlive()))) {
				spielThread = new SpielThread();
				if (sollThread()) {
					spielThread.start();
				}
			}
		} else {
			spiel.entscheider(x, y);
		}
	}
	
	/**
	 * 
	 * @author felix
	 * Thread um die Bewegung sichtbar zu machen
	 */
	private class SpielThread extends Thread implements Runnable
	{
		public void run() {
			spiel.entscheider(x, y);
		}
	}

	/**
	 * gibt die ID der Figur auf jedem Feld zur&uuml;ck. Das Vorzeichen bezeichnet
	 * hierbei den Spieler.
	 */
	public int figur(int x, int y) {
		return spiel.inhalt(x, y);
	}

	/**
	 * Baut das Spiel in der Startaufstellung auf
	 */
	public void aufbauen() {
		spiel.startAufstellung();
	}

	/**
	 * Setzt eine KI für einen Spieler ein
	 * @param nummer Nummer der KI. 0 ist keine KI, ansonsten sind diese durchnummeriert
	 * @param spieler Spieler für den die KI spielen soll
	 * @param level Parameter für die KI
	 */
	public void ki(int nummer, int spieler, String level)
	{
		kiThread = new KIThread(nummer, spieler, level);
		kiThread.start();
	}

	/**
	 * Thread in dem eine KI läuft. 
	 * @author felix
	 *
	 */
	private class KIThread extends Thread // Thread um die Bewegung sichtbar zu machen
	{
		private int kinummer;
		private int kispieler;
		private String kilevel;

		public KIThread(int nummer, int spieler, String level) {
			this.kinummer = nummer;
			this.kispieler = spieler;
			this.kilevel = level;
		}

		public void run() {
			spiel.waehleKI(kinummer, (byte) kispieler, kilevel);
		}
	}

	/**
	 * Angabe, ob Spiel in einem eigenen Thread laufen soll
	 * @return boolean
	 */
	public abstract boolean sollThread();

	/**
	 * Kommando an GUI: schließe die Lobby
	 */
	public abstract void leaveLobby();

	/**
	 * Fordert die KI auf einen Zug zu machen
	 * @param i Nummer der KI
	 * @return Erfolg
	 */
	public boolean ki(int i) {
		aktualisieren();
		return spiel.ki(i);
	}

	/**
	 * Aktiviere eine KI
	 * @param k Nummer der KI
	 * @param spieler
	 * @param level
	 * @return
	 */
	public boolean aktiviereKI(int k, byte spieler, String level) {
		return spiel.aktiviereKI(k, spieler, level);
	}

	/**
	 * Aktiviere eine KI
	 * @param ki bestehende KI
	 * @param spieler
	 * @param name
	 * @return
	 */
	public boolean aktiviereKI(KI ki, byte spieler, String name) {
		return spiel.aktiviereKI(ki, spieler, name);
	}

	/**
	 * Speichert das Log in eine Datei
	 * @param f Speicherort
	 * @return Erfolg
	 */
	public boolean logSpeichern(File f) {
		return spiel.logSpeichern(f);
	}

	/**
	 * 
	 * @return Wahr, wenn Weiß am Zug ist
	 */
	public boolean player0() {
		return spiel.Player0();
	}

	/**
	 * 
	 * @return Breite des Spielfelds
	 */
	public int getXMax() {
		return spiel.XMax;
	}

	/**
	 * 
	 * @return Höhe des Spielfelds
	 */
	public int getYMax() {
		return spiel.YMax;
	}

	/**
	 * Speichert ein Spiel in eine Datei
	 * @param f Speicherort
	 * @return Erfolg
	 */
	public boolean speichern(File f) {
		return spiel.speichern(f);
	}

	/**
	 * Lädt ein Spiel aus einem InputStream
	 * @param stream 
	 * @throws Exception
	 */
	public void laden(InputStream stream) throws Exception {
		spiel = new Spiel(this, stream);
		aktualisieren();
		stirb(getGeworfen());
	}

	/**
	 * Gibt zurück, welche Figuren bereits geworfen wurden (ids)
	 * @return geworfene Figuren
	 */
	public int[] getGeworfen() {
		return spiel.getGeworfen();
	}

	/**
	 * Übersetzt ein Schachprotokoll für den Kontroller
	 * @param s Schachprotokoll
	 */
	public void uebersetzen(String s) {
		spiel.uebersetzen(s);
	}

	/**
	 * Erkennt, ob ein Spieler von einer KI gesteuert wird
	 * @param player Spieler auf den sich die Nachfrage bezieht
	 * @return ja oder nein
	 */
	public boolean istKI(int player) {
		return spiel.istKI(player);
	}

	// Hoooks um befehle an die AbstractGUI zu senden
	public void farbe(int x, int y, int farbe) {
	}

	public abstract int figurMenu();

	public abstract void meldungAusgeben(String meldung);

	public abstract void aktualisieren(int x, int y);// {}

	public abstract void aktualisieren();// {}

	public abstract void nachricht(String s);

	public void zugGemacht() {
	}

	public void blink() {
	}

	public void stirb(int typ, int x, int y) {
	};

	public void stirb(int[] geworfen) {
	}

	public void chaterhalten(String s) {
	}

	public void drehen() {
	}

	public File getFile() {
		return null;
	}

	public void resetFeld() {

	}

	public static String getVersion() {
		return VERSION;
	}

	public void herausforderung(long gegnerID, int herausforderungID) {

	}

	public void startDenken(String name) {
		meldungAusgeben(name + " denkt");
	}

	public void stopDenken(boolean dran) {
		meldungAusgeben("Du bist am Zug");
	}

	public void patt() {
		nachricht("Patt");
	}

	public void remis() {
		nachricht("Remis");
	}

	public void schach(int x, int y) {
		logger.warn("Methode AbstractGUI#schach noch nicht überschrieben");
		statusMeldungAusgeben("dran und steht im Schach");
	}

	public void statusMeldungAusgeben(String ereignis) {
		meldungAusgeben(spiel.name() + " ist " + ereignis);
	}

	public void matt(String name) {

	}

	public byte getStatus() {
		return spiel.getStatus();
	}

	public String getSpielName() {
		return spiel.getSpielName();
	}

	public void setSpielName(String s) {
		spiel.setSpielName(s);
	}

	public abstract String[] getLogin(boolean falsch);

	public String getSpielDefaultName() {
		return spiel.getSpielDefaultName();
	}

	public abstract void gegnerSpielVerlassen();

	public void gegnerAufgegeben() {
		meldungAusgeben(meldung("gegner_aufgeben"));
	}

	public void verbindungUnterbrochen() {
		meldungAusgeben(meldung("verbindungs_fehler"));
	}

	/**
	 * Herausforderungs Meldung schliessen/entfernen
	 * 
	 * @param herausforderungID
	 */
	public abstract void herausforderungAbbrechen(int herausforderungID);

}
