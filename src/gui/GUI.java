package gui;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GUI extends Application {

	BorderPane pane = new BorderPane();
	Box feld = new Box(500, 500, 10);
	Xform root3D = new Xform();
	private DoubleProperty zoom = new SimpleDoubleProperty(50);

	public static void main(String args[]) throws Exception {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		PerspectiveCamera kamera = new PerspectiveCamera();
		// kamera.setFieldOfView(50.0);

		root3D.getChildren().add(feld);
		pane.setCenter(root3D);
		PhongMaterial feldMaterial = new PhongMaterial();
		feldMaterial.setDiffuseMap(new Image(this.getClass().getClassLoader()
				.getResource("gui/bilder/brett.png").toString()));
		feldMaterial.setBumpMap(new Image(this.getClass().getClassLoader()
				.getResource("gui/bilder/holz_NRM.png").toString()));
		feldMaterial.setSpecularMap(new Image(this.getClass().getClassLoader()
				.getResource("gui/bilder/holz_SPEC.png").toString()));
		feld.setMaterial(feldMaterial);
		root3D.rx.setAngle(-70);

		Scene scene = new Scene(pane, 800, 800);
		scene.setCamera(kamera);

		kamera.fieldOfViewProperty().bind(zoom);

		scene.setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
					zoom.set(zoom.get() + (event.getDeltaY() / 8));
			}
		});

		scene.onMouseDraggedProperty().set(new MouseEventHandler());
		stage.setScene(scene);
		stage.setTitle("Super Schach");
		stage.getIcons()
				.add(new Image(this.getClass().getClassLoader()
						.getResource("gui/bilder/bauer_schwarz.png").toString()));
		stage.show();
		Timeline animation = new Timeline();
		animation.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO, new KeyValue(
						root3D.ry.angleProperty(), 0d)),
				new KeyFrame(Duration.valueOf("2s"), new KeyValue(root3D.ry
						.angleProperty(), 360d)));
		animation.play();
	}

	public class MouseEventHandler implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent mouseEvent) {
			double mouseX = mouseEvent.getSceneX();
			double mouseY = mouseEvent.getSceneY();

			root3D.rz.setAngle(mouseY);
			root3D.rx.setAngle(mouseX);
		}

	}
}
