package gui;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import spiel.Schnittstelle;

public class Login extends Dialog {

	TextField nameEingeben = new TextField();
	PasswordField passwortEingeben = new PasswordField();
	Button ok = new Button(Schnittstelle.meldung("login"));
	Button abbrechen = new Button(Schnittstelle.meldung("offline"));
	Blocker blocker;
	String[] ret;

	public Login(String message, String[] ret, Blocker blocker) {
		System.out.println("login");
		BorderPane root = new BorderPane();
		root.setPadding(new Insets(30));
		GridPane textfelder = new GridPane();
		textfelder.setHgap(10); 
		textfelder.setVgap(10); 
		this.blocker = blocker;
		this.ret = ret;
		ok.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				ret[0] = nameEingeben.getText();
				ret[1] = passwortEingeben.getText();
				blocker.release();
			}

		});

		abbrechen.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				// ret=null;

				blocker.release();
			}

		});

		Label name = new Label(Schnittstelle.meldung("name") + ": ");
		name.setStyle("-fx-font-weight: bold;-fx-text-fill: #111111;-fx-font-size:14;-fx-effect: dropshadow( three-pass-box , rgba(255,255,255,0.2) , 1, 0.0 , 0 , 1);");
		Label passwort = new Label(Schnittstelle.meldung("passwort") + ": ");
		passwort.setStyle("-fx-font-weight: bold;-fx-text-fill: #111111;-fx-font-size:14;-fx-effect: dropshadow( three-pass-box , rgba(255,255,255,0.2) , 1, 0.0 , 0 , 1);");
		textfelder.add(name, 0, 0);
		textfelder.add(nameEingeben, 1, 0);
		textfelder.add(passwort, 0, 1);
		textfelder.add(passwortEingeben, 1, 1);
		textfelder.add(new Label(message), 2, 1);

		HBox buttons = new HBox();

		ok.setPrefWidth(145);
		abbrechen.setPrefWidth(145);
		String style = "-fx-background-color: linear-gradient(#686868 0%, #232723 25%, #373837 75%, #757575 100%),linear-gradient(#020b02, #3a3a3a),"
				+ "linear-gradient(#b9b9b9 0%, #c2c2c2 20%, #afafaf 80%, #c8c8c8 100%),linear-gradient(#f5f5f5 0%, #dbdbdb 50%, #cacaca 51%, #d7d7d7 100%);"
				+ "-fx-background-insets: 0,1,4,5;-fx-background-radius: 9,8,5,4;-fx-padding: 10 10 10 10;-fx-font-family:\"Helvetica\";"
				+ " -fx-font-size: 14px;-fx-font-weight: bold;-fx-text-fill: #333333;-fx-effect: dropshadow( three-pass-box , rgba(255,255,255,0.2) , 1, 0.0 , 0 , 1);";
		ok.setStyle(style);
		abbrechen.setStyle(style);

		buttons.getChildren().addAll(ok, abbrechen);
		root.setBottom(buttons);
		root.setCenter(textfelder);
		getChildren().add(root);
	}

}
