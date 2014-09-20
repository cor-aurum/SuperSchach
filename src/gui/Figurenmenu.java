package gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import spiel.Schnittstelle;

public class Figurenmenu extends Menu {

	ColorPicker pick_weiss;
	ColorPicker pick_schwarz;
	public Figurenmenu(GUI gUI) {
		super(gUI, Schnittstelle.meldung("figuren"));
		HBox box = new HBox();
		pick_weiss = new ColorPicker(gUI.farbe_weiss.getValue());
		pick_schwarz = new ColorPicker(gUI.farbe_schwarz.getValue());
		box.getChildren().addAll(pick_weiss, new Separator(), pick_schwarz);
		pick_weiss.valueProperty().bindBidirectional(gUI.farbe_weiss);
		pick_schwarz.valueProperty().bindBidirectional(gUI.farbe_schwarz);

		pick_schwarz.valueProperty().addListener(new ChangeListener<Color>() {

			@Override
			public void changed(ObservableValue<? extends Color> arg0,
					Color arg1, Color arg2) {
				gUI.einstellungen.speichern();
			}
		});
		pick_weiss.valueProperty().addListener(new ChangeListener<Color>() {

			@Override
			public void changed(ObservableValue<? extends Color> arg0,
					Color arg1, Color arg2) {
				gUI.einstellungen.speichern();
			}
		});
		addInhalt(new Node[]{box});
	}

	@Override
	public void zurueck() {
		switchFenster(new Grafikmenu(gUI));
	}

}
