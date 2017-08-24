package com.superschach.superschach.kontroller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.superschach.superschach.kontroller.figuren.Bauer;
import com.superschach.superschach.kontroller.figuren.Dame;
import com.superschach.superschach.kontroller.figuren.Figur;
import com.superschach.superschach.kontroller.figuren.Janus;
import com.superschach.superschach.kontroller.figuren.Koenig;
import com.superschach.superschach.kontroller.figuren.Laeufer;
import com.superschach.superschach.kontroller.figuren.Springer;
import com.superschach.superschach.kontroller.figuren.Turm;

/**
 * Write a description of class Spiel here.
 * 
 * @author Jan Hofmeier, Felix Sch&uuml;tze
 * @version (a version number or a date)
 * 
 * @Bauer 6 --- 8
 * @L&uuml;ufer 3 ---2
 * @Springer 2 --- 4
 * @Turm 1 --- 1
 * @Dame 4 --- 3
 * @K&ouml;nig 5 --- 16
 * @Janus 6
 */
public abstract class Kontroller {
	private byte player;
	public final byte XMax;
	public final byte YMax;
	/**
	 * Referenzen zu den Figurenobjekten
	 */
	private Pruefer pruefer;
	protected Figur[][] figur;
	protected Figur[] koenig;
	protected Figur[][] figurListe;
	protected boolean aktualisieren = true;
	private int zuganzahl;
	private Figur[][][] verlauf; // [zug][variable]
	private Figur[][][] listenVerlauf;
	private int[][][] verlaufbewegt;
	protected int letzterZug[] = new int[5];
	private int vorletzterZug[] = new int[5];
	public final static boolean debug = true;
	// private int zug[]= new int[4];
	private byte[][] anzFiguren = new byte[2][6];
	private int[] gesammtAnzFiguren = new int[2];
	private int[] geworfen;
	private int anzGeworfen = 0;
	private String spielName = getSpielDefaultName();
	private Logger logger = Logger.getLogger(Kontroller.class);

	// kiThread kithread;
	/**
	 * TODO: --Den K&ouml;nig fragen lassen ob er im Schach steht
	 */
	public Kontroller(byte laenge, byte breite) {
		XMax = (byte) (laenge - 1);
		YMax = (byte) (breite - 1);
		setup();
		aktualisieren = true;
	}

	public Kontroller(InputStream stream) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));// new
		// File("D:\\start.txt")));
		aktualisieren = false;
		try {
			if (!br.readLine().equals("Super-Schach Spielstand Version 1.0")) {
				br.close();
				throw new Exception();
			}
			byte aktPlayer = (byte) (br.read() - 48);
			br.readLine();
			String[] temp = br.readLine().split(" ");
			XMax = (byte) (Integer.parseInt(temp[0]) - 1);
			YMax = (byte) (Integer.parseInt(temp[1]) - 1);
			setup();
			for (int i = 0; i <= XMax; i++) {
				temp = br.readLine().split(" ");
				for (int j = 0; j <= YMax; j++) {
					int z = Integer.parseInt(temp[j * 2]);
					int f = 1;
					player = 0;
					if (z < 0) {
						player = 1;
						f = -1;
						z = z * f;
					}
					machFigur(z, i, j);
					if (figur[i][j] != null) {
						figur[i][j].setzeBewegt(Integer.parseInt(temp[j * 2 + 1]));
					}

					if (f == -1) {
						togglePlayer();
					}
				}
			}
			this.player = aktPlayer;

			temp = br.readLine().split(" ");
			for (int i = 0; i < letzterZug.length; i++) {
				letzterZug[i] = Integer.parseInt(temp[i]);
			}

			temp = br.readLine().split(" ");
			for (String fig : temp)
				geworfen[anzGeworfen++] = Integer.parseInt(fig);

			try {
				setSpielName(br.readLine());
			} catch (Exception e) {
				setSpielName(getSpielDefaultName());
			}
			br.close();
			aktualisieren = true;
		} catch (Exception e) {
			br.close();
			e.printStackTrace();
			throw e;
		}

	}

	public Kontroller(Figur[][] figur, Figur[] koenig) {
		XMax = (byte) (figur.length - 1);
		YMax = (byte) (figur[0].length - 1);
		this.figur = figur;
		this.koenig = koenig;
		figurListe = new Figur[2][16];
		pruefer = new Pruefer(this, this.figur, figurListe);
		zuganzahl = 0;
		verlauf = new Figur[1000][figur.length][figur[0].length];
		listenVerlauf = new Figur[1000][2][16];
		verlaufbewegt = new int[1000][figur.length][figur[0].length];
		geworfen = new int[2 * figurListe[0].length];
	}

	protected void setPlayer(byte player) {
		this.player = player;
	}

	private void setup() {
		figur = new Figur[XMax + 1][YMax + 1];
		figurListe = new Figur[2][2 * (XMax + 1)];
		geworfen = new int[2 * figurListe[0].length];
		pruefer = new Pruefer(this, this.figur, figurListe);
		koenig = new Figur[2];
		zuganzahl = 0;
		verlauf = new Figur[1000][figur.length][figur[0].length];
		verlaufbewegt = new int[1000][figur.length][figur[0].length];
		listenVerlauf = new Figur[1000][figurListe.length][figurListe[0].length];
	}

	public void machFigur(int nummer, int x, int y) {
		switch (nummer) {
		case 8:
			machBauer(x, y);
			break;
		case 1:
			machTurm(x, y);
			break;
		case 4:
			machSpringer(x, y);
			break;
		case 2:
			machLaeufer(x, y);
			break;
		case 3:
			machDame(x, y);
			break;
		case 16:
			machKoenig(x, y);
			break;
		case 6:
			machJanus(x, y);
			break;
		default:
			loesche(x, y);
			break;
		}
	}

	public byte anzFiguren(byte player, int typ) {
		return anzFiguren[player][typ];
	}

	public byte anzFiguren(int nummer) {
		byte player = 0;
		if (nummer < 0) {
			player = 1;
			nummer = nummer * -1;
		}
		return anzFiguren[player][nummer];
	}

	public byte[] letzterZug() {
		byte[] ret = new byte[letzterZug.length];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = (byte) letzterZug[i];
		}
		return ret;
	}

	public int letzterZug(int x) {
		return letzterZug[x];
	}

	public int vorletzterZug(int x) {
		return vorletzterZug[x];
	}

	public int gebeZugAnz() {
		return zuganzahl;
	}

	public void resetPlayer() {
		player = 0;
	}

	public String getFen() {
		String ret = "";
		for (int i = figur.length-1; i >= 0; i--) {
			int z = 0;
			for (int j = 0; j < figur[i].length; j++) {
				if (figur[j][i] == null)
					z++;
				else {
					if (z > 0) {
						ret += z;
						z = 0;
					}
					ret += figur[j][i].gebePlayer() < 0 ? figur[j][i].getCode()
							: ("" + figur[j][i].getCode()).toUpperCase();

				}
			}
			if (z > 0) {
				ret += z;
			}
			ret += "/";
		}
		ret = ret.substring(0, ret.length() - 1);

		ret += player != 0 ? " b " : " w ";
		boolean b = false;
		if (!koenig[0].wurdeBewegt()) {
			ret += "KQ";
			b = true;
		}
		if (!koenig[1].wurdeBewegt()) {
			ret += "kq";
			b = true;
		}
		if (!b)
			ret += "-";
		ret+=" - ";
		ret+="0 ";
		ret+=(zuganzahl+1);
		return ret;
	}

	private int zugNummer() // verhinder out of bounds Exception / �berlauf
	{
		int zug = zuganzahl;
		while (!(zug < verlauf.length)) {
			zug = zug - verlauf.length;
		}
		return zug;
	}

	public void speicherVerlauf() {
		int zug = zugNummer();
		zuganzahl++;
		for (int i = 0; i < verlauf[zug].length; i++) {
			for (int j = 0; j < verlauf[zug][i].length; j++) {
				verlauf[zug][i][j] = figur[i][j];
				if (figur[i][j] != null) {
					verlaufbewegt[zug][i][j] = figur[i][j].bewegt();
				} else {
					verlaufbewegt[zug][i][j] = 1000;
				}
			}
		}
		for (int i = 0; i < figurListe.length; i++) {
			for (int j = 0; j < figurListe[i].length; j++) {
				listenVerlauf[zug][i][j] = figurListe[i][j];
			}
		}
	}

	// public void uebergebeFigur(Figur[][] figurarray){}

	// public void uebergebePruefer(Pruefer prueferObject){}

	public boolean ladeVerlauf() {
		if (zuganzahl > 0) {
			this.zuganzahl--;
			int zug = zugNummer();
			int diff = 0;
			for (int i = 0; i < verlauf[zug].length; i++) {
				for (int j = 0; j < verlauf[zug][i].length; j++) {
					if (figur[i][j] != null)
						diff--;
					figur[i][j] = verlauf[zug][i][j];
					if (figur[i][j] != null) {
						diff++;
						figur[i][j].setzePos(i, j);
						figur[i][j].setzeBewegt(verlaufbewegt[zug][i][j]);
					}
				}
			}
			if (diff != 0 && diff != 1) {
				logger.warn("Das sollte nicht so sein (Kontroller.ladeVerlauf()): " + diff);
			}
			anzGeworfen -= diff;
			for (int i = 0; i < figurListe.length; i++) {
				for (int j = 0; j < figurListe[i].length; j++) {
					figurListe[i][j] = listenVerlauf[zug][i][j];
				}
			}
			togglePlayer();
			return true;
		} else {
			return false;
		}
	}

	public void verschiebe(int xalt, int yalt, int xneu, int yneu) {
		if (!((xalt == xneu) & (yalt == yneu))) {
			if (figur[xneu][yneu] != null) {
				figurListe[(-figur[xneu][yneu].gebePlayer() + 1) / 2][figur[xneu][yneu].gebeIndex()] = null;
			}
			figur[xneu][yneu] = figur[xalt][yalt];
			figur[xalt][yalt] = null;
			if (aktualisieren) {
				aktualisieren(xalt, yalt);
				aktualisieren(xneu, yneu);
			}
		}
	}

	public void speicher(int x, int y, Figur figurneu) {
		if (figur[x][y] != null)
			figur[x][y].stirb();
		figur[x][y] = figurneu;
		if (aktualisieren) {
			aktualisieren(x, y);
		}
	}

	public boolean wurdeBewegt(int x, int y) {
		return figur[x][y] != null ? figur[x][y].wurdeBewegt() : true;
	}

	public void loesche(int x, int y) {
		if (figur[x][y] != null) {
			figurListe[(1 - figur[x][y].gebePlayer()) / 2][figur[x][y].gebeIndex()] = null;
			wurf(figur[x][y].gebeTyp(), x, y);
			figur[x][y].stirb();
			figur[x][y] = null;
			if (aktualisieren) {
				farbeFeld(x, y, 4);
			}
		}
	}

	public int koenigPosX() {
		return koenig[player].gebePosX();
	}

	public int koenigPosY() {
		return koenig[player].gebePosY();
	}

	public int inhaltFaktor(int x, int y) {
		return ((x >= 0 && x <= XMax && y >= 0 && y <= YMax) && figur[x][y] != null)
				? figur[x][y].gebeTyp() * playerFaktor()
				: 0;
	}

	public int inhalt(int x, int y) {
		return figur[x][y] != null ? figur[x][y].gebeTyp() : 0;
	}

	public int vorzeichen(int x, int y) {
		return figur[x][y] != null ? figur[x][y].gebePlayer() : 0;
	}

	public int zugMoeglich(int posx, int posy, int zielx, int ziely) {
		int ret = 0;
		Figur figurlokal = figur[posx][posy]; // getfield ist zu lahm
		if (figurlokal != null) {
			if (figurlokal.zugMoeglich(zielx, ziely)) {
				if (pruefer.ausSchach(posx, posy, zielx, ziely)) {
					ret = 1;
					if (inhaltFaktor(zielx, ziely) < 0) {
						ret = 2;
					}
				} else {
					ret = -1;
				}
			} else {
				if (figurlokal.rochadeMoeglich(zielx, ziely)) {
					if (pruefer.ausSchach(posx, posy, zielx, ziely)) {
						ret = 3;
					} else {
						ret = -1;
					}
				} else {
					if (figurlokal.enPassantMoeglich(zielx, ziely)) {
						if (pruefer.ausSchach(posx, posy, zielx, ziely)) {
							ret = 4;
						} else {
							ret = -1;
						}
					}
				}
			}
		}
		return ret;
	}

	public int zugMoeglichOhneSchachPruefung(int posx, int posy, int zielx, int ziely) {
		int ret = 0;
		if (figur[posx][posy] != null) {
			if (figur[posx][posy].zugMoeglich(zielx, ziely)) {
				ret = 1;
				if (inhaltFaktor(zielx, ziely) < 0) {
					ret = 2;
				}
			} else {
				if (figur[posx][posy].rochadeMoeglich(zielx, ziely)) {
					if (pruefer.ausSchach(posx, posy, zielx, ziely)) {
						ret = 3;
					} else {
						ret = -1;
					}
				}
			}
		}
		return ret;
	}

	public boolean ausSchach(int posx, int posy, int zielx, int ziely) {
		return pruefer.ausSchach(posx, posy, zielx, ziely);
	}

	public void testzug(int posx, int posy, int zielx, int ziely) {
		if (figur[posx][posy] != null) {
			if (aktualisieren) {
				farbeFeld(posx, posy, 5);
				if (inhalt(zielx, ziely) < 0) {
					farbeFeld(zielx, ziely, 4);
				} else {
					farbeFeld(zielx, ziely, 3);
				}
			}
			figur[posx][posy].zug(zielx, ziely);
		}
	}

	public boolean zug(int posx, int posy, int zielx, int ziely) {
		boolean ret = false;
		aktualisieren = true;
		if (figur[posx][posy] != null) {
			int m = zugMoeglich(posx, posy, zielx, ziely);
			if (m > 0) {
				ret = true;
				speicherVerlauf();
				if (m == 2) {
					wurf(figur[zielx][ziely].gebeTyp(), zielx, ziely);
				}
				figur[posx][posy].zug(zielx, ziely);
				// aktualisieren(posx,posy);
				if (m == 3) {
					farbeFeld(zielx, ziely, 4);
				} else {
					farbeFeld(zielx, ziely, 3);
				}
				farbeFeld(posx, posy, 5);
				letzterZug[0] = posx;
				letzterZug[1] = posy;
				letzterZug[2] = zielx;
				letzterZug[3] = ziely;
				letzterZug[4] = 0;
				if (((ziely == 0) || (7 == ziely)) && (inhaltFaktor(zielx, ziely) == 8)) {
					int figur = figurMenu();
					machFigur(figur, zielx, ziely);
					letzterZug[4] = figur;
					// aktualisieren(zielx,ziely);
				}
				togglePlayer();
				nachZug(m == 2 || m == 4);
			}
		} else
			ret = false;
		if (ret)
			zugGemacht();
		blink();
		return ret;
	}

	public void machTurm(int x, int y) {
		if (figur[x][y] == null) {
			figurListe[player][gesammtAnzFiguren[player]] = new Turm(this, x, y, playerFaktor(), pruefer,
					gesammtAnzFiguren[player]);
			gesammtAnzFiguren[player]++;
		} else {
			new Turm(this, x, y, playerFaktor(), pruefer, figur[x][y].gebeIndex());
		}
		anzFiguren[player][0]++;
		if (aktualisieren)
			aktualisieren(x, y);
	}

	public void machSpringer(int x, int y) {
		if (figur[x][y] == null) {
			figurListe[player][gesammtAnzFiguren[player]] = new Springer(this, x, y, playerFaktor(), pruefer,
					gesammtAnzFiguren[player]);
			gesammtAnzFiguren[player]++;
		} else {
			new Springer(this, x, y, playerFaktor(), pruefer, figur[x][y].gebeIndex());
		}
		anzFiguren[player][1]++;
		if (aktualisieren)
			aktualisieren(x, y);
	}

	public void machJanus(int x, int y) {
		if (figur[x][y] == null) {
			figurListe[player][gesammtAnzFiguren[player]] = new Janus(this, x, y, playerFaktor(), pruefer,
					gesammtAnzFiguren[player]);
			gesammtAnzFiguren[player]++;
		} else {
			new Janus(this, x, y, playerFaktor(), pruefer, figur[x][y].gebeIndex());
		}
		anzFiguren[player][1]++;
		if (aktualisieren)
			aktualisieren(x, y);
	}

	public void machKoenig(int x, int y) {
		if (figur[x][y] == null) {
			koenig[player] = new Koenig(this, x, y, playerFaktor(), pruefer, gesammtAnzFiguren[player]);
			figurListe[player][gesammtAnzFiguren[player]] = koenig[player];
			gesammtAnzFiguren[player]++;
		} else {
			new Koenig(this, x, y, playerFaktor(), pruefer, figur[x][y].gebeIndex());
		}
		anzFiguren[player][4]++;
		if (aktualisieren)
			aktualisieren(x, y);
	}

	public void machLaeufer(int x, int y) {
		if (figur[x][y] == null) {
			figurListe[player][gesammtAnzFiguren[player]] = new Laeufer(this, x, y, playerFaktor(), pruefer,
					gesammtAnzFiguren[player]);
			gesammtAnzFiguren[player]++;
		} else {
			new Laeufer(this, x, y, playerFaktor(), pruefer, figur[x][y].gebeIndex());
		}
		anzFiguren[player][2]++;
		if (aktualisieren)
			aktualisieren(x, y);
	}

	public void machDame(int x, int y) {
		if (figur[x][y] == null) {
			figurListe[player][gesammtAnzFiguren[player]] = new Dame(this, x, y, playerFaktor(), pruefer,
					gesammtAnzFiguren[player]);
			gesammtAnzFiguren[player]++;
		} else {
			new Dame(this, x, y, playerFaktor(), pruefer, figur[x][y].gebeIndex());
		}
		anzFiguren[player][3]++;
		if (aktualisieren)
			aktualisieren(x, y);
	}

	public void machBauer(int x, int y) {
		if (figur[x][y] == null) {
			figurListe[player][gesammtAnzFiguren[player]] = new Bauer(this, x, y, playerFaktor(), pruefer,
					gesammtAnzFiguren[player]);
			gesammtAnzFiguren[player]++;
		} else {
			new Bauer(this, x, y, playerFaktor(), pruefer, figur[x][y].gebeIndex());
		}
		anzFiguren[player][5]++;
		if (aktualisieren)
			aktualisieren(x, y);
	}

	public boolean Player0() {
		return player == 0;
	}

	public byte playerFaktor() {
		return (byte) (1 - (player * 2));
	}

	public byte getPlayer() {
		return player;
	}

	public void togglePlayer() {
		player = (byte) (1 - player);
	}

	public boolean equals(Kontroller kontroller) {
		if (figur.length == kontroller.figur.length && this.player == kontroller.player) {
			for (int i = 0; i < figur.length; i++) {
				if (figur[i].length == kontroller.figur[i].length) {
					for (int j = 0; j < figur[i].length; j++) {
						int inhalt = (inhalt(i, j));
						if (inhalt != kontroller.inhalt(i, j)) {
							return false;
						}
						if (inhalt != 0 ? (figur[i][j].wurdeBewegt() != kontroller.figur[i][j].wurdeBewegt()) : false) {
							return false;
						}
					}
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}

	public boolean istSchach() {
		return pruefer.istSchach();
	}

	public boolean keinZugMoeglich() {
		return pruefer.keinZugMoeglich();
	}

	public boolean istRemis() {
		return pruefer.remis();
	}

	public boolean speichern(File f) {
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter(f));
			try {
				bw.write("Super-Schach Spielstand Version 1.0");
				bw.newLine();
				bw.write(player + " ");
				bw.newLine();
				bw.write(figur.length + " " + figur[0].length + " ");
				bw.newLine();
				for (int i = 0; i < figur.length; i++) {
					for (int j = 0; j < figur[i].length; j++) {
						if (figur[i][j] != null) {
							bw.write(figur[i][j].gebeTyp() + " " + figur[i][j].bewegt() + " ");
						} else {
							bw.write(0 + " " + 0 + " ");
						}
					}
					bw.newLine();
				}

				for (int i = 0; i < letzterZug.length; i++) {
					bw.write(letzterZug[i] + " ");
				}
				bw.newLine();
				// bw.write(anzGeworfen +"");
				// bw.newLine();
				for (int i = 0; i < anzGeworfen; i++) {
					bw.write(geworfen[i] + " ");
				}
				if (anzGeworfen == 0) {
					bw.write(" ");
				}
				bw.newLine();
				bw.write(getSpielName());
				bw.write(" ");
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
				bw.close();
				return false;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}
		return true;

	}

	public String toString() {
		StringBuffer ret = new StringBuffer("Super-Schach Spielstand Version 1.0\n");
		ret.append(player);
		ret.append(" \n");
		ret.append(figur.length);
		ret.append(" ");
		ret.append(figur[0].length);
		ret.append(" \n");
		for (int i = 0; i < figur.length; i++) {
			for (int j = 0; j < figur[i].length; j++) {
				if (figur[i][j] != null) {
					ret.append(figur[i][j].gebeTyp());
					ret.append(" ");
					ret.append(figur[i][j].bewegt());
					ret.append(" ");
				} else {
					ret.append("0 0 ");
				}
			}
			ret.append("\n");
		}

		for (int i = 0; i < letzterZug.length; i++) {
			ret.append(letzterZug[i]);
			ret.append(" ");
		}
		ret.append("\n");
		// bw.write(anzGeworfen +"");
		// bw.newLine();
		for (int i = 0; i < anzGeworfen; i++) {
			ret.append(geworfen[i]);
			ret.append(" ");
		}
		ret.append(" ");
		return ret.toString();
	}

	// Hooks
	public void aktualisieren() {
	}

	public void blink() {
	}

	public void aktualisieren(int x, int y) {
	}

	public void farbeFeld(int x, int y, int farbe) {
	}

	protected void zugGemacht() {
	};

	private void wurf(int typ, int x, int y) {
		geworfen[anzGeworfen++] = typ;
		if (aktualisieren) {
			stirb(typ, x, y);
		}
	}

	public int[] getGeworfen() {
		int[] ret = new int[anzGeworfen];
		System.arraycopy(geworfen, 0, ret, 0, anzGeworfen);
		return ret;
	}

	public void stirb(int typ, int x, int y) {

	}

	public void drehen() {
	}

	public int figurMenu() {
		return 4;
	}

	public void meldungAusgeben(String ereignis) {
	}

	public void nachZug(boolean wurf) {
	}

	public String getSpielDefaultName() {
		Date date = java.util.Calendar.getInstance().getTime();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
		String dateString = dateFormatter.format(date);
		return dateString;
	}

	public String getSpielName() {
		return spielName;
	}

	public void setSpielName(String spielName) {
		this.spielName = spielName;
	}
}
