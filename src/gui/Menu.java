package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;
import spiel.Schnittstelle;

public abstract class Menu extends Fenster {

	private VBox komp = new VBox();

	public Menu(GUI gUI, String ueberschrift) {
		super(gUI);
		setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ESCAPE) {
					zurueck();
				}
			}
		});

		setPadding(new Insets(100, 100, 100, 100));
		Label titel = new Label(ueberschrift);
		// titel.setStyle("-fx-font-size:50;");
		titel.setFont(Font.font("Impact", 50));

		komp.getChildren().add(titel);
		Pane p = new Pane();
		p.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5);");
		getChildren().add(p);
		setzeInhalt(komp);
	}

	public void addInhalt(Node[] punkte) {
		komp.getChildren().add(new Label("\n"));
		for (Node p : punkte) {
			HBox box=new HBox();
			Pane pane=new Pane();
			pane.setMinWidth(150);
			box.getChildren().addAll(pane,p);
			komp.getChildren().add(box);
			//p.minWidthProperty().bind(this.widthProperty().subtract(350));
			//Stop[] stops = new Stop[] { new Stop(0, Color.BLACK), new Stop(1, Color.WHITE)};
			//LinearGradient lG=new LinearGradient(0,0,25,0,true, CycleMethod.NO_CYCLE, stops);
			
			p.setStyle("-fx-background-color:linear-gradient(from 0px 0px to 50px 0px, #000000,rgba(255, 255, 255, 0) ); -fx-font-family: \"Impact\"; -fx-font-size: 30pt;");
			//p.setFont(Font.font("Impact", 30));
			box.setOnMouseEntered(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (gUI.sounds.getValue()) {
						AudioClip plonkSound = new AudioClip(this.getClass()
								.getClassLoader()
								.getResource("gui/sounds/hover.aiff")
								.toString());
						plonkSound.play();
					}
					pane.setMinWidth(200);
				}
			});
			box.setOnMouseExited(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					pane.setMinWidth(150);
				}
			});
		}
		komp.getChildren().add(new Label("\n"));
		Button zurueck = new Button(Schnittstelle.meldung("zurueck"));
		zurueck.setStyle("-fx-background-color:linear-gradient(from 0px 0px to 50px 0px, #000000,rgba(255, 255, 255, 0) );");
		HBox box=new HBox();
		Pane pane=new Pane();
		pane.setMinWidth(100);
		box.getChildren().addAll(pane,zurueck);
		zurueck.setFont(Font.font("Impact", 30));
		box.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (gUI.sounds.getValue()) {
					AudioClip plonkSound = new AudioClip(this.getClass()
							.getClassLoader()
							.getResource("gui/sounds/hover.aiff").toString());
					plonkSound.play();
				}
				pane.setMinWidth(150);
			}
		});
		box.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				pane.setMinWidth(100);
			}
		});

		komp.getChildren().add(box);
		zurueck.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				zurueck();
			}
		});
	}

	public abstract void zurueck();
}
