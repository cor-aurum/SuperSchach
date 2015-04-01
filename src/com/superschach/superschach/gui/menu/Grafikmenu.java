package com.superschach.superschach.gui.menu;

import com.superschach.superschach.gui.GUI;
import com.superschach.superschach.spiel.Schnittstelle;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class Grafikmenu extends Menu {

	Button umschalten;
	Button vollbild;
	ComboBox<String> stil = new ComboBox<String>();

	public Grafikmenu(GUI gUI) {
		super(gUI, Schnittstelle.meldung("grafik"));

		umschalten = new Button();
		pruefeText(umschalten);
		umschalten.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				gUI.getZweid().setValue(!gUI.getZweid().getValue());
				pruefeText(umschalten);
				gUI.getEinstellungen().speichern();
			}
		});

		Button hintergrund = new Button(Schnittstelle.meldung("hintergrund"));
		hintergrund.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				switchFenster(new Hintergrundmenu(gUI));
			}
		});

		Button figuren = new Button(Schnittstelle.meldung("figuren"));
		figuren.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				switchFenster(new Figurenmenu(gUI));
			}
		});

		stil.getItems().addAll(Schnittstelle.meldung("klassisch"),
				Schnittstelle.meldung("modern"));
		stil.setValue(Schnittstelle.meldung(gUI.getCss().getValue()));
		stil.valueProperty().addListener(
				(ChangeListener<String>) (ov, t, t1) -> {
					if (stil.getValue().equals(
							Schnittstelle.meldung("klassisch"))) {
						gUI.getCss().setValue("klassisch");
					} else if (stil.getValue().equals(
							Schnittstelle.meldung("modern"))) {
						gUI.getCss().setValue("modern");
					}
					gUI.getScene().getStylesheets().clear();
					gUI.getScene().getStylesheets().add(
							"com/superschach/superschach/gui/" + gUI.getCss().getValue() + ".css");
					gUI.getEinstellungen().speichern();
				});
		vollbild = new Button();
		pruefeText(vollbild);
		vollbild.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				gUI.getStage().setFullScreen(!gUI.getStage().isFullScreen());
				pruefeText(vollbild);
				gUI.getEinstellungen().speichern();
			}
		});
		addInhalt(new Node[] { hintergrund, figuren, stil, umschalten, vollbild });
	}

	@Override
	public void zurueck() {
		switchFenster(getGUI().getHauptmenu());
	}

	private void pruefeText(Button p) {
		if (p == umschalten) {
			if (!getGUI().getZweid().getValue()) {
				umschalten.setText(Schnittstelle.meldung("dreiDAus"));
			} else {
				umschalten.setText(Schnittstelle.meldung("dreiDAn"));
			}
		}
		if (p == vollbild) {
			if (getGUI().getStage().isFullScreen()) {
				vollbild.setText(Schnittstelle.meldung("vollbild_aus"));
			} else {
				vollbild.setText(Schnittstelle.meldung("vollbild_an"));
			}
		}
	}

}
