package com.superschach.superschach.ki;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import com.superschach.superschach.kontroller.KIKontroller;
import com.superschach.superschach.kontroller.Kontroller;

/**
 * Diese Klasse ist im Rahmen meiner Studienarbeit entstanden
 * 
 * @author felix
 *
 */
public class NumerikKI implements KI {

	public final static int TIEFE = 25;
	private int spieler;
	private Logger logger = Logger.getLogger(NumerikKI.class);

	@Override
	public void zug(Kontroller spiel, Zug zug) throws Exception {
		this.spieler = spiel.playerFaktor();
		logger.debug("Suche numerisch den besten Zug");
		int max = Integer.MIN_VALUE;
		Moeglichkeit bestes = null;
		for (Moeglichkeit m : getMoeglicheZuege(new KIKontroller(spiel)).collect(Collectors.toList())) {
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
		if (m.getKontroller().zug(m.getVonX(), m.getVonY(), m.getBisX(), m.getBisY()) && hop == 0) {
			ret = new Bewerter().bewerte(m.getKontroller().getFigur()) * spieler;
		} else {
			//TODO orelse wird viel zu häufig aufgerufen. Warum?
			ret = getMoeglicheZuege(m.getKontroller()).map(z -> versucheZug(z, hop - 1)).max(Integer::compare)
					.orElse(new Bewerter().bewerte(m.getKontroller().getFigur()) * spieler);
		}
		m.getKontroller().zug(m.getBisX(), m.getBisY(), m.getVonX(), m.getVonY());
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
