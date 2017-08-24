package com.superschach.superschach.ki;

import java.io.PrintWriter;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.superschach.superschach.kontroller.Kontroller;
import com.superschach.superschach.spiel.AbstractGUI;

/*
 * TODO
 * Bietet eine Schnittstelle zu externen KIs mittels des Universal Chess Interfaces
 */
public class UCIKi extends KISchnittstelle implements KI {

	private Scanner sc;
	private PrintWriter prt;
	private boolean active = true;
	private Logger logger=Logger.getLogger(UCIKi.class);

	public UCIKi(String exec) {
		super(null);
		try {
			Process uci = Runtime.getRuntime().exec(exec);
			sc = new Scanner(uci.getInputStream());
			prt = new PrintWriter(uci.getOutputStream());
			logger.debug(sc.nextLine());
			logger.debug("uci");
			prt.println("uci");
			prt.flush();
			String line = "";
			while (!(line = sc.nextLine()).equals("uciok")) {
				logger.debug(line);
			}
			logger.debug(line);
			prt.println("isready");
			prt.flush();
			while (!(line = sc.nextLine()).equals("readyok")) {
				logger.debug(line);
			}
			logger.debug(line);
			prt.println("ucinewgame");
			prt.flush();
			logger.info("UCI erfolgreich initiiert");
		} catch (Exception e) {
			logger.error(exec + " konnte nicht gestartet werden");
			active = false;
		}

	}

	@Override
	public void zug(Kontroller spiel, Zug zug) throws Exception {
		if (!active) {
			zug.nachricht(AbstractGUI.meldung("ucifehlstart"));
		}
		logger.debug(spiel.getFen());
		prt.println("position fen " + spiel.getFen());
		prt.flush();
		prt.println("go");
		prt.flush();
		String line;
		while (!(line = sc.nextLine()).startsWith("bestmove"))
			;
		logger.debug(line);
		line = line.split(" ")[1];
		int posx = line.charAt(0) - 97;
		int posy = line.charAt(1) - 49;
		int zielx = line.charAt(2) - 97;
		int ziely = line.charAt(3) - 49;
		if (line.length() > 4) {
			switch (line.charAt(4)) {
			case 'q':
				zug.tausch(3);
				break;
			case 'b':
				zug.tausch(2);
				break;
			case 'n':
				zug.tausch(4);
				break;
			case 'r':
				zug.tausch(1);
				break;
			}
		}
		zug.zug(posx, posy, zielx, ziely);
	}

	@Override
	public boolean machZug() {
		boolean ret = true;
		Zug zugOb = new Zug();
		ret = zug(zugOb.getPosX(), zugOb.getPosY(), zugOb.getZielX(), zugOb.getZielY());
		return ret;
	}

}
