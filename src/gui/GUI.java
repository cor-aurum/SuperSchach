package gui;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GUI extends Application {

	BorderPane pane = new BorderPane();
	BorderPane root = new BorderPane();
	Node rechts;
	Box feld = new Box(500, 500, 10);
	Xform root3D = new Xform();
	protected String hintergrund = "marmor";
	PhongMaterial feldMaterial = new PhongMaterial();
	Slider xslider = new Slider();
	Slider yslider = new Slider();
	Slider zslider = new Slider();
	private Feld[][] felder;
	private Figur[][] figuren;
	FxSchnittstelle spiel = new FxSchnittstelle(this);
	private boolean farbe = true;
	Canvas brett = new Canvas(feld.getWidth(), feld.getHeight());
	private DoubleProperty zoom = new SimpleDoubleProperty(0.0);
	private Box[] rand = new Box[4];
	public String form="standard";
	public boolean modell_farbe=false;

	@Override
	public void start(Stage stage) throws Exception {
		PerspectiveCamera kamera = new PerspectiveCamera();
		// kamera.setFieldOfView(50.0);
		felder = new Feld[spiel.getXMax() + 1][spiel.getYMax() + 1];
		figuren = new Figur[spiel.getXMax() + 1][spiel.getYMax() + 1];
		for (int a = 0; a < spiel.getYMax() + 1; a++) {
			for (int b = 0; b < spiel.getXMax() + 1; b++) {
				felder[b][a] = new Feld(this, b, a);
				figuren[b][a] = null;
			}
		}

		root3D.getChildren().add(feld);

		for (int i = 0; i < 4; i++) {
			int temp = (i & 1) * 500;
			rand[i] = new Box(temp + 20, 520 - temp, 10);
			root3D.getChildren().add(rand[i]);
			if (temp == 500) {
				rand[i].setTranslateY(i == 1 ? 250 : -250);
			} else {
				rand[i].setTranslateX(i == 0 ? 250 : -250);
			}
			rand[i].setTranslateZ(feld.getTranslateZ());
		}
		rechts = new Rechts(this);
		pane.setRight(rechts);
		root.setCenter(pane);
		pane.setCenter(root3D);
		pane.setLeft(xslider);
		pane.setBottom(yslider);
		pane.setTop(zslider);
		xslider.setOrientation(Orientation.VERTICAL);
		// zslider.setOrientation(Orientation.VERTICAL);

		resetBrett();

		feld.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				spiel.klick(((int) (event.getX()
						/ (feld.getWidth() / felder.length) + 4)),
						((int) (event.getY()
								/ (feld.getHeight() / felder.length) + 4)));
				aktualisieren();
			}
		});

		Scene scene = new Scene(root, 1200, 800);

		scene.setCamera(kamera);

		scene.setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				if (zoom.get() > 0.5 || event.getDeltaY() > 0) {
					zoom.set(zoom.get() + event.getDeltaY() / 400);
				}
			}
		});
		zoom.set(root3D.s.getX());
		root3D.s.xProperty().bind(zoom);
		root3D.s.yProperty().bind(zoom);
		root3D.s.zProperty().bind(zoom);

		xslider.setMin(110);
		xslider.setMax(250);
		yslider.setMin(0);
		yslider.setMax(180);
		zslider.setMin(-80);
		zslider.setMax(80);
		root3D.rx.angleProperty().bind(xslider.valueProperty());
		root3D.rz.angleProperty().bind(yslider.valueProperty());
		root3D.ry.angleProperty().bind(zslider.valueProperty());
		aktualisieren();
		aktualisiereMap();

		// scene.onMouseDraggedProperty().set(new MouseEventHandler());
		stage.setScene(scene);
		stage.setTitle("Super Schach");
		stage.getIcons()
				.add(new Image(this.getClass().getClassLoader()
						.getResource("gui/bilder/bauer_schwarz.png").toString()));
		stage.show();
		Timeline animation = new Timeline();
		animation.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO, new KeyValue(
						yslider.valueProperty(), 180d)),
				new KeyFrame(Duration.ZERO, new KeyValue(xslider
						.valueProperty(), 0d)),
				new KeyFrame(Duration.valueOf("1s"), new KeyValue(yslider
						.valueProperty(), 0d)),
				new KeyFrame(Duration.valueOf("1.5s"), new KeyValue(xslider
						.valueProperty(), 150d)));

		animation.play();
	}

	public void aktualisiereMap() {
		/*
		 * feldMaterial.setDiffuseMap(new Image(this.getClass().getClassLoader()
		 * .getResource("gui/bilder/brett.png").toString()));
		 */
		feldMaterial.setDiffuseMap(brett.snapshot(null, null));
		feldMaterial.setBumpMap(new Image(this.getClass().getClassLoader()
				.getResource("gui/bilder/" + hintergrund + "_NRM.png")
				.toString()));
		feldMaterial.setSpecularMap(new Image(this.getClass().getClassLoader()
				.getResource("gui/bilder/" + hintergrund + "_SPEC.png")
				.toString()));
		feld.setMaterial(feldMaterial);

		PhongMaterial material = new PhongMaterial();
		switch (hintergrund) {
		case "marmor":
			material.setDiffuseColor(Color.BLANCHEDALMOND);
			material.setSpecularColor(Color.CHOCOLATE);
			break;
		case "gras":
			material.setDiffuseColor(Color.BEIGE);
			material.setSpecularColor(Color.WHITE);
			break;
		case "holz":
			material.setDiffuseColor(Color.DARKOLIVEGREEN);
			material.setSpecularColor(Color.GREEN);
			break;
		default:
			material.setDiffuseColor(Color.AQUA);
			material.setSpecularColor(Color.AQUAMARINE);
			break;
		}
		for (int i = 0; i < 4; i++) {
			rand[i].setMaterial(material);
		}
	}

	public void farbe(int x, int y, int farbe) {
		if (this.farbe) {
			switch (farbe) {
			case 3:
				brett.getGraphicsContext2D()
						.drawImage(
								new Image(this.getClass().getClassLoader()
										.getResource("gui/bilder/gruen.png")
										.toString()), translateX(x),
								translateY(y));
				break;
			case 4:
				brett.getGraphicsContext2D().drawImage(
						new Image(this.getClass().getClassLoader()
								.getResource("gui/bilder/rot.png").toString()),
						translateX(x), translateY(y));
				break;
			case 5:
				brett.getGraphicsContext2D()
						.drawImage(
								new Image(this.getClass().getClassLoader()
										.getResource("gui/bilder/gelb.png")
										.toString()), translateX(x),
								translateY(y));
				break;
			}
			feldMaterial.setDiffuseMap(brett.snapshot(null, null));
		}
	}

	public void resetBrett() {
		brett.getGraphicsContext2D().drawImage(
				new Image(this.getClass().getClassLoader()
						.getResource("gui/bilder/brett.png").toString()), 0, 0);
	}

	protected double translateX(int x) {
		return (feld.getWidth() / felder.length) * (7 - x);
	}

	protected double translateY(int y) {
		return (feld.getHeight() / felder.length) * y;
	}

	public void aktualisieren() {
		resetBrett();
		for (int x = 0; x < felder.length; x++)
			for (int y = 0; y < felder[x].length; y++) {
				aktualisierenFigur(x, y);
			}
		System.gc();
	}

	public void aktualisierenFigur(int x, int y) {
		int figur = felder[x][y].gebeInhalt();
		if (figur != 0) {
			if (figuren[x][y] == null) {
				figuren[x][y] = new Figur(felder[x][y], figur);
			} else if (figur != figuren[x][y].figur) {
				figuren[x][y].stirb();
				figuren[x][y] = new Figur(felder[x][y], figur);
			}
		} else {
			if (figuren[x][y] != null) {
				figuren[x][y].stirb();
				figuren[x][y] = null;
			}
		}
	}

	public void drehen() {
		// xslider.setValue(xslider.getMin()+(xslider.getMax()-xslider.getValue()));
		// yslider.setValue(yslider.getMin()+(yslider.getMax()-yslider.getValue()));
		Timeline animation = new Timeline();
		animation.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO, new KeyValue(
						yslider.valueProperty(), yslider.getValue())),
				new KeyFrame(Duration.ZERO, new KeyValue(xslider
						.valueProperty(), xslider.getValue())),
				new KeyFrame(Duration.ZERO, new KeyValue(zslider
						.valueProperty(), zslider.getValue())),
				new KeyFrame(Duration.valueOf("1s"), new KeyValue(zslider
						.valueProperty(), zslider.getMin()
						+ (zslider.getMax() - zslider.getValue()))),
				new KeyFrame(Duration.valueOf("1s"), new KeyValue(yslider
						.valueProperty(), yslider.getMin()
						+ (yslider.getMax() - yslider.getValue()))),
				new KeyFrame(Duration.valueOf("1s"), new KeyValue(xslider
						.valueProperty(), xslider.getMin()
						+ (xslider.getMax() - xslider.getValue()))));
		animation.play();
	}

	public static void main(String args[]) throws Exception {
		launch(args);
	}
}
