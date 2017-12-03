package com.superschach.superschach.kontroller;

import com.superschach.superschach.kontroller.figuren.Figur;

/**
 * Diese Klasse ist im Rahmen meiner Studienarbeit entstanden
 * 
 * @author felix
 *
 */
public class Probezug {
	private final Figur figur;
	private final byte player;
	private final int posx;
	private final int posy;
	private final int zielx;
	private final int ziely;
	private final int bewegt;
	private final int bauernregel;
	public Probezug(Figur figur, byte player, int posx, int posy, int zielx, int ziely, int bewegt, int bauernregel) {
		this.figur = figur;
		this.player = player;
		this.posx = posx;
		this.posy = posy;
		this.zielx = zielx;
		this.ziely = ziely;
		this.bewegt=bewegt;
		this.bauernregel=bauernregel;
	}
	public Figur getFigur() {
		return figur;
	}
	public byte getPlayer() {
		return player;
	}
	public int getPosx() {
		return posx;
	}
	public int getPosy() {
		return posy;
	}
	public int getZielx() {
		return zielx;
	}
	public int getZiely() {
		return ziely;
	}
	public int getBewegt() {
		return bewegt;
	}
	public int getBauernregel() {
		return bauernregel;
	}
}
