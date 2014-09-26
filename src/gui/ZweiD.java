package gui;

import javafx.animation.RotateTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class ZweiD extends MyStackPane {

	private StackPane root = new StackPane();
	private Pane figurenEbene = new Pane();
	private Canvas feld = new Canvas(700, 700);
	GUI gUI;
	private Feld[][] felder;
	private DoubleProperty zoom = new SimpleDoubleProperty(1.0);
	private boolean rotate = true;

	public ZweiD(GUI gUI) {
		this.gUI = gUI;
		root.setMaxWidth(700);
		root.setMaxHeight(700);
		root.getChildren().addAll(feld, figurenEbene);
		felder = new Feld[gUI.spiel.getXMax() + 1][gUI.spiel.getYMax() + 1];
		for (int a = 0; a < gUI.spiel.getYMax() + 1; a++) {
			for (int b = 0; b < gUI.spiel.getXMax() + 1; b++) {
				felder[b][a] = new Feld(gUI, this, b, a);
			}
		}
		root.setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				if (zoom.get() > 0.5 || event.getDeltaY() > 0) {
					zoom.set(zoom.get() + event.getDeltaY() / 400);
				}
			}
		});
		// zoom.set(feld.getWidth());
		// zoom.set(700);
		feld.scaleYProperty().bind(root.scaleYProperty());
		feld.scaleXProperty().bind(root.scaleXProperty());
		figurenEbene.scaleYProperty().bind(root.scaleYProperty());
		figurenEbene.scaleXProperty().bind(root.scaleXProperty());
		root.scaleYProperty().bind(zoom);
		root.scaleXProperty().bind(zoom);
		figurenEbene.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				gUI.spiel.klick(
						gUI.spiel.getXMax()
								- ((int) (event.getX() / (feld.getWidth() / felder.length))),
						((int) (event.getY() / (feld.getHeight() / felder.length))));
				aktualisieren();
			}
		});

		getChildren().add(root);
		drehen();
	}

	@Override
	public void drehen() {
		RotateTransition rt = new RotateTransition(Duration.millis(1500), root);
		rt.setByAngle(180);
		rt.play();
		for (Node iV : figurenEbene.getChildren()) {
			RotateTransition rt2 = new RotateTransition(Duration.millis(1500),
					iV);
			rt2.setByAngle(-180);
			rt2.play();
			if (iV.getRotate() == 0) {
				rotate = true;
			} else {
				rotate = false;
			}
		}
	}

	@Override
	public void zug(byte[] zug) {
		aktualisieren();
	}

	@Override
	public void aktualisieren() {
		figurenEbene.getChildren().clear();
		for (int x = 0; x < gUI.spiel.getXMax() + 1; x++) {
			for (int y = 0; y < gUI.spiel.getYMax() + 1; y++) {
				aktualisierenFigur(x, y);
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
				if(f.equals("koenig_schwarz.png")&&gUI.spiel.getStatus()==1 && !gUI.spiel.Player0())
				{
					f="koenig_rot.png";
				}
				img = new Image(this.getClass().getClassLoader()
						.getResource("gui/bilder/" + f).toString());
			} else {
				f = f + "_weiss.png";
				if(f.equals("koenig_weiss.png")&&gUI.spiel.getStatus()==1 && gUI.spiel.Player0())
				{
					f="koenig_rot.png";
				}
				img = new Image(this.getClass().getClassLoader()
						.getResource("gui/bilder/" + f).toString());
			}
			ImageView iV = new ImageView(img);
			iV.setFitWidth(700 / gUI.spiel.getXMax());
			iV.setFitHeight(700 / gUI.spiel.getYMax());
			iV.relocate(translateX(gUI.spiel.getXMax() - x), translateY(y));
			if (rotate)
				iV.setRotate(180);
			figurenEbene.getChildren().add(iV);
		}
	}

	@Override
	public void startaufstellung() {
		aktualisieren();
	}

	@Override
	public void resetBrett() {
		feld.getGraphicsContext2D().drawImage(gUI.brettbild2d, 0, 0,
				feld.getWidth(), feld.getHeight());
		aktualisieren();
	}

	@Override
	public Image getScreenshot() {
		return feld.snapshot(null, null);
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

	@Override
	public void entferneFiguren() {

	}

	@Override
	public Node getRoot() {
		return root;
	}
}
