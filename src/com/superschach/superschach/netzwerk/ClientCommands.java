package com.superschach.superschach.netzwerk;

/**
 * 
 * Befehle des Clients an den Server
 * 
 * @author jan
 *
 */
public enum ClientCommands implements Commands{
	
	close(false,false),
	getId(false,true),
	reconnect(true,true),
	getLobby(false,true),
	herausfordern(true,true),
	herausforderungAnnehmen(true,true),
	aufgeben(false,false),
	enterLobby(false,false),
	getSpiel(true,true);
	
	
	
	private final boolean hasParam;
	private final boolean answer;
	ClientCommands(final boolean param,final boolean answer)
	{
		this.hasParam=param;
		this.answer=answer;
	}
	public boolean hasParam()
	{
		return hasParam;
	}
	
	public boolean answer()
	{
		return answer;
	}

}
