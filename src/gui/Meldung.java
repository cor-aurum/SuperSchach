package gui;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import spiel.Schnittstelle;

public class Meldung extends Dialog {
	public Meldung(String nachricht, MyStackPane feld)
	{
		BorderPane root=new BorderPane();
		Label text=new Label(nachricht);
		Button ok=new Button(Schnittstelle.meldung("ok"));
		ok.setStyle("-fx-background-color: linear-gradient(#686868 0%, #232723 25%, #373837 75%, #757575 100%),linear-gradient(#020b02, #3a3a3a),"
				+ "linear-gradient(#b9b9b9 0%, #c2c2c2 20%, #afafaf 80%, #c8c8c8 100%),linear-gradient(#f5f5f5 0%, #dbdbdb 50%, #cacaca 51%, #d7d7d7 100%);"
				+ "-fx-background-insets: 0,1,4,5;-fx-background-radius: 9,8,5,4;-fx-padding: 10 10 10 10;-fx-font-family:\"Helvetica\";"
				+ " -fx-font-size: 14px;-fx-font-weight: bold;-fx-text-fill: #333333;-fx-effect: dropshadow( three-pass-box , rgba(255,255,255,0.2) , 1, 0.0 , 0 , 1);");
		text.setStyle("-fx-font-weight: bold;-fx-text-fill: #111111;-fx-font-size:40;-fx-effect: dropshadow( three-pass-box , rgba(255,255,255,0.2) , 1, 0.0 , 0 , 1);");
		ok.setPrefWidth(100);
		root.setCenter(text);
		root.setBottom(ok);
		BorderPane.setAlignment(ok, Pos.BASELINE_CENTER);
		getChildren().add(root);
		Pane p=new Pane();
		feld.getChildren().add(p);
		//p.setStyle("-fx-background-color: #006464;-fx-background-radius: 10;");
		feld.getChildren().add(this);
		ok.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				feld.getChildren().remove(p);
				feld.getChildren().remove(Meldung.this);
			}
		});
	}
}
