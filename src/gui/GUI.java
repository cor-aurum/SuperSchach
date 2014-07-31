package gui;

import javafx.application.Application;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.shape.Box;
import javafx.stage.Stage;


public class GUI extends Application{

	Box myBox = new Box(500,300,200);
	Xform root3D = new Xform();
	public static void main(String args[]) throws Exception
	{
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		PerspectiveCamera kamera = new PerspectiveCamera(true);
		kamera.setFieldOfView(50.0);
		
		root3D.getChildren().add(myBox);
		
		Scene scene=new Scene(root3D);
		scene.setCamera(kamera);
		stage.setScene(scene);
		stage.setTitle("Super Schach");
		stage.getIcons().add(new Image(this.getClass().getClassLoader().getResource("gui/bilder/bauer_schwarz.png").toString()));
		stage.show();
	}
}
