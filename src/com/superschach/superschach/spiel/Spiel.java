package com.superschach.superschach.spiel;

import java.io.File;
import java.io.InputStream;

import org.apache.log4j.Logger;

import com.superschach.superschach.ki.KI;
import com.superschach.superschach.ki.KISpieler;
import com.superschach.superschach.kontroller.Kontroller;

/**
 * letzter Uebergabgewert des super Konstruktors gibt die KI an
 * 
 * @author Felix Sch&uuml;tze, Jan Hofmeier
 * @version (a version number or a date)
 */
public class Spiel extends Kontroller {
	final static int[] TAUSCHBAR = { 1, 2, 3, 4 };
	final AbstractGUI spielBrett;
	// KISchnittstelle ki;
	private int aktuellx = -1;
	private int aktuelly = -1;
	private boolean geklickt;
	private boolean[] kian = new boolean[2];
	// private KISchnittstelle ki[] = new KISchnittstelle[2];
	private KISpieler[] ki = new KISpieler[2];
	// public int welche_ki = 17;
	private byte status = 0;
	private Protokollant protokollant = new Protokollant(this);
	private Logger logger = Logger.getLogger(Spiel.class);

	/**
	 * Constructor for objects of class Spiel
	 */
	public Spiel(AbstractGUI gui) {
		this(gui, (byte) 8, (byte) 8);
	}

	public Spiel(AbstractGUI gui, byte laenge, byte breite) {
		super(laenge, breite);
		this.spielBrett = gui;
		geklickt = false;
		startAufstellung();
		kian[0] = false;
		kian[1] = false;
		// ki= new ZufallKI(this);
	}

	public Spiel(AbstractGUI gui, InputStream stream) throws Exception {
		super(stream);
		this.spielBrett = gui;
		setStatus();
	}

	public void startAufstellung() {
		aktualisieren = false;
		resetPlayer();
		for (int i = 0; i < 8; i = i + 7)
			machTurm(i, 0);
		for (int i = 1; i < 8; i = i + 5)
			machSpringer(i, 0);
		for (int i = 2; i < 8; i = i + 3)
			machLaeufer(i, 0);
		machDame(3, 0);
		machKoenig(4, 0);
		for (int i = 0; i < 8; i++)
			machBauer(i, 1);
		// aktualisieren=false;

		togglePlayer();

		// aktualisieren=;
		for (int i = 0; i < 8; i = i + 7)
			machTurm(i, 7);
		for (int i = 1; i < 8; i = i + 5)
			machSpringer(i, 7);
		for (int i = 2; i < 8; i = i + 3)
			machLaeufer(i, 7);
		machDame(3, 7);
		machKoenig(4, 7);
		for (int i = 0; i < 8; i++)
			machBauer(i, 6);

		togglePlayer();

		for (int i = 2; i <= 5; i++)
			for (int j = 0; j < 8; j++)
				loesche(j, i); // Leere reihen, falls neu aufgebaut wird
								// m�ssen
								// auch gel�scht werden

		aktualisieren = true;
	}

	public void entscheider(int x, int y) {
		if (istKIAmZug())
			return;
		spielBrett.resetFeld();
		// bereit=false;
		// if(!geklickt)
		// speicherVerlauf(); //macht jetzt der Kontroller
		if ((inhaltFaktor(x, y) > 0)) {

			if (geklickt && aktuellx == x && aktuelly == y) {
				return;// aktualisieren();
			} else {
				aktuellx = x;
				aktuelly = y;
				farbe(x, y);
			}
			geklickt = true;

		} else if (geklickt & ((inhalt(x, y) * playerFaktor()) <= 0)) {
			if (inhaltFaktor(aktuellx, aktuelly) != 0) {
				if (ausSchach(aktuellx, aktuelly, x, y)) {
					if (zug(aktuellx, aktuelly, x, y)) {
						// tauscheBauer(x,y);
						geklickt = false;
						if (istKIAmZug())// &&(!matt()))
						{

							kiSchleife();

						}
					}
				}
			}
		}
	}

	public boolean zugzurueck() {
		boolean ret = ladeVerlauf();
		geklickt = false;
		aktualisieren();
		spielBrett.stirb(this.getGeworfen());
		return ret;
	}

	public void waehleKI(int ki, byte spieler, String level) {
		aktiviereKI(ki, spieler, level);
		if (istKIAmZug() & (!matt())) {
			kiSchleife();
		}
	}

	public void waehleKIohneStart(int ki, byte spieler, String level) {
		aktiviereKI(ki, spieler, level);
	}

	public void kiSchleife() {
		boolean keinFehler = true;
		boolean matt = matt();
		while (istKIAmZug() && (!matt) && keinFehler) {
			spielBrett.resetFeld();
			speicherVerlauf();
			spielBrett.startDenken(ki[getPlayer()].name);
			keinFehler = ki(getPlayer());
			matt = matt();
		}
		if (istKIAmZug() && matt) {
			ki[getPlayer()].tellMatt(this);
		}
		spielBrett.stopDenken(!matt);
		if (!keinFehler) {
			logger.warn("[Spiel]: Fehler in der KI");
		}
	}

	private byte setStatus() {
		if (istSchach()) {
			if (keinZugMoeglich()) {
				spielBrett.matt(name());
				status = 3;
			} else {
				spielBrett.schach(super.koenigPosX(), super.koenigPosY());
				;
				status = 1;
			}
		} else {
			if (keinZugMoeglich()) {
				spielBrett.patt();
				status = 4;
			} else {
				if (istRemis()) {
					spielBrett.remis();
					status = 2;
				} else
					status = 0;
			}
		}
		return status;
	}

	public byte getStatus() {
		return status;
	}

	public void aktualisieren(int x, int y) {
		spielBrett.aktualisieren(x, y);
	}

	public String name() {
		return kian[getPlayer()] ? ki[getPlayer()].name : AbstractGUI.meldung(getPlayer() == 1 ? "schwarz" : "weiss");
	}

	@Override
	public void stirb(int typ, int x, int y) {
		spielBrett.stirb(typ, x, y);
	}

	public int figurMenu() {
		int f;
		if (istKIAmZug()) {
			f = ki[getPlayer()].tauscheBauer();
		} else {
			f = spielBrett.figurMenu();
		}
		for (int i = 0; i < TAUSCHBAR.length; i++) {
			if (TAUSCHBAR[i] == f) {
				return f;
			}
		}
		return 3;
	}

	public void farbe(int x, int y) {
		/**
		 * 1=Schwarz 2=Wei� 3=Gruen 4=Rot 5=Gelb
		 */
		spielBrett.farbe(x, y, 5);
		for (int a = 0; a <= YMax; a++) {
			for (int b = 0; b <= XMax; b++) {
				int zug = zugMoeglich(x, y, b, a);
				if (zug == 1) {
					spielBrett.farbe(b, a, 3);
				} else {
					if (zug == 2) {
						spielBrett.farbe(b, a, 4);
					} else {
						if (zug == 3) {
							spielBrett.farbe(b, a, 5);
						} else {
							if (zug == 4) {
								spielBrett.farbe(b, a, 5);
								spielBrett.farbe(b, a - playerFaktor(), 4);
							}
						}
					}
				}
			}
		}
	}

	public void farbeFeld(int x, int y, int farbe) {
		spielBrett.farbe(x, y, farbe);
	}

	public void aktualisieren() {
		spielBrett.aktualisieren();
	}

	public void blink() {
		spielBrett.blink();
	}

	public void drehen() {
		spielBrett.drehen();
	}

	public boolean ki(int spieler) {
		boolean ret = ki[spieler].machZug();
		// aktualisieren();
		return ret;
	}

	public boolean aktiviereKI(int k, byte spieler, String level) {
		try {
			new KISpieler(k, level, this, spieler);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean aktiviereKI(KI ki, byte farbe, String name) {

		try {
			new KISpieler(this, ki, farbe, name);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public boolean istKI(int player) {
		return kian[player];
	}

	public boolean logSpeichern(File f) {
		return protokollant.speichern(f);
	}

	public File getFile() {
		return spielBrett.getFile();
	}

	public boolean istKIAmZug() {
		return kian[getPlayer()];
	}

	@Override
	public void nachZug(boolean wurf) {
		int status = setStatus();
		try {
			protokollant.protokoll(letzterZug[0], letzterZug[1], letzterZug[2], letzterZug[3], status, wurf,
					letzterZug[4]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Spielbrett.zugGemacht();
	}

	public void uebersetzen(String s) {
		protokollant.uebersetzen(s);
	}

	public boolean matt() {
		return status > 1;
	}

	public void setKISpieler(KISpieler ki, byte farbe) {
		if (farbe == 0 || farbe == 1) {
			this.ki[farbe] = ki;
			kian[farbe] = ki != null;
		}
	}

	@Override
	protected void zugGemacht() {
		spielBrett.zugGemacht();
	}

	public void nachricht(String nachricht) {
		spielBrett.nachricht(nachricht);
	}

	public String getSpielName() {
		return super.getSpielName();
	}

	public void setSpielName(String s) {
		super.setSpielName(s);
	}

	public String toString() {
		return super.toString() + "\n" + getSpielName();
	}

}
