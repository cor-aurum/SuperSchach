package com.superschach.superschach.kontroller.figuren;

import com.superschach.superschach.kontroller.Kontroller;

/**
 * 
 * @author Jan Hofmeier, Felix Sch�tze
 * @version (a version number or a date)
 */
public abstract class Figur// extends Pr�fer
{
	private int bewegt;
	private int posx;
	private int posy;
	private int pos;
	// Figur[][] feld;
	public final int nummer;
	final private byte player;
	private Kontroller kontroller;

	public Figur(final Kontroller kontroller, int x, int y, int typ, byte playerfaktor, int pos) {
		this.kontroller = kontroller;
		// this.feld=feldarray;
		this.nummer = typ;
		this.player = playerfaktor;
		speicherPosition(x, y);
		this.bewegt = 0;
		this.pos = pos;
	}

	private void speicherPosition(int x, int y) {
		posx = x;
		posy = y;
		kontroller.speicher(x, y, this);
	}

	public int gebeIndex() {
		return pos;
	}

	public int gebePosX() {
		return posx;
	}

	public int gebePosY() {
		return posy;
	}

	public int gebeTyp() {
		return nummer * player;
	}

	public byte gebePlayer() {
		return player;
	}

	public void stirb() {
		// Figur soll sich aufloesen
	}

	public void setzePos(int x, int y) {
		posx = x;
		posy = y;
	}

	public void setzeBewegt(boolean bewegt) {
		if (!bewegt)
			this.bewegt = 0;
		else {
			if (this.bewegt == 0) {
				this.bewegt = 1;
			}
		}
	}

	public void geradeMoeglich() {
	}

	public int bewegt() {
		return this.bewegt;
	}

	public void setzeBewegt(int x) {
		this.bewegt = x;
	}

	public boolean wurdeBewegt() {
		return (bewegt > 0);
	}

	public byte vorzeichen() {
		return player;
	}

	public void versetzen(int x, int y) {
		kontroller.verschiebe(posx, posy, x, y);
		posx = x;
		posy = y;
		bewegt++;
	}

	/**
	 * Diese Methode prüft, bis zu welchem Feld im positiven Bereich auf der X-Achse
	 * gerade begehbar sind
	 */
	public byte geradeFreiXPlus() {
		byte posx = (byte) this.posx;
		while (posx < kontroller.XMax) {
			posx++;
			int inhalt = kontroller.inhaltFaktor(posx, posy);
			if (inhalt != 0) {
				if (inhalt < 0) {
					return posx;
				} else {
					return --posx;
				}
			}
		}
		return posx;
	}

	/**
	 * Diese Methode prüft, bis zu welchem Feld im positiven Bereich auf der Y-Achse
	 * gerade begehbar sind
	 */
	public byte geradeFreiYPlus() {
		byte posy = (byte) this.posy;
		while (posy < kontroller.YMax) {
			posy++;
			int inhalt = kontroller.inhaltFaktor(posx, posy);
			if (inhalt != 0) {
				if (inhalt < 0) {
					return posy;
				} else {
					return --posy;
				}
			}
		}
		return posy;
	}

	/**
	 * Diese Methode prüft, bis zu welchem Feld im negativen Bereich auf der X-Achse
	 * gerade begehbar sind
	 */
	public byte geradeFreiXMinus() {
		byte posx = (byte) this.posx;
		while (posx > 0) {
			posx--;
			int inhalt = kontroller.inhaltFaktor(posx, posy);
			if (inhalt != 0) {
				return inhalt < 0 ? posx : ++posx;
			}
		}
		return posx;
	}

	/**
	 * Diese Methode prüft, bis zu welchem Feld im negativen Bereich auf der Y-Achse
	 * gerade begehbar sind
	 */
	public byte geradeFreiYMinus() {
		byte posy = (byte) this.posy;
		while (posy > 0) {
			posy--;// posy wird um eins erniedrigt (Ey du posy du!)
			int inhalt = kontroller.inhaltFaktor(posx, posy); // kontroller.inhaltFaktor(posx, posy) wird
																// zwischengespeichert
			if (inhalt != 0) {
				return inhalt < 0 ? posy : ++posy;
			}
		}
		return posy;
	}

	public byte diagonalFreiPositivPositiv() {
		byte randx = (byte) (posx > posy ? kontroller.XMax : kontroller.XMax - (posy - posx));
		byte y = (byte) posy;
		for (byte x = (byte) posx; y < randx; x++) {
			int inhalt = kontroller.inhaltFaktor(x, y);
			if (inhalt != 0) {
				return inhalt < 0 ? x : --x;
			}
			y++;
		}
		return randx;
	}

	public byte diagonalFreiPositivNegativ() {
		// abstand zum xrand=7-posx
		// abstand zum yrand=posy
		// anzahl begehbarer fehlder=posy
		// letztes begehbares xfeld=posx+posy
		byte randx = (byte) ((7 - posx) < (posy) ? kontroller.XMax : (posx + posy));
		byte y = (byte) posy;
		for (byte x = (byte) posx; y < randx; x++) {
			int inhalt = kontroller.inhaltFaktor(x, y);
			if (inhalt != 0) {
				return inhalt < 0 ? x : --x;
			}
			y--;
		}
		return randx;
	}

	public byte diagonalFreiNegativPositiv() {
		byte randx = (byte) (posx > posy ? kontroller.XMax : kontroller.XMax - (posy - posx));
		byte y = (byte) posy;
		for (byte x = (byte) posx; y > randx; x--) {
			int inhalt = kontroller.inhaltFaktor(x, y);
			if (inhalt != 0) {
				return inhalt < 0 ? x : ++x;
			}
			y--;
		}
		return randx;
	}

	public byte diagonalFreiNegativNegativ() {
		// abstand zum xrand=7-posx
		// abstand zum yrand=posy
		// anzahl begehbarer fehlder=posy
		// letztes begehbares xfeld=posx+posy
		byte randx = (byte) ((kontroller.XMax - posx) < (posy) ? 7 : (posx + posy));
		byte y = (byte) posy;
		for (byte x = (byte) posx; y > randx; x--) {
			int inhalt = kontroller.inhaltFaktor(x, y);
			if (inhalt != 0) {
				return inhalt < 0 ? x : ++x;
			}
			y++;
		}
		return randx;
	}

	public abstract String toString();

	// Hooks
	public void zug(int x, int y) {
		versetzen(x, y);
	}

	public abstract boolean zugMoeglich(int x, int y);

	public boolean rochadeMoeglich(int x, int y) {
		return false;
	}

	public void rochade(int x, int y) {
	}
	
	public abstract int getWert();

	public boolean enPassantMoeglich(int x, int y) {
		return false;
	}

	public abstract char getCode();
}