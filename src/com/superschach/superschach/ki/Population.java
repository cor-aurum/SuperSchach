package com.superschach.superschach.ki;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
		individuum = Arrays.stream(individuum).parallel()
				.map(in -> Individuum.createZufall(new KIKontroller(kontroller), TIEFE, list, spieler))
				.collect(Collectors.toList()).toArray(individuum);
	}

	public Population(byte spieler, Kontroller kontroller, int hop) {
		this.spieler = spieler;
		this.kontroller = kontroller;
		this.hop = hop;
		list = zaehleMoeglicheZuege();
		individuum = new Individuum[(int) Math.pow(POT, hop)];
		individuum = Arrays.stream(individuum).map(in -> Individuum.createZufall(kontroller, hop, list, spieler))
				.collect(Collectors.toList()).toArray(individuum);
	}

	/**
	 * 
	 * @return bestes Individuum einer Population
	 */
	public Optional<Individuum> getBestes() {
		if (kontroller.playerFaktor() == spieler)
			return Arrays.stream(individuum).parallel().filter(Objects::nonNull)
					.max((i1, i2) -> Integer.compare(i1.getWert(), i2.getWert()));
		else
			return Arrays.stream(individuum).parallel().filter(Objects::nonNull)
					.min((i1, i2) -> Integer.compare(i1.getWert(), i2.getWert()));
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
		//tauscheSchlechteste();
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
			individuum = Arrays.stream(individuum).parallel().skip(individuum.length / 2)
					.map(in -> Individuum.createZufall(new KIKontroller(kontroller), hop, list, spieler))
					.collect(Collectors.toList()).toArray(individuum);
		}
	}

	private boolean existiertIndividuum(int xa, int ya, int xn, int yn) {
		if (individuum != null) {
			Arrays.stream(individuum).filter(Objects::nonNull)
					.filter(i -> (i.getVonX() == xa && i.getVonY() == ya && i.getBisX() == xn && i.getBisY() == yn))
					.findAny().isPresent();
		}
		return false;
	}

	/**
	 * 
	 * @return Anzahl der Möglichen Züge aller Figuren
	 */
	public Collection<int[]> zaehleMoeglicheZuege() {
		ArrayList<int[]> list = new ArrayList<int[]>();
		for (Figur[] f0 : kontroller.getFigur())
			for (Figur f : f0)
				if (f != null)
					for (int[] komb : f.getMoeglicheZuege())
						if (kontroller.zugMoeglich(komb[0], komb[1], komb[2], komb[3]) > 0)
							if (!existiertIndividuum(komb[0], komb[1], komb[2], komb[3]))
								list.add(komb);
		return Collections.synchronizedCollection(list);
	}
}
