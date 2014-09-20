package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import spiel.Schnittstelle;


public class Hauptmenu extends Menu {

	public Hauptmenu(GUI gUI) {
		super(gUI, Schnittstelle.meldung("hauptmenu"));
		Button[] punkte=new Button[6];
		punkte[0]=new Button(Schnittstelle.meldung("lobby"));
		punkte[1]=new Button(Schnittstelle.meldung("game_optionen"));
		punkte[2]=new Button(Schnittstelle.meldung("multiplayer_optionen"));
		punkte[3]=new Button(Schnittstelle.meldung("grafik"));
		punkte[4]=new Button(Schnittstelle.meldung("sound"));
		punkte[5]=new Button(Schnittstelle.meldung("quit"));
		addInhalt(punkte);
		
		punkte[0].setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				gUI.gegner=new GegnerWaehler(gUI);
				switchFenster(gUI.gegner);
				Fenster.setSichtbar(false);
			}
		});
		punkte[1].setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				
			}
		});
		punkte[2].setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				
			}
		});
		punkte[3].setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				switchFenster(new Grafikmenu(gUI));
			}
		});
		punkte[4].setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				switchFenster(new Soundmenu(gUI));
			}
		});
		punkte[5].setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				System.exit(0);
			}
		});
	}

	@Override
	public void zurueck() {
		hide();
	}

}
