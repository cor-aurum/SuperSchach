package com.superschach.superschach.netzwerk.client;

import java.io.IOException;
import java.net.Socket;

import com.superschach.superschach.netzwerk.AbstractControlReceiveThread;
import com.superschach.superschach.netzwerk.Commands;
import com.superschach.superschach.netzwerk.Farbe;
import com.superschach.superschach.netzwerk.ServerCommands;

public class ControlReceiveThread extends AbstractControlReceiveThread
		implements Runnable
{

	private Client spieler;

	public ControlReceiveThread(Client spieler, Socket deinControlSocket)
			throws IOException
	{
		super(deinControlSocket);
		this.spieler = spieler;
	}

	@Override
	protected void bearbeiteBefehl(int befehl) throws NumberFormatException,
			IOException
	{
		ServerCommands[] commands = ServerCommands.values();
		switch (commands[befehl])
		{
		case getName:
			sendName();
			break;
		case herausfordern:
			fordereheraus();
			break;
		case getFarbe:
			sendFarbe();
			break;
		case close:
			close();
			break;
		case starteSpiel:
			starteSpiel();
			break;
		case meldung:
			meldung();
			break;
		case getVersion:
			getVersion();
			break;
		case requestLogin:
			getLogin();
		case reconnect:
			break;
		case spielVerlassen:
			verlasseSpiel();
			break;
		case aufgegeben:
			aufgegeben();
			break;
		case herausforderungAbbrechen:
			herausforderungAbbrechen();
		}

	}

	private void herausforderungAbbrechen() throws NumberFormatException, IOException
	{
		spieler.herausforderungAbbrechen(Integer.parseInt(readParameter()));
	}
	
	private void aufgegeben()
	{
		spieler.gegnerAufgegeben();
	}

	private void verlasseSpiel() throws NumberFormatException, IOException
	{
		spieler.gegnerSpielVerlassen();
	}

	void getVersion()
	{
		answer(spieler.getVersion());
	}

	void meldung() throws IOException
	{
		spieler.meldung(readParameter());
	}

	void sendName()
	{
		answer(spieler.getName());
	}

	void sendFarbe()
	{
		answer(Math.random() < 0.5 ? "WEISS" : "SCHWARZ");
	}

	void fordereheraus() throws NumberFormatException, IOException
	{
		String[] ids = readParameter().split(Commands.SEPERATOR + "");
		spieler.fordereheraus(Long.parseLong(ids[0]), Integer.parseInt(ids[1]));
	}

	void starteSpiel() throws IOException
	{
		boolean b = false;
		String[] param = readParameter().split(Commands.SEPERATOR + "");
		try
		{
			spieler.starteSpiel(Farbe.values()[Integer.parseInt(param[0])],
					param[1]);
			b = true;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		answer(b + "");
	}

	void getLogin()
	{
		String[] login = null;
		try
		{
			login = spieler.getLogin(Boolean.parseBoolean(readParameter()));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		answer(login != null ? login[0] + Commands.SEPERATOR + login[1]
				: "error");
	}

	@Override
	public void melde(String meldung)
	{
		spieler.meldung(meldung);

	}

}
