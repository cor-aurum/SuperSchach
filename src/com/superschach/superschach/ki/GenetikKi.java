package com.superschach.superschach.ki;

import org.apache.log4j.Logger;

import com.superschach.superschach.kontroller.Kontroller;

/**
 * Diese Klasse ist im Rahmen meiner Studienarbeit entstanden
 * @author felix
 *
 */
public class GenetikKi implements KI {
	
	private Logger logger = Logger.getLogger(GenetikKi.class);

	@Override
	public void zug(Kontroller spiel, Zug zug) throws Exception {
		logger.debug("Starte Berechnung des Zuges für die Genetische KI");
		Population pop=new Population(spiel);
		Individuum i=pop.getBestes();
		zug.zug(i.getVonX(), i.getVonY(), i.getBisX(), i.getBisY());
	}
	@Override
	public void tellMatt(Kontroller spiel) {
		// TODO Auto-generated method stub
		
	}
}
