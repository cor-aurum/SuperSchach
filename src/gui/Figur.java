package gui;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class Figur extends Box {

	Feld f;
	int figur;

	public Figur(Feld f, int figur) {
		super(25, 25, 100);
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
		PhongMaterial material = new PhongMaterial();
		if (figur > 0) {
			material.setDiffuseColor(Color.ANTIQUEWHITE);
			material.setSpecularColor(Color.LIGHTGREY);
		}
		else
		{
			material.setDiffuseColor(Color.BLACK);
			material.setSpecularColor(Color.DARKSLATEBLUE);
		}

		setMaterial(material);
		f.gUI.root3D.getChildren().add(this);
		setzeFigur();

	}

	private void setzeFigur() {
		localToScene(f);
		setTranslateX(f.getX());
		setTranslateY(f.getY());
		setTranslateZ(f.getZ() + getDepth() / 2);
	}

	public void setFeld(Feld f) {
		this.f = f;
		setzeFigur();
	}
}
