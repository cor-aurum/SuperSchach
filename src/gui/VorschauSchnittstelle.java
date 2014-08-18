package gui;

import spiel.Schnittstelle;

public class VorschauSchnittstelle extends Schnittstelle{
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
}
