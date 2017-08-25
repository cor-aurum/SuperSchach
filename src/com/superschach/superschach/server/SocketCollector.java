package com.superschach.superschach.server;

import java.net.Socket;

public class SocketCollector {

	private int count=0;
	private Socket[] sockets=new Socket[4];
	//private TimerTask timeOutTask;
	//private Timer timeOutTimer;
	private long token;
	
	public SocketCollector(long token) 
	{
		//addSocket(socket);
		this.token=token;
		/*
		timeOutTask=new TimerTask(){
			
			@Override
			public void run() {
				close();
				
			}
			
		};*/
	}
	
	public void addSocket(Socket socket)
	{
		int index=socket.getLocalPort()-Server.getBasePort()[0];
		if(sockets[index]==null)
			count++;
		this.sockets[index]=socket;
	}
	
	public boolean isReady()
	{
		if(count<sockets.length)
			return false;
		for(int i=0; i<sockets.length; i++)
		{
			if(sockets[i].isClosed())
				return false;
			if(!sockets[i].isConnected())
				return false;
		}
		return true;
	}
	
	public Socket getMeinControlSocket()
	{
		return sockets[0];
	}
	
	public Socket getDeinControlSocket()
	{
		return sockets[1];
	}
	
	public Socket getSpielSocket()
	{
		return sockets[2];
	}
	
	public Socket getChatSocket()
	{
		return sockets[3];
	}
	
	public long getToken()
	{
		return token;
	}

}
