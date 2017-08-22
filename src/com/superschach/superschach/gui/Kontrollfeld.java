package com.superschach.superschach.gui;

import org.apache.log4j.Logger;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

public class Kontrollfeld extends HBox {

	private Button drehen = new Button();
	private Button chat = new Button();
	private Button menu = new Button();
	private TextField name = new TextField();
	private Logger logger=Logger.getLogger(Kontrollfeld.class);

	public Kontrollfeld(GUI gUI) {
		setMaxWidth(600);
		setMaxHeight(50);
		setAlignment(Pos.BOTTOM_RIGHT);
		getChildren().addAll(name, drehen, chat, menu);

		name.setPadding(new Insets(10, 10, 10, 10));
		name.setId("namensfeld");
		name.setAlignment(Pos.BASELINE_RIGHT);

		name.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					gUI.spiel.setSpielName(name.getText());
					drehen.requestFocus();
					gUI.getSpeichern().speichern();
				}
			}
		});

		drehen.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				gUI.feld.drehen();
			}
		});

		menu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (!gUI.getHauptmenu().isShowed()) {
					gUI.getHauptmenu().show();
				}
			}
		});

		chat.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				logger.debug("Chat wird geöffnet");
				if (gUI.chat.isShowed())
					gUI.chat.hide();
				else
					gUI.chat.show();
			}
		});
		drehen.setGraphic(new ImageView(new Image(this
				.getClass()
				.getClassLoader()
				.getResource(
						"com/superschach/superschach/gui/bilder/drehen.png")
				.toString())));
		drehen.setId("pfeil-detail");
		menu.setGraphic(new ImageView(
				new Image(
						this.getClass()
								.getClassLoader()
								.getResource(
										"com/superschach/superschach/gui/bilder/einstellungen.png")
								.toString())));
		menu.setId("pfeil-detail");
		chat.textProperty().bind(Bindings.concat(gUI.chat.ungelesenProperty()));
		chat.setId("pfeil-detail");
		chat.setGraphic(new ImageView(new Image(this.getClass()
				.getClassLoader()
				.getResource("com/superschach/superschach/gui/bilder/chat.png")
				.toString())));
	}

	public void setName(String s) {
		name.setText(s);
	}
}
