package com.superschach.superschach.gui;

import java.io.IOException;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Skin;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;

import com.superschach.superschach.network.client.Client;
import com.superschach.superschach.network.client.Spieler;
import com.superschach.superschach.spiel.AbstractGUI;

public class GegnerWaehler extends Fenster {

	GUI gUI;
	BorderPane pane = new BorderPane();
	VBox liste = new VBox();
	Client client;
	ScrollPane scroll;
	VBox herausforderung=new VBox();
	//MenuButton herausforderung =new MenuButton(AbstractGUI.meldung("herausforderungen"));
	boolean internet = true;

	public GegnerWaehler(GUI gUI) {
		super(gUI);

		this.gUI = gUI;

		Label waehler = new Label(AbstractGUI.meldung("gegnerWaehlen"));
		BorderPane.setMargin(waehler, new Insets(0, 0, 20, 0));
		waehler.setId("waehler");
		scroll = new ScrollPane();
		scroll.setContent(liste);
		liste.setSpacing(15);
		scroll.skinProperty().addListener(new ChangeListener<Skin<?>>() {

			@Override
			public void changed(ObservableValue<? extends Skin<?>> ov,
					Skin<?> t, Skin<?> t1) {
				if (t1 != null && t1.getNode() instanceof Region) {
					Region r = (Region) t1.getNode();
					r.setBackground(Background.EMPTY);

					r.getChildrenUnmodifiable().stream()
							.filter(n -> n instanceof Region)
							.map(n -> (Region) n)
							.forEach(n -> n.setBackground(Background.EMPTY));

					r.getChildrenUnmodifiable().stream()
							.filter(n -> n instanceof Control)
							.map(n -> (Control) n)
							.forEach(c -> c.skinProperty().addListener(this)); // *
				}
			}
		});
		pane.setPadding(new Insets(30, 30, 45, 30));
		pane.setTop(waehler);
		
		herausforderung.setPrefWidth(75);
		pane.setLeft(herausforderung);
		setzeInhalt(pane);
		pane.setCenter(scroll);
		BorderPane.setAlignment(waehler, Pos.CENTER);
		addBots();
		//pane.setBottom(herausforderung);
	}

	public void starteVerbindung() {
		try {
			client = new Client("localhost", gUI.name, gUI.spiel);
			gUI.setClient(client);// = client;
		} catch (Exception e) {
			internet = false;
		}
		addBots();
	}

	public void starteAktualisierung() {
		aktualisieren();

		Timeline aktualisieren = new Timeline(new KeyFrame(Duration.seconds(5),
				new EventHandler<ActionEvent>() {

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
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				liste.getChildren().clear();
				if (internet) {
					Spieler[] spieler = null;
					try {
						spieler = client.getLobby();
					} catch (IOException e) {
						e.printStackTrace();
					}
					SpielerButton[] button = new SpielerButton[spieler.length];
					for (int i = 0; i < spieler.length; i++) {
						button[i] = new SpielerButton(spieler[i].getName(),
								spieler[i].getID(), spieler[i].getFarbe());
					}
					liste.getChildren().addAll(button);
				}
				addBots();
			}
		});
	}

	private void addBots() {
		try {
			liste.getChildren().add(
					new SpielerButtonBot("Kiana ("
							+ AbstractGUI.meldung("weiss") + ")", 4, "WEISS",
							true));
			liste.getChildren().add(
					new SpielerButtonBot("Kiana ("
							+ AbstractGUI.meldung("schwarz") + ")", 4,
							"SCHWARZ", true));
			liste.getChildren().add(
					new SpielerButtonBot("Ivan Zufallski ("
							+ AbstractGUI.meldung("weiss") + ")", 1, "WEISS",
							false));
			liste.getChildren().add(
					new SpielerButtonBot("Ivan Zufallski ("
							+ AbstractGUI.meldung("schwarz") + ")", 1,
							"SCHWARZ", false));
			liste.getChildren()
					.add(new KeinSpielerButton(AbstractGUI
							.meldung("keinSpieler")));
		} catch (Exception e) {
		}
	}

	private class SpielerButton extends Button {
		public SpielerButton(String s, long id, String farbe) {
			super(s);
			prefWidthProperty().bind(
					GegnerWaehler.this.widthProperty().divide(2));
			setId("spieler");
			setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					listener(s, id, farbe);
				}
			});

			setOnMouseEntered(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (gUI.getSounds().getValue()) {
						AudioClip plonkSound = new AudioClip(this.getClass()
								.getClassLoader()
								.getResource("com/superschach/superschach/gui/sounds/hover.aiff")
								.toString());
						plonkSound.play();
					}
				}
			});

		}

		public void listener(String s, long id, String farbe) {
			Detail d=new Detail(s, id, farbe);
			pane.setRight(d);
			BorderPane.setAlignment(d, Pos.CENTER);
		}
	}

	private class SpielerButtonBot extends SpielerButton {

		boolean slider;

		public SpielerButtonBot(String s, long id, String farbe, boolean slider) {
			super(s, id, farbe);
			this.slider = slider;
		}

		@Override
		public void listener(String s, long id, String farbe) {
			Detail d=new DetailBot(s, id, farbe, slider);
			pane.setRight(d);
			BorderPane.setAlignment(d, Pos.CENTER);
		}
	}

	private class KeinSpielerButton extends SpielerButton {

		public KeinSpielerButton(String s) {
			super(s, -1, "WEISS");
		}

		@Override
		public void listener(String s, long id, String farbe) {
			Detail d=new KeinDetail(s);
			pane.setRight(d);
			BorderPane.setAlignment(d, Pos.CENTER);
		}
	}

	private class Detail extends ScrollPane {
		SpielVorschau sV;
		BorderPane mitte = new BorderPane();

		public Detail(String name, long id, String farbe) {
			sV = new SpielVorschau(name, gUI);
			BorderPane root = new BorderPane();
			this.setContent(root);
			
			this.skinProperty().addListener(new ChangeListener<Skin<?>>() {

				@Override
				public void changed(ObservableValue<? extends Skin<?>> ov,
						Skin<?> t, Skin<?> t1) {
					if (t1 != null && t1.getNode() instanceof Region) {
						Region r = (Region) t1.getNode();
						r.setBackground(Background.EMPTY);

						r.getChildrenUnmodifiable()
								.stream()
								.filter(n -> n instanceof Region)
								.map(n -> (Region) n)
								.forEach(n -> n.setBackground(Background.EMPTY));

						r.getChildrenUnmodifiable()
								.stream()
								.filter(n -> n instanceof Control)
								.map(n -> (Control) n)
								.forEach(
										c -> c.skinProperty().addListener(this)); // *
					}
				}
			});
			Label nameLabel = new Label(name);
			root.setTop(nameLabel);
			String farbtemp = farbe.equals("WEISS") ? "white" : "black";
			String farbtemp2 = farbe.equals("WEISS") ? "black" : "white";
			nameLabel.prefWidthProperty().bind(this.widthProperty());
			nameLabel
					.setStyle("-fx-text-fill:"
							+ farbtemp2
							+ ";-fx-background-color:"
							+ farbtemp
							+ ";-fx-font-size:20;-fx-font-weight: bold;-fx-padding:10px;");
			nameLabel.setUnderline(true);
			// root.setStyle("-fx-background-color:rgba(0,100,100,0.7);-fx-background-radius: 10;-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );-fx-padding:20px;");
			root.setId("detail-root");

			//sV.prefHeightProperty().bind(this.widthProperty());
			//sV.setStyle("-fx-padding:0px;");
			mitte.setCenter(sV);
			BorderPane.setAlignment(mitte, Pos.CENTER);
			root.setCenter(mitte);

			Button herausfordern = new Button("Herausfordern");
			root.setBottom(herausfordern);
			setPrefWidth(455);
			root.setPrefWidth(440);
			herausfordern.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					listener(id, farbe);
				}
			});
			BorderPane.setAlignment(herausfordern, Pos.CENTER);
		}

		public void listener(long id, String farbe) {
			try {
				/*
				GegnerWaehler.this.hide();
				gUI.spiel.laden(sV.getSelected());
				if(sV.isSelecectedPreDefined())
				{
					gUI.spiel.setSpielName(gUI.spiel.getSpielDefaultName());
				}
				gUI.getKontrolle().setName(gUI.spiel.getSpielName());
				gUI.feld.entferneFiguren();
				gUI.feld.startaufstellung();
				*/
				client.herausfordern(id);
				gUI.spiel.meldungAusgeben(AbstractGUI.meldung("gegner_herausgefordert"));
				gUI.speichern = null;
			} catch (Exception e1) {
			}
		}
	}

	private class DetailBot extends Detail {

		Slider waehlen = new Slider();
		String name = "";

		public DetailBot(String name, long id, String farbe, boolean slider) {
			super(name, id, farbe);
			this.name = name;
			if (slider) {
				waehlen.setMax(5);
				waehlen.setMin(3);
				waehlen.setValue(4);
				// System.out.println(waehlen.lookup(".thumb"));
				Label staerke = new Label(
						AbstractGUI.meldung("staerkeWaehlen"));
				// staerke.setStyle("-fx-font-weight:bold;-fx-text-fill:#ffffff;");
				VBox liste = new VBox();
				liste.getChildren().addAll(staerke, waehlen);
				mitte.setBottom(liste);
			}
		}

		@Override
		public void listener(long id, String farbe) {

			GegnerWaehler.this.hide();
			try {
				gUI.spiel.laden(sV.getSelected());
				if(sV.isSelecectedPreDefined())
				{
					gUI.spiel.setSpielName(gUI.spiel.getSpielDefaultName());
				}
				gUI.getKontrolle().setName(gUI.spiel.getSpielName());
				gUI.feld.entferneFiguren();
				gUI.feld.startaufstellung();
				gUI.spiel.ki((int) id, farbe == "WEISS" ? 0 : 1,
						(int) Math.round(waehlen.getValue()));
				if (!sV.getFile().canWrite()) {
					gUI.speichern = new Speichern(gUI, name);
				} else {
					gUI.speichern = new Speichern(gUI, sV.getFile());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private class KeinDetail extends Detail {
		String name = "";

		public KeinDetail(String name) {
			super(name, -1, "WEISS");
			this.name = name;
		}

		@Override
		public void listener(long id, String farbe) {
			GegnerWaehler.this.hide();
			try {
				gUI.spiel.laden(sV.getSelected());
				if(sV.isSelecectedPreDefined())
				{
					gUI.spiel.setSpielName(gUI.spiel.getSpielDefaultName());
				}
				gUI.getKontrolle().setName(gUI.spiel.getSpielName());
				gUI.feld.entferneFiguren();
				gUI.feld.startaufstellung();
				if (!sV.getFile().canWrite()) {
					gUI.speichern = new Speichern(gUI, name);
				} else {
					gUI.speichern = new Speichern(gUI, sV.getFile());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void herausgefordert() {
		hide();
		gUI.feld.entferneFiguren();
		gUI.feld.startaufstellung();
	}
	
	public void addHerausforderung(Herausforderung h)
	{
		//herausforderung.getItems().add(new MenuItem(h.getName()));
	}
}
