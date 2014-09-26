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
		
		String style = "-fx-background-color: linear-gradient(#686868 0%, #232723 25%, #373837 75%, #757575 100%),linear-gradient(#020b02, #3a3a3a),"
				+ "linear-gradient(#b9b9b9 0%, #c2c2c2 20%, #afafaf 80%, #c8c8c8 100%),linear-gradient(#f5f5f5 0%, #dbdbdb 50%, #cacaca 51%, #d7d7d7 100%);"
				+ "-fx-background-insets: 0,1,4,5;-fx-background-radius: 9,8,5,4;-fx-padding: 10 10 10 10;-fx-font-family:\"Helvetica\";"
				+ " -fx-font-size: 14px;-fx-font-weight: bold;-fx-text-fill: #333333;-fx-effect: dropshadow( three-pass-box , rgba(255,255,255,0.2) , 1, 0.0 , 0 , 1);";
		annehmen.setStyle(style);
		abbrechen.setStyle(style);
		text.setStyle("-fx-font-weight: bold;-fx-text-fill: #111111;-fx-font-size:20;-fx-effect: dropshadow( three-pass-box , rgba(255,255,255,0.2) , 1, 0.0 , 0 , 1);");

		getChildren().add(root);
		schnittstelle.gUI.feld.getChildren().add(p);
		schnittstelle.gUI.feld.getChildren().add(this);
	}
}