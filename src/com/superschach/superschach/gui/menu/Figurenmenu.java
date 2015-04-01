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

public class Figurenmenu extends Menu {

	ColorPicker pick_weiss;
	ColorPicker pick_schwarz;
	public Figurenmenu(GUI gUI) {
		super(gUI, Schnittstelle.meldung("figuren"));
		HBox box = new HBox();
		pick_weiss = new ColorPicker(gUI.getFarbe_weiss().getValue());
		pick_schwarz = new ColorPicker(gUI.getFarbe_schwarz().getValue());
		box.getChildren().addAll(pick_weiss, new Separator(), pick_schwarz);
		pick_weiss.valueProperty().bindBidirectional(gUI.getFarbe_weiss());
		pick_schwarz.valueProperty().bindBidirectional(gUI.getFarbe_schwarz());

		pick_schwarz.valueProperty().addListener(new ChangeListener<Color>() {

			@Override
			public void changed(ObservableValue<? extends Color> arg0,
					Color arg1, Color arg2) {
				gUI.getEinstellungen().speichern();
			}
		});
		pick_weiss.valueProperty().addListener(new ChangeListener<Color>() {

			@Override
			public void changed(ObservableValue<? extends Color> arg0,
					Color arg1, Color arg2) {
				gUI.getEinstellungen().speichern();
			}
		});
		addInhalt(new Node[]{box});
	}

	@Override
	public void zurueck() {
		switchFenster(new Grafikmenu(getGUI()));
	}

}
