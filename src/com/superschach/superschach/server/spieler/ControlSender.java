package com.superschach.superschach.server.spieler;

import java.io.IOException;
import java.net.Socket;

import com.superschach.superschach.network.AbstractControlSender;
import com.superschach.superschach.network.Commands;
import com.superschach.superschach.network.Farbe;
import com.superschach.superschach.network.ServerCommands;

public class ControlSender extends AbstractControlSender{

 
	private Connection connection;
	
	public ControlSender(Socket meinControlSocket, Connection con)
			throws IOException {
		super(meinControlSocket);
		this.connection=con;
	}



	public String getName() throws IOException {
		return message(ServerCommands.getName, null);
	}
	
	public byte farbe() throws IOException
	{
			return Farbe.valueOf(message(ServerCommands.getFarbe,null)).nummer;
	}
	
	public void startSpiel(byte farbe,String gegnerName) throws IOException
	{
		message(ServerCommands.starteSpiel,farbe+""+Commands.SEPERATOR+gegnerName);
	}
	
	public void herausfordern(long id, int herausforderungID) throws IOException
	{
		message(ServerCommands.herausfordern,id+""+Commands.SEPERATOR+""+herausforderungID);
	}
	
	public String getVersion() throws IOException
	{
		return message(ServerCommands.getVersion, null);
	}
	
	public void meldung(String meldung) throws IOException
	{
		message(ServerCommands.meldung, meldung);
	}
	
	public void spielBeendet() throws IOException
	{
		message(ServerCommands.spielVerlassen,null);
	}
	
	public String requesLogin(boolean falsch) throws IOException
	{
		return message(ServerCommands.requestLogin,falsch+"");
	}
	
	public void aufgegeben() throws IOException
	{
		message(ServerCommands.aufgegeben,null);
	}

	public void herausforderungAbbrechen(int herausforderungID) throws IOException
	{
		message(ServerCommands.herausforderungAbbrechen,herausforderungID+"");
	}

	@Override
	public void ioError() {
		connection.close();
	}
}
