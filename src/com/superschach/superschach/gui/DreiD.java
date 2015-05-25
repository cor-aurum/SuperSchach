package com.superschach.superschach.gui;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.util.Duration;

public class DreiD extends MyStackPane {

	GUI gUI;
	SubScene scene;
	Xform root3D = new Xform();
	Box feld = new Box(500, 500, 10);
	private Feld[][] felder;
	private Figur[][] figuren;
	Canvas brett = new Canvas(feld.getWidth(), feld.getHeight());
	private Box[] rand = new Box[4];
	private DoubleProperty zoom = new SimpleDoubleProperty(0.0);
	Slider xslider = new Slider();
	Slider yslider = new Slider();
	Slider zslider = new Slider();
	private final int animationDuration = 600;
	private Figur koenigWeiss;
	private Figur koenigSchwarz;
	private boolean drehen = true;

	public DreiD(GUI gUI) {
		this.gUI = gUI;
		maxWidthProperty().bind(gUI.getStage().widthProperty());
		maxHeightProperty().bind(gUI.getStage().heightProperty().subtract(32));
		PerspectiveCamera kamera = new PerspectiveCamera();
		// kamera.setFieldOfView(50.0);
		felder = new Feld[gUI.spiel.getXMax() + 1][gUI.spiel.getYMax() + 1];
		figuren = new Figur[gUI.spiel.getXMax() + 1][gUI.spiel.getYMax() + 1];
		for (int a = 0; a < gUI.spiel.getYMax() + 1; a++) {
			for (int b = 0; b < gUI.spiel.getXMax() + 1; b++) {
				felder[b][a] = new Feld(gUI, this, b, a);
				figuren[b][a] = null;
			}
		}
		root3D.getChildren().add(feld);
		for (int i = 0; i < 4; i++) {
			int temp = (i & 1) * 520;
			rand[i] = new Box(temp + 20, 540 - temp, 10);
			root3D.getChildren().add(rand[i]);
			if (temp == 520) {
				rand[i].setTranslateY(i == 1 ? 260 : -260);
			} else {
				rand[i].setTranslateX(i == 0 ? 260 : -260);
			}
			rand[i].setTranslateZ(feld.getTranslateZ());
		}
		scene = new SubScene(root3D, 0, 0, true, SceneAntialiasing.BALANCED);
		scene.widthProperty().bind(widthProperty());
		scene.heightProperty().bind(heightProperty());
		root3D.translateXProperty().bind(widthProperty().divide(2));
		root3D.translateYProperty().bind(heightProperty().divide(2));
		getChildren().add(scene);
		feld.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
					gUI.spiel.klick(((int) (event.getX()
							/ (feld.getWidth() / felder.length) + 4)),
							((int) (event.getY()
									/ (feld.getHeight() / felder.length) + 4)));
					aktualisieren();
			}
		});
		scene.setCamera(kamera);
		scene.setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				if (zoom.get() > 0.5 || event.getDeltaY() > 0) {
					zoom.set(zoom.get() + event.getDeltaY() / 400);
				}
			}
		});
		feld.setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.isSecondaryButtonDown()) {
					double mouseY = mouseEvent.getSceneY();
					double mouseX = mouseEvent.getSceneX();

					xslider.setValue(mouseY / xslider.getMax() * 100);
					zslider.setValue(((mouseX - scene.getWidth() / 2) * 2
							/ scene.getWidth() * zslider.getMax())
							* -1);
				}
			}

		});
		zoom.set(root3D.s.getX());
		// zoom.set(700);
		root3D.s.xProperty().bind(zoom);
		root3D.s.yProperty().bind(zoom);
		root3D.s.zProperty().bind(zoom);
		xslider.setMin(110);
		xslider.setMax(250);
		yslider.setMin(0);
		yslider.setMax(180);
		zslider.setMin(-80);
		zslider.setMax(80);
		root3D.rx.angleProperty().bind(xslider.valueProperty());
		root3D.rz.angleProperty().bind(yslider.valueProperty());
		root3D.ry.angleProperty().bind(zslider.valueProperty());
		aktualisiereMap();
		Timeline animation = new Timeline();
		animation.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO, new KeyValue(
						yslider.valueProperty(), 180d)),
				new KeyFrame(Duration.ZERO, new KeyValue(xslider
						.valueProperty(), xslider.getMax())),
				new KeyFrame(Duration.valueOf("1s"), new KeyValue(yslider
						.valueProperty(), 0d)),
				new KeyFrame(Duration.valueOf("1.5s"), new KeyValue(xslider
						.valueProperty(), 150d)));

		animation.play();
	}

	public void aktualisiereMap() {
		/*
		 * feldMaterial.setDiffuseMap(new Image(this.getClass().getClassLoader()
		 * .getResource("gui/bilder/brett.png").toString()));
		 */
		gUI.feldMaterial.setDiffuseMap(brett.snapshot(null, null));
		gUI.feldMaterial.setBumpMap(new Image(this
				.getClass()
				.getClassLoader()
				.getResource(
						"com/superschach/superschach/gui/bilder/"
								+ gUI.hintergrund + "_NRM.png").toString()));
		gUI.feldMaterial.setSpecularMap(new Image(this
				.getClass()
				.getClassLoader()
				.getResource(
						"com/superschach/superschach/gui/bilder/"
								+ gUI.hintergrund + "_SPEC.png").toString()));
		feld.setMaterial(gUI.feldMaterial);

		PhongMaterial material = new PhongMaterial();
		switch (gUI.hintergrund) {
		case "marmor":
			material.setDiffuseColor(Color.BLANCHEDALMOND);
			material.setSpecularColor(Color.CHOCOLATE);
			gUI.feldMaterial.setSpecularPower(32.0);
			break;
		case "gras":
			material.setDiffuseColor(Color.BEIGE);
			material.setSpecularColor(Color.WHITE);
			gUI.feldMaterial.setSpecularPower(128.0);
			break;
		case "holz":
			material.setDiffuseColor(Color.DARKOLIVEGREEN);
			material.setSpecularColor(Color.GREEN);
			gUI.feldMaterial.setSpecularPower(1024.0);
			break;
		case "glas":
			material.setDiffuseColor(Color.TRANSPARENT);
			material.setSpecularColor(Color.WHITE);
			gUI.feldMaterial.setSpecularPower(2.0);
			break;
		default:
			material.setDiffuseColor(Color.AQUA);
			material.setSpecularColor(Color.AQUAMARINE);
			break;
		}
		for (int i = 0; i < 4; i++) {
			rand[i].setMaterial(material);
		}
	}

	public void farbe(int x, int y, int farbe) {
		if (gUI.getFarbe()) {
			switch (farbe) {
			case 3:
				brett.getGraphicsContext2D()
						.drawImage(
								new Image(
										this.getClass()
												.getClassLoader()
												.getResource(
														"com/superschach/superschach/gui/bilder/gruen.png")
												.toString()), translateX(x),
								translateY(y));
				break;
			case 4:
				brett.getGraphicsContext2D()
						.drawImage(
								new Image(
										this.getClass()
												.getClassLoader()
												.getResource(
														"com/superschach/superschach/gui/bilder/rot.png")
												.toString()), translateX(x),
								translateY(y));
				break;
			case 5:
				brett.getGraphicsContext2D()
						.drawImage(
								new Image(
										this.getClass()
												.getClassLoader()
												.getResource(
														"com/superschach/superschach/gui/bilder/gelb.png")
												.toString()), translateX(x),
								translateY(y));
				break;
			}
			gUI.feldMaterial.setDiffuseMap(brett.snapshot(null, null));
		}
	}

	@Override
	public void drehen() {
		if (!drehen)
			return;
		Timeline animation = new Timeline();
		animation.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO, new KeyValue(
						yslider.valueProperty(), yslider.getValue())),
				new KeyFrame(Duration.ZERO, new KeyValue(xslider
						.valueProperty(), xslider.getValue())),
				new KeyFrame(Duration.ZERO, new KeyValue(zslider
						.valueProperty(), zslider.getValue())),
				new KeyFrame(Duration.valueOf("1s"), new KeyValue(zslider
						.valueProperty(), zslider.getMin()
						+ (zslider.getMax() - zslider.getValue()))),
				new KeyFrame(Duration.valueOf("1s"), new KeyValue(yslider
						.valueProperty(), yslider.getMin()
						+ (yslider.getMax() - yslider.getValue()))),
				new KeyFrame(Duration.valueOf("1s"), new KeyValue(xslider
						.valueProperty(), xslider.getMin()
						+ (xslider.getMax() - xslider.getValue()))));
		drehen = false;
		animation.play();
		animation.setOnFinished(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				drehen = true;
			}
		});
	}

	public Figur[][] gebeFiguren() {
		return figuren;
	}

	@Override
	public synchronized void zug(byte[] zug) {
		final int sum = gUI.spiel.getXMax();
		Feld anfang = felder[sum - zug[0]][zug[1]];
		Feld ende = felder[sum - zug[2]][zug[3]];
		// Figur tempfigur = new Figur(anfang, anfang.gebeInhalt());
		Figur tempfigur;
		try {
			tempfigur = figuren[sum - anfang.x][anfang.y];

			root3D.getChildren().remove(figuren[sum - ende.x][ende.y]);
			figuren[sum - ende.x][ende.y] = tempfigur;
			Timeline animation = new Timeline(60.0);
			animation.getKeyFrames().addAll(
					new KeyFrame(Duration.ZERO, new KeyValue(
							xslider.valueProperty(), xslider.getValue())),
					new KeyFrame(Duration.ZERO, new KeyValue(tempfigur.getMesh()
							.translateXProperty(), anfang.getX())),
					new KeyFrame(Duration.ZERO, new KeyValue(tempfigur.getMesh()
							.translateYProperty(), anfang.getY())),
					new KeyFrame(Duration.valueOf("0.3s"), new KeyValue(
							tempfigur.getMesh().translateXProperty(), ende.getX())),
					new KeyFrame(Duration.valueOf("0.3s"), new KeyValue(
							tempfigur.getMesh().translateYProperty(), ende.getY())));
			animation.setOnFinished(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					// root3D.getChildren().remove(tempfigur);
					figuren[sum - anfang.x][anfang.y] = null;
					figuren[sum - ende.x][ende.y]
							.setFeld(felder[sum - ende.x][ende.y]);

					if (gUI.spiel.getStatus() == 1) {
						if (gUI.spiel.Player0()) {
							koenigWeiss.getMesh()
									.setMaterial(new PhongMaterial(Color.RED));
						} else {
							koenigSchwarz.getMesh().setMaterial(new PhongMaterial(
									Color.RED));
						}
					} else {
						koenigWeiss.getMesh().setMaterial(gUI.gebeFigurenMaterial(16));
						koenigSchwarz.getMesh().setMaterial(gUI.gebeFigurenMaterial(-16));
					}
				}
			});
			animation.play();
		} catch (Exception e) {
			// System.out.println("Animation nicht gefunden");
		}
	}

	public void resetBrett() {
		brett.getGraphicsContext2D().drawImage(gUI.brettbild, 0, 0);
	}

	protected double translateX(int x) {
		return (feld.getWidth() / felder.length) * (gUI.spiel.getXMax() - x);
	}

	protected double translateY(int y) {
		return (feld.getHeight() / felder.length) * y;
	}

	public void aktualisieren() {
		resetBrett();
	}

	public void aktualisierenFigur(int x, int y) {
		/*
		 * x=gUI.spiel.getXMax()-x; int figur = felder[x][y].gebeInhalt(); if
		 * (figur != 0) { if (figuren[x][y] != null) { if (figur != ((Figur)
		 * figuren[x][y]).figur) { root3D.getChildren().remove(figuren[x][y]);
		 * // figuren[x][y] = new Figur(felder[x][y], figur); // zug(); } } }
		 */
	}

	public void startaufstellung() {
		for (int x = 0; x < gUI.spiel.getXMax() + 1; x++) {
			for (int y = 0; y < gUI.spiel.getYMax() + 1; y++) {
				int figur = felder[x][y].gebeInhalt();
				figuren[x][y] = null;
				if (figur != 0) {
						figuren[x][y] = new Figur();
						try{
							figuren[x][y].setMesh(gUI.gebeMesh(figur));
							figuren[x][y].erstelleFigur(felder[x][y], figur, this);
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
				}
			}
		}
	}

	@Override
	public Image getScreenshot() {
		return scene.snapshot(null, null);
	}

	@Override
	public Node getFeld() {
		return feld;
	}

	@Override
	public void entferneFiguren() {
		List<Node> zuEntfernen = new ArrayList<Node>();
		for (int i = 0; i < root3D.getChildren().size(); i++) {
			if (root3D.getChildren().get(i) instanceof MeshView) {
				zuEntfernen.add(root3D.getChildren().get(i));
			}
		}
		root3D.getChildren().removeAll(zuEntfernen);
	}

	@Override
	public Node getRoot() {
		return scene;
	}

	@Override
	public void waitForAnimation() {
		try {
			Thread.sleep(animationDuration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void setKoenigWeiss(Figur f) {
		koenigWeiss = f;
	}

	public void setKoenigSchwarz(Figur f) {
		koenigSchwarz = f;
	}
}
