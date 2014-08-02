package gui;

import java.io.File;
import java.net.URISyntaxException;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.MeshView;

import com.interactivemesh.jfx.importer.stl.StlMeshImporter;

public class Figur extends MeshView {

	Feld f;
	int figur;

	public Figur(Feld f, int figur) {
		// super(25, 25, 75);
		this.f = f;
		this.figur = figur;

		String modell;

		switch (Math.abs(figur)) {
		case 1:
			modell = "turm";
			break;
		case 4:
			modell = "springer";
			break;
		case 2:
			modell = "laeufer";
			break;
		case 3:
			modell = "dame";
			break;
		case 16:
			modell = "koenig";
			break;
		case 8:
			modell = "bauer";
			break;
		default:
			modell = "";
			return;
		}

		if (f.gUI.modell_farbe) {
			if (figur > 0) {
				modell += "_weiss";
			} else {
				modell += "_schwarz";
			}
		}

		File file = null;
		try {
			file = new File(this
					.getClass()
					.getClassLoader()
					.getResource(
							"gui/meshes/" + f.gUI.form + "_" + modell + ".stl")
					.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		StlMeshImporter importer = new StlMeshImporter();
		importer.read(file);
		Mesh mesh = importer.getImport();
		setMesh(mesh);

		setScaleX(8);
		setScaleY(8);
		setScaleZ(8);
		setRotate(figur > 0 ? 90 : -90);
		// getTransforms().add(new Rotate(0, 0, 0));

		PhongMaterial material = new PhongMaterial();
		if (figur > 0) {
			material.setDiffuseColor(Color.AZURE);
			material.setSpecularColor(Color.LIGHTYELLOW);
		} else {
			material.setDiffuseColor(Color.NAVY);
			material.setSpecularColor(Color.AQUAMARINE);
		}

		setMaterial(material);
		f.gUI.root3D.getChildren().add(this);
		setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				f.gUI.spiel.klick(f.x, f.y);
				f.gUI.aktualisieren();
			}
		});
		setzeFigur();

	}

	private void setzeFigur() {
		// localToScene(f);
		setTranslateX(f.getX());
		setTranslateY(f.getY());
		setTranslateZ((getBoundsInLocal().getDepth() * getScaleZ()) / 2 + 5);
	}

	public void stirb() {
		f.gUI.root3D.getChildren().remove(this);
	}

	public void setFeld(Feld f) {
		this.f = f;
		setzeFigur();
	}
}
