package com.superschach.superschach.kontroller;

import org.apache.log4j.Logger;

import com.superschach.superschach.kontroller.figuren.Figur;

/**
 * Write a description of class Prüfer here.
 * 
 * @author (your name)
 * @version (a version number or a date)
 */
public class Pruefer {
	private Kontroller kontroller;
	private Figur[][] figur;
	private Figur[][] figurListe;
	private Logger logger = Logger.getLogger(Pruefer.class);

	public Pruefer(Kontroller kontroller, Figur[][] figur, Figur[][] figurListe) {
		this.kontroller = kontroller;
		this.figurListe = figurListe;
		this.figur = figur;
	}

	public boolean gerade(int xa, int ya, int xb, int yb) {
		return ((xa == xb) || (ya == yb));
	}

	public boolean frei(int xa, int ya, int xb, int yb) {
		boolean ret = true;
		byte fx = 1;
		byte fy = 1;
		if (xb < xa) {
			fx = -1;
		}
		if (yb < ya) {
			fy = -1;
		}
		if (xa == xb) {
			for (int j = 1; j < ((yb - ya) * fy); j++) {
				if (kontroller.inhalt(xa, ya + j * fy) != 0) {
					ret = false;
				}
			}
		} else {
			if (ya == yb) {
				for (int j = 1; j < ((xb - xa) * fx); j++) {
					if (kontroller.inhalt(xa + j * fx, ya) != 0) {
						ret = false;
					}
				}
			} else {
				if (diagonal(xa, ya, xb, yb)) {
					for (int i = 1; i < ((xb - xa) * fx); i++) {
						if (kontroller.inhalt(xa + i * fx, ya + i * fy) != 0)
							ret = false;
					}
				} else {
					ret = false;
				}
			}
		}
		return ret;
	}

	public boolean diagonal(int xa, int ya, int xb, int yb) {
		/*
		 * int xdiff=xb-xa; return ((xdiff==(yb-ya))||(xdiff==(ya-yb)));
		 */
		return Math.abs(xb - xa) == Math.abs(ya - yb);
	}

	public void aktualisiereFigur(Figur[][] figur) {
		this.figur = figur;
	}

	public boolean diffEins(int xa, int ya, int xb, int yb) {
		/*
		 * boolean ret=false; for(int i=-1; i<=1; i=i+2) { for(int j=-1; j<=1; j=j+2) {
		 * if((((xb-xa)*i)==1)|(((yb-ya)*j)==1))//|(((xb-xa)*j)==0)|(((yb -ya)*j)==0)) {
		 * return true; } } } return ret;
		 */
		int xdiff = xa - xb;
		int ydiff = ya - yb;
		return (!(xa == xb && ya == yb)) && (xdiff == 1 || xdiff == -1 || ydiff == 1 || ydiff == -1);
	}

	public boolean istSchach() {;
		return istBedroht(kontroller.koenigPosX(), kontroller.koenigPosY());
	}

	public boolean istBedroht(int koenigx, int koenigy) {
		int player = ((1 - kontroller.getPlayer()) * 2) - 1;
		for (byte i = 0; i < figur.length; i++) {
			for (byte j = 0; j < figur[i].length; j++) {

				if (figur[i][j] != null) {
					if ((figur[i][j].gebePlayer() != player) && figur[i][j].zugMoeglich(koenigx, koenigy)) {
						return true;
					}
				}

			}
		}
		return false;
	}

	public boolean keinZugMoeglich() {
		// boolean ret=true;
		for (int i = 0; i < figur.length; i++) {
			for (int j = 0; j < figur[i].length; j++) {
				for (int k = 0; k < figur.length; k++) {
					for (int l = 0; l < figur[i].length; l++) {
						if (kontroller.zugMoeglich(i, j, k, l) > 0) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	public boolean remis() {
		for (int i = 0; i < figur.length; i++) {
			for (int j = 0; j < figur[i].length; j++) {
				if ((figur[i][j] != null) && (figur[i][j].gebeTyp() != 5) && (figur[i][j].gebeTyp() != -5))
					return false;
			}
		}
		return true;
	}

	public boolean ausSchach(int posx, int posy, int zielx, int ziely) {
		boolean ret = false;
		if (figur[posx][posy] != null) {
			if ((kontroller.inhaltFaktor(posx, posy) > 0)) {
				boolean swap = kontroller.aktualisieren;
				boolean zweitesziel = false;
				kontroller.aktualisieren = false;
				Figur ziel = figur[zielx][ziely];
				Figur ziel2 = null;
				if (((ziely == 2) && (kontroller.inhalt(posx, posy) == -8))
						|| ((ziely == kontroller.YMax - 2) && (kontroller.inhalt(posx, posy) == 8))) {
					ziel2 = figur[zielx][ziely - kontroller.vorzeichen(posx, posy)];
					zweitesziel = true;
				}
				synchronized (figur[posx][posy]) {
					int bewegt = figur[posx][posy].bewegt();
					figur[posx][posy].versetzen(zielx, ziely);

					if (!istSchach()) {
						ret = true;
					}
					if (figur[zielx][ziely] == null) {
						logger.error(posx + " " + posy + " " + zielx + " " + ziely + " --- Figur existiert nicht");
					}
					figur[zielx][ziely].versetzen(posx, posy); // rückgängig
																// machen
					figur[posx][posy].setzeBewegt(bewegt); // bewegt
															// wiederherstellen
				}
				if (ziel != null) {
					figur[zielx][ziely] = ziel;
					figurListe[(1 - ziel.gebePlayer()) / 2][ziel.gebeIndex()] = ziel;
				}
				if (zweitesziel) {
					figur[zielx][ziely - kontroller.vorzeichen(posx, posy)] = ziel2;
					if (ziel2 != null)
						figurListe[(1 - ziel2.gebePlayer()) / 2][ziel2.gebeIndex()] = ziel2;
				}
				kontroller.aktualisieren = swap;
			}

		}

		return ret;
	}

	public boolean springer(int xa, int ya, int xb, int yb) {
		boolean ret = false;
		for (int i = -1; i <= 1; i = i + 2) {
			for (int j = -1; j <= 1; j = j + 2) {
				if (((((xb - xa) * i) == 2) && (((yb - ya) * j) == 1))
						|| ((((xb - xa) * i) == 1) && (((yb - ya) * j) == 2))) {
					return true;
				}
			}
		}
		return ret;
	}
}
