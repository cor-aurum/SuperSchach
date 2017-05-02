package com.superschach.superschach.network.client;

import java.io.IOException;
import java.net.Socket;

import com.superschach.superschach.network.AbstractControlSender;
import com.superschach.superschach.network.ClientCommands;
import com.superschach.superschach.network.Commands;

public class ControlSender extends AbstractControlSender
{

	private Client client;
	
	public ControlSender(Socket meinControlSocket,Client client) throws IOException
	{
		super(meinControlSocket);
		this.client=client;
	}

	public String getLobby() throws IOException
	{
		return message(ClientCommands.getLobby, null);
	}

	boolean herausfordern(long id) throws IOException
	{
		return message(ClientCommands.herausfordern, id + "").equals("true");
	}

	public long getID() throws NumberFormatException, IOException
	{
		return Long.parseLong(message(ClientCommands.getId, null));
	}

	public boolean nehmeHerausforderungAn(int herausforderungID)
			throws IOException
	{
		return Boolean
				.parseBoolean(message(ClientCommands.herausforderungAnnehmen,
						"" + herausforderungID));
	}

	public boolean reconnect(long id, String pw) throws IOException
	{
		return Boolean.parseBoolean(message(ClientCommands.reconnect, id
				+ Commands.SEPERATOR + pw));
	}

	public void aufgeben()
	{
		try
		{
			message(ClientCommands.aufgeben, null);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void enterLobby() throws IOException
	{
		message(ClientCommands.enterLobby,null);
	}

	public String getSpiel(long spielID) throws IOException
	{
		return message(ClientCommands.getSpiel,spielID+"");
	}
	
	@Override
	public void ioError()
	{
		client.error();
	}

}
