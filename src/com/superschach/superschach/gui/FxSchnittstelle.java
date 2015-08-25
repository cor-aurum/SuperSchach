package com.superschach.superschach.gui;

import com.superschach.superschach.network.AbortReason;
import com.superschach.superschach.spiel.Schnittstelle;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;

public class FxSchnittstelle extends Schnittstelle {

	GUI gUI;
	Login login;
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
		new Meldung(meldung.split(System.lineSeparator()), gUI.feld, 20);
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
		// TODO Auto-generated method stub

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
				login = new Login(b ? Schnittstelle.meldung("passwort_falsch")
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
				gUI.feld.getChildren().add(new Warten());
			}
		});

	}

	@Override
	public void stopDenken(boolean b) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					for (Node n : gUI.feld.getChildren()) {
						if (n instanceof Warten) {
							gUI.feld.getChildren().remove(n);
						}
					}
				} catch (Exception e) {

				}
			}
		});
	}

	@Override
	public void matt(String name) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				new Meldung(
						new String[] { Schnittstelle.meldung("schachmatt") },
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
	public void spielBeendet(AbortReason grund) {
		meldungAusgeben(grund.name());
	}
}
