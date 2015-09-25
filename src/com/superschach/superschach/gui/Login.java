package com.superschach.superschach.gui;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.NoSuchAlgorithmException;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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

import com.sun.deploy.uitoolkit.impl.fx.HostServicesFactory;
import com.sun.javafx.application.HostServicesDelegate;
import com.superschach.superschach.network.MDFiver;
import com.superschach.superschach.spiel.AbstractGUI;

public class Login extends Dialog {

	TextField nameEingeben = new TextField();
	PasswordField passwortEingeben = new PasswordField();
	Button ok = new Button(AbstractGUI.meldung("login"));
	Button abbrechen = new Button(AbstractGUI.meldung("offline"));
	Blocker blocker;
	String[] ret;
	CheckBox speichern = new CheckBox();
	PasswortSpeicher speicher;

	public Login(String message, String[] ret, Blocker blocker, GUI gUI) {
		setMaxWidth(450);
		BorderPane root = new BorderPane();
		root.setPadding(new Insets(30));
		GridPane textfelder = new GridPane();
		textfelder.setHgap(10);
		textfelder.setVgap(10);
		this.blocker = blocker;
		this.ret = ret;
		speichern.setText(AbstractGUI.meldung("passwort_speichern"));
		ObjectInputStream ois = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(AbstractGUI.verzeichnis() + "login");
			ois = new ObjectInputStream(fis);
			Object obj = ois.readObject();
			if (obj instanceof PasswortSpeicher) {
				speicher = (PasswortSpeicher) obj;
				nameEingeben.setText(speicher.getName());
				passwortEingeben.setText(speicher.getPasswort());
			}
		} catch (IOException e) {
			speicher = new PasswortSpeicher();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (ois != null)
				try {
					ois.close();
				} catch (IOException e) {
				}
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
				}
		}

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
		steuerung.getChildren().add(speichern);
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
		if (speichern.isSelected()) {
			speicher.setName(ret[0]);
			speicher.setPasswort(ret[1]);
			
			ObjectOutputStream oos = null;
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(AbstractGUI.verzeichnis()
						+ "login");
				oos = new ObjectOutputStream(fos);
				oos.writeObject(speicher);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (oos != null)
					try {
						oos.close();
					} catch (IOException e) {
					}
				if (fos != null)
					try {
						fos.close();
					} catch (IOException e) {
					}
			}
		}
		
		blocker.release();
	}
}
