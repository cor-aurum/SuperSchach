package com.superschach.superschach.netzwerk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.log4j.Logger;

public abstract class AbstractControlSender
{

	private Socket socket;
	private InputStream inputStream;
	private OutputStream outputStream;
	private BufferedReader streamReader;
	private PrintWriter streamWriter;
	private Logger logger=Logger.getLogger(AbstractControlSender.class);

	public AbstractControlSender(Socket meinControlSocket) throws IOException
	{
		this.socket = meinControlSocket;
		this.outputStream = meinControlSocket.getOutputStream();
		this.inputStream = meinControlSocket.getInputStream();
		this.streamReader=new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
		streamWriter= new PrintWriter(outputStream);
	}

	public void close() throws IOException
	{
		streamReader.close();
		socket.close();
	}

	protected String message(Commands command, String param) throws IOException
	{
		try{
		logger.info("Sende Befehl: "+ command.name());
		synchronized (this) {
			streamWriter.println(command.ordinal());
			if (command.hasParam()) {
				streamWriter.println(param);
			}
			streamWriter.flush();
			if (!command.answer()) {
				return null;
			}
			return recive();
		}}catch(IOException e){
			ioError();
			throw e;
		}
	}

	public abstract void ioError();
	
	private String recive() throws IOException
	{
		return streamReader.readLine();
	}

}