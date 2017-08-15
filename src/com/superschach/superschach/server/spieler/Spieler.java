package com.superschach.superschach.server.spieler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.superschach.superschach.network.Commands;
import com.superschach.superschach.network.Farbe;
import com.superschach.superschach.server.Herausforderung;
import com.superschach.superschach.server.Server;

public class Spieler {

	private long id;
	private byte farbe;
	private String name;
	private AtomicInteger anzSpiele;
	private AtomicInteger gewonneneSpiele;
	private AtomicInteger verloreneSpiele;
	private ArrayList<Connection> connections = new ArrayList<Connection>();
	private ArrayList<Connection> inLobby = new ArrayList<Connection>();
	private HashMap<Integer, Herausforderung> herausforderungen = new HashMap<Integer, Herausforderung>();
	private boolean modified = true;
	private String attribute = "";
	private Server server;
	private AtomicInteger anzHerausforderungen = new AtomicInteger(0);

	public Spieler(long id, String name, int anzSpiele, int gewonneneSpiele, int verloreneSpiele, Server server) {
		this.server = server;
		this.name = name;
		this.id = id;
		this.anzSpiele = new AtomicInteger(anzSpiele);
		this.gewonneneSpiele = new AtomicInteger(gewonneneSpiele);
		this.verloreneSpiele = new AtomicInteger(verloreneSpiele);
	}

	public void addConnection(Connection con) {
		synchronized (connections) {
			connections.add(con);
			setFarbe(con.askFarbe());
			betreteLobby(con);
		}

	}

	void betreteLobby(Connection con) {
		synchronized (inLobby) {
			inLobby.add(con);
			if (inLobby.size() == 1)
				server.betreteLobby(this);
		}
	}

	public void removeConnection(Connection con) {
		boolean remove;
		synchronized (connections) {
			verlasseLobby(con);
			connections.remove(con);
			remove = connections.size() == 0;
		}
		if (remove)
			server.removeSpieler(this);

	}

	public Herausforderung fordereheraus(long gegnerID, Connection herausforderer) throws IOException {
		return server.fordereheraus(gegnerID, herausforderer);
	}

	public String getLobby() {
		return server.getLobby();
	}

	public void verlasseLobby(Connection con) {
		synchronized (inLobby) {
			inLobby.remove(con);
			if (inLobby.size() == 0)
				server.verlasseLobby(this);
		}
	}

	public int getAnzConnections() {
		return connections.size();
	}

	private void setFarbe(byte b) {
		this.farbe = b;
		modified = true;
	}

	public byte getFarbeByte() {
		return farbe;
	}

	public Farbe getFarbe() {
		return Farbe.values()[farbe];
	}

	public String getName() {
		return name;
	}

	public int getPunkte() {
		return anzSpiele.intValue() + gewonneneSpiele.intValue() - verloreneSpiele.intValue();
	}

	public long getId() {
		return id;
	}

	public boolean nehmeHerausforderungAn(int herausforderungID, Connection c) {
		try {
			Herausforderung h = null;
			synchronized (herausforderungen) {
				h = herausforderungen.get(herausforderungID);
			}
			if (h == null)
				return false;
			return h.annehmen(c);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private void generateString() {
		StringBuffer temp = new StringBuffer();
		temp.append(id);
		temp.append(Commands.SEPERATOR2);
		temp.append(name);
		temp.append(Commands.SEPERATOR2);
		temp.append(Farbe.values()[farbe]);
		temp.append(Commands.SEPERATOR2);
		temp.append(getPunkte());
		temp.append(Commands.SEPERATOR);
		attribute = temp.toString();
	}

	public String toString() {
		synchronized (attribute) {
			if (modified) {
				modified = false;
				generateString();
			}
		}
		return attribute;
	}

	public String getSpiel(long spielID) {
		return server.getSpiel(spielID, id);
	}

	public void beendeHerausforderung(final int h) {
		synchronized (herausforderungen) {
			herausforderungen.remove(h);
		}

		synchronized (inLobby) {
			for (final Connection con : inLobby) {
				new Thread() {
					@Override
					public void run() {
						con.herausforderungAbbrechen(h);
					}
				}.start();
			}
		}
	}

	public void herausfordern(final Herausforderung h) {
		synchronized (herausforderungen) {
			herausforderungen.put(h.getId(), h);
		}
		synchronized (inLobby) {
			for (int i = 0; i < inLobby.size(); i++) {
				final int ii = i;
				new Thread() {
					public void run() {
						try {
							inLobby.get(ii).herausfordern(h.getHerausforderID(), h.getId());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}.start();
			}
		}
	}

	public int getNexHerausforderungsID() {
		return anzHerausforderungen.incrementAndGet();
	}

}
