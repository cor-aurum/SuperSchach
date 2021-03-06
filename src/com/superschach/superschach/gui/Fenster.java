package com.superschach.superschach.gui;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public abstract class Fenster extends StackPane {

	private GUI gUI;
	private boolean sichtbar = false;

	public Fenster(GUI gUI) {
		this.setGUI(gUI);
	}

	public void show() {
		GaussianBlur gB = new GaussianBlur();
		ColorAdjust cA = new ColorAdjust();
		gB.setInput(cA);
		getGUI().feld.getRoot().setEffect(gB);
		getGUI().feld.getChildren().add(this);
		Timeline animation = new Timeline();
		animation.getKeyFrames()
				.addAll(new KeyFrame(Duration.ZERO, new KeyValue(
						opacityProperty(), 0)),
						new KeyFrame(Duration.valueOf("0.6s"), new KeyValue(
								opacityProperty(), 1)),
						new KeyFrame(Duration.ZERO, new KeyValue(gB
								.radiusProperty(), 0)),
						new KeyFrame(Duration.valueOf("0.6s"), new KeyValue(gB
								.radiusProperty(), 15)),
						new KeyFrame(Duration.ZERO, new KeyValue(cA
								.saturationProperty(), 0)),
						new KeyFrame(Duration.valueOf("0.6s"), new KeyValue(cA
								.saturationProperty(), -1)));

		animation.play();
		sichtbar = true;

	}

	public void hide() {
		GaussianBlur gB = new GaussianBlur();
		ColorAdjust cA = new ColorAdjust();
		gB.setInput(cA);
		getGUI().feld.getRoot().setEffect(gB);
		Timeline animation = new Timeline();
		animation.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO,
						new KeyValue(opacityProperty(), 1.0)),
				new KeyFrame(Duration.valueOf("0.6s"), new KeyValue(
						opacityProperty(), 0)),
				new KeyFrame(Duration.ZERO, new KeyValue(gB.radiusProperty(),
						15)),
				new KeyFrame(Duration.valueOf("0.6s"), new KeyValue(gB
						.radiusProperty(), 0)),
				new KeyFrame(Duration.ZERO, new KeyValue(cA
						.saturationProperty(), -1)),
				new KeyFrame(Duration.valueOf("0.6s"), new KeyValue(cA
						.saturationProperty(), 0)));

		animation.play();
		// gUI.ablage.getChildren().remove(this);
		animation.setOnFinished(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				getGUI().feld.getChildren().remove(Fenster.this);
				sichtbar = false;
				getGUI().feld.getRoot().setEffect(null);
			}
		});
	}

	public void switchFenster(Fenster f) {
		getGUI().feld.getChildren().remove(this);
		getGUI().feld.getChildren().add(f);
		sichtbar = false;
	}

	public void setzeInhalt(Region r) {
		getChildren().add(r);
		// r.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5);");
	}

	public boolean isShowed() {
		return sichtbar;
	}

	public GUI getGUI() {
		return gUI;
	}

	public void setGUI(GUI gUI) {
		this.gUI = gUI;
	}
}
