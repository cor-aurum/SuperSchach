package com.superschach.superschach.gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.superschach.superschach.spiel.AbstractGUI;

import javafx.scene.paint.Color;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Einstellungen extends Fenster {

	GUI gUI;
	VBox settings = new VBox();

	ToggleGroup map = new ToggleGroup();
	RadioButton map_marmor = new RadioButton(AbstractGUI.meldung("marmor"));

	RadioButton map_holz = new RadioButton(AbstractGUI.meldung("holz"));

	RadioButton map_gras = new RadioButton(AbstractGUI.meldung("gras"));

	RadioButton map_glas = new RadioButton(AbstractGUI.meldung("glas"));

	ToggleGroup figur = new ToggleGroup();
	RadioButton figur_standard = new RadioButton(AbstractGUI.meldung("standard"));

	RadioButton figur_modern = new RadioButton(AbstractGUI.meldung("modern"));

	public Einstellungen(GUI gUI) {
		// setzeInhalt(tab);
		super(gUI);
		setzeInhalt(settings);
		settings.setPadding(new Insets(20));
		this.gUI = gUI;
		// this.setPrefWidth(200);
		map_marmor.setToggleGroup(map);
		map_holz.setToggleGroup(map);
		map_gras.setToggleGroup(map);
		map_glas.setToggleGroup(map);
		map_marmor.setSelected(true);

		Label ob = new Label(AbstractGUI.meldung("oberflaeche"));
		ob.setStyle("fx-text-fill: black;-fx-font-size:18;-fx-font-family: \"Arial Narrow\";-fx-font-weight: bold;");
		settings.getChildren().add(ob);
		settings.getChildren().add(map_marmor);
		settings.getChildren().add(map_holz);
		settings.getChildren().add(map_gras);
		settings.getChildren().add(map_glas);
		addLeer(settings);

		Label bg = new Label(AbstractGUI.meldung("hintergrund"));
		bg.setStyle("fx-text-fill: black;-fx-font-size:18;-fx-font-family: \"Arial Narrow\";-fx-font-weight: bold;");
		settings.getChildren().add(bg);

		Label multi = new Label(AbstractGUI.meldung("mehrspieler"));
		multi.setStyle("fx-text-fill: black;-fx-font-size:18;-fx-font-family: \"Arial Narrow\";-fx-font-weight: bold;");
		settings.getChildren().add(multi);
		TextField name = new TextField();
		Button nameOK = new Button(AbstractGUI.meldung("nameAendern"));
		HBox namenBox = new HBox();
		namenBox.getChildren().addAll(name, nameOK);
		settings.getChildren().add(namenBox);

		addLeer(settings);

		Label fig = new Label(AbstractGUI.meldung("figuren"));
		fig.setStyle("fx-text-fill: black;-fx-font-size:18;-fx-font-family: \"Arial Narrow\";-fx-font-weight: bold;");
		settings.getChildren().add(fig);
		// settings.getChildren().add(box);
		figur_standard.setToggleGroup(figur);
		figur_modern.setToggleGroup(figur);
		figur_standard.setSelected(true);
		settings.getChildren().add(figur_standard);
		settings.getChildren().add(figur_modern);
		addLeer(settings);

		Label sonstiges = new Label(AbstractGUI.meldung("sonstiges"));
		sonstiges.setStyle(
				"fx-text-fill: black;-fx-font-size:18;-fx-font-family: \"Arial Narrow\";-fx-font-weight: bold;");
		settings.getChildren().add(sonstiges);

		name.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (!name.getText().equals("")) {
					gUI.setName(name.getText());
					name.setText("");
					speichern();
				}
			}
		});
		laden();
		map.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
				if (map.getSelectedToggle() == map_holz) {
					gUI.setHintergrund("holz");
				} else if (map.getSelectedToggle() == map_marmor) {
					gUI.setHintergrund("marmor");
				} else if (map.getSelectedToggle() == map_gras) {
					gUI.setHintergrund("gras");
				} else if (map.getSelectedToggle() == map_glas) {
					gUI.setHintergrund("glas");
				}
				gUI.feld.aktualisiereMap();
				speichern();
			}
		});

		figur.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
				if (figur.getSelectedToggle() == figur_modern) {
					gUI.form = "modern";
				} else if (figur.getSelectedToggle() == figur_standard) {
					gUI.form = "standard";
				}
				try {
					gUI.feld.entferneFiguren();
					gUI.feld.startaufstellung();
					gUI.feld.aktualisieren();
					speichern();
				} catch (Exception e) {
				}
			}
		});

		nameOK.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (!name.getText().equals("")) {
					gUI.setName(name.getText());
					name.setText("");
					speichern();
				}
			}
		});
	}

	public void addLeer(VBox vbox) {
		Label j = new Label("\n");
		j.setMaxHeight(20);
		vbox.getChildren().add(j);
	}

	public void speichern() {
		try {
			BufferedWriter br = new BufferedWriter(new FileWriter(AbstractGUI.verzeichnis() + "gui.save"));
			br.write(gUI.getHintergrund());
			br.write(System.getProperty("line.separator"));
			br.write(toRGBCode(gUI.getFarbe_weiss().getValue()));
			br.write(System.getProperty("line.separator"));
			br.write(toRGBCode(gUI.getFarbe_schwarz().getValue()));
			br.write(System.getProperty("line.separator"));
			br.write(gUI.getName());
			br.write(System.getProperty("line.separator"));
			br.write("" + gUI.getSounds().getValue());
			br.write(System.getProperty("line.separator"));
			br.write("" + gUI.getZweid().getValue());
			br.write(System.getProperty("line.separator"));
			br.write(gUI.form);
			br.write(System.getProperty("line.separator"));
			br.write(gUI.getVonFarbe().getValue());
			br.write(System.getProperty("line.separator"));
			br.write(gUI.getBisFarbe().getValue());
			br.write(System.getProperty("line.separator"));
			br.write("" + gUI.getStage().isFullScreen());
			br.write(System.getProperty("line.separator"));
			br.write("" + gUI.getCss().getValue());
			br.flush();
			br.close();
		} catch (IOException e) {
			gUI.spiel.meldungAusgeben(AbstractGUI.meldung("speichernFehlgeschlagen"));
		}

	}

	public void laden() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(AbstractGUI.verzeichnis() + "gui.save"));
			gUI.setHintergrund(br.readLine());
			gUI.getFarbe_weiss().setValue(Color.web(br.readLine()));
			gUI.getFarbe_schwarz().setValue(Color.web(br.readLine()));
			gUI.setName(br.readLine());
			gUI.getSounds().setValue(Boolean.parseBoolean(br.readLine()));
			gUI.getZweid().setValue(Boolean.parseBoolean(br.readLine()));
			gUI.form = br.readLine();
			gUI.getVonFarbe().setValue(br.readLine());
			gUI.getBisFarbe().setValue(br.readLine());
			gUI.getStage().setFullScreen(Boolean.parseBoolean(br.readLine()));
			gUI.getCss().setValue(br.readLine());
			br.close();

			switch (gUI.getHintergrund()) {
			case "marmor":
				map_marmor.setSelected(true);
				break;
			case "glas":
				map_glas.setSelected(true);
				break;
			case "gras":
				map_gras.setSelected(true);
				break;
			case "holz":
				map_holz.setSelected(true);
				break;
			}

			switch (gUI.form) {
			case "standard":
				figur_standard.setSelected(true);
				break;
			case "modern":
				figur_modern.setSelected(true);
				break;
			}

		} catch (Exception e) {
			GUI.logger.error(e.getMessage());
		}
	}
	
	public String toRGBCode(Color color) {
		return String.format("#%02X%02X%02X", (int) (color.getRed() * 255),
				(int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
	}
}
