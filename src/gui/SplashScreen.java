package gui;

import javafx.application.Preloader;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SplashScreen extends Preloader {

	ProgressBar bar;
	Stage stage;
	StackPane root = new StackPane();

	@Override
	public void start(Stage stage) throws Exception {

		this.stage = new Stage();
		stage.setScene(new Scene(root, 640, 400));
		bar = new ProgressBar();
		ImageView view = new ImageView(new Image(this.getClass()
				.getClassLoader().getResource("gui/bilder/splash.png")
				.toString()));
		root.getChildren().add(view);
		root.getChildren().add(bar);
		bar.setTranslateX(128);
		bar.setTranslateY(135);
		view.setStyle("-fx-border-color: #5d5d5d;-fx-border-width: 3px;-fx-border-radius: 10;");
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setTitle("Super Schach");
		stage.getIcons()
				.add(new Image(this.getClass().getClassLoader()
						.getResource("gui/bilder/bauer_schwarz.png").toString()));
		stage.show();
		GUI.geladen.addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov,
					Boolean old_val, Boolean new_val) {
				stage.close();
			}
		});
	}

	public void verschwinde() {
		stage.close();
	}
}
