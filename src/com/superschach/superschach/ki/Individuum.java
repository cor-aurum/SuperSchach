package com.superschach.superschach.ki;

import com.superschach.superschach.kontroller.Kontroller;

/**
 * Diese Klasse ist im Rahmen meiner Studienarbeit entstanden
 * 
 * @author felix
 *
 */
public class Individuum {
	private byte vonX;
	private byte vonY;
	private byte bisX;
	private byte bisY;
	private Population population;
	private Kontroller kontroller;

	private Individuum(byte vonX, byte vonY, byte bisX, byte bisY, Kontroller kontroller, int hop) {
		this.vonX = vonX;
		this.vonY = vonY;
		this.bisX = bisX;
		this.bisY = bisY;
		this.kontroller=kontroller;
		//kontroller.verschiebe(vonX, vonY, bisX, bisY);
		kontroller.getFigur()[vonX][vonY].versetzen(bisX, bisY);
		if (hop > 0) {
			population = new Population(kontroller, hop - 1);
		}
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
		if (population == null)
			return new Bewerter().bewerte(kontroller.getFigur());
		return population.getBestes().getWert();
	}

	/**
	 * 
	 * @return Zufälliges Individuum
	 */
	public static Individuum createZufall(Kontroller kontroller, int hop) {
		boolean erfolg = false;
		byte xa = 0, ya = 0, xn = 0, yn = 0;
		int ccc = 0;
		while (!erfolg) {
			xa = 0;
			ya = 0;
			xn = 0;
			yn = 0;
			do {
				xa = (byte) (Math.random() * 8);
				ya = (byte) (Math.random() * 8);
			} while (kontroller.inhaltFaktor(xa, ya) <= 0);
			for (int counter = 0; counter < 100; counter++) {
				if (!erfolg) {
					xn = (byte) (Math.random() * 8);
					yn = (byte) (Math.random() * 8);
					erfolg = kontroller.zugMoeglich(xa, ya, xn, yn) > 0;
					ccc++;
				}
				if (ccc == 10000) {
					if (kontroller.keinZugMoeglich()) {
						return null;
					}
				}
			}
		}
		if (xa + ya + xn + yn == 0)
			return null;
		return new Individuum(xa, ya, xn, yn, kontroller, hop);
	}
}
