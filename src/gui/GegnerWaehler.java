package gui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import client.Client;
import client.Spieler;

public class GegnerWaehler extends Fenster {

	GUI gUI;
	BorderPane pane = new BorderPane();
	VBox liste = new VBox();
	Client client;
	boolean internet = true;

	public GegnerWaehler(GUI gUI) {
		super(gUI);

		try {
			client = new Client("localhost", gUI.name);
		} catch (Exception e) {
			internet = false;
		}
		this.gUI = gUI;
		setzeInhalt(pane);
		pane.setCenter(liste);
		Button aktualisieren = new Button("Aktualisieren");
		setBottom(aktualisieren);
		aktualisieren.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				aktualisieren();
			}
		});
		aktualisieren();
	}

	public void aktualisieren() {
		liste.getChildren().clear();
		if (internet) {
			Spieler[] spieler = null;
			try {
				spieler = client.getLobby();
			} catch (IOException e) {
			}
			SpielerButton[] button = new SpielerButton[spieler.length];
			for (int i = 0; i < spieler.length; i++) {
				button[i] = new SpielerButton(spieler[i].getName(),
						spieler[i].getID(), spieler[i].getFarbe());
			}
			liste.getChildren().addAll(button);
		}
		liste.getChildren().add(
				new SpielerButtonBot("Kiana (weiß)", 4, "WEISS"));
		liste.getChildren().add(
				new SpielerButtonBot("Kiana (schwarz)", 4, "SCHWARZ"));
		liste.getChildren().add(
				new SpielerButtonBot("Ivan Zufallski (weiß)", 1, "WEISS"));
		liste.getChildren().add(
				new SpielerButtonBot("Ivan Zufallski (schwarz)", 1, "SCHWARZ"));
	}

	private class SpielerButton extends Button {
		public SpielerButton(String s, long id, String farbe) {
			super(s);
			System.out.println(farbe);
			prefWidthProperty().bind(
					GegnerWaehler.this.widthProperty().divide(2));
			setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					listener(s, id, farbe);
				}
			});
		}

		public void listener(String s, long id, String farbe) {
			pane.setRight(new Detail(s, id, farbe));
		}
	}

	private class SpielerButtonBot extends SpielerButton {

		public SpielerButtonBot(String s, long id, String farbe) {
			super(s, id, farbe);
		}

		@Override
		public void listener(String s, long id, String farbe) {
			pane.setRight(new DetailBot(s, id, farbe));
		}
	}

	private class Detail extends BorderPane {
		public Detail(String name, long id, String farbe) {
			Label nameLabel = new Label(name);
			setTop(nameLabel);
			String farbtemp = farbe.equals("WEISS") ? "white" : "black";
			String farbtemp2 = farbe.equals("WEISS") ? "black" : "white";
			nameLabel.setStyle("-fx-text-fill:" + farbtemp2
					+ ";-fx-background-color:" + farbtemp + ";");
			Button herausfordern = new Button("Herausfordern");
			setBottom(herausfordern);
			prefWidthProperty().bind(
					GegnerWaehler.this.widthProperty().divide(2));
			herausfordern.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					listener(id, farbe);
				}
			});
		}

		public void listener(long id, String farbe) {
			try {
				client.herausfordern(id);
				GegnerWaehler.this.hide();
			} catch (IOException e1) {
			}
		}
	}

	private class DetailBot extends Detail {

		public DetailBot(String name, long id, String farbe) {
			super(name, id, farbe);
		}

		@Override
		public void listener(long id, String farbe) {
			gUI.spiel.ki((int) id, farbe == "WEISS" ? 0 : 1, 4);
			GegnerWaehler.this.hide();
		}
	}
}
