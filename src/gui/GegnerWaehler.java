package gui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import client.Client;
import client.Spieler;

public class GegnerWaehler extends Fenster {

	GUI gUI;
	BorderPane pane = new BorderPane();
	VBox liste = new VBox();
	Client client;

	public GegnerWaehler(GUI gUI) {
		super(gUI);

		try {
			client = new Client("localhost", gUI.name);
		} catch (Exception e) {
			gUI.spiel.ki(4, 1, 4);
			hide();
			return;
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

	private class SpielerButton extends Button {
		public SpielerButton(String s, long id, String farbe) {
			super(s);
			System.out.println(farbe);
			prefWidthProperty().bind(
					GegnerWaehler.this.widthProperty().divide(2));
			setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					listener(s,id);
				}
			});
		}
		
		public void listener(String s, long id)
		{
			pane.setRight(new Detail(s, id));
		}
	}
	
	private class SpielerButtonBot extends SpielerButton {

		public SpielerButtonBot(String s, long id, String farbe) {
			super(s, id, farbe);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void listener(String s, long id)
		{
			pane.setRight(new DetailBot(s, id));
		}
	}

	private class Detail extends BorderPane {
		public Detail(String name, long id) {
			setTop(new Text(name));
			Button herausfordern = new Button("Herausfordern");
			setBottom(herausfordern);
			prefWidthProperty().bind(
					GegnerWaehler.this.widthProperty().divide(2));
			herausfordern.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					listener(id);
				}
			});
		}

		public void listener(long id) {
			try {
				client.herausfordern(id);
				GegnerWaehler.this.hide();
			} catch (IOException e1) {
			}
		}
	}

	private class DetailBot extends Detail {

		public DetailBot(String name, long id) {
			super(name, id);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void listener(long id) {
			gUI.spiel.ki(4, (int)id, 4);
			GegnerWaehler.this.hide();
		}
	}
}
