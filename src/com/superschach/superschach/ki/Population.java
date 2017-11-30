package com.superschach.superschach.ki;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

import org.apache.log4j.Logger;

import com.superschach.superschach.kontroller.KIKontroller;
import com.superschach.superschach.kontroller.Kontroller;
import com.superschach.superschach.kontroller.figuren.Figur;

/**
 * Diese Klasse ist im Rahmen meiner Studienarbeit entstanden
 * 
 * @author felix
 *
 */
public class Population {

	public static final int POT = 2;
	public static final int ANZ_THREADS = 4;
	private static final int TIEFE = 6;
	private Individuum[] individuum;
	private Logger logger = Logger.getLogger(Population.class);
	private Kontroller kontroller;
	private final int hop;
	private Collection<int[]> list;
	private final byte spieler;

	public Population(byte spieler, Kontroller kontroller) {
		logger.debug("Erzeuge Population");
		this.spieler = spieler;
		this.kontroller = kontroller;
		list = zaehleMoeglicheZuege();
		logger.debug("Anzahl möglicher Züge: " + list.size());
		this.hop = TIEFE;
		individuum = new Individuum[(int) Math.pow(POT, TIEFE)];
		Erzeuger[] erzeuger = new Erzeuger[ANZ_THREADS];
		int weite = individuum.length / ANZ_THREADS;
		for (int i = 0; i < ANZ_THREADS; i++) {
			int start = i * weite;
			logger.debug("Erzeuge Erzeuger-Thread " + i + " mit start=" + start + " und weite=" + weite);
			erzeuger[i] = new Erzeuger(new KIKontroller(kontroller), TIEFE, start, start + weite);
			erzeuger[i].start();
		}
		for (Erzeuger e : erzeuger) {
			try {
				e.join();
				logger.debug("Erzeuger-Thread " + e + " ist fertig");
			} catch (InterruptedException e1) {
				logger.fatal("Ein Fehler ist beim Erzeugen der Population aufgetreten: " + e1);
			}
		}
	}

	public Population(byte spieler, Kontroller kontroller, int hop) {
		this.spieler = spieler;
		this.kontroller = kontroller;
		this.hop = hop;
		list = zaehleMoeglicheZuege();
		individuum = new Individuum[(int) Math.pow(POT, hop)];
		for (int i = 0; i < individuum.length; i++) {
			individuum[i] = Individuum.createZufall(kontroller, hop, list, spieler);
		}
	}

	/**
	 * 
	 * @return bestes Individuum einer Population
	 */
	public Optional<Individuum> getBestes() {
		return Arrays.stream(individuum).parallel().filter(Objects::nonNull)
				.max((i1, i2) -> Integer.compare(i1.getWert(), i2.getWert()));
	}

	/**
	 * 
	 * @return Größe der Population ohne nullwerte
	 */
	public int getGroesse() {
		return (int) Arrays.stream(individuum).parallel().filter(Objects::nonNull).count();
	}

	/**
	 * Führt Funktionen der Evolution durch
	 */
	public void evolution() {
		tauscheSchlechteste();
	}

	/**
	 * Sortiere die Individuen
	 */
	private void sort() {
		Arrays.sort(individuum, new Comparator<Individuum>() {
			@Override
			public int compare(Individuum o1, Individuum o2) {
				if (o1 == null && o2 == null) {
					return 0;
				}
				if (o1 == null) {
					return 1;
				}
				if (o2 == null) {
					return -1;
				}
				return o1.compareTo(o2);
			}
		});
	}

	/**
	 * Ersetzt die schlechteste Hälfte der Population gegen neue Elemente
	 */
	public void tauscheSchlechteste() {
		if (individuum != null) {
			sort();
			logger.debug("Tausche die schlechtesten Individuen aus");
			Erzeuger[] erzeuger = new Erzeuger[individuum.length / 2];
			for (int i = 0; i < individuum.length / 2; i++) {
				erzeuger[i] = new Erzeuger(new KIKontroller(kontroller), hop, i + individuum.length / 2,
						i + 1 + individuum.length / 2);
				erzeuger[i].start();
			}
			for (Erzeuger e : erzeuger) {
				if (e != null)
					try {
						e.join();
					} catch (InterruptedException e1) {
						logger.fatal("Ein Fehler ist beim Ersetzen der Population aufgetreten: " + e1);
					}
			}
		}
	}

	private boolean existiertIndividuum(int xa, int ya, int xn, int yn) {
		if (individuum != null)
			for (Individuum i : individuum) {
				if (i != null)
					if (i.getVonX() == xa && i.getVonY() == ya && i.getBisX() == xn && i.getBisY() == yn) {
						return true;
					}
			}
		return false;
	}

	/**
	 * 
	 * @return Anzahl der Möglichen Züge aller Figuren
	 */
	public Collection<int[]> zaehleMoeglicheZuege() {
		ArrayList<int[]> list = new ArrayList<int[]>();
		for (Figur[] f0 : kontroller.getFigurListe())
			for (Figur f : f0)
				if (f != null)
					for (int[] komb : f.getMoeglicheZuege())
						if (kontroller.zugMoeglich(f.gebePosX(), f.gebePosY(), komb[0], komb[1]) > 0)
							if (!existiertIndividuum(f.gebePosX(), f.gebePosY(), komb[0], komb[1]))
								list.add(new int[] { f.gebePosX(), f.gebePosY(), komb[0], komb[1] });
		return Collections.synchronizedCollection(list);
	}

	private class Erzeuger extends Thread {
		private Kontroller kontroller;
		private int hop;
		private int start;
		private int stop;

		public Erzeuger(Kontroller kontroller, int hop, int start, int stop) {
			this.kontroller = kontroller;
			this.hop = hop;
			this.start = start;
			this.stop = stop;
		}

		public void run() {
			for (int i = 0; i < stop - start; i++) {
				Individuum tmp = Individuum.createZufall(kontroller, hop, list, spieler);
				individuum[start + i] = tmp;
				// logger.debug("individuum["+(start+i)+"]= "+tmp);
			}
		}
	}
}
