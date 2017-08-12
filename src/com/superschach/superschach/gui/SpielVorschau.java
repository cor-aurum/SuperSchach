package com.superschach.superschach.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import com.superschach.superschach.spiel.AbstractGUI;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class SpielVorschau extends GridPane {

	VorschauSchnittstelle schnittstelle = new VorschauSchnittstelle(this);
	String[] spiele;
	int index = 0;
	Canvas canvas = new Canvas(300, 300);
	ImageView iV = new ImageView();
	ComboBox<String> auswahl = new ComboBox<String>();
	Label name = new Label(schnittstelle.getSpielName());
	boolean extern = false;

	public SpielVorschau(String gegner, GUI gUI) {
		setId("spiel-vorschau");
		setStandardArray();
		try {
			schnittstelle.laden(this.getClass().getClassLoader()
					.getResourceAsStream(spiele[index]));
		} catch (Exception e) {
			e.printStackTrace();
		}
		setStyle("-fx-background-color:linear-gradient(from 25% 25% to 100% 100%, "
				+ gUI.getVonFarbe().getValue()
				+ ", "
				+ gUI.getBisFarbe().getValue() + ");");
		auswahl.getItems().addAll(AbstractGUI.meldung("standardSpiele"),
				gegner, AbstractGUI.meldung("alle"));
		auswahl.setValue(AbstractGUI.meldung("standardSpiele"));
		auswahl.valueProperty()
				.addListener(
						(ChangeListener<String>) (ov, t, t1) -> {
							laden(spiele[0]);
							if (t1.equals(AbstractGUI
									.meldung("standardSpiele"))) {
								setStandardArray();
							} else if (t1.equals(gegner)) {
								File f = new File(AbstractGUI.verzeichnis()
										+ File.separator + gegner);
								File[] fileArray = f.listFiles();
								int zaehler = 1;
								try {
									for (File file : fileArray) {
										if (file.toString().endsWith(".schach")) {
											zaehler++;
										}
									}
									spiele = new String[zaehler];
									spiele[0] = "com/superschach/superschach/gui/spiele/00.schach";
									for (int i = 0; i < fileArray.length; i++) {
										if (fileArray[i].toString().endsWith(
												".schach")) {
											spiele[i + 1] = fileArray[i]
													.getAbsolutePath();
										}
									}
								} catch (Exception e) {
								}
							} else if (t1.equals(AbstractGUI.meldung("alle"))) {
								ArrayList<String> list = new ArrayList<String>();
								listeDateien(
										new File(AbstractGUI.verzeichnis()),
										list);

								spiele = new String[list.size() + 1];
								spiele[0] = "com/superschach/superschach/gui/spiele/00.schach";
								for (int i = 0; i < list.size(); i++) {
									spiele[i + 1] = list.get(i);
								}
							}

						});
		Button links = new Button();
		Button rechts = new Button();
		add(links, 1, 0);
		add(iV, 2, 0);
		add(rechts, 3, 0);
		add(name, 2, 1);
		add(auswahl, 2, 2);
		add(new Label(), 2, 3);

		auswahl.prefWidthProperty().bind(canvas.widthProperty());
		links.setAlignment(Pos.CENTER);
		links.setStyle("-fx-padding:10px");
		rechts.setAlignment(Pos.CENTER);
		rechts.setStyle("-fx-padding:10px");
		links.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (index > 0) {
					index--;
				} else {
					index = spiele.length - 1;
				}
				laden(spiele[index]);
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
				laden(spiele[index]);

			}
		});
		rechts.setGraphic(new ImageView(
				new Image(
						this.getClass()
								.getClassLoader()
								.getResource(
										"com/superschach/superschach/gui/bilder/pfeil_rechts.png")
								.toString())));
		links.setGraphic(new ImageView(
				new Image(
						this.getClass()
								.getClassLoader()
								.getResource(
										"com/superschach/superschach/gui/bilder/pfeil_links.png")
								.toString())));

		rechts.setId("pfeil-detail");
		links.setId("pfeil-detail");

		laden(spiele[0]);
	}

	public void aktualisieren() {
		canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(),
				canvas.getHeight());
		canvas.getGraphicsContext2D().setFill(Color.BLUE);
		Image brettbild = new Image(this
				.getClass()
				.getClassLoader()
				.getResource(
						"com/superschach/superschach/gui/bilder/brett2d.png")
				.toString());
		canvas.getGraphicsContext2D().drawImage(brettbild, 0, 0, 300, 300);
		for (int x = 0; x < schnittstelle.getXMax() + 1; x++) {
			for (int y = 0; y < schnittstelle.getYMax() + 1; y++) {
				aktualisierenFigur(x, y);
			}
		}
		iV.setImage(canvas.snapshot(null, null));
	}

	private void setStandardArray() {
		spiele = new String[2];
		spiele[0] = "com/superschach/superschach/gui/spiele/00.schach";
		spiele[1] = "com/superschach/superschach/gui/spiele/01.schach";
	}

	private void listeDateien(File verzeichnis, ArrayList<String> list) {
		for (File f : verzeichnis.listFiles()) {
			if (f.isDirectory()) {
				listeDateien(f, list);
			} else if (f.toString().endsWith(".schach")) {
				list.add(f.getAbsolutePath());
			}
		}
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
					.getResource("com/superschach/superschach/gui/bilder/" + f)
					.toString());
			canvas.getGraphicsContext2D().drawImage(img, translateX(x),
					translateY(y));
		}
	}

	private void laden(String s) {
		try {
			schnittstelle.laden(this.getClass().getClassLoader()
					.getResourceAsStream(s));
			extern = false;
		} catch (Exception ex) {
			try {
				schnittstelle.laden(new FileInputStream(s));
				extern = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		name.setText(schnittstelle.getSpielName());
	}

	protected double translateX(int x) {
		return (300 / (schnittstelle.getXMax() + 1)) * x;
	}

	protected double translateY(int y) {
		return (300 / (schnittstelle.getYMax() + 1))
				* (schnittstelle.getYMax() - y);
	}

	public InputStream getSelected() {
		if (extern) {
			try {
				return new FileInputStream(spiele[index]);
			} catch (FileNotFoundException e) {
				return this.getClass().getClassLoader()
						.getResourceAsStream(spiele[0]);
			}
		} else {
			return this.getClass().getClassLoader()
					.getResourceAsStream(spiele[index]);
		}
	}

	public File getFile() {
		return new File(spiele[index]);
	}

	public boolean isSelecectedPreDefined() {
		try {
			if(index==0 || auswahl.getValue().equals(AbstractGUI.meldung("standardSpiele")))
				return true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
}
