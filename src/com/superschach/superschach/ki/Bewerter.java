package com.superschach.superschach.ki;

import com.superschach.superschach.kontroller.figuren.Figur;

/**
 * Diese Klasse ist im Rahmen meiner Studienarbeit entstanden
 * 
 * @author felix
 *
 */
public class Bewerter {

	public int minimax(Figur[] figur) {
		int ret = 0;
		for (Figur f : figur) {
			if (f != null)
				ret += f.getWert();
		}
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
		return minimax(figurenliste)*100;
	}
}
