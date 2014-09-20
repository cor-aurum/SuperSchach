package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import spiel.Schnittstelle;

public class Soundmenu extends Menu {

	GUI gUI;
	Button umschalten;
	public Soundmenu(GUI gUI) {
		super(gUI, Schnittstelle.meldung("sound"));
		umschalten=new Button(Schnittstelle.meldung("ton_aus"));
		umschalten.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				gUI.sounds.setValue(!gUI.sounds.getValue());
				pruefeText();
				gUI.einstellungen.speichern();
			}
		});
		addInhalt(new Button[]{umschalten});
		this.gUI=gUI;
	}
	
	@Override
	public void show()
	{
		super.show();
		pruefeText();
	}

	@Override
	public void zurueck() {
		switchFenster(gUI.hauptmenu);
	}

	private void pruefeText()
	{
		if(gUI.sounds.getValue())
		{
			umschalten.setText(Schnittstelle.meldung("ton_aus"));
		}
		else
		{
			umschalten.setText(Schnittstelle.meldung("ton_an"));
		}
	}
}
