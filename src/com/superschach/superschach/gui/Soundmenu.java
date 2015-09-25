package com.superschach.superschach.gui;

import com.superschach.superschach.gui.menu.Menu;
import com.superschach.superschach.spiel.AbstractGUI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class Soundmenu extends Menu {

	GUI gUI;
	Button umschalten;
	public Soundmenu(GUI gUI) {
		super(gUI, AbstractGUI.meldung("sound"));
		umschalten=new Button(AbstractGUI.meldung("ton_aus"));
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
			umschalten.setText(AbstractGUI.meldung("ton_aus"));
		}
		else
		{
			umschalten.setText(AbstractGUI.meldung("ton_an"));
		}
	}
}
