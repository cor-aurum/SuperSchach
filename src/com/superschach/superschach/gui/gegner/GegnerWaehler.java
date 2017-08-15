package com.superschach.superschach.gui.gegner;

import com.superschach.superschach.gui.Fenster;
import com.superschach.superschach.gui.GUI;
import com.superschach.superschach.gui.Speichern;
import com.superschach.superschach.network.client.Client;
import com.superschach.superschach.spiel.AbstractGUI;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class GegnerWaehler extends Fenster {

	GUI gUI;
	BorderPane pane = new BorderPane();
	Client client;
	ScrollPane scroll;
	private VBox herausforderung = new VBox();
	boolean internet = true;
	private GegnerMenu links;
	private GegnerMenu rechts;
	private long weiss = 0;
	private long schwarz = 0;
	private String nameWeiss = "";
	private String nameSchwarz = "";
	private String levelWeiss="";
	private String levelSchwarz="";

	public GegnerWaehler(GUI gUI) {
		super(gUI);
		this.gUI = gUI;

		Label waehler = new Label(AbstractGUI.meldung("gegnerWaehlen"));
		BorderPane.setMargin(waehler, new Insets(0, 0, 20, 0));
		waehler.setId("waehler");
		pane.setPadding(new Insets(30, 30, 45, 30));
		pane.setTop(waehler);
		BorderPane.setAlignment(waehler, Pos.CENTER);
		links = new GegnerMenu(gUI, "weiss", this);
		rechts = new GegnerMenu(gUI, "schwarz", this);
		pane.setLeft(links);
		pane.setRight(rechts);
		pane.setBottom(herausforderung);
		BorderPane.setAlignment(herausforderung, Pos.CENTER);
		// links.prefWidthProperty().bind(this.widthProperty().divide(2));
		// rechts.prefWidthProperty().bind(this.widthProperty().divide(2));
		setzeInhalt(pane);
	}

	public void starteVerbindung() {
		try {
			client = new Client(AbstractGUI.meldung("host"), gUI.getName(), gUI.spiel);
			gUI.setClient(client);// = client;
		} catch (Exception e) {
			internet = false;
		}
		addBots();
	}

	public void setzeGegner(long id, String farbe, String name, String level) {
		if (farbe.toLowerCase().equals("weiss")) {
			weiss = id;
			nameWeiss = name;
			levelWeiss=level;
		} else {
			schwarz = id;
			nameSchwarz = name;
			levelSchwarz=level;
		}
		if (weiss != 0 && schwarz != 0) {
			setzeHerausforderung();
		}
	}

	private void setzeHerausforderung() {
		herausforderung.getChildren().clear();
		SpielVorschau sV = new SpielVorschau(nameWeiss + " vs " + nameSchwarz, gUI);
		herausforderung.getChildren().add(sV);
		Button herausfordern = new Button(AbstractGUI.meldung("herausfordern"));
		herausfordern.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					
					gUI.spiel.laden(sV.getSelected());
					if (sV.isSelecectedPreDefined()) {
						gUI.spiel.setSpielName(gUI.spiel.getSpielDefaultName());
					}
					gUI.getKontrolle().setName(gUI.spiel.getSpielName());
					gUI.feld.entferneFiguren();
					gUI.feld.startaufstellung();
					gUI.spiel.ki((int) weiss, 0, levelWeiss);
					gUI.spiel.ki((int) schwarz, 1, levelSchwarz);
					if (!sV.getFile().canWrite()) {
						gUI.setSpeichern(new Speichern(gUI, nameWeiss + " vs " + nameSchwarz));
					} else {
						gUI.setSpeichern(new Speichern(gUI, sV.getFile()));
					}
					GegnerWaehler.this.hide();
				} catch (Exception ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}
			}
		});
		herausforderung.getChildren().add(herausfordern);
	}

	public void starteAktualisierung() {
		aktualisieren();

		Timeline aktualisieren = new Timeline(new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (internet && isShowed())
					aktualisieren();
			}
		}));

		aktualisieren.setCycleCount(Timeline.INDEFINITE);
		aktualisieren.play();
	}

	public void aktualisieren() {
		links.aktualisieren(internet, client);
		rechts.aktualisieren(internet, client);
	}

	private void addBots() {
		links.addBots();
		rechts.addBots();
	}

	public void herausgefordert() {
		hide();
		gUI.feld.entferneFiguren();
		gUI.feld.startaufstellung();
	}

	public void addHerausforderung(Herausforderung h) {
		// herausforderung.getItems().add(new MenuItem(h.getName()));
	}
}
