package gui;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public abstract class MyStackPane extends StackPane {

	public MyStackPane()
	{
		Pane background=new Pane();
		background.setStyle("-fx-background-color:linear-gradient(from 25% 25% to 100% 100%, #cd5c5c, #ffffff);");
		getChildren().add(background);
	}
	public abstract Node getFeld();
	public abstract void drehen();
	public abstract void zug(byte[] zug);
	public abstract void aktualisieren();
	public abstract void aktualisierenFigur(int x, int y);
	public abstract void startaufstellung();
	public abstract void resetBrett();
	public abstract Image getScreenshot();
	public abstract void farbe(int x, int y, int farbe);
	public abstract void aktualisiereMap();
	public abstract void entferneFiguren();
	protected abstract double translateX(int x);
	protected abstract double translateY(int x);
	public abstract Node getRoot();
	public synchronized void waitForAnimation() {}
}
