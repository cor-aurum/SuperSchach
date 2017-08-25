package com.superschach.superschach.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.superschach.superschach.server.datenbank.Datenbank;
import com.superschach.superschach.server.spieler.Connection;
import com.superschach.superschach.server.spieler.Spieler;
import com.superschach.superschach.spiel.AbstractGUI;

public class Server {

	private static int[] basePort = { Integer.parseInt(AbstractGUI.prop("baseport")),
			Integer.parseInt(AbstractGUI.prop("controlport")), Integer.parseInt(AbstractGUI.prop("spielport")),
			Integer.parseInt(AbstractGUI.prop("chatport")) };
	private ServerSocket[] serverSockets = new ServerSocket[4];
	private Thread[] listenThreads = new Thread[serverSockets.length];
	private Lobby lobby;
	private Warteliste warteliste;
	private Datenbank datenbank;
	private HashMap<Long, Spieler> aktiveSpieler = new HashMap<Long, Spieler>();
	private HashMap<Long, Spiel> aktiveSpiele = new HashMap<Long, Spiel>();
	public final static String VERSION = "2.2";
	private Logger logger = Logger.getLogger(Server.class);

	// private HashMap<Long,Long>herausforderungen=new HashMap<Long,Long>();

	public Server() throws IOException, SQLException, NoSuchAlgorithmException {
		datenbank = new Datenbank();
		lobby = new Lobby(this);
		warteliste = new Warteliste(this);
		for (int i = 0; i < serverSockets.length; i++) {
			final ServerSocket s = serverSockets[i] = new ServerSocket(basePort[i]);
			listenThreads[i] = new Thread() {
				public void run() {
					while (!s.isClosed()) {
						try {
							final Socket cs = s.accept();
							new Thread() {
								@Override
								public void run() {
									try {
										warteliste.addSocket(cs);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}.start();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			};
			listenThreads[i].start();
		}
		logger.info("Server wurde gestartet...");
	}

	public void neuerClient(SocketCollector sC) {
		try {
			new Connection(sC, this);
			logger.debug("neuer Client");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Spieler logIn(String name, String pw) throws SQLException {
		Spieler neu = datenbank.logIn(name, pw, this);
		if (neu == null)
			return neu;
		synchronized (aktiveSpieler) {
			Spieler ret = aktiveSpieler.get(neu.getId());
			if (ret == null) {
				ret = neu;
				aktiveSpieler.put(neu.getId(), neu);
			}
			return ret;
		}
	}

	public Herausforderung fordereheraus(long gegnerID, Connection herausforderer)
			throws NullPointerException, IOException {
		return lobby.fordereheraus(gegnerID, herausforderer);
	}

	public static int[] getBasePort() {
		return basePort;
	}

	public String getLobby() {
		return lobby.toString();
	}

	public String getName(long id) throws SQLException {
		return datenbank.getName(id);
	}

	public void leaveLobby(Spieler spieler) {
		lobby.removeSpieler(spieler.getId());
	}

	public void removeSpieler(Spieler spieler) {
		leaveLobby(spieler);
		synchronized (aktiveSpieler) {
			if (spieler.getAnzConnections() == 0)
				aktiveSpieler.remove(spieler.getId());
		}
	}

	public void verlasseLobby(Spieler spieler) {
		leaveLobby(spieler);
	}

	public void betreteLobby(Spieler spieler) {
		lobby.addSpieler(spieler);
	}

	public static void main(String[] args, boolean nixda) throws IOException, NoSuchAlgorithmException {
		try {
			new Server();
		} catch (SQLException e) {
			System.err.println(e.getSQLState());
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void spielBeendet(long id) {
		aktiveSpiele.remove(id);
	}

	public void neuesSpiel(Connection herausforderer, Connection annehmer) throws SQLException, IOException {
		Spiel spiel = new Spiel(annehmer, herausforderer, this, datenbank);
		aktiveSpiele.put(spiel.getID(), spiel);
	}

	public String getSpiel(long spielID, long spielerID) {
		try {
			return datenbank.getSpiel(spielID, spielerID);
		} catch (SQLException e) {
			e.printStackTrace();
			return "null";
		}
	}

}
