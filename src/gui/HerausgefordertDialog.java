package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import spiel.Schnittstelle;

public class HerausgefordertDialog extends Stage{

	public HerausgefordertDialog(FxSchnittstelle schnittstelle,String name)
	{
		BorderPane root=new BorderPane();
		BorderPane buttons=new BorderPane();
		Button annehmen = new Button(Schnittstelle.meldung("annehmen"));
		Button abbrechen = new Button(Schnittstelle.meldung("abbrechen"));
		Label text=new Label(Schnittstelle.meldung("herausforderung")+" "+name);
		root.setCenter(text);
		buttons.setLeft(annehmen);
		buttons.setRight(abbrechen);
		root.setBottom(buttons);
		root.setPadding(new Insets(20,20,20,20));
		
		annehmen.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

			}
		});
		
		abbrechen.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

			}
		});
		
		
		
		setTitle(Schnittstelle.meldung("herausgefordert"));
		setScene(new Scene(root, 300, 100));
		getIcons().add(new Image(this.getClass().getClassLoader()
				.getResource("gui/bilder/bauer_schwarz.png").toString()));
		initModality(Modality.APPLICATION_MODAL);
		show();
		toFront();
	}
}