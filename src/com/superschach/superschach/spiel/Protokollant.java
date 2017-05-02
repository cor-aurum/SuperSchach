package com.superschach.superschach.spiel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;


public class Protokollant
{
	private Spiel spiel;
	private int nummer;
	private String protokoll = "Automatisch Generiertes Protokoll von Super-Schach, www.super-schach.com";

	public Protokollant(Spiel spiel)
	{
		this.spiel = spiel;
		this.nummer = 1;
	}

	public void protokoll(int ax, int ay, int nx, int ny, int schach,
			boolean wurf, int bauer)
	{
		if (spiel.getPlayer() != 1)
		{
			protokoll = protokoll + " ";
		} else
		{
			protokoll = protokoll + System.getProperty("line.separator")
					+ nummer + ".";
			nummer++;
		}
		protokoll = protokoll + halbZug(ax, ay, nx, ny, schach, wurf, bauer);
		try
		{
			FileWriter writer = new FileWriter(AbstractGUI.verzeichnis()
					+ "protokoll.slog");
			BufferedWriter bw = new BufferedWriter(writer);
			bw.write(protokoll);
			bw.close();
		} catch (Exception e)
		{
		}
	}

	private String halbZug(int ax, int ay, int nx, int ny, int schach,
			boolean wurf, int bauer)
	{
		String zug = "";
		switch (Math.abs(spiel.inhalt(nx, ny)))
		{
		case 1:
			zug = zug + "T";
			break;
		case 2:
			zug = zug + "L";
			break;
		case 3:
			zug = zug + "D";
			break;
		case 4:
			zug = zug + "S";
			break;
		case 6:
			zug = zug + "J";
			break;
		case 16:
			zug = zug + "K";
			break;
		}
		zug = zug
				+ (char) (ax + 65)
				+ (ay + 1)
				+ (wurf ? "x" : "")
				+ "-"
				+ (char) (nx + 65)
				+ (ny + 1)
				+ (schach == 1 ? "+" : schach == 3 ? "++" : schach == 2 ? "�:�"
						: schach == 4 ? "�:�" : "");
		return zug;
	}

	public void uebersetzen(String zug)
	{
		String[] koo = zug.split("-");
		int ax;
		int ay;
		int nx;
		int ny;
		if (Character.isLetter(koo[0].charAt(1)))
		{
			koo[0] = koo[0].substring(1);
		}
		if (Character.isLetter(koo[0].charAt(koo[0].length() - 1)))
		{
			koo[0] = koo[0].substring(0, koo[0].length() - 1);
		}
		String s = "";
		for (int i = 0; i < koo[1].length(); i++)
		{
			if (Character.isDigit(koo[1].charAt(i))
					|| Character.isLetter(koo[1].charAt(i)))
			{
				s = s + koo[1].charAt(i);
			}
		}
		koo[1] = s;

		try
		{
			ax = koo[0].charAt(0) - 65;
			ay = Integer.parseInt(koo[0].substring(1)) - 1;
			nx = koo[1].charAt(0) - 65;
			ny = Integer.parseInt(koo[1].substring(1)) - 1;
			if (spiel.zugMoeglich(ax, ay, nx, ny) > 0)
			{
				spiel.spielBrett.meldungAusgeben("Zug ausgef�hrt");
			} else
			{
				spiel.spielBrett.meldungAusgeben("Zug nicht m�glich");
			}
			spiel.zug(ax, ay, nx, ny);
		} catch (Exception e)
		{
			spiel.spielBrett.meldungAusgeben("Zug nicht m�glich");
		}
	}

	public boolean speichern(File f)
	{
		// if
		// (!gUI.brett.spiel.speichern(chooser.getSelectedFile()))
		try
		{
			BufferedWriter w = new BufferedWriter(new FileWriter(f));
			w.write(protokoll);
			w.close();
		} catch (Exception e)
		{
			System.out.println(e);
			return false;
		}
		return true;
	}
}
