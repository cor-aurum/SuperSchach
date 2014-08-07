package gui;

import spiel.Schnittstelle;

public class FxSchnittstelle extends Schnittstelle{
	
	GUI gUI;
	
	public FxSchnittstelle(GUI gUI)
	{
		this.gUI=gUI;
	}

	@Override
	public int figurMenu() {
		// TODO Auto-generated method stub
		return 4;
	}

	@Override
	public void meldungAusgeben(String meldung) {
		System.out.println(meldung);
	}

	@Override
	public void aktualisieren(int x, int y) {
		gUI.aktualisierenFigur(x, y);
	}

	@Override
	public void aktualisieren() {
		gUI.aktualisieren();
	}

	@Override
	public void nachricht(String s) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void farbe(int x, int y, int farbe)
	{
		gUI.farbe(x, y, farbe);
	}

}
