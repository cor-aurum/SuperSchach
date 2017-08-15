package com.superschach.superschach.server;

import java.io.IOException;
import java.sql.SQLException;

import com.superschach.superschach.server.spieler.Connection;
import com.superschach.superschach.server.spieler.Spieler;

public class Herausforderung {

	private boolean open = true;
	private Connection herausforderer;
	private Spieler gegner;
	private final int id;
	private final Server server;

	public Herausforderung(Connection herausforderer, Spieler gegner, Server s) {
		this.server = s;
		this.herausforderer = herausforderer;
		this.gegner = gegner;
		id = gegner.getNexHerausforderungsID();
		gegner.herausfordern(this);
	}

	public boolean isOpen() {
		return open;
	}

	public synchronized boolean annehmen(Connection annehmer) {
		if (!open)
			return false;
		synchronized (herausforderer) {
			if (herausforderer.isInGame())
				return false;
			synchronized (annehmer) {
				if (annehmer.isInGame())
					return false;

				try {
					server.neuesSpiel(herausforderer, annehmer);
				} catch (IOException | SQLException e) {
					try {
						annehmer.meldung("spiel_erstellen_fehlgeschlagen");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					e.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}

	public void close()
	{
		if (open) {
			open = false;
			synchronized (gegner) {
				gegner.beendeHerausforderung(id);
			}
		}
	}


	public long getHerausforderID() {
		return herausforderer.getId();
	}

	public int getId() {
		return id;
	}

}
