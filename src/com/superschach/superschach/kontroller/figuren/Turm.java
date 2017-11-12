package com.superschach.superschach.kontroller.figuren;

import com.superschach.superschach.kontroller.Kontroller;
import com.superschach.superschach.kontroller.Pruefer;

/**
 * Write a description of class Turm here.
 * 
 * @author (your name)
 * @version (a version number or a date)
 */
public class Turm extends Figur {
	Kontroller kontroller;
	Pruefer pruefer;
	public byte[][] ziele = new byte[15][2];

	public Turm(Kontroller kontroller, int x, int y, byte faktorplayer, Pruefer pruefer, int index) {
		super(kontroller, x, y, 1, faktorplayer, index);
		this.kontroller = kontroller;
		this.pruefer = pruefer;
	}

	public boolean zugMoeglich(int x, int y)//
	{
		return ((kontroller.inhalt(x, y) * vorzeichen() <= 0) && pruefer.gerade(gebePosX(), gebePosY(), x, y)
				&& pruefer.frei(gebePosX(), gebePosY(), x, y));
	}

	public String toString() {
		return "Turm";
	}

	@Override
	public char getCode() {
		return 'r';
	}
}
