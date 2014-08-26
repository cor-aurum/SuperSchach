package gui;

import java.io.File;
import java.io.InputStream;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class SpielVorschau extends GridPane {

	VorschauSchnittstelle schnittstelle = new VorschauSchnittstelle(this);
	String[] spiele = new String[2];
	int index = 0;
	Canvas canvas = new Canvas(300, 300);
	ImageView iV = new ImageView();

	public SpielVorschau(File[] customs) {
		setStyle("-fx-padding:30 0 0 0;");
		spiele[0] = "gui/spiele/00.schach";
		spiele[1] = "gui/spiele/01.schach";
		try {
			schnittstelle.laden(this.getClass().getClassLoader()
					.getResourceAsStream(spiele[index]));
		} catch (Exception e) {
			e.printStackTrace();
		}

		Button links = new Button();
		Button rechts = new Button();
		add(links, 1, 1);
		add(iV, 2, 1);
		add(rechts, 3, 1);
		links.setAlignment(Pos.CENTER);
		rechts.setAlignment(Pos.CENTER);
		links.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (index > 0) {
					index--;
				} else {
					index = spiele.length - 1;
				}
				try {
					schnittstelle.laden(this.getClass().getClassLoader()
							.getResourceAsStream(spiele[index]));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		rechts.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (index < spiele.length - 1) {
					index++;
				} else {
					index = 0;
				}
				try {
					schnittstelle.laden(this.getClass().getClassLoader()
							.getResourceAsStream(spiele[index]));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		rechts.setGraphic(new ImageView(new Image(this.getClass()
				.getClassLoader().getResource("gui/bilder/pfeil_rechts.png")
				.toString())));
		links.setGraphic(new ImageView(new Image(this.getClass()
				.getClassLoader().getResource("gui/bilder/pfeil_links.png")
				.toString())));

		rechts.setStyle("-fx-background-color:transparent;");
		links.setStyle("-fx-background-color:transparent;");
	}

	public void aktualisieren() {
		canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(),
				canvas.getHeight());
		canvas.getGraphicsContext2D().setFill(Color.BLUE);
		Image brettbild = new Image(this.getClass().getClassLoader()
				.getResource("gui/bilder/brett2d.png").toString());
		canvas.getGraphicsContext2D().drawImage(brettbild, 0, 0, 300, 300);
		for (int x = 0; x < schnittstelle.getXMax() + 1; x++) {
			for (int y = 0; y < schnittstelle.getYMax() + 1; y++) {
				aktualisierenFigur(x, y);
			}
		}
		iV.setImage(canvas.snapshot(null, null));
	}

	public void aktualisierenFigur(int x, int y) {
		int figur = schnittstelle.figur(x, y);

		if (figur != 0) {
			String f = "";
			switch (Math.abs(figur)) {
			case 1:
				f = "turm";
				break;
			case 4:
				f = "springer";
				break;
			case 2:
				f = "laeufer";
				break;
			case 3:
				f = "dame";
				break;
			case 16:
				f = "koenig";
				break;
			case 8:
				f = "bauer";
				break;
			}
			if (figur < 0) {
				f = f + "_schwarz.png";
			} else {
				f = f + "_weiss.png";
			}
			Image img = new Image(this.getClass().getClassLoader()
					.getResource("gui/bilder/" + f).toString());
			canvas.getGraphicsContext2D().drawImage(img, translateX(x),
					translateY(y));
		}
	}

	protected double translateX(int x) {
		return (300 / (schnittstelle.getXMax() + 1)) * x;
	}

	protected double translateY(int y) {
		return (300 / (schnittstelle.getYMax() + 1))
				* (schnittstelle.getYMax() - y);
	}

	public InputStream getSelected() {
		return this.getClass().getClassLoader()
				.getResourceAsStream(spiele[index]);
	}
}
