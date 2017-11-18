package com.superschach.superschach.kontroller.figuren;

import com.superschach.superschach.kontroller.Kontroller;
import com.superschach.superschach.kontroller.Pruefer;

public class Janus extends Figur {
	Kontroller kontroller;
	Pruefer pruefer;

	public Janus(Kontroller kontroller, int x, int y, byte faktorplayer, Pruefer pruefer, int index) {
		super(kontroller, x, y, 6, faktorplayer, index);
		this.kontroller = kontroller;
		this.pruefer = pruefer;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Janus";
	}

	public boolean zugMoeglich(int x, int y) {
		return ((kontroller.inhalt(x, y) * vorzeichen() <= 0) && (pruefer.diagonal(gebePosX(), gebePosY(), x, y))
				&& pruefer.frei(gebePosX(), gebePosY(), x, y))
				|| (pruefer.springer(gebePosX(), gebePosY(), x, y)) & (kontroller.inhalt(x, y) * vorzeichen() <= 0);
	}

	@Override
	public char getCode() {
		return 'j';
	}

	@Override
	public int getWert() {
		return 7*vorzeichen();
	}

}
