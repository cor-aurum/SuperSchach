package com.superschach.superschach.ki;

import com.superschach.superschach.kontroller.Kontroller;

/**
 * Write a description of class KISchnittstelle here.
 * 
 * @author Felix Sch&uuml;tze
 * @version (a version number or a date)
 */
public abstract class KISchnittstelle {
	// instance variables - replace the example below with your own
	private Kontroller kontroller;

	public KISchnittstelle(Kontroller kontroller) {
		this.kontroller = kontroller;
	}

	public int inhalt(int x, int y) {
		return kontroller.inhaltFaktor(x, y);
	}

	public boolean zug(int posx, int posy, int zielx, int ziely) {
		if (kontroller.zugMoeglich(posx, posy, zielx, ziely) > 0)
			return kontroller.zug(posx, posy, zielx, ziely);
		else
			return false;
	}

	public byte player() {
		return kontroller.playerFaktor();
	}

	public int wertPruefer() {
		int wert = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				switch(Math.abs(inhalt(i,j)))
				{
				case 16:
					wert+= 9999991;
				case 3:
					wert+=4;
				case 1:
					wert+= 2;
				case 2:
				case 4:
					wert += 2;
				case 8:
					wert++;
				}
			}
		}
		return wert;
	}

	public void zugAusgabe(int[] zug) {
		zugAusgabe(zug[0], zug[1], zug[2], zug[3]);
	}

	public void zugAusgabe(int posx, int posy, int zielx, int ziely) {
		kontroller.meldungAusgeben(
				"[Zug]: Von " + intToChar(posx) + (posy + 1) + " Auf " + intToChar(zielx) + (ziely + 1));
	}

	private char intToChar(int feld) {
		return (char) ('A' + feld);
	}

	// Hook zum aufrufen der KI
	public abstract boolean machZug();

	public int tauscheBauer() {
		return 4;
	}

	public void chat(String s) throws Exception {
	}

	public void tellMatt(Kontroller spiel) {
		// TODO Auto-generated method stub

	}
}
