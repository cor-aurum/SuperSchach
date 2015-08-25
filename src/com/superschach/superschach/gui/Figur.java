package com.superschach.superschach.gui;

import com.superschach.superschach.spiel.Schnittstelle;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.MeshView;

public class Figur {

	private Feld f;
	private int figur;
	private MeshView mesh;
	private DreiD dreid;
	private int geschlageneFiguren;
	private boolean bewegt=false;
	
	
	public Figur(MeshView meshview, Feld f, int figur, DreiD dreid) {
		// super(25, 25, 75);
		this.dreid = dreid;
		this.f = f;
		this.figur = figur;
		mesh = meshview;
		/*
		 * try { //this = dreid.gUI.gebeMesh(figur); } catch (Exception e) {
		 * e.printStackTrace();
		 * System.out.println(Schnittstelle.meldung("figurNichtGefunden")); }
		 */

		/*
		 * if (dreid.gUI.form.equals("modern")) { mesh.setScaleX(2);
		 * mesh.setScaleY(2); mesh.setScaleZ(2);
		 * mesh.setTranslateZ((mesh.getBoundsInLocal().getDepth() *
		 * mesh.getScaleZ()) / 2 + 5); }
		 */
		mesh.setScaleX(40);
		mesh.setScaleY(40);
		mesh.setScaleZ(40);
		mesh.setTranslateZ((mesh.getBoundsInLocal().getDepth() * mesh
				.getScaleZ()) / 2 + 5);
		// mesh.getTransforms().add(new Rotate(figur > 0 ? 0 : 180,0,0));
		mesh.setRotate(figur > 0 ? 0 : 180);
		// mesh.setRotationAxis(arg0);
		mesh.setCache(true);

		mesh.setMaterial(dreid.gUI.gebeFigurenMaterial(figur));
		dreid.root3D.getChildren().add(mesh);
		mesh.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (event.getButton().equals(MouseButton.SECONDARY)) {
					dreid.gUI.spiel.meldungAusgeben(Schnittstelle.meldung((!bewegt?"nicht_":"")+"bewegt")+
				System.lineSeparator()+
				Schnittstelle.meldung("figuren_geschlagen").replaceAll("%", ""+geschlageneFiguren));
				} else {
					dreid.gUI.spiel.klick(Figur.this.f.x, Figur.this.f.y);
					dreid.aktualisieren();
				}
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

	public void setMeshView(MeshView mv) {
		mesh = mv;
		dreid.root3D.getChildren().add(mesh);
	}

	public int getID() {
		return figur;
	}

	public MeshView getMeshView() {
		return mesh;
	}
	
	public void setzeBewegt()
	{
		bewegt=true;
	}
	
	public void inkrementGeschlageneFiguren()
	{
		geschlageneFiguren++;
	}
}
