package gui;

import javafx.animation.RotateTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.util.Duration;

public class ZweiD extends MyStackPane {

	private Canvas feld = new Canvas(700, 700);
	GUI gUI;
	private Feld[][] felder;
	private DoubleProperty zoom = new SimpleDoubleProperty(1.0);

	public ZweiD(GUI gUI) {
		this.gUI = gUI;
		felder = new Feld[gUI.spiel.getXMax() + 1][gUI.spiel.getYMax() + 1];
		for (int a = 0; a < gUI.spiel.getYMax() + 1; a++) {
			for (int b = 0; b < gUI.spiel.getXMax() + 1; b++) {
				felder[b][a] = new Feld(gUI, this, b, a);
			}
		}
		feld.setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				if (zoom.get() > 0.5 || event.getDeltaY() > 0) {
					zoom.set(zoom.get() + event.getDeltaY() / 400);
				}
			}
		});
		//zoom.set(feld.getWidth());
		// zoom.set(700);
		feld.scaleYProperty().bind(zoom);
		feld.scaleXProperty().bind(zoom);
		feld.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				gUI.spiel.klick(
						((int) (event.getX() / (feld.getWidth() / felder.length))),
						((int) (event.getY() / (feld.getHeight() / felder.length))));
				aktualisieren();
			}
		});
		drehen();
		getChildren().add(feld);
	}

	@Override
	public void drehen() {
		RotateTransition rt = new RotateTransition(Duration.millis(1500), feld);
		rt.setByAngle(180);
		rt.play();
	}

	@Override
	public void zug() {
		aktualisieren();
	}

	@Override
	public void aktualisieren() {
		for (int x = 0; x < gUI.spiel.getXMax() + 1; x++) {
			for (int y = 0; y < gUI.spiel.getYMax() + 1; y++) {
			}
		}
	}

	@Override
	public void aktualisierenFigur(int x, int y) {
		int figur = felder[x][y].gebeInhalt();

		if (figur != 0) {
			Image img;
			String f = "";
			switch (Math.abs(figur)) {
			case 1:
				f = "turm";
				break;
			case 4:
				f = "springer";
				break;
			case 2:
				f = "laeufer";
				break;
			case 3:
				f = "dame";
				break;
			case 16:
				f = "koenig";
				break;
			case 8:
				f = "bauer";
				break;
			}
			if (figur < 0) {
				f = f + "_schwarz.png";
				img = new Image(this.getClass().getClassLoader()
						.getResource("gui/bilder/" + f).toString());
			} else {
				f = f + "_weiss.png";
				img = new Image(this.getClass().getClassLoader()
						.getResource("gui/bilder/" + f).toString());
			}

			feld.getGraphicsContext2D().drawImage(img, translateX(x),
					translateY(y), feld.getWidth() / gUI.spiel.getXMax(),
					feld.getHeight() / gUI.spiel.getYMax());
		}
	}

	@Override
	public void startaufstellung() {
		
	}

	@Override
	public void resetBrett() {
		feld.getGraphicsContext2D().drawImage(gUI.brettbild, 0, 0, feld.getWidth(), feld.getHeight());
		aktualisieren();
	}

	@Override
	public Image getScreenshot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void farbe(int x, int y, int farbe) {
		if (gUI.getFarbe()) {
			String f = "";
			switch (farbe) {

			case 3:
				f = "gruen";
				break;
			case 4:
				f = "rot";
				break;
			case 5:
				f = "gelb";
				break;
			}
			feld.getGraphicsContext2D()
					.drawImage(
							new Image(this.getClass().getClassLoader()
									.getResource("gui/bilder/" + f + ".png")
									.toString()), translateX(x), translateY(y),
							feld.getWidth() / gUI.spiel.getXMax(),
							feld.getHeight() / gUI.spiel.getYMax());
			// startaufstellung();
		}
	}

	@Override
	public void aktualisiereMap() {
		// TODO Auto-generated method stub

	}

	protected double translateX(int x) {
		return (feld.getWidth() / felder.length) * (gUI.spiel.getXMax() - x);
	}

	protected double translateY(int y) {
		return (feld.getHeight() / felder.length) * y;
	}

	@Override
	public Node getFeld() {
		return feld;
	}

}
