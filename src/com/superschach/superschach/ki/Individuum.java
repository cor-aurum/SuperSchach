package com.superschach.superschach.ki;

import java.util.ArrayList;

import com.superschach.superschach.kontroller.Kontroller;
import com.superschach.superschach.kontroller.figuren.Figur;

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
		this.kontroller = kontroller;
		// kontroller.verschiebe(vonX, vonY, bisX, bisY);
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
		xa = 0;
		ya = 0;
		xn = 0;
		yn = 0;
		Figur f = null;
		while (!erfolg) {
			do {
				do {
					f = kontroller.getFigurListe()[(int) (Math.random() * 1)][(int) (Math.random()
							* kontroller.getFigurListe()[0].length)];
					kontroller.getFigurListe();
				} while (f == null);
				xa = (byte) f.gebePosX();
				ya = (byte) f.gebePosY();
			} while (kontroller.inhaltFaktor(xa, ya) <= 0);
			ArrayList<int[]> list = f.getMoeglicheZuege();
			do {
				if (list.size() == 0) {
					erfolg = false;
					break;
				}
				int[] t = list.get((int) (Math.random() * list.size()));
				list.remove(t);
				xn = (byte) (t[0]);
				yn = (byte) (t[1]);
				erfolg = true;
			} while (kontroller.zugMoeglich(xa, ya, xn, yn) <= 0);
		}
		if (xa + ya + xn + yn == 0)
			return null;
		return new Individuum(xa, ya, xn, yn, kontroller, hop);
	}
}
