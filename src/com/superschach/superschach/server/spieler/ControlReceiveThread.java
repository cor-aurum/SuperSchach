package com.superschach.superschach.server.spieler;

import java.io.IOException;
import java.net.Socket;

import com.superschach.superschach.netzwerk.AbstractControlReceiveThread;
import com.superschach.superschach.netzwerk.ClientCommands;

public class ControlReceiveThread extends AbstractControlReceiveThread {

	private Connection spieler;
	private final static ClientCommands[] commands = ClientCommands.values();

	public ControlReceiveThread(Connection spieler, Socket deinControlSocket)
			throws IOException {
		super(deinControlSocket);
		this.spieler = spieler;
	}

	private void nehmeHerausforderungAn() throws NumberFormatException,
			IOException {
		answer(spieler
				.nehmeHerausforderungAn(Integer.parseInt(readParameter())) + "");
	}

	private void fordereheraus() throws NumberFormatException, IOException {
		answer(spieler.fordereheraus(Long.parseLong(readParameter())) + "");
	}

	private void sendLobby() {
		answer(spieler.getLobby());
	}

	private void sendID() {
		answer(spieler.getId() + "");
	}
	
	private void getSpiel() throws NumberFormatException, IOException{
		answer(spieler.getSpiel(Long.parseLong(readParameter())));
	}
	
	@Override
	protected void bearbeiteBefehl(int befehl) throws NumberFormatException,
			IOException {
		switch (commands[befehl]) {
		case herausfordern:
			fordereheraus();
			break;
		case getLobby:
			sendLobby();
			break;
		case close:
			close();
			break;
		case getId:
			sendID();
			break;
		case herausforderungAnnehmen:
			nehmeHerausforderungAn();
		case reconnect:
			break;
		case aufgeben:
			spieler.aufgeben();
			break;
		case enterLobby: spieler.betreteLobby();
			break;
		case getSpiel:
			getSpiel();
			break;
		}

	}
	
	@Override
	protected void melde(String meldung) {
		// TODO Auto-generated method stub

	}
}
