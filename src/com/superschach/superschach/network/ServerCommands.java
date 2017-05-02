package com.superschach.superschach.network;


/**
 * Befehle des Servers an den Client
 * @author jan
 *
 */
public enum ServerCommands implements Commands{
	
	getVersion(false,true),
	close(false,false),
	meldung(true,false),
	getName(false,true),
	herausfordern(true,false),
	getFarbe(false,true),
	starteSpiel(true,true),
	spielVerlassen(true,false),
	reconnect(true,true),
	requestLogin(true,true),
	aufgegeben(false,false),
	herausforderungAbbrechen(true,false);
	
	
	private final boolean hasParam;
	private final boolean answer;
	ServerCommands(final boolean param, final boolean answer)
	{
		this.hasParam=param;
		this.answer=answer;
	}
	
	@Override
	public boolean hasParam()
	{
		return hasParam;
	}

	@Override
	public boolean answer()
	{
		return answer;
	}

}
