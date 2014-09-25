package gui;

import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Warten extends ImageView {
	public Warten() {
		setImage(new Image(this.getClass().getClassLoader()
				.getResource("gui/bilder/warten.png").toString()));

		RotateTransition rt = new RotateTransition(Duration.millis(1200), this);
		rt.setByAngle(-360);
		rt.setCycleCount(Animation.INDEFINITE);
		//rt.setAutoReverse(true);

		rt.play();
	}

}
