package gui;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.MeshView;

public class Figur extends MeshView {

	Feld f;
	int figur;

	public Figur(Feld f, int figur, DreiD dreid) {
		// super(25, 25, 75);
		this.f = f;
		this.figur = figur;

		try {
			setMesh(dreid.gUI.gebeMesh(figur));
		} catch (Exception e) {
			System.out.println("Figur nicht gefunden");
		}

		if (dreid.gUI.form.equals("modern")) {
			setScaleX(2);
			setScaleY(2);
			setScaleZ(2);
			setTranslateZ((getBoundsInLocal().getDepth() * getScaleZ()) / 2 + 5);
		}
		setRotate(figur > 0 ? 90 : -90);
		setCache(true);

		setMaterial(dreid.gUI.gebeFigurenMaterial(figur));
		dreid.root3D.getChildren().add(this);
		setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				dreid.gUI.spiel.klick(Figur.this.f.x, Figur.this.f.y);
				dreid.aktualisieren();
			}
		});
		setzeFigur();

	}

	private void setzeFigur() {
		// localToScene(f);
		setTranslateX(f.getX());
		setTranslateY(f.getY());
		// setTranslateZ((getBoundsInLocal().getDepth() * getScaleZ()) / 2 + 5);
		//setTranslateZ(0);
	}

	public void setFeld(Feld f) {
		this.f = f;
		// setzeFigur();
	}
}
