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
	private final int inhaltVon;
	private final int inhaltBis;
	private final int posx;
	private final int posy;
	private final int zielx;
	private final int ziely;
	public Probezug(Figur figur, byte player, int inhaltVon, int inhaltBis, int posx, int posy, int zielx, int ziely) {
		this.figur = figur;
		this.player = player;
		this.inhaltVon = inhaltVon;
		this.inhaltBis = inhaltBis;
		this.posx = posx;
		this.posy = posy;
		this.zielx = zielx;
		this.ziely = ziely;
	}
	public Figur getFigur() {
		return figur;
	}
	public byte getPlayer() {
		return player;
	}
	public int getInhaltVon() {
		return inhaltVon;
	}
	public int getInhaltBis() {
		return inhaltBis;
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
}
