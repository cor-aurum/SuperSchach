package com.superschach.superschach.gui.gegner;

import com.superschach.superschach.gui.Fenster;
import com.superschach.superschach.gui.GUI;
import com.superschach.superschach.network.client.Client;
import com.superschach.superschach.network.client.Spieler;
import com.superschach.superschach.spiel.AbstractGUI;

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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;

public class GegnerMenu extends Fenster {

	private GUI gUI;
	private BorderPane pane = new BorderPane();
	private VBox liste = new VBox();
	private ScrollPane scroll;
	private String farbe;
	private GegnerWaehler gegner;
	private String level="";

	public GegnerMenu(GUI gUI, String farbe, GegnerWaehler gegner) {
		super(gUI);
		this.gUI = gUI;
		this.farbe = farbe;
		this.gegner = gegner;
		Label waehler = new Label(AbstractGUI.meldung(farbe));
		BorderPane.setMargin(waehler, new Insets(0, 0, 20, 0));
		waehler.setId("waehler");
		pane.setPadding(new Insets(30, 30, 45, 30));
		pane.setTop(waehler);
		BorderPane.setAlignment(waehler, Pos.CENTER);

		scroll = new ScrollPane();
		scroll.setContent(liste);
		liste.setSpacing(15);
		scroll.skinProperty().addListener(new ChangeListener<Skin<?>>() {

			@Override
			public void changed(ObservableValue<? extends Skin<?>> ov, Skin<?> t, Skin<?> t1) {
				if (t1 != null && t1.getNode() instanceof Region) {
					Region r = (Region) t1.getNode();
					r.setBackground(Background.EMPTY);

					r.getChildrenUnmodifiable().stream().filter(n -> n instanceof Region).map(n -> (Region) n)
							.forEach(n -> n.setBackground(Background.EMPTY));

					r.getChildrenUnmodifiable().stream().filter(n -> n instanceof Control).map(n -> (Control) n)
							.forEach(c -> c.skinProperty().addListener(this)); // *
				}
			}
		});

		pane.setCenter(scroll);

		setzeInhalt(pane);
	}

	public void aktualisieren(boolean internet, Client client) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				liste.getChildren().clear();
				if (internet) {
					Spieler[] spieler = null;
					try {
						spieler = client.getLobby();
					} catch (Exception e) {
						e.printStackTrace();
					}

					SpielerButton[] button = new SpielerButton[spieler.length];
					for (int i = 0; i < spieler.length; i++) {
						button[i] = new SpielerButton(spieler[i].getName(), spieler[i].getID(), spieler[i].getFarbe());
					}
					liste.getChildren().addAll(button);

				}
				addBots();
			}
		});
	}

	public void addBots() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					liste.getChildren().add(new KeinSpielerButton(AbstractGUI.meldung("computerspieler")));
					liste.getChildren().add(new SpielerButtonBot("Kiana", 4));
					liste.getChildren().add(new SpielerButtonBot("UCI", 2));
					liste.getChildren().add(new SpielerButtonBot("Ivan Zufallski", 1));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void setLevel(String level)
	{
		this.level=level;
	}
	
	public void ok(long id, String s)
	{
		gegner.setzeGegner(id, farbe, s,level);
	}

	private class SpielerButton extends Button {
		public SpielerButton(String s, long id, String farbe) {
			super(s);
			// prefWidthProperty().bind(GegnerMenu.this.widthProperty().divide(2));
			setId("spieler");
			setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					listener(s, id, farbe.toUpperCase());
				}
			});

			setOnMouseEntered(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (gUI.getSounds().getValue()) {
						AudioClip plonkSound = new AudioClip(this.getClass().getClassLoader()
								.getResource("com/superschach/superschach/gui/sounds/hover.aiff").toString());
						plonkSound.play();
					}
				}
			});

		}

		public void listener(String s, long id, String farbe) {
			Detail d = new Detail(gUI, id, farbe, s, GegnerMenu.this);
			pane.setRight(d);
			BorderPane.setAlignment(d, Pos.CENTER);
		}
	}

	private class SpielerButtonBot extends SpielerButton {

		public SpielerButtonBot(String s, long id) {
			super(s, id, farbe);
		}
	}

	private class KeinSpielerButton extends SpielerButton {

		public KeinSpielerButton(String s) {
			super(s, -1, farbe);
		}
	}
}
