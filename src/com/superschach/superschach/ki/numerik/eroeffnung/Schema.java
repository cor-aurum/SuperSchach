package com.superschach.superschach.ki.numerik.eroeffnung;

/**
 * Diese Klasse ist im Rahmen meiner Studienarbeit entstanden
 * 
 * @author felix
 *
 */
public enum Schema {
	STAUNTON("Staunton"),
	BILGUER("Von Bilguer"),
	STEINITZ("Steinitz"),
	BRONSTEIN("Bronstein"),
	KAUFMANN("Kaufmann");
	
	private String name;

	Schema(String name) {
		this.name = name;
	}

	String getName() {
		return name;
	}
}
