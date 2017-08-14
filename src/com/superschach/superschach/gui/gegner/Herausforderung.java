package com.superschach.superschach.gui.gegner;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import com.superschach.superschach.gui.Dialog;
import com.superschach.superschach.gui.FxSchnittstelle;
import com.superschach.superschach.network.client.Spieler;
import com.superschach.superschach.spiel.AbstractGUI;

public class Herausforderung extends Button{

	private String name="Heinz Hugo";
	private long id;
	HerausgefordertDialog dialog;
	Pane p = new Pane();

	public Herausforderung(GegnerWaehler gegner, int herausforderung, long id) {
		this.id = id;

		try {
			Spieler[] s = gegner.gUI.getClient().getLobby();

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
		
		setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				//gegner.herausforderung.getChildren()
				//gegner.herausforderung.getChildren().add(new TextField("Herausforderung"));
			}
		});
	}

	public String getName() {
		return name;
	}

	public long getSpielerId() {
		return id;
	}

	class HerausgefordertDialog extends Dialog {

		public HerausgefordertDialog(FxSchnittstelle schnittstelle,
				int herausforderung) {
			setMaxWidth(450);
			BorderPane root = new BorderPane();
			BorderPane buttons = new BorderPane();
			Button annehmen = new Button(AbstractGUI.meldung("annehmen"));
			Button abbrechen = new Button(AbstractGUI.meldung("abbrechen"));
			Label text = new Label(AbstractGUI.meldung("herausforderung")
					+ "\n" + name);
			root.setCenter(text);
			buttons.setLeft(annehmen);
			buttons.setRight(abbrechen);
			root.setBottom(buttons);
			root.setPadding(new Insets(20, 20, 20, 20));

			annehmen.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					schnittstelle.getgUI().feld.getChildren().remove(p);
					schnittstelle.getgUI().feld.getChildren().remove(
							HerausgefordertDialog.this);
					try {
						schnittstelle.getgUI().getClient().nehmeHerausforderungAn(herausforderung);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});

			abbrechen.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					schnittstelle.getgUI().feld.getChildren().remove(p);
					schnittstelle.getgUI().feld.getChildren().remove(
							HerausgefordertDialog.this);
				}
			});

			getChildren().add(root);
			schnittstelle.getgUI().feld.getChildren().add(p);
			schnittstelle.getgUI().feld.getChildren().add(this);
		}
		public void abbrechen(int id, FxSchnittstelle schnittstelle)
		{
			if(id==Herausforderung.this.id)
			{
				schnittstelle.getgUI().feld.getChildren().remove(p);
				schnittstelle.getgUI().feld.getChildren().remove(
						this);
			}
		}
	}
	
	
}
