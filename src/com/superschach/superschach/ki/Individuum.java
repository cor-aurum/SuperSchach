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
		byte xa = 0, ya = 0, xn = 0, yn = 0;
		ArrayList<Figur> figuren = new ArrayList<Figur>();
		for (Figur[] f0 : kontroller.getFigurListe()) {
			for (Figur f : f0) {
				if (f != null)
					figuren.add(f);
			}
		}
		while (kontroller.zugMoeglich(xa, ya, xn, yn) <= 0) {
			if (figuren.isEmpty()) {
				System.out.println("Keine Züge möglich");
				return null;
			}
			Figur f = null;
			f = figuren.get((int) (Math.random() * figuren.size()));
			xa = (byte) f.gebePosX();
			ya = (byte) f.gebePosY();
			ArrayList<int[]> list = f.getMoeglicheZuege();
			if (list.isEmpty()) {
				figuren.remove(f);
				continue;
			}
			int[] t = list.get((int) (Math.random() * list.size()));
			list.remove(t);
			xn = (byte) (t[0]);
			yn = (byte) (t[1]);
		}
		return new Individuum(xa, ya, xn, yn, kontroller, hop);
	}
}
