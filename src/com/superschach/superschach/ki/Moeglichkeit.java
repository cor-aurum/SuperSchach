package com.superschach.superschach.ki;

import com.superschach.superschach.kontroller.Kontroller;

/**
 * Diese Klasse ist im Rahmen meiner Studienarbeit entstanden
 * 
 * @author felix
 *
 */
public class Moeglichkeit {

	private final int vonX;
	private final int vonY;
	private final int bisX;
	private final int bisY;
	private final Kontroller kontroller;

	/**
	 * 
	 * @param k
	 *            k ist ein Array mit 4 Werten. 0+1 ist die aktuelle Position, 2+3
	 *            die neue
	 */
	public Moeglichkeit(int[] k, Kontroller kontroller) {
		this.vonX = k[0];
		this.vonY = k[1];
		this.bisX = k[2];
		this.bisY = k[3];
		this.kontroller = kontroller;
	}

	public int getVonX() {
		return vonX;
	}

	public int getVonY() {
		return vonY;
	}

	public int getBisX() {
		return bisX;
	}

	public int getBisY() {
		return bisY;
	}

	public Kontroller getKontroller() {
		return kontroller;
	}

	public String toString() {
		return "Zug von: " + vonX + " " + vonY + " nach: " + bisX + " " + bisY;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Moeglichkeit) {
			Moeglichkeit m = (Moeglichkeit) o;
			return m.getBisX() == bisX && m.getBisY() == bisY && m.getVonX() == vonX && m.getVonY() == vonY;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return 1000 * vonX + 100 * vonY + 10 * bisX + bisY;
	}
}
