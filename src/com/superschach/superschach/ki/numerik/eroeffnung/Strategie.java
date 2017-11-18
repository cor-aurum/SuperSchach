package com.superschach.superschach.ki.numerik.eroeffnung;

/**
 * Diese Klasse ist im Rahmen meiner Studienarbeit entstanden
 * 
 * @author felix
 *
 */
public enum Strategie {
	ITALIENISCH("Italienisch"), 
	SPANISCH("Spanisch"), 
	KOENIGSGAMBIT("Königsgambit"), 
	FRANZOESISCH("Französisch"), 
	CAROKANN("Caro-Kann"), 
	SIZILIANISCH("Sizilianisch"), 
	PIRCUFIMZEW("Pirc-Ufimzew-Verteidung"), 
	MODERN("Moderne Verteidung"), 
	SKANDINAVISCH("Skandinavisch");

	private String name;

	Strategie(String name) {
		this.name = name;
	}

	String getName() {
		return name;
	}
}
