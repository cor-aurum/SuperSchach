package gui;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;

public class Chat extends Fenster {

	BorderPane root = new BorderPane();
	ScrollPane scroll = new ScrollPane();
	VBox chat = new VBox();
	TextField tF = new TextField();
	private SimpleIntegerProperty ungelesen = new SimpleIntegerProperty(0);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Chat(GUI gUI) {
		super(gUI);
		setzeInhalt(root);
		scroll.setContent(chat);
		root.setBottom(tF);
		root.setCenter(scroll);
		tF.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (!tF.getText().equals("")) {
					chat.getChildren().add(new Nachricht(tF.getText(), true));
					try {
						gUI.spiel.chat(tF.getText());
					} catch (Exception e1) {
					}
					tF.setText("");
				}
			}
		});
		tF.setStyle("-fx-text-fill: black;-fx-font-size:18;-fx-font-family: \"Arial Narrow\";-fx-background-color: white; -fx-font-weight: bold;");
		chat.heightProperty().addListener(
				(ChangeListener) (observable, oldvalue, newValue) -> scroll
						.setVvalue((Double) newValue));
		scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
	}

	private class Nachricht extends HBox {
		public Nachricht(String text, boolean ausrichtung) {
			if (ausrichtung) {
				setAlignment(Pos.BASELINE_RIGHT);
			}
			prefWidthProperty().bind(Chat.this.widthProperty());
			Label p = new Label(text);
			setMargin(p, new Insets(10));
			String style = "-fx-text-fill: black;-fx-font-size:20;-fx-font-family: \"Arial Narrow\"; -fx-font-weight: bold;-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );-fx-margin:20px;-fx-padding:20px;-fx-background-radius: 10;";
			if (ausrichtung) {
				style += "-fx-background-color: yellow;";
			} else {
				style += "-fx-background-color: green;";
			}
			p.setStyle(style);

			getChildren().add(p);
			p.setOpacity(1);
		}
	}

	public void nachrichtErhalten(String s) {
		chat.getChildren().add(new Nachricht(s, false));
		if (!isShowed()) {
			ungelesen.setValue(ungelesen.getValue() + 1);
			AudioClip plonkSound = new AudioClip(this.getClass()
					.getClassLoader().getResource("gui/sounds/nachricht.aiff")
					.toString());
			plonkSound.play();
		}

	}

	@Override
	public void show() {
		super.show();
		ungelesen.setValue(0);
		tF.requestFocus();
	}

	public int getUngelesen() {
		return ungelesen.getValue();
	}

	public ReadOnlyIntegerProperty ungelesenProperty() {
		return ungelesen;
	}
}
