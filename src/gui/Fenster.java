package gui;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.util.Duration;

public abstract class Fenster extends BorderPane {

	GUI gUI;
	private boolean sichtbar = false;

	public Fenster(GUI gUI) {
		this.gUI = gUI;
		// setOpacity(0.5);
	}

	public void show() {
		gUI.feld.getChildren().add(this);
		Timeline animation = new Timeline();
		animation.getKeyFrames()
				.addAll(new KeyFrame(Duration.ZERO, new KeyValue(
						opacityProperty(), 0)),
						new KeyFrame(Duration.valueOf("0.6s"), new KeyValue(
								opacityProperty(), 1)));

		animation.play();
		sichtbar = true;
	}

	public void hide() {
		Timeline animation = new Timeline();
		animation.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO,
						new KeyValue(opacityProperty(), 1.0)),
				new KeyFrame(Duration.valueOf("0.6s"), new KeyValue(
						opacityProperty(), 0)));

		animation.play();
		//gUI.ablage.getChildren().remove(this);
		animation.setOnFinished(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				gUI.feld.getChildren().remove(Fenster.this);
				sichtbar = false;
			}
		});
	}

	public void setzeInhalt(Region r) {
		setCenter(r);
		r.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5);");
	}

	public boolean isShowed() {
		return sichtbar;
	}
}
