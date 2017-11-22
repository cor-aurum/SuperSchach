package com.superschach.superschach.ki;

import com.superschach.superschach.kontroller.figuren.Figur;

/**
 * Diese Klasse ist im Rahmen meiner Studienarbeit entstanden
 * 
 * @author felix
 *
 */
public class Bewerter {

	// private Logger logger = Logger.getLogger(Bewerter.class);

	public int minimax(Figur[] figur) {
		int ret = 0;
		for (Figur f : figur) {
			if (f != null) {
				ret += f.getWert();
			}
		}
		return ret;
	}

	public int doppelbauer(Figur[][] f) {
		int ret = 0;
		for (int i = 1; i < f[0].length - 1; i++)
			for (int j = 1; j < f.length - 1; j++)
				if (f[i][j] != null && (f[i + 1][j] != null && f[i][j].getWert() == f[i + 1][j].getWert()
						|| f[i - 1][j] != null && f[i][j].getWert() == f[i - 1][j].getWert()))
					ret -= f[i][j].getWert();
		return ret;
	}

	public int bewerte(Figur[][] f) {
		Figur[] figurenliste = new Figur[f.length * f[0].length];
		int z = 0;
		for (Figur[] f0 : f) {
			for (Figur f1 : f0) {
				figurenliste[z] = f1;
				z++;
			}
		}
		return minimax(figurenliste) * 100 + doppelbauer(f) * 10;
	}
}
