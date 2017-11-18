package com.superschach.superschach.ki.GenerikKi;

import com.superschach.superschach.ki.KI;
import com.superschach.superschach.ki.KISchnittstelle;
import com.superschach.superschach.ki.Zug;
import com.superschach.superschach.kontroller.Kontroller;

/**
 * Diese Klasse ist im Rahmen meiner Studienarbeit entstanden
 * @author felix
 *
 */
public class GenetikKi extends KISchnittstelle implements KI {

	public GenetikKi(Kontroller kontroller) {
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
