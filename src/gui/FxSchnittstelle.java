package gui;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import spiel.Schnittstelle;

public class FxSchnittstelle extends Schnittstelle {

	GUI gUI;
	Login login;
	Pane sperre;

	public FxSchnittstelle(GUI gUI) {
		this.gUI = gUI;
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
				gUI.stage.toFront();
			}
		});
	}

	// @Override
	public void klickf(final int x, final int y) {
		final Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				FxSchnittstelle.super.klick(x, y);
				return null;
			}
		};
		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();
	}

	@Override
	public void stirb(int typ, int x, int y) {

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
				gUI.gegner.herausgefordert();
			}
		});
	}

	@Override
	public void herausforderung(String s) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				new HerausgefordertDialog(FxSchnittstelle.this, s);
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
				login = new Login(b?Schnittstelle.meldung("passwort_falsch"):"", ret, blocker);
				sperre=new Pane();
				GaussianBlur gB = new GaussianBlur();
				ColorAdjust cA = new ColorAdjust();
				gB.setInput(cA);
				gUI.gegner.setEffect(gB);
				gUI.feld.getChildren().add(sperre);
				gUI.feld.getChildren().add(login);

			}
		});
		blocker.block();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				gUI.gegner.setEffect(null);
				gUI.feld.getChildren().remove(sperre);
				gUI.feld.getChildren().remove(login);
			}
		});
		return ret;
	}
}
