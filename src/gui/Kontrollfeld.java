package gui;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class Kontrollfeld extends HBox {

	private Button drehen = new Button();
	private Button zurueck = new Button();
	private Button chat = new Button("chat");
	private Button einstellungen = new Button();


	public Kontrollfeld(GUI gUI) {
		setAlignment(Pos.BOTTOM_RIGHT);
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
				gUI.chat.nachrichtErhalten("Hallo");
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
		drehen.setGraphic(new ImageView(new Image(this.getClass().getClassLoader()
				.getResource("gui/bilder/drehen.png").toString())));
		drehen.setStyle("-fx-background-color:transparent;");
		zurueck.setGraphic(new ImageView(new Image(this.getClass().getClassLoader()
				.getResource("gui/bilder/zurueck.png").toString())));
		zurueck.setStyle("-fx-background-color:transparent;");
		einstellungen.setGraphic(new ImageView(new Image(this.getClass().getClassLoader()
				.getResource("gui/bilder/einstellungen.png").toString())));
		einstellungen.setStyle("-fx-background-color:transparent;");
		chat.textProperty().bind(Bindings.concat(gUI.chat.ungelesenProperty()));
		chat.setStyle("-fx-background-color:transparent;");
		chat.setGraphic(new ImageView(new Image(this.getClass().getClassLoader()
				.getResource("gui/bilder/chat.png").toString())));
	}
}
