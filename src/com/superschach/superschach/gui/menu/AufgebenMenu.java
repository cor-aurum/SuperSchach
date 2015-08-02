package com.superschach.superschach.gui.menu;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;

import com.superschach.superschach.gui.GUI;
import com.superschach.superschach.spiel.Schnittstelle;

public class AufgebenMenu extends Menu {

	Button aufgeben;
	Button remis;

	public AufgebenMenu(GUI gUI) {
		super(gUI, Schnittstelle.meldung("beenden"));

		aufgeben = new Button(Schnittstelle.meldung("aufgeben"));
		aufgeben.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				getGUI().getClient().aufgeben();
			}
		});
		remis = new Button(Schnittstelle.meldung("remis"));
		remis.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				getGUI().getClient().remis();
			}
		});

		addInhalt(new Node[] { aufgeben,remis });
	}

	@Override
	public void zurueck() {
		switchFenster(getGUI().getHauptmenu());
	}
}
