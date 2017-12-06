package com.superschach.superschach.ki;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import com.superschach.superschach.kontroller.Kontroller;
import com.superschach.superschach.kontroller.Probezug;

/**
 * Diese Klasse ist im Rahmen meiner Studienarbeit entstanden
 * 
 * @author felix
 *
 */
public class NumerikKI implements KI {

	public final static int TIEFE = 4;
	private int spieler;
	private Logger logger = Logger.getLogger(NumerikKI.class);
	private Bewerter bewerter = new Bewerter();

	@Override
	public void zug(Kontroller spiel, Zug zug) throws Exception {
		this.spieler = spiel.playerFaktor();
		logger.debug("Suche numerisch den besten Zug");
		int max = Integer.MIN_VALUE;
		Moeglichkeit bestes = null;
		for (Moeglichkeit m : getMoeglicheZuege(spiel).collect(Collectors.toList())) {
			int tmp = versucheZug(m, TIEFE);
			if (tmp > max) {
				max = tmp;
				bestes = m;
			}
		}
		logger.debug("Sende Zug: " + bestes.getVonX() + " " + bestes.getVonY() + " " + bestes.getBisX() + " "
				+ bestes.getBisY());
		logger.debug("Erwarteter Wert: " + max);
		zug.zug(bestes.getVonX(), bestes.getVonY(), bestes.getBisX(), bestes.getBisY());
	}

	private int versucheZug(Moeglichkeit m, int hop) {
		int ret = 0;
		Probezug speicher = m.getKontroller().testZug(m.getVonX(), m.getVonY(), m.getBisX(), m.getBisY());
		if (hop == 0) {
			ret = bewerter.bewerte(m.getKontroller().getFigur()) * spieler;
		} else {
			if (spieler == m.getKontroller().playerFaktor()) {
				ret = getMoeglicheZuege(m.getKontroller()).map(z -> versucheZug(z, hop - 1)).max(Integer::compare)
						.orElse(bewerter.bewerte(m.getKontroller().getFigur()) * spieler);
			} else {
				ret = getMoeglicheZuege(m.getKontroller()).map(z -> versucheZug(z, hop - 1)).min(Integer::compare)
						.orElse(bewerter.bewerte(m.getKontroller().getFigur()) * spieler);
			}
		}
		m.getKontroller().testZugZurueck(speicher);
		return ret;
	}

	private Stream<Moeglichkeit> getMoeglicheZuege(Kontroller k) {
		return Arrays.stream(k.getFigur()).flatMap(Arrays::stream).filter(Objects::nonNull)
				.map(f -> f.getMoeglicheZuege()).flatMap(l -> l.stream())
				.filter(n -> k.zugMoeglich(n[0], n[1], n[2], n[3]) > 0).map(z -> new Moeglichkeit(z, k));
	}

	@Override
	public void tellMatt(Kontroller spiel) {
	}
}
