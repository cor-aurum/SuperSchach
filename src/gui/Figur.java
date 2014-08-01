package gui;

import java.io.File;
import java.net.URISyntaxException;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

import com.interactivemesh.jfx.importer.stl.StlMeshImporter;

public class Figur extends MeshView {

	Feld f;
	int figur;

	public Figur(Feld f, int figur) {
		//super(25, 25, 75);
		this.f = f;
		this.figur = figur;
		
		

		switch (Math.abs(figur)) {
		case 1:
			
			break;
		case 4:

			break;
		case 2:

			break;
		case 3:

			break;
		case 16:

			break;
		case 8:

			break;
		default:
			return;
		}
		
		
		File file=null;
		try {
			file = new File(this.getClass().getClassLoader()
					.getResource("gui/meshes/test2.stl")
					.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	    StlMeshImporter importer = new StlMeshImporter();
	    importer.read(file);
	    Mesh mesh = importer.getImport();
	    setMesh(mesh);
	    
	    setScaleX(15);
	    setScaleY(15);
	    setScaleZ(15);
	    setRotate(figur>0?90:-90);
	    //getTransforms().add(new Rotate(0, 0, 0));
		
		PhongMaterial material = new PhongMaterial();
		if (figur > 0) {
			material.setDiffuseColor(Color.AZURE);
			material.setSpecularColor(Color.LIGHTYELLOW);
		}
		else
		{
			material.setDiffuseColor(Color.NAVY);
			material.setSpecularColor(Color.AQUAMARINE);
		}

		setMaterial(material);
		f.gUI.root3D.getChildren().add(this);
		setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				f.gUI.spiel.klick(f.x,f.y);
				f.gUI.aktualisieren();
			}
		});
		setzeFigur();

	}

	private void setzeFigur() {
		//localToScene(f);
		setTranslateX(f.getX());
		setTranslateY(f.getY());
		setTranslateZ(50);
	}
	
	public void stirb()
	{
		f.gUI.root3D.getChildren().remove(this);
	}

	public void setFeld(Feld f) {
		this.f = f;
		setzeFigur();
	}
}
