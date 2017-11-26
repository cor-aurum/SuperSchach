package com.superschach.superschach.ki;

import java.util.ArrayList;

import com.superschach.superschach.kontroller.Kontroller;

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

	private Individuum(byte vonX, byte vonY, byte bisX, byte bisY, Kontroller kontroller, int hop) {
		this.vonX = vonX;
		this.vonY = vonY;
		this.bisX = bisX;
		this.bisY = bisY;
		// kontroller.verschiebe(vonX, vonY, bisX, bisY);
		kontroller.getFigur()[vonX][vonY].versetzen(bisX, bisY);
		if (hop > 0) {
			population = new Population(kontroller, hop - 1);
		}
		int w = new Bewerter().bewerte(kontroller.getFigur());
		if (population != null)
			w += population.getBestes().getWert();
		wert=w;
		kontroller.getFigur()[bisX][bisY].versetzen(vonX, vonY);
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

	/**
	 * 
	 * @return Zufälliges Individuum
	 */
	public static Individuum createZufall(Kontroller kontroller, int hop, ArrayList<int[]> list) {
		byte xa = 0, ya = 0, xn = 0, yn = 0;
		boolean erfolg = false;

		do {
			if (list.isEmpty()) {
				return null;
			}
			int[] zug = list.get((int) (Math.random() * list.size()));
			xa = (byte) zug[0];
			ya = (byte) zug[1];
			xn = (byte) zug[2];
			yn = (byte) zug[3];
			if (kontroller.zugMoeglich(xa, ya, xn, yn) > 0) {
				erfolg = true;
			}
			list.remove(zug);
		} while (!erfolg);
		return new Individuum(xa, ya, xn, yn, kontroller, hop);
	}

	@Override
	public String toString() {
		return "Individuum: x=" + vonX + " y=" + vonY + " x'=" + bisX + " y'=" + bisY;
	}

	public void ersetzeUnmoegliche(Kontroller kontroller) {
		if (population != null)
			population.ersetzeUnmoegliche(kontroller);

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
