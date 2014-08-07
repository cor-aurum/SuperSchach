package gui;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.MeshView;

public class Figur extends MeshView {

	Feld f;
	int figur;

	public Figur(Feld f, int figur) {
		// super(25, 25, 75);
		this.f = f;
		this.figur = figur;

		try {
			setMesh(f.gUI.gebeMesh(figur));
		} catch (Exception e) {
			System.out.println("Figur nicht gefunden");
		}

		if (f.gUI.form.equals("modern")) {
			setScaleX(2);
			setScaleY(2);
			setScaleZ(2);
			setTranslateZ((getBoundsInLocal().getDepth() * getScaleZ()) / 2 + 5);
		}
		setRotate(figur > 0 ? 90 : -90);
		// getTransforms().add(new Rotate(0, 0, 0));

		setMaterial(f.gUI.gebeFigurenMaterial(figur));
		f.gUI.root3D.getChildren().add(this);
		setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				f.gUI.spiel.klick(Figur.this.f.x, Figur.this.f.y);
				f.gUI.aktualisieren();
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
