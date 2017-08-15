package com.superschach.superschach.server.datenbank;

import java.io.Closeable;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import com.superschach.superschach.network.MDFiver;
import com.superschach.superschach.server.Server;
import com.superschach.superschach.server.spieler.Spieler;
import com.superschach.superschach.spiel.AbstractGUI;

public class Datenbank implements Closeable {

	// private Connection conn;
	private  Connection connection = DriverManager.getConnection(
            "jdbc:hsqldb:file:"+AbstractGUI.verzeichnis()+"schach.db"+";shutdown=true", "sa", "");
	private final MDFiver md5 = new MDFiver();
	private LinkedList<Zug> ungespeicherteZuege = new LinkedList<Zug>();
	private Thread nachholer = new Thread() {
		@Override
		public void run() {
			while (true) {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				synchronized (ungespeicherteZuege) {
					int size = ungespeicherteZuege.size();
					if (size > 0) {
						int lastsize;
						do {
							lastsize = size;
							Zug z = ungespeicherteZuege.getFirst();
							speicherZug(z.spielid, z.zugnummer, z.zug);
							size = ungespeicherteZuege.size();
						} while (size < lastsize && size > 0);
					}
				}
			}
		}
	};
	
	static {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
        } catch (ClassNotFoundException e) {
            throw new Error(e);
        }
    }
	private final String[] zugTeile = { "xstart", "ystart", "xziel", "yziel", "extra" };

	public Datenbank() throws SQLException, NoSuchAlgorithmException {
		this("name", "pw", "172.17.0.1", 3306, "datenbank");
	}

	public Datenbank(String user, String password, String server, int port, String schachDatabase)
			throws SQLException, NoSuchAlgorithmException {
		//dataSource = new JDBCDataSource();
		// dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		// dataSource.setUrl("jdbc:mysql://" + server + ":" + port + "/" +
		// schachDatabase);
		// dataSource.setUser(user);
		// dataSource.setPassword(password);

		/*
		 * dataSource.setUser(user); dataSource.setPassword(password);
		 * dataSource.setServerName(server); dataSource.setPort(port);
		 * dataSource.setAutoReconnect(true); dataSource.setCachePrepStmts(true);
		 */
		nachholer.start();

	}

	public Spieler logIn(String name, String pw, Server server) throws SQLException {

		Spieler ret = null;
		try (PreparedStatement statement = connection.prepareStatement(
						"SELECT id,name,anzSpiele,gewonneneSpiele,verloreneSpiele FROM spieler WHERE name= ? AND passwort= ?");) {
			statement.setString(1, name);
			statement.setString(2, md5.md5(pw));

			try (ResultSet rs = statement.executeQuery()) {
				if (rs.next())
					ret = new Spieler(rs.getLong("id"), name, rs.getInt("anzSpiele"), rs.getInt("gewonneneSpiele"),
							rs.getInt("verloreneSpiele"), server);
			}
		}
		return ret;
	}

	public String getName(long id) throws SQLException {
		String ret = null;
		try (PreparedStatement statement = connection.prepareStatement("SELECT name FROM spieler WHERE id= ?");) {
			statement.setLong(1, id);
			try (ResultSet rs = statement.executeQuery()) {
				if (rs.next())
					ret = rs.getString("name");
			}
		}
		return ret;

	}

	public Long neuesSpiel(long weiss, long schwarz) throws SQLException {
		Long ret = null;
		try (PreparedStatement insert = connection
						.prepareStatement("INSERT INTO spiele (weiss, schwarz) VALUES(?,?)");
				PreparedStatement getID = connection.prepareStatement("SELECT LAST_INSERT_ID()");) {
			insert.setLong(1, weiss);
			insert.setLong(2, schwarz);
			insert.executeUpdate();
			try (ResultSet rs = getID.executeQuery()) {
				rs.next();
				ret = rs.getLong(1);
			}
		}
		return ret;
	}

	public void speicherZug(long spielid, int zugnummer, byte[] zug) {
		try (PreparedStatement insert = connection.prepareStatement(
						"INSERT INTO zuege (spiel, nummer,xstart,ystart,xziel,yziel,extra) VALUES(?,?,?,?,?,?,?)");) {
			insert.setLong(1, spielid);
			insert.setInt(2, zugnummer);
			insert.setByte(3, zug[0]);
			insert.setByte(4, zug[1]);
			insert.setByte(5, zug[2]);
			insert.setByte(6, zug[3]);
			insert.setByte(7, zug[4]);
			insert.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			synchronized (ungespeicherteZuege) {
				ungespeicherteZuege.add(new Zug(spielid, zugnummer, zug));
			}
		}
	}

	private class Zug {
		public final long spielid;
		public final int zugnummer;
		public final byte[] zug;

		public Zug(long spielid, int zugnummer, byte[] zug) {
			this.spielid = spielid;
			this.zugnummer = zugnummer;
			this.zug = zug.clone();
		}
	}

	public String getSpiel(long spielID, long spielerID) throws SQLException {
		String ret = null;
		try (PreparedStatement test = connection
						.prepareStatement("SELECT id FROM spiele WHERE id= ? AND (weiss= ? OR schwarz= ?)");) {
			test.setLong(1, spielID);
			test.setLong(2, spielerID);
			test.setLong(3, spielerID);
			try (ResultSet rs = test.executeQuery()) {
				if (rs.next()) {
					try (PreparedStatement statement = connection.prepareStatement(
							"SELECT xstart,ystart,xziel,yziel,extra FROM zuege WHERE spiel= ? ORDER BY nummer");) {
						statement.setLong(1, spielID);
						try (ResultSet zuege = statement.executeQuery()) {
							StringBuilder sb = new StringBuilder();
							while (zuege.next()) {
								for (String zugTeil : zugTeile)
									sb.append((char) zuege.getShort(zugTeil));
							}
							ret = sb.toString();
						}
					}
				}
			}
		}
		return ret;
	}

	public static void main(String[] args) throws NoSuchAlgorithmException, SQLException {
		Datenbank db = new Datenbank();
		try {
			System.out.println(db.getSpiel(1, 1));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			db.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	@Override
	public void close() throws IOException {
		try {
			//dataSource.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
