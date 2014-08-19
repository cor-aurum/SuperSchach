package gui;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.util.Duration;

public abstract class Fenster extends BorderPane {

	GUI gUI;
	private boolean sichtbar = false;
	private static boolean vorhanden = false;

	public Fenster(GUI gUI) {
		this.gUI = gUI;
		setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ESCAPE) {
					if (sichtbar)
						hide();
				}
			}
		});
	}

	public void show() {
		if (!Fenster.vorhanden) {
			Fenster.vorhanden = true;
			GaussianBlur gB = new GaussianBlur();
			ColorAdjust cA=new ColorAdjust();
			gB.setInput(cA);
			gUI.feld.getRoot().setEffect(gB);
			gUI.feld.getChildren().add(this);
			Timeline animation = new Timeline();
			animation.getKeyFrames().addAll(
					new KeyFrame(Duration.ZERO, new KeyValue(opacityProperty(),
							0)),
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
	}

	public void hide() {
		Fenster.vorhanden = false;
		GaussianBlur gB = new GaussianBlur();
		ColorAdjust cA=new ColorAdjust();
		gB.setInput(cA);
		gUI.feld.getRoot().setEffect(gB);
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
				gUI.feld.getChildren().remove(Fenster.this);
				sichtbar = false;
				gUI.feld.getRoot().setEffect(null);
			}
		});
	}

	public void setzeInhalt(Region r) {
		setCenter(r);
		//r.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5);");
	}

	public boolean isShowed() {
		return sichtbar;
	}
}
