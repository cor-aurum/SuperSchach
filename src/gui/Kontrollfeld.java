package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;

public class Kontrollfeld extends HBox {

	private Button drehen = new Button("drehen");
	private Button zurueck = new Button("zurück");
	private Button chat = new Button("chat");
	private Button einstellungen = new Button("einstellungen");


	public Kontrollfeld(GUI gUI) {
		getChildren().addAll(drehen, new Separator(), zurueck, new Separator(),
				chat, new Separator(), einstellungen);
		drehen.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				gUI.feld.drehen();
			}
		});

		zurueck.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				gUI.spiel.zurueck();
			}
		});

		chat.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (gUI.chat.isShowed())
					gUI.chat.hide();
				else
					gUI.chat.show();
			}
		});
		
		einstellungen.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (gUI.einstellungen.isShowed())
					gUI.einstellungen.hide();
				else
					gUI.einstellungen.show();
			}
		});
	}
}
