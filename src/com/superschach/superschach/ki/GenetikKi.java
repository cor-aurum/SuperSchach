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

	@Override
	public void zug(Kontroller spiel, Zug zug) throws Exception {
		logger.info("Starte Berechnung des Zuges für die Genetische KI");
		pop = new Population(spiel.playerFaktor(), spiel);
		pop.evolution();
		Individuum i = pop.getBestes().get();
		logger.info("Größe der berechneten Population: "+pop.getGroesse());
		logger.info("Größe aller berechneten Populationen: "+pop.getGroesseRekursiv());
		logger.info("Sende Zug: " + i.getVonX() + " " + i.getVonY() + " " + i.getBisX() + " " + i.getBisY()
				+ ". Erwarteter Wert: " + i.getWert());
		zug.zug(i.getVonX(), i.getVonY(), i.getBisX(), i.getBisY());
	}

	@Override
	public void tellMatt(Kontroller spiel) {
		// TODO Auto-generated method stub

	}
}
