package com.superschach.superschach.ki;

import com.superschach.superschach.kontroller.Kontroller;

/*
 * TODO
 * Bietet eine Schnittstelle zu externen KIs mittels des Universal Chess Interfaces
 */
public class UCIKi extends KISchnittstelle implements KI{

	public UCIKi(Kontroller kontroller, String exec) {
		super(kontroller);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void zug(Kontroller spiel, Zug zug) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean machZug() {
		// TODO Auto-generated method stub
		return false;
	}

}
