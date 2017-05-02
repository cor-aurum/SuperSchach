package com.superschach.superschach.network.client;

import com.superschach.superschach.network.Commands;


public class Spieler {

	private final long id;
	private String name;
	private String farbe;
	private int punkte;
	
	
	
	public Spieler(long id, String name, String farbe, int punkte) {
		this.id = id;
		this.name = name;
		this.farbe=farbe;
		this.punkte = punkte;
	}
	
	public Spieler(String spielerString) {
		String[] eigenschaften=spielerString.split(Commands.SEPERATOR2+"");
		this.id = Long.parseLong(eigenschaften[0]);
		this.name = eigenschaften[1];
		this.farbe=eigenschaften[2];
		this.punkte = Integer.parseInt(eigenschaften[3]);
	}

	
	public String getName() {
		return name;
	}


	public String getFarbe() {
		return farbe;
	}


	public int getPunkte() {
		return punkte;
	}


	public long getID() {
		return id;
	}

}
