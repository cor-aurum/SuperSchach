package gui;

import java.io.File;
import java.net.URISyntaxException;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Mesh;
import javafx.stage.Stage;
import javafx.util.Duration;

import com.interactivemesh.jfx.importer.stl.StlMeshImporter;

public class GUI extends Application {

	Stage stage;
	BorderPane pane = new BorderPane();
	// BorderPane root = new BorderPane();
	Rechts rechts;
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
	public String form = "standard";
	public boolean modell_farbe = false;
	SimpleObjectProperty<Color> farbe_weiss = new SimpleObjectProperty<Color>(
			Color.AZURE);
	SimpleObjectProperty<Color> farbe_schwarz = new SimpleObjectProperty<Color>(
			Color.NAVY);

	@Override
	public void start(Stage stage) throws Exception {
		if (!Platform.isSupported(ConditionalFeature.SCENE3D)) {
			System.out.println("3D wird von diesem System nicht unterstützt");
			return;
		}
		this.stage = stage;
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
		// feld.toBack();

		for (int i = 0; i < 4; i++) {
			int temp = (i & 1) * 520;
			rand[i] = new Box(temp + 20, 540 - temp, 10);
			root3D.getChildren().add(rand[i]);
			if (temp == 520) {
				rand[i].setTranslateY(i == 1 ? 260 : -260);
			} else {
				rand[i].setTranslateX(i == 0 ? 260 : -260);
			}
			rand[i].setTranslateZ(feld.getTranslateZ());
		}
		// rechts = new Rechts(this);
		// pane.setRight(rechts);
		// root.setCenter(pane);
		StackPane ablage = new StackPane();
		SubScene subscene = new SubScene(root3D, 0, 0, true,
				SceneAntialiasing.BALANCED);
		subscene.widthProperty().bind(ablage.widthProperty());
		subscene.heightProperty().bind(ablage.heightProperty());
		root3D.translateXProperty().bind(ablage.widthProperty().divide(2));
		root3D.translateYProperty().bind(ablage.heightProperty().divide(2));
		subscene.setFill(Color.GRAY);
		ablage.getChildren().add(subscene);
		pane.setCenter(ablage);

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

		Scene scene = new Scene(pane, 1200, 800);

		subscene.setCamera(kamera);

		subscene.setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				if (zoom.get() > 0.5 || event.getDeltaY() > 0) {
					zoom.set(zoom.get() + event.getDeltaY() / 400);
				}
			}
		});

		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.isControlDown()) {
					if (keyEvent.getCode() == KeyCode.C) {
						final Clipboard clipboard = Clipboard
								.getSystemClipboard();
						final ClipboardContent content = new ClipboardContent();
						content.putImage(subscene.snapshot(null, null));
						clipboard.setContent(content);
						spiel.meldungAusgeben("Screenshot in Zwischenablage kopiert");
					}
				}
			}
		});

		zoom.set(root3D.s.getX());
		// zoom.set(700);
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
		// aktualisieren();
		startaufstellung();
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
						.valueProperty(), xslider.getMax())),
				new KeyFrame(Duration.valueOf("1s"), new KeyValue(yslider
						.valueProperty(), 0d)),
				new KeyFrame(Duration.valueOf("1.5s"), new KeyValue(xslider
						.valueProperty(), 150d)));

		animation.play();
		spiel.ki(4, 1, 4);
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
			feldMaterial.setSpecularPower(32.0);
			break;
		case "gras":
			material.setDiffuseColor(Color.BEIGE);
			material.setSpecularColor(Color.WHITE);
			feldMaterial.setSpecularPower(128.0);
			break;
		case "holz":
			material.setDiffuseColor(Color.DARKOLIVEGREEN);
			material.setSpecularColor(Color.GREEN);
			feldMaterial.setSpecularPower(1024.0);
			break;
		case "glas":
			material.setDiffuseColor(Color.TRANSPARENT);
			material.setSpecularColor(Color.WHITE);
			feldMaterial.setSpecularPower(2.0);
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
			zug();
		}
	}

	public void resetBrett() {
		brett.getGraphicsContext2D().drawImage(
				new Image(this.getClass().getClassLoader()
						.getResource("gui/bilder/brett.png").toString()), 0, 0);
	}

	protected double translateX(int x) {
		return (feld.getWidth() / felder.length) * (spiel.getXMax() - x);
	}

	protected double translateY(int y) {
		return (feld.getHeight() / felder.length) * y;
	}

	public void aktualisieren() {
		resetBrett();
		for (int x = 0; x < felder.length; x++)
			for (int y = 0; y < felder[x].length; y++) {
				//aktualisierenFigur(x, y);
			}
		System.gc();
	}

	public void aktualisierenFigur(int x, int y) {
		int figur = felder[x][y].gebeInhalt();
		if (figur != 0) {
			if (figuren[x][y] != null) {
				if (figur != ((Figur) figuren[x][y]).figur) {
					root3D.getChildren().remove(figuren[x][y]);
					// figuren[x][y] = new Figur(felder[x][y], figur);
					// zug();
				}
			}
		}
	}

	public void startaufstellung() {
		for (int x = 0; x < spiel.getXMax() + 1; x++) {
			for (int y = 0; y < spiel.getYMax() + 1; y++) {
				int figur = felder[x][y].gebeInhalt();
				if (figur != 0) {
					figuren[x][y] = new Figur(felder[x][y], figur);
				} else {
					figuren[x][y] = null;
				}
			}
		}
	}

	public void zug() {
		byte[] zug = spiel.letzterZug();
		int summe = 0;
		for (int i = 0; i < zug.length; i++) {
			summe += zug[i];
		}
		assert summe != 0;
		final int sum = spiel.getXMax();
		Feld anfang = felder[sum - zug[0]][zug[1]];
		Feld ende = felder[sum - zug[2]][zug[3]];
		// Figur tempfigur = new Figur(anfang, anfang.gebeInhalt());
		Figur tempfigur;
		try {
			tempfigur = figuren[sum - anfang.x][anfang.y];// new
															// MeshView(gebeMesh(ende.gebeInhalt()));
			// tempfigur.setMaterial(gebeFigurenMaterial(ende.gebeInhalt()));
			// root3D.getChildren().add(tempfigur);
			double unsauber = xslider.getValue() == xslider.getMax() ? -0.00000001
					: 0.00000001;
			Timeline animation = new Timeline(60.0);
			animation.getKeyFrames().addAll(
					new KeyFrame(Duration.ZERO, new KeyValue(
							xslider.valueProperty(), xslider.getValue())),
					new KeyFrame(Duration.ZERO, new KeyValue(tempfigur
							.translateXProperty(), anfang.getX())),
					new KeyFrame(Duration.ZERO, new KeyValue(tempfigur
							.translateYProperty(), anfang.getY())),
					new KeyFrame(Duration.valueOf("0.15s"), new KeyValue(
							xslider.valueProperty(), xslider.getValue()
									+ unsauber)),
					new KeyFrame(Duration.valueOf("0.3s"), new KeyValue(xslider
							.valueProperty(), xslider.getValue())),
					new KeyFrame(Duration.valueOf("0.3s"), new KeyValue(
							tempfigur.translateXProperty(), ende.getX())),
					new KeyFrame(Duration.valueOf("0.3s"), new KeyValue(
							tempfigur.translateYProperty(), ende.getY())));
			animation.play();
			animation.setOnFinished(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					// root3D.getChildren().remove(tempfigur);
					figuren[sum - anfang.x][anfang.y] = null;
					figuren[sum - ende.x][ende.y] = tempfigur;
					figuren[sum - ende.x][ende.y]
							.setFeld(felder[sum - ende.x][ende.y]);
					
				}
			});
		} catch (Exception e) {
			// System.out.println("Animation nicht gefunden");
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

	public PhongMaterial gebeFigurenMaterial(int figur) {
		PhongMaterial material = new PhongMaterial();
		if (figur > 0) {
			material.diffuseColorProperty().bind(farbe_weiss);
			material.specularColorProperty().bind(farbe_weiss);
		} else {
			material.diffuseColorProperty().bind(farbe_schwarz);
			material.specularColorProperty().bind(farbe_schwarz);
		}
		return material;
	}

	public String gebeFigur(int figur) {
		switch (Math.abs(figur)) {
		case 1:
			return "turm";
		case 4:
			return "springer";
		case 2:
			return "laeufer";
		case 3:
			return "dame";
		case 16:
			return "koenig";
		case 8:
			return "bauer";
		default:
			return "";
		}
	}

	public Mesh gebeMesh(int figur) throws Exception {
		String modell = gebeFigur(figur);
		if (modell_farbe) {
			if (figur > 0) {
				modell += "_weiss";
			} else {
				modell += "_schwarz";
			}
		}
		File file = null;
		try {
			file = new File(this.getClass().getClassLoader()
					.getResource("gui/meshes/" + form + "_" + modell + ".stl")
					.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		StlMeshImporter importer = new StlMeshImporter();
		importer.read(file);
		return importer.getImport();
	}
	
	public Figur[][] gebeFiguren()
	{
		return figuren;
	}

	public static void main(String args[]) throws Exception {
		launch(args);
	}
}
