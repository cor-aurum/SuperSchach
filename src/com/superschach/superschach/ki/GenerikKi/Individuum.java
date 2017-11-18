package com.superschach.superschach.ki.GenerikKi;

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
	public Individuum(byte vonX, byte vonY, byte bisX, byte bisY) {
		super();
		this.vonX = vonX;
		this.vonY = vonY;
		this.bisX = bisX;
		this.bisY = bisY;
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
	
	
}
