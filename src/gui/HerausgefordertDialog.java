package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import spiel.Schnittstelle;

public class HerausgefordertDialog extends Dialog{

	public HerausgefordertDialog(FxSchnittstelle schnittstelle,String name)
	{
		BorderPane root=new BorderPane();
		BorderPane buttons=new BorderPane();
		Button annehmen = new Button(Schnittstelle.meldung("annehmen"));
		Button abbrechen = new Button(Schnittstelle.meldung("abbrechen"));
		Label text=new Label(Schnittstelle.meldung("herausforderung")+"\n"+name);
		root.setCenter(text);
		buttons.setLeft(annehmen);
		buttons.setRight(abbrechen);
		root.setBottom(buttons);
		root.setPadding(new Insets(20,20,20,20));
		Pane p=new Pane();
		
		annehmen.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				schnittstelle.gUI.feld.getChildren().remove(p);
				schnittstelle.gUI.feld.getChildren().remove(HerausgefordertDialog.this);
			}
		});
		
		abbrechen.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				schnittstelle.gUI.feld.getChildren().remove(p);
				schnittstelle.gUI.feld.getChildren().remove(HerausgefordertDialog.this);
			}
		});

		getChildren().add(root);
		schnittstelle.gUI.feld.getChildren().add(p);
		schnittstelle.gUI.feld.getChildren().add(this);
	}
}