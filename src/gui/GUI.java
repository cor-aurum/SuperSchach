package gui;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GUI extends Application {

	BorderPane pane = new BorderPane();
	BorderPane root=new BorderPane();
	Node rechts;
	Box feld = new Box(500, 500, 10);
	Xform root3D = new Xform();
	protected String hintergrund = "marmor";
	PhongMaterial feldMaterial = new PhongMaterial();
	Slider xslider = new Slider();
	Slider yslider = new Slider();
	Slider zslider = new Slider();
	private Feld[][] felder;
	FxSchnittstelle spiel=new FxSchnittstelle(this);
	private boolean farbe=true;
	Canvas brett=new Canvas(feld.getWidth(),feld.getHeight());

	

	@Override
	public void start(Stage stage) throws Exception {
		PerspectiveCamera kamera = new PerspectiveCamera();
		// kamera.setFieldOfView(50.0);
		felder = new Feld[spiel.getXMax() + 1][spiel.getYMax() + 1];
		for (int a = 0; a < spiel.getYMax() + 1; a++)
		{
			for (int b = 0; b < spiel.getXMax() + 1; b++)
			{
				felder[b][a] = new Feld(this, b, a);
			}
		}
		root3D.getChildren().add(feld);
		rechts = new Rechts(this);
		root.setRight(rechts);
		root.setCenter(pane);
		pane.setCenter(root3D);
		pane.setLeft(xslider);
		pane.setBottom(yslider);
		pane.setTop(zslider);
		xslider.setOrientation(Orientation.VERTICAL);

		resetBrett();
		aktualisiereMap();
		
		feld.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                spiel.klick(((int)(event.getX()/(feld.getWidth()/felder.length)+4)),((int)(event.getY()/(feld.getHeight()/felder.length)+4)));
            }
        });

		Scene scene = new Scene(root, 1200, 800);
		
		scene.setCamera(kamera);

		xslider.setMin(110);
		xslider.setMax(250);
		yslider.setMin(0);
		yslider.setMax(180);
		zslider.setMin(-80);
		zslider.setMax(80);
		root3D.rx.angleProperty().bind(xslider.valueProperty());
		root3D.rz.angleProperty().bind(yslider.valueProperty());
		root3D.ry.angleProperty().bind(zslider.valueProperty());
		

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
				new KeyFrame(Duration.valueOf("1s"), new KeyValue(xslider
						.valueProperty(), 180d)));

		animation.play();
	}

	public void aktualisiereMap() {
		/*
		feldMaterial.setDiffuseMap(new Image(this.getClass().getClassLoader()
				.getResource("gui/bilder/brett.png").toString()));

		 */
		feldMaterial.setDiffuseMap(brett.snapshot(null, null));
		feldMaterial.setBumpMap(new Image(this.getClass().getClassLoader()
				.getResource("gui/bilder/" + hintergrund + "_NRM.png")
				.toString()));
		feldMaterial.setSpecularMap(new Image(this.getClass().getClassLoader()
				.getResource("gui/bilder/" + hintergrund + "_SPEC.png")
				.toString()));
		feld.setMaterial(feldMaterial);
	}
	
	public void farbe(int x, int y, int farbe)
	{
		if(this.farbe)
		{
			switch (farbe)
			{
			case 3:
				brett.getGraphicsContext2D().drawImage(new Image(this.getClass().getClassLoader()
						.getResource("gui/bilder/gruen.png").toString()), translateX(x), translateY(y));
				break;
			case 4:
				brett.getGraphicsContext2D().drawImage(new Image(this.getClass().getClassLoader()
						.getResource("gui/bilder/rot.png").toString()), translateX(x), translateY(y));
				break;
			case 5:
				brett.getGraphicsContext2D().drawImage(new Image(this.getClass().getClassLoader()
						.getResource("gui/bilder/gelb.png").toString()), translateX(x), translateY(y));
				break;
			}
			feldMaterial.setDiffuseMap(brett.snapshot(null, null));
		}
	}
	
	public void resetBrett()
	{
		brett.getGraphicsContext2D().drawImage(new Image(this.getClass().getClassLoader()
				.getResource("gui/bilder/brett.png").toString()), 0, 0);
	}
	
	protected double translateX(int x)
	{
		return (feld.getWidth()/felder.length)*(7-x);
	}
	
	protected double translateY(int y)
	{
		return (feld.getHeight()/felder.length)*y;
	}
	
	public static void main(String args[]) throws Exception {
		launch(args);
	}
}
