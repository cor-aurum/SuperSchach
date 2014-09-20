package gui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import spiel.Schnittstelle;

public class Login extends Dialog {
	
	TextField nameEingeben =new TextField();
	PasswordField passwortEingeben = new PasswordField();
	Button ok=new Button(Schnittstelle.meldung("login"));
	Button abbrechen=new Button(Schnittstelle.meldung("offline"));
	public Login(String message) {
		BorderPane root=new BorderPane();
		GridPane textfelder = new GridPane();
		Label name=new Label(Schnittstelle.meldung("name")+": ");
		Label passwort=new Label(Schnittstelle.meldung("passwort")+": ");
		textfelder.add(name, 0, 0);
		textfelder.add(nameEingeben, 1, 0);
		textfelder.add(passwort, 0, 1);
		textfelder.add(passwortEingeben, 1, 1);
		textfelder.add(new Label(message), 2, 1);
		
		VBox buttons=new VBox();
		buttons.getChildren().addAll(ok, abbrechen);
		root.setBottom(buttons);
		root.setCenter(textfelder);
		getChildren().add(root);
	}
	
	public String[] getLogin()
	{
		return null;
	}
}
