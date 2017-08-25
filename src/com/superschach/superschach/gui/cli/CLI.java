package com.superschach.superschach.gui.cli;

import org.apache.log4j.Logger;

import com.superschach.superschach.spiel.AbstractGUI;

public class CLI extends AbstractGUI {
	private Logger logger = Logger.getLogger(CLI.class);

	public CLI(String[] args) {
		super(args[0].equals("-s"));
		logger.info("cli gestartet");
		if (!args[0].equals("-s")) {
			switch (args[0]) {
			case "-x":
				//TODO
				break;
			default:
			case "-h":
				System.out.println("SuperSchach  Copyright (C) 2017  Felix Schütze\n"
						+ "    This program comes with ABSOLUTELY NO WARRANTY.\n"
						+ "    This is free software, and you are welcome to redistribute it\n"
						+ "    under certain conditions");
				System.out.println();
				System.out.println("Benutzung: java -jar SuperSchach.jar [Options]");
				System.out.println("Keine Angabe von Optionen startet das GUI für die manuelle Interaktion.");
				System.out.println("Optionen:");
				System.out.println("\t -s: Startet einen Schachserver.");
				System.out.println("\t -x [-v] <datei.xml>: Startet Spiele wie es in der XML Datei definiert ist.");
				System.out.println("\t -h: Öffnet diese Hilfeseite.");
				System.exit(0);
				break;
			}
		}
	}

	@Override
	public boolean sollThread() {
		return true;
	}

	@Override
	public void leaveLobby() {
		logger.warn("CLI#leaveLobby sollte nicht aufgerufen werden");
	}

	@Override
	public int figurMenu() {
		logger.warn("CLI#figurMenu sollte nicht aufgerufen werden");
		return 0;
	}

	@Override
	public void meldungAusgeben(String meldung) {
		logger.info(meldung);
	}

	@Override
	public void aktualisieren(int x, int y) {
		aktualisieren();
	}

	@Override
	public void aktualisieren() {
		// TODO Auto-generated method stub

	}

	@Override
	public void nachricht(String s) {
		meldungAusgeben(s);
	}

	@Override
	public String[] getLogin(boolean falsch) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void gegnerSpielVerlassen() {
		logger.error("Verbindung unterbrochen");
	}

	@Override
	public void herausforderungAbbrechen(int herausforderungID) {
	}
}
