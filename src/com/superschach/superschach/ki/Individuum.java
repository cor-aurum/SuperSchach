package com.superschach.superschach.ki;

import java.util.Collection;
import java.util.Optional;

import com.superschach.superschach.kontroller.Kontroller;
import com.superschach.superschach.kontroller.Probezug;

/**
 * Diese Klasse ist im Rahmen meiner Studienarbeit entstanden
 * 
 * @author felix
 *
 */
public class Individuum implements Comparable<Individuum> {
	private byte vonX;
	private byte vonY;
	private byte bisX;
	private byte bisY;
	private Population population;
	private final int wert;

	private Individuum(byte vonX, byte vonY, byte bisX, byte bisY, Kontroller kontroller, int hop, byte spieler) {
		this.vonX = vonX;
		this.vonY = vonY;
		this.bisX = bisX;
		this.bisY = bisY;
		Probezug speicher=kontroller.testZug(vonX, vonY, bisX, bisY);
		if (hop > 0) {
			population = new Population(spieler, kontroller, hop - 1);
		}
		wert = new Bewerter().bewerte(kontroller.getFigur()) * spieler;
		kontroller.testZugZurueck(speicher);
	}

	public byte getVonX() {
		return vonX;
	}

	public byte getVonY() {
		return vonY;
	}

	public byte getBisX() {
		return bisX;
	}

	public byte getBisY() {
		return bisY;
	}

	public int getWert() {
		return wert;
	}

	public Population getPopulation() {
		return population;
	}

	/**
	 * 
	 * @return Zufälliges Individuum
	 */
	public static Individuum createZufall(Kontroller kontroller, int hop, Collection<int[]> list, byte spieler) {
		byte xa = 0, ya = 0, xn = 0, yn = 0;
		// int[] zug = list.get((int) (Math.random() * list.size()));
		int[] zug;
		synchronized (list) {
			Optional<int[]> o = list.stream().findAny();
			if (!o.isPresent())
				return null;
			zug = o.get();
			list.remove(zug);
		}
		xa = (byte) zug[0];
		ya = (byte) zug[1];
		xn = (byte) zug[2];
		yn = (byte) zug[3];
		return new Individuum(xa, ya, xn, yn, kontroller, hop, spieler);
	}

	@Override
	public String toString() {
		return "Individuum: x=" + vonX + " y=" + vonY + " x'=" + bisX + " y'=" + bisY;
	}

	public void evolution() {
		if (population != null)
			population.evolution();
	}

	@Override
	public int compareTo(Individuum o) {
		if (o == null || wert > o.getWert())
			return 1;
		if (wert < o.getWert())
			return -1;
		return 0;
	}
}
