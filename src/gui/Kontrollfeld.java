package gui;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class Kontrollfeld extends HBox {

	private Button drehen = new Button();
	private Button zurueck = new Button();
	private Button chat = new Button();
	private Button vollbild = new Button();
	private Button einstellungen = new Button();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Kontrollfeld(GUI gUI) {
		setAlignment(Pos.BOTTOM_RIGHT);
		getChildren().addAll(drehen, zurueck, chat, vollbild, einstellungen);
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
				gUI.feld.resetBrett();
				gUI.feld.aktualisieren();
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
		
		vollbild.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				gUI.stage.setFullScreen(!gUI.stage.isFullScreen());
			}
		});
		
		gUI.stage.fullScreenProperty().addListener(new ChangeListener(){

			@Override
			public void changed(ObservableValue arg0, Object arg1, Object arg2) {
				if(gUI.stage.isFullScreen())
				{
					vollbild.setGraphic(new ImageView(new Image(this.getClass()
							.getClassLoader()
							.getResource("gui/bilder/vollbild_beenden.png").toString())));
				}
				else
				{
					vollbild.setGraphic(new ImageView(new Image(this.getClass()
							.getClassLoader()
							.getResource("gui/bilder/vollbild_starten.png").toString())));
				}
			}});

		einstellungen.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (gUI.einstellungen.isShowed())
					gUI.einstellungen.hide();
				else
					gUI.einstellungen.show();
			}
		});
		drehen.setGraphic(new ImageView(new Image(this.getClass()
				.getClassLoader().getResource("gui/bilder/drehen.png")
				.toString())));
		drehen.setStyle("-fx-background-color:transparent;");
		zurueck.setGraphic(new ImageView(new Image(this.getClass()
				.getClassLoader().getResource("gui/bilder/zurueck.png")
				.toString())));
		zurueck.setStyle("-fx-background-color:transparent;");
		vollbild.setGraphic(new ImageView(new Image(this.getClass()
				.getClassLoader()
				.getResource("gui/bilder/vollbild_starten.png").toString())));
		vollbild.setStyle("-fx-background-color:transparent;");
		einstellungen.setGraphic(new ImageView(new Image(this.getClass()
				.getClassLoader().getResource("gui/bilder/einstellungen.png")
				.toString())));
		einstellungen.setStyle("-fx-background-color:transparent;");
		chat.textProperty().bind(Bindings.concat(gUI.chat.ungelesenProperty()));
		chat.setStyle("-fx-background-color:transparent;");
		chat.setGraphic(new ImageView(
				new Image(this.getClass().getClassLoader()
						.getResource("gui/bilder/chat.png").toString())));
	}
}
