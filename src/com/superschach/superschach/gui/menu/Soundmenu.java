package com.superschach.superschach.gui.menu;

import com.superschach.superschach.gui.GUI;
import com.superschach.superschach.spiel.Schnittstelle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class Soundmenu extends Menu {

	GUI gUI;
	Button umschalten;
	public Soundmenu(GUI gUI) {
		super(gUI, Schnittstelle.meldung("sound"));
		umschalten=new Button(Schnittstelle.meldung("ton_aus"));
		umschalten.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				gUI.getSounds().setValue(!gUI.getSounds().getValue());
				pruefeText();
				gUI.getEinstellungen().speichern();
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
		switchFenster(gUI.getHauptmenu());
	}

	private void pruefeText()
	{
		if(gUI.getSounds().getValue())
		{
			umschalten.setText(Schnittstelle.meldung("ton_aus"));
		}
		else
		{
			umschalten.setText(Schnittstelle.meldung("ton_an"));
		}
	}
}
