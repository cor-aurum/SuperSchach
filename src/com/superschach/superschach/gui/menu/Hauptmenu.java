package com.superschach.superschach.gui.menu;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

import com.superschach.superschach.gui.GUI;
import com.superschach.superschach.gui.GegnerWaehler;
import com.superschach.superschach.spiel.AbstractGUI;

public class Hauptmenu extends Menu {

	public Hauptmenu(GUI gUI) {
		super(gUI, AbstractGUI.meldung("hauptmenu"));
		File datei = new File(AbstractGUI.verzeichnis() + "login");
		Button[] punkte;
		if (datei.exists()) {
			 punkte= new Button[7];
			 punkte[6] = new Button(AbstractGUI.meldung("logout"));
		}
		else
		{
			punkte = new Button[6];
		}
		punkte[0] = new Button(AbstractGUI.meldung("lobby"));
		punkte[1] = new Button(AbstractGUI.meldung("game_optionen"));
		punkte[2] = new Button(AbstractGUI.meldung("multiplayer_optionen"));
		punkte[3] = new Button(AbstractGUI.meldung("grafik"));
		punkte[4] = new Button(AbstractGUI.meldung("sound"));
		punkte[5] = new Button(AbstractGUI.meldung("quit"));
		addInhalt(punkte);

		punkte[0].setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				if (gUI.feld.getChildren().contains(gUI.getGegner())) {
					gUI.feld.getChildren().remove(this);
				} else {
					gUI.setGegner(new GegnerWaehler(gUI));
					gUI.feld.getChildren().remove(gUI.getKontrolle());

					switchFenster(gUI.getGegner());
					gUI.feld.getChildren().add(gUI.getKontrolle());
				}
				if (gUI.getClient() != null) {
					try {
						gUI.getClient().betreteLobby();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		punkte[1].setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

			}
		});
		punkte[2].setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

			}
		});
		punkte[3].setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				switchFenster(new Grafikmenu(gUI));
			}
		});
		punkte[4].setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				switchFenster(new Soundmenu(gUI));
			}
		});
		punkte[5].setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (getGUI().getClient() != null) {
					switchFenster(new AufgebenMenu(gUI));
				} else {
					System.exit(0);
				}
			}
		});

		try{
		punkte[6].setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					datei.delete();
					gUI.getClient().close();
					gUI.setClient(null);
					getChildren().remove(punkte[6]);
				} catch (Exception ex) {

				}
			}
		});
		}
		catch(Exception e)
		{}
	}

	@Override
	public void zurueck() {
		if (getGUI().feld.getChildren().contains(getGUI().getGegner())) {
			getGUI().feld.getChildren().remove(this);
		} else {
			hide();
		}
	}

}
