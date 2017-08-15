package com.superschach.superschach.server;

import java.io.IOException;
import java.sql.SQLException;

import com.superschach.superschach.network.Farbe;
import com.superschach.superschach.server.datenbank.Datenbank;
import com.superschach.superschach.server.spieler.Connection;

public class Spiel {

	private Connection[] spieler = new Connection[2];
	private final Server server;
	private Datenbank db;
	private final long id;
	private int zugnummer=0;

	public Spiel(Connection spieler1, Connection herausforderer, Server server,
			Datenbank db) throws IOException, SQLException {
		this.server = server;
		this.db = db;
		spieler[0] = spieler1;
		spieler[1] = herausforderer;
		Farbe[] farbe = new Farbe[spieler.length];
		for (int i = 0; i < spieler.length; i++) {
			farbe[i] = spieler[i].getFarbe();
		}
		if (farbe[0].nummer == Farbe.EGAL.nummer) {
			if (farbe[1].nummer == Farbe.EGAL.nummer) {
				farbe[1] = (Math.random() >= 0.5 ? Farbe.SCHWARZ : Farbe.WEISS);
			}
			farbe[0] = (farbe[1].nummer == Farbe.WEISS.nummer ? Farbe.SCHWARZ
					: Farbe.WEISS);
		} else {
			farbe[1] = (farbe[0].nummer == Farbe.WEISS.nummer ? Farbe.SCHWARZ
					: Farbe.WEISS);
		}

		id = db.neuesSpiel(
				farbe[0] == Farbe.WEISS ? spieler[0].getId() : spieler[1]
						.getId(),
				farbe[0] == Farbe.SCHWARZ ? spieler[0].getId() : spieler[1]
						.getId());

		for (int i = 0; i < spieler.length; i++) {
			spieler[i].startSpiel(this, farbe[i]);
		}
	}

	private Connection otherConnection(Connection con) {
		return ((spieler[0] == con) ? spieler[1] : spieler[0]);
	}

	public void chat(int count, byte[] buff, Connection sender)
			throws IOException {
		otherConnection(sender).sendChat(buff, count);
	}

	public void zug(int count, byte[] buff, Connection sender)
			throws IOException {
		db.speicherZug(id, zugnummer++,buff);
		otherConnection(sender).sendMove(buff, count);
	}

	public void aufgeben(Connection aufgeber) {
		otherConnection(aufgeber).aufgegeben();
	}

	public void spielVerlassen(final Connection verlasser) {
		new Thread() {
			public void run() {
				otherConnection(verlasser).spielBeendet();
			}
		}.start();
		server.spielBeendet(id);
	}

	public void verbindungUnterbrochen(Connection unterbrecher) {

	}

	public String getGegnerName(long meineID) {
		if (spieler[0].getId() != meineID)
			return spieler[0].getName();
		return spieler[1].getName();
	}

	public Long getID() {
		return id;
	}
}
