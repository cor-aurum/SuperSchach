package gui;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import spiel.Schnittstelle;

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
				gUI.zweid.setValue(!gUI.zweid.getValue());
				pruefeText(umschalten);
				gUI.einstellungen.speichern();
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
		stil.setValue(Schnittstelle.meldung("klassisch"));
		stil.valueProperty().addListener(
				(ChangeListener<String>) (ov, t, t1) -> {
					if (stil.getValue().equals(
							Schnittstelle.meldung("klassisch"))) {
						gUI.css.setValue("klassisch");
					} else if (stil.getValue().equals(
							Schnittstelle.meldung("modern"))) {
						gUI.css.setValue("modern");
					}
					gUI.scene.getStylesheets().clear();
					gUI.scene.getStylesheets().add("gui/"+gUI.css.getValue()+".css");
					gUI.einstellungen.speichern();
				});
		vollbild = new Button();
		pruefeText(vollbild);
		vollbild.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				gUI.stage.setFullScreen(!gUI.stage.isFullScreen());
				pruefeText(vollbild);
				gUI.einstellungen.speichern();
			}
		});
		addInhalt(new Node[] { hintergrund, figuren, stil, umschalten, vollbild });
	}

	@Override
	public void zurueck() {
		switchFenster(gUI.hauptmenu);
	}

	private void pruefeText(Button p) {
		if (p == umschalten) {
			if (!gUI.zweid.getValue()) {
				umschalten.setText(Schnittstelle.meldung("dreiDAus"));
			} else {
				umschalten.setText(Schnittstelle.meldung("dreiDAn"));
			}
		}
		if (p == vollbild) {
			if (gUI.stage.isFullScreen()) {
				vollbild.setText(Schnittstelle.meldung("vollbild_aus"));
			} else {
				vollbild.setText(Schnittstelle.meldung("vollbild_an"));
			}
		}
	}

}
