package com.superschach.superschach.server.spieler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;

import com.superschach.superschach.network.Commands;
import com.superschach.superschach.network.Farbe;
import com.superschach.superschach.server.Herausforderung;
import com.superschach.superschach.server.Server;
import com.superschach.superschach.server.SocketCollector;
import com.superschach.superschach.server.Spiel;

public class Connection {

	private Spieler spieler;
	private Socket meinControlSocket;
	private Socket deinControlSocket;
	private Socket spielSocket;
	private Socket chatSocket;
	private InputStream spielInputStream;
	private OutputStream spielOutputStream;
	private InputStream chatInputStream;
	private OutputStream chatOutputStream;
	// private Server server;
	private ControlSender controlSender;
	private Spiel spiel;
	private boolean closed = false;
	private Farbe spielFarbe;
	private ControlReceiveThread controlReciver;
	//private boolean inGame = false;
	private HashMap<Long, Herausforderung> sentHerausforderungen = new HashMap<Long, Herausforderung>();

	public Connection(SocketCollector socketCollector, Server server)
			throws IOException {
		// this.server = server;
		this.meinControlSocket = socketCollector.getMeinControlSocket();
		this.deinControlSocket = socketCollector.getDeinControlSocket();
		this.spielSocket = socketCollector.getSpielSocket();
		this.spielOutputStream = spielSocket.getOutputStream();
		this.spielInputStream = spielSocket.getInputStream();
		this.chatSocket = socketCollector.getChatSocket();
		this.chatOutputStream = chatSocket.getOutputStream();
		this.chatInputStream = chatSocket.getInputStream();
		this.controlSender = new ControlSender(meinControlSocket,this);
		if (!Server.VERSION.equals(controlSender.getVersion())) {
			controlSender.meldung("versionZuAlt");
			close();
			deinControlSocket.close();
			throw new IOException();
		}
		spieler = null;
		for (int i = 0; spieler == null; i++) {
			if (i == 5) {
				close();
				throw new IOException();
			}
			String[] daten = controlSender.requesLogin(i != 0).split(
					Commands.SEPERATOR + "");
			if (daten.length == 2)
				try {
					spieler = server.logIn(daten[0], daten[1]);
				} catch (SQLException e) {
					controlSender.meldung("Serverfehler");
					e.printStackTrace();
				}

		}
		spieler.addConnection(this);
		askFarbe();
		controlReciver = new ControlReceiveThread(this,
				socketCollector.getDeinControlSocket());
		controlReciver.start();
		new ChatBridgeThread().start();
		new SpielBridgeThread().start();
	}

	public void close() {
		closed = true;
		if (spiel != null)
			spiel.verbindungUnterbrochen(this);
		else if (spieler != null)
			spieler.removeConnection(this);
		try {
			spielSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			chatSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			controlSender.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (controlReciver != null && !controlReciver.isClosed())
			controlReciver.close();
		synchronized (sentHerausforderungen) {
			Herausforderung[] toClose = new Herausforderung[sentHerausforderungen
					.size()];
			toClose = sentHerausforderungen.values().toArray(toClose);
			for (Herausforderung h : toClose) {
				h.close();
			}
		}
	}

	void betreteLobby()
	{
		Spiel spiel=this.spiel;
		if(spiel!=null)
		{
			spiel.spielVerlassen(this);
			this.spiel=null;
		}
		spieler.betreteLobby(this);
	}
	
	public void aufgegeben() {
		try {
			controlSender.aufgegeben();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void spielBeendet() {
		spiel=null;
		try {
			controlSender.spielBeendet();
		} catch (IOException e) {
			e.printStackTrace();
		}
		spieler.betreteLobby(this);
	}

	public void aufgeben() {
		if (spiel != null)
			spiel.aufgeben(this);
	}

	public void sendChat(byte[] buff, int count) throws IOException {
		chatOutputStream.write(buff, 0, count);
		chatOutputStream.flush();
	}

	private int readChat(byte[] buff) throws IOException {
		return chatInputStream.read(buff);
	}

	public void sendMove(byte[] buff, int count) throws IOException {
		spielOutputStream.write(buff, 0, count);
		spielOutputStream.flush();
	}

	private int readMove(byte[] buff) throws IOException {
		return spielInputStream.read(buff);
	}

	public String getName() {
		return spieler.getName();
	}

	public long getId() {
		return spieler.getId();
	}

	public byte askFarbe() {
		try {
			return controlSender.farbe();
		} catch (IOException e) {
			return Farbe.EGAL.nummer;
		}
	}

	public void meldung(String meldung) throws IOException
	{
		controlSender.meldung(meldung);
	}
	
	public void herausfordern(long gegnerID, int heruasforderungID)
			throws IOException {
		controlSender.herausfordern(gegnerID, heruasforderungID);
	}

	boolean fordereheraus(long gegnerID) throws IOException {
		Herausforderung neu = spieler.fordereheraus(gegnerID, this);
		if (neu == null)
			return false;
		synchronized (sentHerausforderungen) {
			Herausforderung alt = sentHerausforderungen.remove(gegnerID);
			if (alt != null)
				alt.close();
			sentHerausforderungen.put(gegnerID, neu);
		}
		return true;
	}

	public boolean nehmeHerausforderungAn(int id) {
		return spieler.nehmeHerausforderungAn(id, this);
	}

	public synchronized void startSpiel(Spiel spiel, Farbe spielFarbe)
			throws IOException {
		if (spiel==null)
			throw new NullPointerException();
		spieler.verlasseLobby(this);
		this.spiel = spiel;
		this.spielFarbe = spielFarbe;
		controlSender.startSpiel(spielFarbe.nummer,
				spiel.getGegnerName(spieler.getId()));
	}

	public boolean isInGame() {
		return spiel!=null;
	}

	public Farbe getFarbe() {
		return spieler.getFarbe();
	}

	public String getLobby() {
		return spieler.getLobby();
	}

	public Farbe getSpielFarbe() {
		return spielFarbe;
	}

	public void removeSpiel(Spiel spiel) {
		this.spiel = null;
	}

	public String getSpiel(long spielID)
	{
		return spieler.getSpiel(spielID);
	}
	
	public void herausforderungAbbrechen(int herausforderungID)
	{
		try {
			controlSender.herausforderungAbbrechen(herausforderungID);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private class ChatBridgeThread extends Thread {
		public void run() {
			try {
				byte[] buff = new byte[250];
				int i = 0;
				do {
					i = readChat(buff);
					Spiel s = spiel;
					if (s != null && i >= 0)
						s.chat(i, buff, Connection.this);
				} while (i != -1 && !closed);
			} catch (IOException e) {
			}
			close();
		}
	}

	private class SpielBridgeThread extends Thread {

		public void run() {
			try {
				byte[] buff = new byte[5];
				int i = 0;
				do {
					i = readMove(buff);
					Spiel s = spiel;
					if (s != null)
						s.zug(i, buff, Connection.this);
				} while (i != -1 && !closed);
			} catch (IOException e) {
			}
			close();
		}
	}

}
