package gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import spiel.Schnittstelle;

public class Einstellungen extends Fenster {

	GUI gUI;
	VBox settings = new VBox();
	ColorPicker pick_weiss;
	ColorPicker pick_schwarz;
	ColorPicker pick_oben;
	ColorPicker pick_unten;
	ToggleGroup map = new ToggleGroup();
	RadioButton map_marmor = new RadioButton(Schnittstelle.meldung("marmor"));

	RadioButton map_holz = new RadioButton(Schnittstelle.meldung("holz"));

	RadioButton map_gras = new RadioButton(Schnittstelle.meldung("gras"));

	RadioButton map_glas = new RadioButton(Schnittstelle.meldung("glas"));

	ToggleGroup figur = new ToggleGroup();
	RadioButton figur_standard = new RadioButton(
			Schnittstelle.meldung("standard"));

	RadioButton figur_modern = new RadioButton(Schnittstelle.meldung("modern"));

	CheckBox dreiDAn = new CheckBox(Schnittstelle.meldung("dreiDAn"));
	CheckBox sounds = new CheckBox(Schnittstelle.meldung("tonAn"));

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

		Label ob = new Label(Schnittstelle.meldung("oberflaeche"));
		ob.setStyle("fx-text-fill: black;-fx-font-size:18;-fx-font-family: \"Arial Narrow\";-fx-font-weight: bold;");
		settings.getChildren().add(ob);
		settings.getChildren().add(map_marmor);
		settings.getChildren().add(map_holz);
		settings.getChildren().add(map_gras);
		settings.getChildren().add(map_glas);
		addLeer(settings);

		Label bg = new Label(Schnittstelle.meldung("hintergrund"));
		bg.setStyle("fx-text-fill: black;-fx-font-size:18;-fx-font-family: \"Arial Narrow\";-fx-font-weight: bold;");
		settings.getChildren().add(bg);
		HBox boxHintergrund = new HBox();
		pick_oben = new ColorPicker();
		pick_unten = new ColorPicker();
		pick_oben.valueProperty().addListener(new ChangeListener<Color>() {

			@Override
			public void changed(ObservableValue<? extends Color> arg0,
					Color arg1, Color arg2) {
				gUI.vonFarbe.setValue(toRGBCode(arg2));
				speichern();
			}

		});

		pick_unten.valueProperty().addListener(new ChangeListener<Color>() {

			@Override
			public void changed(ObservableValue<? extends Color> arg0,
					Color arg1, Color arg2) {
				gUI.bisFarbe.setValue(toRGBCode(arg2));
				speichern();
			}

		});
		boxHintergrund.getChildren().addAll(
				new Label(Schnittstelle.meldung("farbverlauf") + ": "),
				pick_oben, new Separator(), pick_unten);
		settings.getChildren().add(boxHintergrund);
		addLeer(settings);

		Label multi = new Label(Schnittstelle.meldung("mehrspieler"));
		multi.setStyle("fx-text-fill: black;-fx-font-size:18;-fx-font-family: \"Arial Narrow\";-fx-font-weight: bold;");
		settings.getChildren().add(multi);
		TextField name = new TextField();
		Button nameOK = new Button(Schnittstelle.meldung("nameAendern"));
		HBox namenBox = new HBox();
		namenBox.getChildren().addAll(name, nameOK);
		settings.getChildren().add(namenBox);

		addLeer(settings);

		HBox box = new HBox();
		pick_weiss = new ColorPicker(gUI.farbe_weiss.getValue());
		pick_schwarz = new ColorPicker(gUI.farbe_schwarz.getValue());
		box.getChildren().addAll(
				new Label(Schnittstelle.meldung("weiss") + ": "), pick_weiss,
				new Separator(),
				new Label(Schnittstelle.meldung("schwarz") + ": "),
				pick_schwarz);
		pick_weiss.valueProperty().bindBidirectional(gUI.farbe_weiss);
		pick_schwarz.valueProperty().bindBidirectional(gUI.farbe_schwarz);
		Label fig = new Label(Schnittstelle.meldung("figuren"));
		fig.setStyle("fx-text-fill: black;-fx-font-size:18;-fx-font-family: \"Arial Narrow\";-fx-font-weight: bold;");
		settings.getChildren().add(fig);
		settings.getChildren().add(box);
		figur_standard.setToggleGroup(figur);
		figur_modern.setToggleGroup(figur);
		figur_standard.setSelected(true);
		settings.getChildren().add(figur_standard);
		settings.getChildren().add(figur_modern);
		addLeer(settings);

		gUI.sounds.bind(sounds.selectedProperty());
		gUI.zweid.bind(dreiDAn.selectedProperty().not());
		Label sonstiges = new Label(Schnittstelle.meldung("sonstiges"));
		sonstiges
				.setStyle("fx-text-fill: black;-fx-font-size:18;-fx-font-family: \"Arial Narrow\";-fx-font-weight: bold;");
		settings.getChildren().add(sonstiges);
		settings.getChildren().add(dreiDAn);
		settings.getChildren().add(sounds);
		sounds.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov,
					Boolean old_val, Boolean new_val) {
				speichern();
			}
		});
		dreiDAn.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov,
					Boolean old_val, Boolean new_val) {
				//gUI.wechsleDimension(new_val);
				speichern();
			}
		});

		name.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (!name.getText().equals("")) {
					gUI.name = name.getText();
					name.setText("");
					speichern();
				}
			}
		});
		// laden();
		map.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov,
					Toggle old_toggle, Toggle new_toggle) {
				if (map.getSelectedToggle() == map_holz) {
					gUI.hintergrund = "holz";
				} else if (map.getSelectedToggle() == map_marmor) {
					gUI.hintergrund = "marmor";
				} else if (map.getSelectedToggle() == map_gras) {
					gUI.hintergrund = "gras";
				} else if (map.getSelectedToggle() == map_glas) {
					gUI.hintergrund = "glas";
				}
				gUI.feld.aktualisiereMap();
				speichern();
			}
		});

		figur.selectedToggleProperty().addListener(
				new ChangeListener<Toggle>() {
					public void changed(ObservableValue<? extends Toggle> ov,
							Toggle old_toggle, Toggle new_toggle) {
						if (figur.getSelectedToggle() == figur_modern) {
							gUI.form = "modern";
						} else if (figur.getSelectedToggle() == figur_standard) {
							gUI.form = "standard";
						}
						gUI.feld.entferneFiguren();
						gUI.feld.startaufstellung();
						gUI.feld.aktualisieren();
						speichern();
					}
				});

		pick_schwarz.valueProperty().addListener(new ChangeListener<Color>() {

			@Override
			public void changed(ObservableValue<? extends Color> arg0,
					Color arg1, Color arg2) {
				speichern();
			}
		});
		pick_weiss.valueProperty().addListener(new ChangeListener<Color>() {

			@Override
			public void changed(ObservableValue<? extends Color> arg0,
					Color arg1, Color arg2) {
				speichern();
			}
		});

		nameOK.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (!name.getText().equals("")) {
					gUI.name = name.getText();
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

	private void speichern() {
		try {
			BufferedWriter br = new BufferedWriter(new FileWriter(
					Schnittstelle.verzeichnis() + "gui.save"));
			br.write(gUI.hintergrund);
			br.write(System.getProperty("line.separator"));
			br.write(toRGBCode(pick_weiss.getValue()));
			br.write(System.getProperty("line.separator"));
			br.write(toRGBCode(pick_schwarz.getValue()));
			br.write(System.getProperty("line.separator"));
			br.write(gUI.name);
			br.write(System.getProperty("line.separator"));
			br.write("" + gUI.sounds.getValue());
			br.write(System.getProperty("line.separator"));
			br.write("" + gUI.zweid.getValue());
			br.write(System.getProperty("line.separator"));
			br.write(gUI.form);
			br.write(System.getProperty("line.separator"));
			br.write(toRGBCode(pick_oben.getValue()));
			br.write(System.getProperty("line.separator"));
			br.write(toRGBCode(pick_unten.getValue()));
			br.flush();
			br.close();
		} catch (IOException e) {
			gUI.spiel.meldungAusgeben(Schnittstelle
					.meldung("speichernFehlgeschlagen"));
		}

	}

	public void laden() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					Schnittstelle.verzeichnis() + "gui.save"));
			gUI.hintergrund = br.readLine();
			pick_weiss.setValue(Color.web(br.readLine()));
			pick_schwarz.setValue(Color.web(br.readLine()));
			gUI.name = br.readLine();
			sounds.setSelected(Boolean.parseBoolean(br.readLine()));
			dreiDAn.setSelected(!Boolean.parseBoolean(br.readLine()));
			gUI.form = br.readLine();
			gUI.vonFarbe.setValue(br.readLine());
			gUI.bisFarbe.setValue(br.readLine());
			br.close();

			switch (gUI.hintergrund) {
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
			pick_unten.setValue(Color.web(gUI.bisFarbe.getValue()));
			pick_oben.setValue(Color.web(gUI.vonFarbe.getValue()));
			
		} catch (IOException e) {
			sounds.setSelected(true);
			dreiDAn.setSelected(true);
		}
	}

	public String toRGBCode(Color color) {
		return String.format("#%02X%02X%02X", (int) (color.getRed() * 255),
				(int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
	}
}
