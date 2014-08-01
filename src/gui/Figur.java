package gui;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class Figur extends Box {

	Feld f;
	int figur;

	public Figur(Feld f, int figur) {
		super(25, 25, 75);
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
			material.setSpecularColor(Color.LIGHTYELLOW);
		}
		else
		{
			material.setDiffuseColor(Color.DARKORCHID);
			material.setSpecularColor(Color.DARKSLATEBLUE);
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
		setTranslateZ(f.getZ() + getDepth() / 2);
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
