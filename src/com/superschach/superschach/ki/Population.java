package com.superschach.superschach.ki;

import java.util.ArrayList;

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

	public static final int GROESSEPOT = 2;
	public static final int ANZ_THREADS = 4;
	private static final int TIEFE = 6;
	private Individuum[] individuum;
	private Logger logger = Logger.getLogger(Population.class);
	private Kontroller kontroller;
	private final int hop;
	private ArrayList<int[]> list;

	public Population(Kontroller kontroller) {
		logger.debug("Erzeuge Population");
		this.kontroller = kontroller;
		list=zaehleMoeglicheZuege();
		this.hop = TIEFE;
		individuum = new Individuum[(int) Math.pow(GROESSEPOT, TIEFE)];
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
			} catch (InterruptedException e1) {
				logger.fatal("Ein Fehler ist beim Erzeugen der Population aufgetreten: " + e1);
			}
		}
	}

	public Population(Kontroller kontroller, int hop) {
		this.kontroller = kontroller;
		this.hop = hop;
		list=zaehleMoeglicheZuege();
		individuum = new Individuum[(int) Math.pow(GROESSEPOT, hop)];
		for (int i = 0; i < individuum.length; i++) {
			individuum[i] = Individuum.createZufall(kontroller, hop, list);
		}
	}

	/**
	 * Ersetzt alle Individuen der Population die keine Gültigkeit mehr besitzen
	 */
	public void ersetzeUnmoegliche(Kontroller kontroller) {
		this.kontroller=kontroller;
		list=zaehleMoeglicheZuege();
		Erzeuger[] erzeuger = new Erzeuger[individuum.length];
		for (int i = 0; i < individuum.length; i++) {
			boolean neu = false;
			if (individuum[i] == null) {
				neu = true;
			} else {
				if (kontroller.zugMoeglich(individuum[i].getVonX(), individuum[i].getVonY(),
						individuum[i].getBisX(), individuum[i].getBisY()) <= 0) {
					neu = true;
					logger.debug("Zug " + individuum[i].getVonX() + " " + individuum[i].getVonY() + " "
							+ individuum[i].getBisX() + " " + individuum[i].getBisY() + " ist nicht mehr möglich");
				}
			}
			if (neu) {
				erzeuger[i] = new Erzeuger(new KIKontroller(kontroller), hop, i, i + 1);
				erzeuger[i].start();
			}else {
				//individuum[i].ersetzeUnmoegliche(kontroller);
			}
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

	/**
	 * 
	 * @return bestes Individuum einer Population
	 */
	public Individuum getBestes() {
		Individuum in = individuum[0];
		for (Individuum i : individuum) {
			if (i != null) {
				if (i.getWert() > in.getWert()) {
					in = i;
				}
			}
		}
		// logger.debug("Wert des besten Individuums: "+in.getWert());
		return in;
	}
	
	public ArrayList<int[]> zaehleMoeglicheZuege()
	{
		ArrayList<int[]> list = new ArrayList<int[]>();
		for (Figur[] f0 : kontroller.getFigurListe())
			for (Figur f : f0)
				if (f != null)
					for (int[] komb : f.getMoeglicheZuege())
						list.add(new int[] { f.gebePosX(), f.gebePosY(), komb[0], komb[1] });
		return list;
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
				individuum[start + i] = Individuum.createZufall(kontroller, hop,list);
			}
		}
	}
}
