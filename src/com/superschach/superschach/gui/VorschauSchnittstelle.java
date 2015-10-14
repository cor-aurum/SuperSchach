package com.superschach.superschach.gui;

import com.superschach.superschach.spiel.AbstractGUI;

public class VorschauSchnittstelle extends AbstractGUI{
	SpielVorschau vorschau;
	public VorschauSchnittstelle(SpielVorschau vorschau)
	{
		this.vorschau=vorschau;
	}
	@Override
	public boolean sollThread() {
		return false;
	}

	@Override
	public int figurMenu() {
		return 0;
	}

	@Override
	public void meldungAusgeben(String meldung) {
	}

	@Override
	public void aktualisieren(int x, int y) {
	}

	@Override
	public void aktualisieren() {
		vorschau.aktualisieren();
	}

	@Override
	public void nachricht(String s) {
	}
	@Override
	public void leaveLobby() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String[] getLogin(boolean b) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void schach(int x, int y)
	{
		
	}
	@Override
	public void gegnerSpielVerlassen() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void herausforderungAbbrechen(int herausforderungID) {
		// TODO Auto-generated method stub
		
	}
}
