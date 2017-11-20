package com.superschach.superschach.ki;

import org.apache.log4j.Logger;

import com.superschach.superschach.kontroller.Kontroller;

/**
 * Diese Klasse ist im Rahmen meiner Studienarbeit entstanden
 * 
 * @author felix
 *
 */
public class GenetikKi implements KI {

	private Logger logger = Logger.getLogger(GenetikKi.class);
	private Population pop;
	private static GenetikKi ich;

	private GenetikKi() {
		
	}

	@Override
	public void zug(Kontroller spiel, Zug zug) throws Exception {
		logger.debug("Starte Berechnung des Zuges für die Genetische KI");
		if (pop == null) {
			pop = new Population(spiel);
		} else {
			pop.ersetzeUnmoegliche();
		}
		Individuum i = pop.getBestes();
		logger.debug("Sende Zug: " + i.getVonX() + " " + i.getVonY() + " " + i.getBisX() + " " + i.getBisY());
		zug.zug(i.getVonX(), i.getVonY(), i.getBisX(), i.getBisY());
	}

	@Override
	public void tellMatt(Kontroller spiel) {
		// TODO Auto-generated method stub

	}
	
	public static GenetikKi create()
	{
		if(ich==null)
			return new GenetikKi();
		return ich;
	}
}
