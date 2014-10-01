package gui;

import java.io.ObjectInputStream;

import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Mesh;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import spiel.Schnittstelle;
import client.Client;

import com.sun.javafx.application.LauncherImpl;

public class GUI extends Application {

	Stage stage;
	// BorderPane root = new BorderPane();
	// BorderPane pane = new BorderPane();
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
	Image brettbild2d = new Image(this.getClass().getClassLoader()
			.getResource("gui/bilder/brett2d.png").toString());
	Chat chat = new Chat(this);
	String name = System.getProperty("user.name");
	SimpleBooleanProperty zweid = new SimpleBooleanProperty(false);
	SimpleBooleanProperty sounds = new SimpleBooleanProperty(true);

	SimpleStringProperty vonFarbe = new SimpleStringProperty("#cd5c5c");
	SimpleStringProperty bisFarbe = new SimpleStringProperty("#232323");

	Einstellungen einstellungen;
	GegnerWaehler gegner;
	Client client;
	public MyStackPane feld;
	static SimpleBooleanProperty geladen = new SimpleBooleanProperty(false);
	Hauptmenu hauptmenu = new Hauptmenu(this);
	Kontrollfeld kontrolle;
	Speichern speichern;

	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;

		einstellungen = new Einstellungen(this);
		einstellungen.laden();
		
		if (!Platform.isSupported(ConditionalFeature.SCENE3D)
				|| zweid.getValue()) {
			feld = new ZweiD(this);
		} else {
			feld = new DreiD(this);
		}
		this.stage = stage;
		Scene scene = new Scene(feld, 1200, 800);
		scene.getStylesheets().add("gui/style.css");
		stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
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
						spiel.meldungAusgeben(Schnittstelle
								.meldung("screenshot"));
					}
				} else {
					if (keyEvent.getCode() == KeyCode.ESCAPE) {
						if (!hauptmenu.isShowed()) {
							hauptmenu.show();
						}
					}
				}
			}
		});
		kontrolle = new Kontrollfeld(this);
		gegner = new GegnerWaehler(this);
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				Client client = GUI.this.client;
				if (client != null) {
					client.close();
				}
				System.exit(0);
			}
		});
		// aktualisieren();
		feld.startaufstellung();
		feld.resetBrett();
		feld.aktualisiereMap();

		vonFarbe.addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String arg2) {
				feld.background
						.setStyle("-fx-background-color:linear-gradient(from 25% 25% to 100% 100%, "
								+ vonFarbe.getValue()
								+ ", "
								+ bisFarbe.getValue() + ");");
			}

		});
		bisFarbe.addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String arg2) {
				feld.background
						.setStyle("-fx-background-color:linear-gradient(from 25% 25% to 100% 100%, "
								+ vonFarbe.getValue()
								+ ", "
								+ bisFarbe.getValue() + ");");
			}

		});

		feld.background
				.setStyle("-fx-background-color:linear-gradient(from 25% 25% to 100% 100%, "
						+ vonFarbe.getValue()
						+ ", "
						+ bisFarbe.getValue()
						+ ");");
		//feld.getChildren().add(kontrolle);
		StackPane.setAlignment(kontrolle, Pos.BOTTOM_RIGHT);
		// scene.onMouseDraggedProperty().set(new MouseEventHandler());
		stage.setScene(scene);
		stage.setTitle("Super Schach");
		stage.getIcons()
				.add(new Image(this.getClass().getClassLoader()
						.getResource("gui/bilder/bauer_schwarz.png").toString()));

		gegner.show();

		geladen.setValue(true);
		stage.show();
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call()  {
				gegner.starteVerbindung();
				gegner.starteAktualisierung();

				return null;
			}
		};
		new Thread(task).start();
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
		MyMesh mesh = new MyMesh();
		ObjectInputStream ois = null;
		try {
			/*
			 * file = new File(this.getClass().getClassLoader()
			 * .getResource("gui/meshes/" + form + "_" + modell + ".stl")
			 * .toURI());
			 */
			ois = new ObjectInputStream(this
					.getClass()
					.getClassLoader()
					.getResourceAsStream(
							"gui/meshes/" + form + "_" + modell + ".figur"));
			mesh = (MyMesh) ois.readObject();

			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			ois.close();
		}
		return mesh.getMesh();
	}

	public boolean getFarbe() {
		return farbe;
	}

	public static void main(String args[]) throws Exception {
		LauncherImpl.launchApplication(GUI.class, SplashScreen.class, args);
		launch(args);
	}

	public void wechsleDimension() {
		if (!Platform.isSupported(ConditionalFeature.SCENE3D)
				|| zweid.getValue()) {
			feld = new ZweiD(this);
		} else {
			feld = new DreiD(this);
		}
		feld.entferneFiguren();
		feld.startaufstellung();
		feld.aktualisieren();
	}
}
