package com.superschach.superschach.gui;

import java.util.Locale;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.TTCCLayout;
import org.apache.log4j.helpers.DateLayout;

import com.sun.javafx.application.LauncherImpl;
import com.superschach.superschach.gui.cli.CLI;
import com.superschach.superschach.gui.gegner.GegnerWaehler;
import com.superschach.superschach.gui.menu.Hauptmenu;
import com.superschach.superschach.network.client.Client;
import com.superschach.superschach.spiel.AbstractGUI;

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
import javafx.fxml.FXMLLoader;
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
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GUI extends Application {

	private static Logger logger = Logger.getRootLogger();
	private Stage stage;
	Einstellungen rechts;
	public MeshView[] dreidfiguren = new MeshView[17];

	private String hintergrund = "marmor";
	PhongMaterial feldMaterial = new PhongMaterial();

	public FxSchnittstelle spiel = new FxSchnittstelle(this);
	private boolean farbe = true;

	public String form = "standard";
	public boolean modell_farbe = false;
	private SimpleObjectProperty<Color> farbe_weiss = new SimpleObjectProperty<Color>(Color.AZURE);
	private SimpleObjectProperty<Color> farbe_schwarz = new SimpleObjectProperty<Color>(Color.NAVY);

	public SimpleObjectProperty<Color> getFarbe_weiss() {
		return farbe_weiss;
	}

	public void setFarbe_weiss(SimpleObjectProperty<Color> farbe_weiss) {
		this.farbe_weiss = farbe_weiss;
	}

	public SimpleObjectProperty<Color> getFarbe_schwarz() {
		return farbe_schwarz;
	}

	public void setFarbe_schwarz(SimpleObjectProperty<Color> farbe_schwarz) {
		this.farbe_schwarz = farbe_schwarz;
	}

	Image brettbild = new Image(this.getClass().getClassLoader()
			.getResource("com/superschach/superschach/gui/bilder/brett.png").toString());
	Image brettbild2d = new Image(this.getClass().getClassLoader()
			.getResource("com/superschach/superschach/gui/bilder/brett2d.png").toString());
	Chat chat = new Chat(this);
	private String name = System.getProperty("user.name");
	private SimpleBooleanProperty zweid = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty sounds = new SimpleBooleanProperty(true);

	private SimpleStringProperty vonFarbe = new SimpleStringProperty("#cd5c5c");
	private SimpleStringProperty bisFarbe = new SimpleStringProperty("#232323");

	private Einstellungen einstellungen;
	private GegnerWaehler gegner;
	private Client client;
	public MyStackPane feld;
	static SimpleBooleanProperty geladen = new SimpleBooleanProperty(false);
	private Hauptmenu hauptmenu = new Hauptmenu(this);
	private Kontrollfeld kontrolle;
	private Speichern speichern;
	private Scene scene;
	private SimpleStringProperty css = new SimpleStringProperty("klassisch");

	@Override
	public void start(Stage stage) throws Exception {
		this.setStage(stage);

		setEinstellungen(new Einstellungen(this));
		// getEinstellungen().laden();

		if (!Platform.isSupported(ConditionalFeature.SCENE3D) || getZweid().getValue()) {
			feld = new ZweiD(this);
		} else {
			feld = new DreiD(this);
		}
		this.setStage(stage);
		setScene(new Scene(feld, 1200, 800));
		getScene().getStylesheets().add("com/superschach/superschach/gui/" + getCss().getValue() + ".css");
		stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.isControlDown()) {
					if (keyEvent.getCode() == KeyCode.C) {
						final Clipboard clipboard = Clipboard.getSystemClipboard();
						final ClipboardContent content = new ClipboardContent();
						content.putImage(feld.getScreenshot());
						clipboard.setContent(content);
						spiel.meldungAusgeben(AbstractGUI.meldung("screenshot"));
					}
				} else {
					if (keyEvent.getCode() == KeyCode.ESCAPE) {
						if (!getHauptmenu().isShowed()) {
							getHauptmenu().show();
						}
					}
				}
			}
		});
		setKontrolle(new Kontrollfeld(this));
		setGegner(new GegnerWaehler(this));
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

		getVonFarbe().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				feld.background.setStyle("-fx-background-color:linear-gradient(from 25% 25% to 100% 100%, "
						+ getVonFarbe().getValue() + ", " + getBisFarbe().getValue() + ");");
			}

		});
		getBisFarbe().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				feld.background.setStyle("-fx-background-color:linear-gradient(from 25% 25% to 100% 100%, "
						+ getVonFarbe().getValue() + ", " + getBisFarbe().getValue() + ");");
			}

		});

		feld.background.setStyle("-fx-background-color:linear-gradient(from 25% 25% to 100% 100%, "
				+ getVonFarbe().getValue() + ", " + getBisFarbe().getValue() + ");");
		// feld.getChildren().add(kontrolle);
		StackPane.setAlignment(getKontrolle(), Pos.BOTTOM_RIGHT);
		// scene.onMouseDraggedProperty().set(new MouseEventHandler());
		stage.setScene(getScene());
		stage.setTitle("Super Schach");
		stage.getIcons().add(new Image(this.getClass().getClassLoader()
				.getResource("com/superschach/superschach/gui/bilder/bauer_schwarz.png").toString()));
		stage.setMinHeight(600);
		stage.setMinWidth(800);

		feld.getChildren().remove(getKontrolle());
		getGegner().show();
		feld.getChildren().add(getKontrolle());
		geladen.setValue(true);
		stage.show();
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() {
				getGegner().starteVerbindung();
				getGegner().starteAktualisierung();

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

	public MeshView gebeMesh(int figur) throws Exception {
		String f = gebeFigur(figur);
		figur = Math.abs(figur);
		if (dreidfiguren[figur] == null) {
			dreidfiguren[figur] = (MeshView) FXMLLoader.load(getClass().getClassLoader()
					.getResource("com/superschach/superschach/gui/meshes/" + f + "_" + form + ".fxml"));
		}
		TriangleMesh ret = new TriangleMesh();
		ret.getPoints().setAll(((TriangleMesh) dreidfiguren[figur].getMesh()).getPoints()
				.toArray(new float[((TriangleMesh) dreidfiguren[figur].getMesh()).getPoints().size()]));
		ret.getFaces().setAll(((TriangleMesh) dreidfiguren[figur].getMesh()).getFaces()
				.toArray(new int[((TriangleMesh) dreidfiguren[figur].getMesh()).getFaces().size()]));
		ret.getTexCoords().setAll(((TriangleMesh) dreidfiguren[figur].getMesh()).getTexCoords()
				.toArray(new float[((TriangleMesh) dreidfiguren[figur].getMesh()).getTexCoords().size()]));

		MeshView mesh = new MeshView();
		mesh.setMesh(ret);
		return mesh;
	}

	public boolean getFarbe() {
		return farbe;
	}

	public static void main(String args[]) throws Exception {
		try {
			DateLayout layout = new TTCCLayout();
			ConsoleAppender consoleAppender = new ConsoleAppender(layout);
			logger.addAppender(consoleAppender);
			FileAppender fileAppender = new FileAppender(layout, AbstractGUI.verzeichnis() + "logs/superschach.log",
					false);
			logger.addAppender(fileAppender);
			// ALL | DEBUG | INFO | WARN | ERROR | FATAL | OFF:
			logger.setLevel(Level.ALL);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		logger.info("===========Spiel wird geladen===========");
		Locale.setDefault(new Locale("de", "DE"));
		if (args.length > 0) {
			new CLI(args);
		} else {
			LauncherImpl.launchApplication(GUI.class, SplashScreen.class, args);
			launch(args);
		}
	}

	public void wechsleDimension() {
		if (!Platform.isSupported(ConditionalFeature.SCENE3D) || getZweid().getValue()) {
			feld = new ZweiD(this);
		} else {
			feld = new DreiD(this);
		}
		feld.entferneFiguren();
		feld.startaufstellung();
		feld.aktualisieren();
	}

	public Einstellungen getEinstellungen() {
		return einstellungen;
	}

	public void setEinstellungen(Einstellungen einstellungen) {
		this.einstellungen = einstellungen;
	}

	public SimpleBooleanProperty getZweid() {
		return zweid;
	}

	public void setZweid(SimpleBooleanProperty zweid) {
		this.zweid = zweid;
	}

	public SimpleStringProperty getCss() {
		return css;
	}

	public void setCss(SimpleStringProperty css) {
		this.css = css;
	}

	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Hauptmenu getHauptmenu() {
		return hauptmenu;
	}

	public void setHauptmenu(Hauptmenu hauptmenu) {
		this.hauptmenu = hauptmenu;
	}

	public SimpleBooleanProperty getSounds() {
		return sounds;
	}

	public void setSounds(SimpleBooleanProperty sounds) {
		this.sounds = sounds;
	}

	public SimpleStringProperty getBisFarbe() {
		return bisFarbe;
	}

	public void setBisFarbe(SimpleStringProperty bisFarbe) {
		this.bisFarbe = bisFarbe;
	}

	public SimpleStringProperty getVonFarbe() {
		return vonFarbe;
	}

	public void setVonFarbe(SimpleStringProperty vonFarbe) {
		this.vonFarbe = vonFarbe;
	}

	public GegnerWaehler getGegner() {
		return gegner;
	}

	public void setGegner(GegnerWaehler gegner) {
		this.gegner = gegner;
	}

	public Kontrollfeld getKontrolle() {
		return kontrolle;
	}

	public void setKontrolle(Kontrollfeld kontrolle) {
		this.kontrolle = kontrolle;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client c) {
		client = c;
	}

	public String getHintergrund() {
		return hintergrund;
	}

	public void setHintergrund(String hintergrund) {
		this.hintergrund = hintergrund;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Speichern getSpeichern() {
		return speichern;
	}

	public void setSpeichern(Speichern speichern) {
		this.speichern = speichern;
	}
}
