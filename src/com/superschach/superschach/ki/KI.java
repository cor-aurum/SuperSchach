package com.superschach.superschach.ki;

import com.superschach.superschach.kontroller.Kontroller;

public interface KI
{
	/**
	 * Diese Methode wird in der KI aufgerufen um sie einen zug machen zu lassen.
	 * @param spiel Kopie des Kontrollers, Aenderungen die hier gemacht werden, werden nicht uebernommen.
	 * @param zug Hier soll der Spielzug reingespeichert werden
	 */
	public void zug(Kontroller spiel, Zug zug) throws Exception;
	
	public void tellMatt(Kontroller spiel);
}