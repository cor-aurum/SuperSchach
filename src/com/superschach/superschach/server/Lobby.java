package com.superschach.superschach.server;

import java.io.IOException;
import java.util.HashMap;

import com.superschach.superschach.netzwerk.Commands;
import com.superschach.superschach.server.spieler.Connection;
import com.superschach.superschach.server.spieler.Spieler;

public class Lobby {

	private HashMap<Long, Spieler> liste = new HashMap<Long, Spieler>();
	private String lobbyString = "";
	private boolean modified = true;
	private final Server server;

	public Lobby(Server server) {
		this.server=server;
	}

	public void addSpieler(Spieler spieler) {
		synchronized (liste) {
			liste.put(new Long(spieler.getId()), spieler);
			modified = true;
		}
	}

	public Herausforderung fordereheraus(long gegnerID, Connection herausforderer)
			throws IOException {
		Spieler gegner;
		synchronized (liste) {
			gegner=liste.get(gegnerID);
		}
			if(gegner==null)
				return null;
			return new Herausforderung(herausforderer, gegner,server);
	}

	public Spieler removeSpieler(long id)
	{
		synchronized(liste)
		{
			modified=true;
			return liste.remove(new Long(id));
		}
	}
	
	public String toString() {
		synchronized (liste) {
			if (modified) {
				modified = false;
				/*
				 * StringBuffer temp=new StringBuffer(); Collection<Spieler>
				 * collection =liste.; for(int i=0; i<collection.size(); i++) {
				 * temp.append(collection..toString()); }
				 * lobbyString=temp.toString();
				 */
				if (liste.size() > 0) {
					String temp = liste.toString().replaceAll(Commands.SEPERATOR + ", .+?=",Commands.SEPERATOR + "");
					lobbyString =temp.substring(3, temp.length() - 2);
				} else {
					lobbyString = "";
				}
			}
			return lobbyString;
		}
	}
	

}
