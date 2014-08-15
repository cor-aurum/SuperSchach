package gui;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.Rectangle;

public class ZweiD extends MyStackPane{

	private Rectangle feld=new Rectangle(100, 100);
	GUI gUI;
	private Feld[][] felder;
	private DoubleProperty zoom = new SimpleDoubleProperty(0.0);
	public ZweiD(GUI gUI)
	{
		this.gUI=gUI;
		for (int a = 0; a < gUI.spiel.getYMax() + 1; a++) {
			for (int b = 0; b < gUI.spiel.getXMax() + 1; b++) {
				felder[b][a] = new Feld(gUI,this, b, a);
			}
		}
		feld.setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				if (zoom.get() > 0.5 || event.getDeltaY() > 0) {
					zoom.set(zoom.get() + event.getDeltaY() / 400);
				}
			}
		});
		zoom.set(feld.getWidth());
		// zoom.set(700);
		feld.xProperty().bind(zoom);
		feld.yProperty().bind(zoom);
		getChildren().add(feld);
	}
	@Override
	public void drehen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void zug() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aktualisieren() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aktualisierenFigur(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startaufstellung() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void resetBrett() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Image getScreenshot() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void farbe(int x, int y, int farbe) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void aktualisiereMap() {
		// TODO Auto-generated method stub
		
	}
	
	protected double translateX(int x) {
		return (feld.getWidth() / felder.length) * (gUI.spiel.getXMax() - x);
	}

	protected double translateY(int y) {
		return (feld.getHeight() / felder.length) * y;
	}
	@Override
	public Node getFeld() {
		return feld;
	}

}
