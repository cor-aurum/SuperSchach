package com.superschach.superschach.gui;

import com.superschach.superschach.spiel.Schnittstelle;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class Meldung extends Dialog {
	public Meldung(String[] nachricht, MyStackPane feld, int fontSize) {
		super((int) (nachricht[0].length() * (fontSize * 0.75)),
				100 + (nachricht.length * fontSize + 10));
		BorderPane root = new BorderPane();
		String s = "";
		for (int i = 0; i < nachricht.length; i++) {
			s += ((i != 0 ? System.lineSeparator() : "") + nachricht[i]);
		}
		;
		Label text = new Label(s);
		Button ok = new Button(Schnittstelle.meldung("ok"));
		text.setStyle("-fx-font-size:" + fontSize + ";");

		ok.setPrefWidth(100);
		root.setCenter(text);
		root.setBottom(ok);
		BorderPane.setAlignment(ok, Pos.BASELINE_CENTER);
		getChildren().add(root);
		Pane p = new Pane();
		feld.getChildren().add(p);
		// p.setStyle("-fx-background-color: #006464;-fx-background-radius: 10;");
		feld.getChildren().add(this);
		ok.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				feld.getChildren().remove(p);
				feld.getChildren().remove(Meldung.this);
			}
		});
	}
}
