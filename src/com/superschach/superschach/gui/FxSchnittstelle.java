package com.superschach.superschach.gui;

import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;

import com.superschach.superschach.spiel.AbstractGUI;

public class FxSchnittstelle extends AbstractGUI {

	GUI gUI;
	public Login login;
	Pane sperre;
	FigurenMenue menue = null;

	public FxSchnittstelle(GUI gUI) {
		this.gUI = gUI;
	}

	@Override
	public int figurMenu() {

		Blocker blocker = new Blocker();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				sperre = new Pane();
				GaussianBlur gB = new GaussianBlur();
				ColorAdjust cA = new ColorAdjust();
				gB.setInput(cA);
				gUI.getGegner().setEffect(gB);
				gUI.feld.getChildren().add(sperre);
				menue = gUI.feld.figurMenu(blocker);
			}
		});
		blocker.block();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				gUI.feld.getChildren().remove(sperre);
				gUI.getGegner().setEffect(null);
			}
		});
		return menue.ret;
	}

	@Override
	public void meldungAusgeben(String meldung) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				new Meldung(meldung.split(System.lineSeparator()), gUI.feld, 20);
			}
		});
	}

	@Override
	public void aktualisieren(int x, int y) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				gUI.feld.aktualisierenFigur(x, y);
			}
		});
	}

	@Override
	public void zugGemacht() {
		final byte[] zug = letzterZug();

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				gUI.feld.zug(zug);
				if (gUI.speichern != null) {
					gUI.speichern.speichern();
				}
			}
		});
		gUI.feld.waitForAnimation();
	}

	@Override
	public void aktualisieren() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				gUI.feld.aktualisieren();
			}
		});
	}

	@Override
	public void nachricht(String s) {
		meldungAusgeben(s);
	}

	@Override
	public void farbe(int x, int y, int farbe) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				gUI.feld.farbe(x, y, farbe);
			}
		});
	}

	@Override
	public boolean sollThread() {
		return true;
	}

	@Override
	public void blink() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				gUI.getStage().toFront();
			}
		});
	}

	@Override
	public void stirb(int typ, int x, int y) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				gUI.feld.stirb(x, y);
			}
		});
	}

	@Override
	public void resetFeld() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				gUI.feld.resetBrett();
			}
		});
	}

	@Override
	public void chaterhalten(String s) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				gUI.chat.nachrichtErhalten(s);
			}
		});
	}

	@Override
	public void leaveLobby() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				gUI.getGegner().herausgefordert();
			}
		});
	}

	@Override
	public String[] getLogin(boolean b) {
		String[] ret = new String[2];
		Blocker blocker = new Blocker();

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				login = new Login(b ? AbstractGUI.meldung("passwort_falsch")
						: "", ret, blocker, gUI);
				sperre = new Pane();
				GaussianBlur gB = new GaussianBlur();
				ColorAdjust cA = new ColorAdjust();
				gB.setInput(cA);
				gUI.getGegner().setEffect(gB);
				gUI.feld.getChildren().add(sperre);
				gUI.feld.getChildren().add(login);

			}
		});
		blocker.block();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				gUI.getGegner().setEffect(null);
				gUI.feld.getChildren().remove(sperre);
				gUI.feld.getChildren().remove(login);
			}
		});
		return ret;
	}

	@Override
	public void startDenken(String s) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				gUI.getScene().setCursor(Cursor.WAIT);
			}
		});

	}

	@Override
	public void stopDenken(boolean b) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				gUI.getScene().setCursor(Cursor.DEFAULT);
			}
		});
	}

	@Override
	public void matt(String name) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				new Meldung(
						new String[] { AbstractGUI.meldung("schachmatt") },
						gUI.feld, 40);
				if (gUI.speichern != null) {
					gUI.speichern.loeschen();
				}
			}
		});

	}

	@Override
	public void schach(int x, int y) {

	}

	@Override
	public void herausforderung(long id, int herausforderung) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				gUI.getGegner().addHerausforderung(
						new Herausforderung(gUI.getGegner(), herausforderung,
								id));
			}
		});
	}

	@Override
	public void gegnerSpielVerlassen() {
		meldungAusgeben(AbstractGUI.meldung("gegner_spiel_verlassen"));
	}

	@Override
	public void herausforderungAbbrechen(int herausforderungID) {
		for(Node n: gUI.feld.getChildren())
		{
			if(n instanceof Herausforderung.HerausgefordertDialog)
			{
				((Herausforderung.HerausgefordertDialog)n).abbrechen(herausforderungID,this);
			}
		}
	}
}
