package com.superschach.superschach.ki;

import java.io.PrintWriter;
import java.util.Scanner;

import com.superschach.superschach.gui.GUI;
import com.superschach.superschach.kontroller.Kontroller;

/*
 * TODO
 * Bietet eine Schnittstelle zu externen KIs mittels des Universal Chess Interfaces
 */
public class UCIKi extends KISchnittstelle implements KI {
	
	private Scanner sc;
	private PrintWriter prt;
	private String zugverlauf="";

	public UCIKi(String exec) {
		super(null);
		try {
			Process uci = Runtime.getRuntime().exec(exec);
			sc = new Scanner(uci.getInputStream());
			prt=new PrintWriter(uci.getOutputStream());
			GUI.logger.debug(sc.nextLine());
			GUI.logger.debug("uci");
			prt.println("uci");
			prt.flush();
			String line = "";
            while(!(line = sc.nextLine()).equals("uciok")) {
            	System.out.println(line);
            }
            GUI.logger.debug(line);
            prt.println("isready");
			prt.flush();
            while(!(line = sc.nextLine()).equals("readyok")) {
            	GUI.logger.debug(line);
            }
            GUI.logger.debug(line);
            prt.println("ucinewgame");
			prt.flush();
			GUI.logger.info("UCI erfolgreich initiiert");
		} catch (Exception e) {
			GUI.logger.warn(e.getStackTrace());
		}

	}

	@Override
	public void zug(Kontroller spiel, Zug zug) throws Exception {
		byte[] b=spiel.letzterZug();
		String letzterZug="";
		letzterZug+=(char)(b[0]+97);
		letzterZug+=(char)(b[1]+49);
		letzterZug+=(char)(b[2]+97);
		letzterZug+=(char)(b[3]+49);
		if(!letzterZug.equals("a1a1"))
		{
			zugverlauf+=" "+letzterZug;
		}
		prt.println("position startpos moves "+zugverlauf);
		prt.flush();
		prt.println("go");
		prt.flush();
		String line;
        while(!(line = sc.nextLine()).startsWith("bestmove"));
        GUI.logger.debug(line);
        line=line.split(" ")[1];
        int posx=line.charAt(0)-97;
        int posy=line.charAt(1)-49;
        int zielx=line.charAt(2)-97;
        int ziely=line.charAt(3)-49;
		zug.zug(posx, posy, zielx, ziely);
		zugverlauf+=" "+line;
	}

	@Override
	public boolean machZug()
	{
		boolean ret = true;
		Zug zugOb = new Zug();
		ret = zug(zugOb.getPosX(), zugOb.getPosY(), zugOb.getZielX(),
				zugOb.getZielY());
		return ret;
	}

}
