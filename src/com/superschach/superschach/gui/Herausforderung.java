package com.superschach.superschach.gui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import com.superschach.superschach.client.Spieler;
import com.superschach.superschach.spiel.Schnittstelle;

public class Herausforderung {

	private String name="Heinz Hugo";
	private long id;
	HerausgefordertDialog dialog;
	Pane p = new Pane();

	public Herausforderung(GegnerWaehler gegner, int herausforderung, long id) {
		this.id = id;

		try {
			Spieler[] s = gegner.gUI.client.getLobby();

			for (Spieler spieler : s) {
				if(spieler.getID()==id)
				{
					name=spieler.getName();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dialog = new HerausgefordertDialog(gegner.gUI.spiel, herausforderung);
	}

	public String getName() {
		return name;
	}

	public long getId() {
		return id;
	}

	private class HerausgefordertDialog extends Dialog {

		public HerausgefordertDialog(FxSchnittstelle schnittstelle,
				int herausforderung) {
			setMaxWidth(450);
			BorderPane root = new BorderPane();
			BorderPane buttons = new BorderPane();
			Button annehmen = new Button(Schnittstelle.meldung("annehmen"));
			Button abbrechen = new Button(Schnittstelle.meldung("abbrechen"));
			Label text = new Label(Schnittstelle.meldung("herausforderung")
					+ "\n" + name);
			root.setCenter(text);
			buttons.setLeft(annehmen);
			buttons.setRight(abbrechen);
			root.setBottom(buttons);
			root.setPadding(new Insets(20, 20, 20, 20));

			annehmen.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					schnittstelle.gUI.feld.getChildren().remove(p);
					schnittstelle.gUI.feld.getChildren().remove(
							HerausgefordertDialog.this);
					try {
						schnittstelle.gUI.client.nehmeHerausforderungAn(herausforderung);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});

			abbrechen.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					schnittstelle.gUI.feld.getChildren().remove(p);
					schnittstelle.gUI.feld.getChildren().remove(
							HerausgefordertDialog.this);
				}
			});

			getChildren().add(root);
			schnittstelle.gUI.feld.getChildren().add(p);
			schnittstelle.gUI.feld.getChildren().add(this);
		}
	}
}
