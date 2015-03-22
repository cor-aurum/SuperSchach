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
	public Meldung(String nachricht, MyStackPane feld)
	{
		BorderPane root=new BorderPane();
		Label text=new Label(nachricht);
		Button ok=new Button(Schnittstelle.meldung("ok"));
		text.setStyle("-fx-font-size:40;");
		ok.setPrefWidth(100);
		root.setCenter(text);
		root.setBottom(ok);
		BorderPane.setAlignment(ok, Pos.BASELINE_CENTER);
		getChildren().add(root);
		Pane p=new Pane();
		feld.getChildren().add(p);
		//p.setStyle("-fx-background-color: #006464;-fx-background-radius: 10;");
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
