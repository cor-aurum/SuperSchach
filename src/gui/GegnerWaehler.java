package gui;

import java.io.File;
import java.io.IOException;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Skin;
import javafx.scene.control.Slider;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import spiel.Schnittstelle;
import client.Client;
import client.Spieler;

public class GegnerWaehler extends Fenster {

	GUI gUI;
	BorderPane pane = new BorderPane();
	VBox liste = new VBox();
	Client client;
	ScrollPane scroll;
	boolean internet = true;

	public GegnerWaehler(GUI gUI) {
		super(gUI);
		try {
			client = new Client("localhost", gUI.name, gUI.spiel);
			gUI.client = client;
		} catch (Exception e) {
			internet = false;
		}
		this.gUI = gUI;

		Label waehler = new Label(Schnittstelle.meldung("gegnerWaehlen"));
		BorderPane.setMargin(waehler, new Insets(0, 0, 20, 0));
		waehler.setStyle("-fx-font-size:28;-fx-font-weight: bold;-fx-padding:30px;-fx-background-color:rgba(0,100,100,0.7);-fx-background-radius: 10;");
		scroll = new ScrollPane();
		scroll.setContent(liste);
		liste.setSpacing(15);
		scroll.skinProperty().addListener(new ChangeListener<Skin<?>>() {

			@Override
			public void changed(ObservableValue<? extends Skin<?>> ov,
					Skin<?> t, Skin<?> t1) {
				if (t1 != null && t1.getNode() instanceof Region) {
					Region r = (Region) t1.getNode();
					r.setBackground(Background.EMPTY);

					r.getChildrenUnmodifiable().stream()
							.filter(n -> n instanceof Region)
							.map(n -> (Region) n)
							.forEach(n -> n.setBackground(Background.EMPTY));

					r.getChildrenUnmodifiable().stream()
							.filter(n -> n instanceof Control)
							.map(n -> (Control) n)
							.forEach(c -> c.skinProperty().addListener(this)); // *
				}
			}
		});
		pane.setPadding(new Insets(30));
		pane.setTop(waehler);
		setzeInhalt(pane);
		pane.setCenter(scroll);
		BorderPane.setAlignment(waehler, Pos.CENTER);
		aktualisieren();
		Timeline aktualisieren = new Timeline(new KeyFrame(Duration.seconds(5),
				new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						if (internet && isShowed())
							aktualisieren();
					}
				}));
		aktualisieren.setCycleCount(Timeline.INDEFINITE);
		aktualisieren.play();
	}

	public void aktualisieren() {
		liste.getChildren().clear();
		if (internet) {
			Spieler[] spieler = null;
			try {
				spieler = client.getLobby();
			} catch (IOException e) {
			}
			SpielerButton[] button = new SpielerButton[spieler.length];
			for (int i = 0; i < spieler.length; i++) {
				button[i] = new SpielerButton(spieler[i].getName(),
						spieler[i].getID(), spieler[i].getFarbe());
			}
			liste.getChildren().addAll(button);
		}
		liste.getChildren().add(
				new SpielerButtonBot("Kiana (" + Schnittstelle.meldung("weiss")
						+ ")", 4, "WEISS", true));
		liste.getChildren()
				.add(new SpielerButtonBot("Kiana ("
						+ Schnittstelle.meldung("schwarz") + ")", 4, "SCHWARZ", true));
		liste.getChildren().add(
				new SpielerButtonBot("Ivan Zufallski ("
						+ Schnittstelle.meldung("weiss") + ")", 1, "WEISS", false));
		liste.getChildren()
				.add(new SpielerButtonBot("Ivan Zufallski ("
						+ Schnittstelle.meldung("schwarz") + ")", 1, "SCHWARZ", false));
		liste.getChildren()
		.add(new KeinSpielerButton(Schnittstelle.meldung("keinSpieler")));
	}

	private class SpielerButton extends Button {
		public SpielerButton(String s, long id, String farbe) {
			super(s);
			prefWidthProperty().bind(
					GegnerWaehler.this.widthProperty().divide(2));
			setStyle("-fx-font-weight:bold;-fx-background-color:linear-gradient(#006464, #006565),linear-gradient(#006464, #006565),linear-gradient(#006464, #006565),linear-gradient(#006464 0%, #006565 50%, #006464 100%),linear-gradient(from 0% 0% to 15% 50%, rgba(175,238,238,0.9), rgba(0,100,100,0));-fx-background-radius: 30;-fx-text-fill: #ffffff;");
			setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					listener(s, id, farbe);
				}
			});
		}

		public void listener(String s, long id, String farbe) {
			pane.setRight(new Detail(s, id, farbe));
		}
	}

	private class SpielerButtonBot extends SpielerButton {

		boolean slider;
		public SpielerButtonBot(String s, long id, String farbe, boolean slider) {
			super(s, id, farbe);
			this.slider=slider;
		}

		@Override
		public void listener(String s, long id, String farbe) {
			pane.setRight(new DetailBot(s, id, farbe, slider));
		}
	}
	
	private class KeinSpielerButton extends SpielerButton
	{

		public KeinSpielerButton(String s) {
			super(s, -1, "WEISS");
		}
		
		@Override
		public void listener(String s, long id, String farbe)
		{
			pane.setRight(new KeinDetail(s));
		}
	}

	private class Detail extends StackPane {
		SpielVorschau sV = new SpielVorschau(new File[] {});
		BorderPane mitte=new BorderPane();
		public Detail(String name, long id, String farbe) {
			BorderPane root = new BorderPane();
			this.getChildren().add(root);
			Label nameLabel = new Label(name);
			root.setTop(nameLabel);
			String farbtemp = farbe.equals("WEISS") ? "white" : "black";
			String farbtemp2 = farbe.equals("WEISS") ? "black" : "white";
			nameLabel.prefWidthProperty().bind(this.widthProperty());
			nameLabel
					.setStyle("-fx-text-fill:"
							+ farbtemp2
							+ ";-fx-background-color:"
							+ farbtemp
							+ ";-fx-font-size:20;-fx-font-weight: bold;-fx-padding:10px;");
			nameLabel.setUnderline(true);
			root.setStyle("-fx-background-color:rgba(0,100,100,0.7);-fx-background-radius: 10;-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );-fx-padding:20px;");

			sV.prefHeightProperty().bind(this.widthProperty());
			mitte.setTop(sV);
			root.setCenter(mitte);

			Button herausfordern = new Button("Herausfordern");
			root.setBottom(herausfordern);
			root.prefWidthProperty().bind(
					GegnerWaehler.this.widthProperty().divide(3));
			maxWidthProperty().bind(
					GegnerWaehler.this.widthProperty().divide(2));
			herausfordern.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					listener(id, farbe);

				}
			});
		}

		public void listener(long id, String farbe) {
			try {

				GegnerWaehler.this.hide();
				gUI.spiel.laden(sV.getSelected());
				gUI.feld.entferneFiguren();
				gUI.feld.startaufstellung();
				client.herausfordern(id);
			} catch (Exception e1) {
			}
		}
	}

	private class DetailBot extends Detail {

		Slider waehlen=new Slider();
		public DetailBot(String name, long id, String farbe, boolean slider) {
			super(name, id, farbe);
			waehlen.setMax(5);
			waehlen.setMin(3);
			waehlen.setValue(4);
			waehlen.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #ffffff, #000000);");
			if(slider)
			{
				mitte.setCenter(new Label(Schnittstelle.meldung("staerkeWaehlen")));
				mitte.setBottom(waehlen);
			}
		}

		@Override
		public void listener(long id, String farbe) {

			GegnerWaehler.this.hide();
			try {
				gUI.spiel.laden(sV.getSelected());
				gUI.feld.entferneFiguren();
				gUI.feld.startaufstellung();
				gUI.spiel.ki((int) id, farbe == "WEISS" ? 0 : 1, (int)Math.round(waehlen.getValue()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private class KeinDetail extends Detail {
		public KeinDetail(String name) {
			super(name, -1, "WEISS");
		}

		@Override
		public void listener(long id, String farbe) {

			GegnerWaehler.this.hide();
			try {
				gUI.spiel.laden(sV.getSelected());
				gUI.feld.entferneFiguren();
				gUI.feld.startaufstellung();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void herausgefordert() {
		hide();
		gUI.feld.entferneFiguren();
		gUI.feld.startaufstellung();
	}
}
