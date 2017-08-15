package com.superschach.superschach.gui;

import java.security.NoSuchAlgorithmException;

import com.sun.deploy.uitoolkit.impl.fx.HostServicesFactory;
import com.sun.javafx.application.HostServicesDelegate;
import com.superschach.superschach.network.MDFiver;
import com.superschach.superschach.spiel.AbstractGUI;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Login extends Dialog {

	TextField nameEingeben = new TextField();
	PasswordField passwortEingeben = new PasswordField();
	Button ok = new Button(AbstractGUI.meldung("login"));
	Button abbrechen = new Button(AbstractGUI.meldung("offline"));
	Blocker blocker;
	String[] ret;

	public Login(String message, String[] ret, Blocker blocker, GUI gUI) {
		setMaxWidth(450);
		BorderPane root = new BorderPane();
		root.setPadding(new Insets(30));
		GridPane textfelder = new GridPane();
		textfelder.setHgap(10);
		textfelder.setVgap(10);
		this.blocker = blocker;
		this.ret = ret;

		setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ENTER) {
					login();
				}
			}
		});
		ok.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				login();
			}

		});

		abbrechen.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				// ret=null;

				blocker.release();
			}

		});

		Label name = new Label(AbstractGUI.meldung("name") + ": ");
		name.setStyle("-fx-font-size:14;");
		Label passwort = new Label(AbstractGUI.meldung("passwort") + ": ");
		passwort.setStyle("-fx-font-size:14;");
		textfelder.add(name, 0, 0);
		textfelder.add(nameEingeben, 1, 0);
		textfelder.add(passwort, 0, 1);
		textfelder.add(passwortEingeben, 1, 1);
		Label mess = new Label(message);
		mess.setId("fehler");
		mess.setStyle("-fx-font-size:14;");
		textfelder.add(mess, 1, 2);

		HBox buttons = new HBox();

		Hyperlink link = new Hyperlink("\nNoch nicht registriert?");
		link.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				HostServicesDelegate hostServices = HostServicesFactory
						.getInstance(gUI);
				hostServices
						.showDocument("http://super-schach.com/SchachPortal/index.php?seite=registrieren");
			}

		});
		ok.setPrefWidth(195);
		ok.setDefaultButton(true);
		abbrechen.setPrefWidth(195);
		buttons.getChildren().addAll(ok, abbrechen);
		VBox steuerung = new VBox();
		steuerung.getChildren().add(buttons);
		root.setCenter(steuerung);
		root.setTop(textfelder);
		root.setBottom(link);
		getChildren().add(root);
	}

	public void login()
	{
		ret[0] = nameEingeben.getText();
		try {
			ret[1] = new MDFiver().md5(passwortEingeben.getText());
		} catch (NoSuchAlgorithmException e1) {
			ret[1]="";
		}
		blocker.release();
	}
}
