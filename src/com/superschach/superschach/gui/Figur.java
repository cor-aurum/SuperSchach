package com.superschach.superschach.gui;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.MeshView;

public class Figur{

	Feld f;
	int figur;
	private MeshView mesh;

	public void erstelleFigur(Feld f, int figur, DreiD dreid) {
		// super(25, 25, 75);
		this.f = f;
		this.figur = figur;

		/*
		try {
			//this = dreid.gUI.gebeMesh(figur);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(Schnittstelle.meldung("figurNichtGefunden"));
		}
		*/

		if (dreid.gUI.form.equals("modern")) {
			mesh.setScaleX(20);
			mesh.setScaleY(20);
			mesh.setScaleZ(20);
			//mesh.setTranslateZ((mesh.getBoundsInLocal().getDepth() * mesh.getScaleZ()) / 2 + 5);
		}
		mesh.setRotate(figur > 0 ? 90 : -90);
		mesh.setCache(true);

		mesh.setMaterial(dreid.gUI.gebeFigurenMaterial(figur));
		dreid.root3D.getChildren().add(mesh);
		mesh.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				dreid.gUI.spiel.klick(Figur.this.f.x, Figur.this.f.y);
				dreid.aktualisieren();
			}
		});

		if (figur == 16) {
			dreid.setKoenigWeiss(this);
		}
		if (figur == -16) {
			dreid.setKoenigSchwarz(this);
		}
		setzeFigur();

	}

	private void setzeFigur() {
		// localToScene(f);
		mesh.setTranslateX(f.getX());
		mesh.setTranslateY(f.getY());
		// setTranslateZ((getBoundsInLocal().getDepth() * getScaleZ()) / 2 + 5);
		// setTranslateZ(0);
	}

	public void setFeld(Feld f) {
		this.f = f;
		// setzeFigur();
	}
	
	public MeshView getMesh()
	{
		return mesh;
	}
	
	public void setMesh(MeshView mesh)
	{
		this.mesh=mesh;
	}
}
