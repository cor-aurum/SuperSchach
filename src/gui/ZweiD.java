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
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class ZweiD extends MyStackPane {

	private Rectangle feld = new Rectangle(700, 700);
	GUI gUI;
	private Feld[][] felder;
	private DoubleProperty zoom = new SimpleDoubleProperty(0.0);
	ImagePattern imagePattern;

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
					zoom.set(zoom.get() + event.getDeltaY() / 5);
				}
			}
		});
		imagePattern = new ImagePattern(gUI.brettbild);
		zoom.set(feld.getWidth());
		// zoom.set(700);
		feld.heightProperty().bind(zoom);
		feld.widthProperty().bind(zoom);
		feld.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				gUI.spiel.klick(
						gUI.spiel.getXMax()
								- ((int) (event.getX() / (feld.getWidth() / felder.length))),
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
		// TODO Auto-generated method stub

	}

	@Override
	public void aktualisieren() {
		// TODO Auto-generated method stub

	}

	@Override
	public void aktualisierenFigur(int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void startaufstellung() {
		for (int x = 0; x < gUI.spiel.getXMax() + 1; x++) {
			for (int y = 0; y < gUI.spiel.getYMax() + 1; y++) {
				int figur = felder[x][y].gebeInhalt();
				String f = "";
				switch (Math.abs(figur)) {
				case 1:
					f = "turm";
				case 4:
					f = "springer";
				case 2:
					f = "laeufer";
				case 3:
					f = "dame";
				case 16:
					f = "koenig";
				case 8:
					f = "bauer";
				default:
					f = "";
				}
				Image img;
				if (figur < 0) {
					f += "_schwarz";
					img = new Image(this.getClass().getClassLoader()
							.getResource("gui/bilder/koenig_schwarz.png").toString());
				} else {
					f += "_weiss";
					img = new Image(this.getClass().getClassLoader()
							.getResource("gui/bilder/koenig_weiss.png").toString());
					
				}
				if (figur != 0) {
					Canvas canvas = new Canvas(feld.getWidth(),
							feld.getHeight());
					canvas.getGraphicsContext2D().drawImage(
							imagePattern.getImage(), 0, 0, feld.getWidth(),
							feld.getHeight());
					canvas.getGraphicsContext2D().drawImage(
							img, translateX(x), translateY(y),
							feld.getWidth() / gUI.spiel.getXMax(),
							feld.getHeight() / gUI.spiel.getYMax());
					imagePattern = new ImagePattern(canvas.snapshot(null, null));
					feld.setFill(imagePattern);
				}
			}
		}
	}

	@Override
	public void resetBrett() {
		imagePattern = new ImagePattern(gUI.brettbild);
		feld.setFill(imagePattern);
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
			Canvas canvas = new Canvas(feld.getWidth(), feld.getHeight());
			canvas.getGraphicsContext2D().drawImage(imagePattern.getImage(), 0,
					0, feld.getWidth(), feld.getHeight());
			canvas.getGraphicsContext2D()
					.drawImage(
							new Image(this.getClass().getClassLoader()
									.getResource("gui/bilder/" + f + ".png")
									.toString()), translateX(x), translateY(y),
							feld.getWidth() / gUI.spiel.getXMax(),
							feld.getHeight() / gUI.spiel.getYMax());
			imagePattern = new ImagePattern(canvas.snapshot(null, null));
			feld.setFill(imagePattern);
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
