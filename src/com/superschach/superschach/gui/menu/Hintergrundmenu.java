package com.superschach.superschach.gui.menu;

import com.superschach.superschach.gui.GUI;
import com.superschach.superschach.spiel.Schnittstelle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class Hintergrundmenu extends Menu {
	ColorPicker pick_oben;
	ColorPicker pick_unten;

	public Hintergrundmenu(GUI gUI) {
		super(gUI, Schnittstelle.meldung("hintergrund"));
		pick_oben = new ColorPicker();
		pick_unten = new ColorPicker();
		pick_unten.setValue(Color.web(gUI.getBisFarbe().getValue()));
		pick_oben.setValue(Color.web(gUI.getVonFarbe().getValue()));
		pick_oben.valueProperty().addListener(new ChangeListener<Color>() {

			@Override
			public void changed(ObservableValue<? extends Color> arg0,
					Color arg1, Color arg2) {
				gUI.getVonFarbe().setValue(toRGBCode(arg2));
				gUI.getEinstellungen().speichern();
			}

		});

		pick_unten.valueProperty().addListener(new ChangeListener<Color>() {

			@Override
			public void changed(ObservableValue<? extends Color> arg0,
					Color arg1, Color arg2) {
				gUI.getBisFarbe().setValue(toRGBCode(arg2));
				gUI.getEinstellungen().speichern();
			}

		});

		HBox boxHintergrund = new HBox();

		boxHintergrund.getChildren().addAll(pick_oben, new Separator(),
				pick_unten);
		addInhalt(new Node[] { boxHintergrund });
	}

	@Override
	public void zurueck() {
		switchFenster(new Grafikmenu(getGUI()));
	}

	public String toRGBCode(Color color) {
		return String.format("#%02X%02X%02X", (int) (color.getRed() * 255),
				(int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
	}

}
