package com.superschach.superschach.gui.menu;

import com.superschach.superschach.gui.Fenster;
import com.superschach.superschach.gui.GUI;
import com.superschach.superschach.spiel.Schnittstelle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;

public abstract class Menu extends Fenster {

	private VBox komp = new VBox();

	public Menu(GUI gUI, String ueberschrift) {
		super(gUI);
		setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ESCAPE) {
					zurueck();
				}
			}
		});

		setPadding(new Insets(100, 100, 100, 100));
		Label titel = new Label(ueberschrift);
		titel.setStyle("-fx-font-size:50;-fx-padding:20,20,20,20;");
		// titel.setFont(Font.font("Impact", 50));

		komp.getChildren().add(titel);
		Pane p = new Pane();
		p.setId("weiss");
		getChildren().add(p);
		setzeInhalt(komp);
	}

	public void addInhalt(Node[] punkte) {
		for (Node p : punkte) {
			HBox box = new HBox();
			Pane pane = new Pane();
			pane.setMinWidth(150);
			box.getChildren().addAll(pane, p);
			komp.getChildren().add(box);

			p.setId("menu-punkte");
			box.setOnMouseEntered(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (getGUI().getSounds().getValue()) {
						AudioClip plonkSound = new AudioClip(this.getClass()
								.getClassLoader()
								.getResource("com/superschach/superschach/gui/sounds/hover.aiff")
								.toString());
						plonkSound.play();
					}
					pane.setMinWidth(200);
				}
			});
			box.setOnMouseExited(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					pane.setMinWidth(150);
				}
			});
		}
		komp.getChildren().add(new Label("\n"));
		Button zurueck = new Button(Schnittstelle.meldung("zurueck"));
		// zurueck.setStyle("-fx-background-color:linear-gradient(from 0px 0px to 50px 0px, #000000,rgba(255, 255, 255, 0) );");
		zurueck.setId("menu-punkte");
		HBox box = new HBox();
		Pane pane = new Pane();
		pane.setMinWidth(100);
		box.getChildren().addAll(pane, zurueck);
		zurueck.setFont(Font.font("Impact", 30));
		box.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (getGUI().getSounds().getValue()) {
					AudioClip plonkSound = new AudioClip(this.getClass()
							.getClassLoader()
							.getResource("com/superschach/superschach/gui/sounds/hover.aiff").toString());
					plonkSound.play();
				}
				pane.setMinWidth(150);
			}
		});
		box.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				pane.setMinWidth(100);
			}
		});

		komp.getChildren().add(box);
		zurueck.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				zurueck();
			}
		});
	}

	public abstract void zurueck();
}
