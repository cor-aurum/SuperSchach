package gui;

import javafx.animation.FadeTransition;
import javafx.application.Preloader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

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
		stage.show();
	}

	public void verschwinde() {
		FadeTransition ft = new FadeTransition(Duration.millis(1000), stage
				.getScene().getRoot());
		ft.setFromValue(1.0);
		ft.setToValue(0.0);
		final Stage s = stage;
		EventHandler<ActionEvent> eh = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				s.hide();
			}
		};
		ft.setOnFinished(eh);
		ft.play();
	}
	
	@Override
    public void handleStateChangeNotification(StateChangeNotification evt) {
        if (evt.getType() == StateChangeNotification.Type.BEFORE_START) {
            stage.hide();
        }
    }  
}
