package gui;

import java.io.File;
import java.net.URISyntaxException;

import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Mesh;
import javafx.stage.Stage;
import client.Client;

import com.interactivemesh.jfx.importer.stl.StlMeshImporter;

public class GUI extends Application {

	Stage stage;
	BorderPane root = new BorderPane();
	BorderPane pane = new BorderPane();
	// BorderPane root = new BorderPane();
	Einstellungen rechts;

	protected String hintergrund = "marmor";
	PhongMaterial feldMaterial = new PhongMaterial();

	FxSchnittstelle spiel = new FxSchnittstelle(this);
	private boolean farbe = true;

	public String form = "standard";
	public boolean modell_farbe = false;
	SimpleObjectProperty<Color> farbe_weiss = new SimpleObjectProperty<Color>(
			Color.AZURE);
	SimpleObjectProperty<Color> farbe_schwarz = new SimpleObjectProperty<Color>(
			Color.NAVY);
	Image brettbild = new Image(this.getClass().getClassLoader()
			.getResource("gui/bilder/brett.png").toString());
	Chat chat = new Chat(this);
	String name = System.getProperty("user.name");
	public boolean zweid = true;
	Einstellungen einstellungen = new Einstellungen(this);
	GegnerWaehler gegner = new GegnerWaehler(this);
	Client client;

	public MyStackPane feld;

	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		if (!Platform.isSupported(ConditionalFeature.SCENE3D) || zweid) {
			feld = new ZweiD(this);
			brettbild = new Image(this.getClass().getClassLoader()
					.getResource("gui/bilder/brett2d.png").toString());
		} else {
			feld = new DreiD(this);
		}
		this.stage = stage;

		pane.setCenter(feld);

		// pane.setLeft(xslider);
		// pane.setBottom(yslider);
		// pane.setTop(zslider);
		// xslider.setOrientation(Orientation.VERTICAL);
		// zslider.setOrientation(Orientation.VERTICAL);

		root.setCenter(pane);
		Kontrollfeld kontrolle = new Kontrollfeld(this);
		root.setBottom(kontrolle);
		Scene scene = new Scene(root, 1200, 800);

		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.isControlDown()) {
					if (keyEvent.getCode() == KeyCode.C) {
						final Clipboard clipboard = Clipboard
								.getSystemClipboard();
						final ClipboardContent content = new ClipboardContent();
						content.putImage(feld.getScreenshot());
						clipboard.setContent(content);
						spiel.meldungAusgeben("Screenshot in Zwischenablage kopiert");
					}
				}
			}
		});

		// aktualisieren();
		feld.startaufstellung();
		feld.resetBrett();
		feld.aktualisiereMap();

		// scene.onMouseDraggedProperty().set(new MouseEventHandler());
		stage.setScene(scene);
		stage.setTitle("Super Schach");
		stage.getIcons()
				.add(new Image(this.getClass().getClassLoader()
						.getResource("gui/bilder/bauer_schwarz.png").toString()));
		einstellungen.laden();
		stage.show();

		gegner.show();
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

	public boolean getFarbe() {
		return farbe;
	}

	public static void main(String args[]) throws Exception {
		launch(args);
	}
}
