package com.superschach.superschach.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class AbstractControlReceiveThread extends Thread
{

	private InputStream inputStream;
	private Socket socket;
	private PrintWriter printWriter;
	private boolean weiter = true;
	private BufferedReader streamReader;
	//private Enum<T> befehle=T;

	public AbstractControlReceiveThread(Socket s) throws IOException
	{
		socket=s;
		this.printWriter = new PrintWriter(s.getOutputStream());
		this.inputStream = s.getInputStream();
		this.streamReader = new BufferedReader(new InputStreamReader(
				inputStream));
	}

	protected String readParameter() throws IOException
	{
		return streamReader.readLine();
	}
	
	protected abstract void bearbeiteBefehl(int befehl) throws NumberFormatException, IOException;
	
	public void run()
	{
		String recived = "";
		try
		{
			
			//T[] commands = new T();
			while (!socket.isClosed() && socket.isConnected() && weiter
					&& recived != null)
			{
				recived = streamReader.readLine();
				try
				{
					int befehl = Integer.parseInt(recived);
					System.out.println("Bekomme Befehl: " + recived);
					bearbeiteBefehl(befehl);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		} catch (Exception e)
		{
			melde("verbindungsFehler");
		}
		if (recived == null)
			melde("verbindungGeschlossen");
		close();
	}

	protected abstract void melde(String meldung);
	
	public boolean isClosed()
	{
		return !weiter;
	}

	public void close()
	{
		weiter = false;
		try
		{
			socket.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		//spieler.close();
	}

	protected void answer(String aw)
	{
		System.out.println("Antworte: " + aw);
		printWriter.println(aw);
		printWriter.flush();
	}

}