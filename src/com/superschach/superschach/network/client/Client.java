package com.superschach.superschach.network.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

import com.superschach.superschach.network.AbstractControlReceiveThread;
import com.superschach.superschach.network.Commands;
import com.superschach.superschach.network.Farbe;
import com.superschach.superschach.spiel.AbstractGUI;

public class Client
{

	private Socket meinControlSocket;
	private Socket deinControlSocket;
	private Socket spielSocket;
	private Socket chatSocket;
	/*
	 * private InputStream meinControlInputStream; private OutputStream
	 * meinControlOutputStream; private InputStream deinControlInputStream;
	 * private OutputStream deinControlOutputStream;
	 */
	private InputStream chatInputStream;
	private OutputStream chatOutputStream;
	private ControlSender controlSender;
	private String name;
	private long id;
	private AbstractGUI gui;
	private PrintWriter chatWriter;
	private BufferedReader chatReader;
	private AtomicBoolean closed = new AtomicBoolean(false);
	private OnlineSpieler spieler;
	private AbstractControlReceiveThread controlReciver;
	private Thread shutdownHook = new ShutdownHook();
	private Logger logger=Logger.getLogger(Client.class);

	public Client(String ip, String name, AbstractGUI gui)
			throws UnknownHostException, IOException
	{
		Runtime.getRuntime().addShutdownHook(shutdownHook);
		this.name = name;
		this.gui = gui;
		byte[] token = new byte[8];
		deinControlSocket = new Socket(ip, Integer.parseInt(AbstractGUI.prop("baseport")));
		deinControlSocket.getInputStream().read(token);
		meinControlSocket = new Socket(ip, Integer.parseInt(AbstractGUI.prop("controlport")));
		meinControlSocket.getOutputStream().write(token);
		spielSocket = new Socket(ip, Integer.parseInt(AbstractGUI.prop("spielport")));
		spielSocket.getOutputStream().write(token);
		chatSocket = new Socket(ip, Integer.parseInt(AbstractGUI.prop("chatport")));
		chatInputStream = chatSocket.getInputStream();
		chatOutputStream = chatSocket.getOutputStream();
		chatOutputStream.write(token);
		controlReciver = new ControlReceiveThread(this, deinControlSocket);
		controlReciver.start();
		controlSender = new ControlSender(meinControlSocket, this);
		chatWriter = new PrintWriter(chatOutputStream);
		chatReader = new BufferedReader(new InputStreamReader(chatInputStream));
		id = controlSender.getID();
		new ChatReaderThread().start();
	}

	public void close()
	{
		close(false);
	}

	void error()
	{
		close();
		gui.verbindungUnterbrochen();
	}

	private void close(boolean shutDown)
	{
		if (!closed.getAndSet(true))
		{
			if (!shutDown)
			{
				try
				{
					Runtime.getRuntime().removeShutdownHook(shutdownHook);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}

			try
			{
				chatSocket.close();
				chatReader.close();
				spielSocket.close();
				controlSender.close();
				if (controlReciver.isClosed())
					controlReciver.close();
			} catch (Exception e)
			{
				logger.info("Netzwerkverbindung wird geschlossen");
			}
		}
	}

	public String getName()
	{
		return name;
	}

	public void meldung(String meldung)
	{
		try
		{
			meldung = AbstractGUI.meldung(meldung);
		} catch (Exception e)
		{
		}
		gui.meldungAusgeben(meldung);

	}

	public void fordereheraus(long gegnerID, final int herausforderungID)
	{
		gui.herausforderung(gegnerID, herausforderungID);
	}

	public boolean herausfordern(long gegnerID) throws IOException
	{
		return controlSender.herausfordern(gegnerID);
	}

	public boolean nehmeHerausforderungAn(int herausforderungID)
			throws IOException
	{
		return controlSender.nehmeHerausforderungAn(herausforderungID);
	}

	public String getVersion()
	{
		return AbstractGUI.getVersion();
	}

	public void betreteLobby() throws IOException
	{
		controlSender.enterLobby();
	}

	synchronized public Spieler[] getLobby() throws IOException
	{
		String lobbyString = controlSender.getLobby();
		if (lobbyString.length() == 0)
			return new Spieler[0];
		String[] spielerStrings = lobbyString.split(Commands.SEPERATOR + "");
		Spieler[] ret = new Spieler[spielerStrings.length];
		int error = 0;
		for (int i = 0; i < ret.length; i++)
		{
			try
			{
				ret[i] = new Spieler(spielerStrings[i]);
				if (ret[i].getID() == id)
				{
					ret[i] = null;
					error++;
				}
			} catch (Exception e)
			{
				e.printStackTrace();
				error++;
			}
		}
		if (error == 0)
			return ret;
		Spieler[] ret2 = new Spieler[ret.length - error];
		for (int i = 0, j = 0; i < ret.length; i++)
		{
			if (ret[i] != null)
			{
				ret2[j] = ret[i];
				j++;
			}
		}
		return ret2;
	}

	public void sendeChat(String nachricht)
	{
		chatWriter.println(nachricht);
		chatWriter.flush();
	}

	public void starteSpiel(final Farbe farbe, String gegnerName)
			throws IOException
	{
		gui.leaveLobby();
		// gui.meldungAusgeben("Beginne Spiel mit " + gegnerName);
		if (spieler == null)
			spieler = new OnlineSpieler(spielSocket);
		gui.aktiviereKI(spieler, farbe.nummer, gegnerName);
		if (gui.Player0() == (farbe.nummer == 0))
			new Thread()
			{
				public void run()
				{
					gui.ki(farbe.nummer);
				}
			}.start();
	}

	private class ChatReaderThread extends Thread
	{
		public void run()
		{
			String nachricht = "";
			try
			{
				while (!closed.get()
						&& null != (nachricht = chatReader.readLine()))
				{
					gui.chaterhalten(nachricht);
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public byte[][] getSpiel(long spielID) throws IOException
	{
		String spiel = controlSender.getSpiel(spielID);
		byte[][] ret = new byte[spiel.length() / 5][5];
		for (int i = 0, j = 0; i < ret.length; i++)
		{
			for (int k = 0; k < ret[i].length; k++)
			{
				ret[i][k] = (byte) spiel.charAt(j++);
			}
		}
		return ret;
	}

	public boolean isOpen()
	{
		return !closed.get() && isOK(meinControlSocket)
				&& isOK(deinControlSocket) && isOK(chatSocket)
				&& isOK(spielSocket);
	}

	private boolean isOK(Socket socket)
	{
		return socket != null && !socket.isClosed() && socket.isConnected();
	}

	public String[] getLogin(boolean falsch)
	{
		return gui.getLogin(falsch);
	}

	public void gegnerAufgegeben()
	{
		gui.gegnerAufgegeben();
	}

	public void aufgeben()
	{
		controlSender.aufgeben();
	}

	public void remis()
	{
		// TODO Auto-generated method stub

	}

	public void herausforderungAbbrechen(int herausforderungID)
	{
		gui.herausforderungAbbrechen(herausforderungID);
	}
	
	void gegnerSpielVerlassen()
	{
		gui.gegnerSpielVerlassen();
	}

	private class ShutdownHook extends Thread
	{
		public void run()
		{
			new Thread()
			{
				public void run()
				{
					try
					{
						sleep(5000);
						ShutdownHook.this.interrupt();
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}.start();
			close(true);
		}
	}

}
