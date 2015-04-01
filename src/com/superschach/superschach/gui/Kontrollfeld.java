package com.superschach.superschach.gui;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class Kontrollfeld extends HBox {

	private Button drehen = new Button();
	private Button chat = new Button();
	private Button menu = new Button();
	private Label name = new Label();

	public Kontrollfeld(GUI gUI) {
		setMaxWidth(600);
		setMaxHeight(50);
		setAlignment(Pos.BOTTOM_RIGHT);
		getChildren().addAll(name, drehen, chat, menu);
		
		name.setPadding(new Insets(10,10,10,10));

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
				System.out.println("Chat");
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
	
	public void setName(String s)
	{
		name.setText(s);
	}
}
