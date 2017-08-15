package com.superschach.superschach.gui.gegner;


import com.superschach.superschach.gui.Fenster;
import com.superschach.superschach.gui.GUI;
import com.superschach.superschach.spiel.AbstractGUI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class Detail extends Fenster {

	private VBox root = new VBox();
	private GegnerMenu menu;
	private long id;

	public Detail(GUI gUI, long id, String farbe, String name, GegnerMenu menu) {
		super(gUI);
		this.menu = menu;
		this.id = id;
		Label waehler = new Label(name);
		BorderPane.setMargin(waehler, new Insets(0, 0, 20, 0));
		waehler.setId("waehler");
		root.setPadding(new Insets(30, 30, 45, 30));
		root.getChildren().add(waehler);
		addEinstellungen(name);
		setzeInhalt(root);
	}

	private void addEinstellungen(String name) {
		switch (name.toLowerCase()) {
		case "kiana":
			addSlider(name);
			break;
		case "uci":
			addTextfield(name);
			break;
		default:
			menu.ok(id, name);
		}
	}

	private void addSlider(String name) {
		Slider slider = new Slider();
		Button waehlen = new Button(AbstractGUI.meldung("waehlen"));
		slider.setMax(5);
		slider.setMin(3);
		slider.setValue(4);
		Label staerke = new Label(AbstractGUI.meldung("staerkeWaehlen"));
		root.getChildren().add(staerke);
		root.getChildren().add(slider);
		root.getChildren().add(waehlen);
		waehlen.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				menu.setLevel("" + (int) Math.round(slider.getValue()));
				menu.ok(id, name);
			}
		});
	}
	
	private void addTextfield(String name) {
		TextField pfad = new TextField();
		Button waehlen = new Button(AbstractGUI.meldung("waehlen"));
		pfad.setPromptText("z.B. stockfish");
		Label staerke = new Label(AbstractGUI.meldung("pfadzu"));
		root.getChildren().add(staerke);
		root.getChildren().add(pfad);
		root.getChildren().add(waehlen);
		waehlen.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				menu.setLevel(pfad.getText());
				menu.ok(id, name);
			}
		});
	}
}