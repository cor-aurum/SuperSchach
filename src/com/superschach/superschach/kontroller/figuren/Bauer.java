package com.superschach.superschach.kontroller.figuren;

import com.superschach.superschach.kontroller.Kontroller;
import com.superschach.superschach.kontroller.Pruefer;

/**
 * Write a description of class Bauer here.
 * 
 * @author (Felix Sch&uuml;tze)
 * @version (a version number or a date)
 */
public class Bauer extends Figur {
	Kontroller kontroller;
	Pruefer pruefer;

	public Bauer(Kontroller kontroller, int x, int y, byte faktorplayer, Pruefer pruefer, int index) {
		super(kontroller, x, y, 8, faktorplayer, index);
		this.kontroller = kontroller;
		this.pruefer = pruefer;
	}

	public void zug(int x, int y) {
		if (zugMoeglich(x, y)) {
			versetzen(x, y);
			// return true;
		} else {
			if (enPassantMoeglich(x, y)) {
				enPassant(x, y);
				// return true;
			} else {
				// return false;
			}
		}
	}

	public void enPassant(int x, int y) {
		versetzen(x, y);
		kontroller.loesche(x, y - vorzeichen());
	}

	public boolean enPassantMoeglich(int x, int y) {
		if (((y == 2) && (vorzeichen() == -1)) || ((y == 5) && (vorzeichen() == 1))) {
			byte[] letzterZug = kontroller.letzterZug();
			if ((letzterZug[0] == x) && (letzterZug[1] == (y + vorzeichen()))
					&& (letzterZug[3] == (y - vorzeichen()))) {
				if (((pruefer.diagonal(gebePosX(), gebePosY(), x, y))
						&& (kontroller.inhalt(x, y - vorzeichen()) * vorzeichen() == -8)
						&& pruefer.diffEins(gebePosX(), gebePosY(), x, y))) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean zugMoeglich(int x, int y) {
		boolean ret = false;
		int ydiff = vorzeichen() * (y - gebePosY());
		if (ydiff == 1)// Vorw�rts
		{
			if (gebePosX() == x)// ((pruefer.gerade(gebePosX(),gebePosY(),x,y)))&&(kontroller.inhalt(x,y)*vorzeichen()<=0)&&pruefer.diffEins(gebePosX(),gebePosY(),x,y))
			{
				if (kontroller.inhalt(x, y) == 0)
					return true;
			} else {
				if ((Math.abs(gebePosX() - x) == 1))// &&(kontroller.inhalt(x,y)*vorzeichen()<0))//((pruefer.diagonal(gebePosX(),gebePosY(),x,y))&(kontroller.inhalt(x,y)*vorzeichen()<0)&pruefer.diffEins(gebePosX(),gebePosY(),x,y)))
				{
					if (kontroller.inhalt(x, y) * vorzeichen() < 0) {
						return true;
						// ret=true;
					}
				}
			}
		} else {
			if ((ydiff == 2) && (!wurdeBewegt()) && (gebePosX() == x))// (kontroller.playerFaktor()*(y-gebePosY())==2)&(gebePosX()==x)&(!wurdeBewegt())&(kontroller.inhalt(x,y)==0))
				if ((kontroller.inhalt(x, y - 1 * vorzeichen()) == 0) && (kontroller.inhalt(x, y) == 0))
					ret = true;
		}
		return ret;
	}

	public String toString() {
		return "Bauer"+" "+nummer+ " Spieler: "+gebePlayer();
	}

	@Override
	public char getCode() {
		return 'p';
	}

	@Override
	public int getWert() {
		return 1*vorzeichen();
	}
}
