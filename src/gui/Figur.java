package gui;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class Figur extends Box {

	Feld f;
	int figur;
	public Figur(Feld f,int figur) {
		super(25, 25, 100);
		this.f=f;
		this.figur=figur;
		
		PhongMaterial material = new PhongMaterial();
		material.setDiffuseColor(Color.GREEN);
		material.setSpecularColor(Color.BLACK);
		
		setMaterial(material);
		f.gUI.root3D.getChildren().add(this);
		setzeFigur();
		
	}
	
	private void setzeFigur()
	{
		localToScene(f);
		setTranslateX(f.getX());
		setTranslateY(f.getY());
		setTranslateZ(f.getZ()+getDepth()/2);
	}
	
	public void setFeld(Feld f)
	{
		this.f=f;
		setzeFigur();
	}
}
