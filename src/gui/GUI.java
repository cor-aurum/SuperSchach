package gui;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.stage.Stage;
import javafx.util.Duration;


public class GUI extends Application{

	BorderPane pane=new BorderPane();
	Box feld = new Box(500,500,15);
	Xform root3D = new Xform();
	public static void main(String args[]) throws Exception
	{
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		//PerspectiveCamera kamera = new PerspectiveCamera(true);
		//kamera.setFieldOfView(50.0);
		
		root3D.getChildren().add(feld);
		pane.setCenter(root3D);
		PhongMaterial feldMaterial=new PhongMaterial();
		feldMaterial.setDiffuseColor(Color.WHITE);
	    feldMaterial.setSpecularColor(Color.WHITE);
		feldMaterial.setBumpMap(new Image(this.getClass().getClassLoader().getResource("gui/bilder/holz_hell.png").toString()));
		feld.setMaterial(feldMaterial);
		root3D.rx.setAngle(-70);
		Scene scene=new Scene(pane,800,800);
		scene.setCamera(new PerspectiveCamera());
		stage.setScene(scene);
		stage.setTitle("Super Schach");
		stage.getIcons().add(new Image(this.getClass().getClassLoader().getResource("gui/bilder/bauer_schwarz.png").toString()));
		stage.show();
		Timeline animation = new Timeline();
        animation.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(root3D.ry.angleProperty(), 0d)
                ),
                new KeyFrame(Duration.valueOf("5s"),
                        new KeyValue(root3D.ry.angleProperty(), 360d)
                ));
        animation.play();
	}
}
