package gui;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GUI extends Application {

	BorderPane pane = new BorderPane();
	Node rechts;
	Box feld = new Box(500, 500, 10);
	Xform root3D = new Xform();
	protected String hintergrund = "marmor";
	PhongMaterial feldMaterial = new PhongMaterial();
	Slider xslider = new Slider();
	Slider yslider = new Slider();
	private Feld[][] felder;
	FxSchnittstelle spiel=new FxSchnittstelle();

	public static void main(String args[]) throws Exception {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		PerspectiveCamera kamera = new PerspectiveCamera();
		// kamera.setFieldOfView(50.0);
		felder = new Feld[spiel.getXMax() + 1][spiel.getYMax() + 1];
		for (int a = 0; a < spiel.getYMax() + 1; a++)
		{
			for (int b = 0; b < spiel.getXMax() + 1; b++)
			{
				felder[b][a] = new Feld(spiel, b, a);
			}
		}
		root3D.getChildren().add(feld);
		rechts = new Rechts(this);
		pane.setRight(rechts);
		pane.setCenter(root3D);
		pane.setLeft(xslider);
		pane.setBottom(yslider);
		xslider.setOrientation(Orientation.VERTICAL);

		aktualisiereMap();
		// root3D.rx.setAngle(-70);

		Scene scene = new Scene(pane, 1200, 800);
		scene.setCamera(kamera);

		xslider.setMin(110);
		xslider.setMax(180);
		yslider.setMin(0);
		yslider.setMax(180);
		root3D.rx.angleProperty().bind(xslider.valueProperty());
		root3D.rz.angleProperty().bind(yslider.valueProperty());
		

		// scene.onMouseDraggedProperty().set(new MouseEventHandler());
		stage.setScene(scene);
		stage.setTitle("Super Schach");
		stage.getIcons()
				.add(new Image(this.getClass().getClassLoader()
						.getResource("gui/bilder/bauer_schwarz.png").toString()));
		stage.show();
		Timeline animation = new Timeline();
		animation.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO, new KeyValue(
						yslider.valueProperty(), 0d)),
				new KeyFrame(Duration.valueOf("2s"), new KeyValue(yslider
						.valueProperty(), 360d)),
				new KeyFrame(Duration.valueOf("1s"), new KeyValue(xslider
						.valueProperty(), 180d)));

		animation.play();
	}

	public void aktualisiereMap() {
		feldMaterial.setDiffuseMap(new Image(this.getClass().getClassLoader()
				.getResource("gui/bilder/brett.png").toString()));

		feldMaterial.setBumpMap(new Image(this.getClass().getClassLoader()
				.getResource("gui/bilder/" + hintergrund + "_NRM.png")
				.toString()));
		// feldMaterial.bumpMapProperty().bind(arg0);
		feldMaterial.setSpecularMap(new Image(this.getClass().getClassLoader()
				.getResource("gui/bilder/" + hintergrund + "_SPEC.png")
				.toString()));
		feld.setMaterial(feldMaterial);
	}
}
