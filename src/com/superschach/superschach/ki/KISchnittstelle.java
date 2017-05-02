package com.superschach.superschach.ki;

import com.superschach.superschach.kontroller.Kontroller;
import com.superschach.superschach.kontroller.figuren.Figur;

/**
 * Write a description of class KISchnittstelle here.
 * 
 * @author Felix Sch&uuml;tze
 * @version (a version number or a date)
 */
public abstract class KISchnittstelle
{
	// instance variables - replace the example below with your own
	private Kontroller kontroller;

	public KISchnittstelle(Kontroller kontroller)
	{
		this.kontroller = kontroller;
	}

	public int inhalt(int x, int y)
	{
		return kontroller.inhaltFaktor(x, y);
	}

	public boolean zug(int posx, int posy, int zielx, int ziely)
	{
		if (kontroller.zugMoeglich(posx, posy, zielx, ziely) > 0)
			return kontroller.zug(posx, posy, zielx, ziely);
		else
			return false;
	}

	/**
	 * public float[] brettWert() { float[] ret = new float[2]; ret[0]=0;
	 * ret[1]=0; for(int i=0; i<8; i++) { for(int j=0; j<8; j++) {
	 * if(inhalt(i,j)>0) { ret[0]=ret[0]+wert(i,j); } else { if(inhalt(i,j)<0) {
	 * ret[1]=ret[1]-wert(i,j); } } } } return ret; }
	 */

	public int wert(Figur figur)
	{
		int inhalt = figur.nummer;
		if (inhalt == 8) // Switch case w�r besser
			return 100;
		else
		{
			if (inhalt == 2)
			{
				/*
				int counter = 0;
				for (int i = 0; i < 8; i++) // noch fehlerhaft
				{
					for (int j = 0; j < 8; j++)
					{
						if (figur.nummer == 2)
						{
							counter++;
							if (counter == 4)
							{
								// return 3.5F;
							}
						}
					}
				}
				*/
				return 300;
			} else
			{
				if (inhalt == 4)
				{
					return 300;
				} else
				{
					if (inhalt == 1)
						return 500;
					else
					{
						if (inhalt == 3)
							return 900;
					}
				}
			}
		}
		// System.out.println("das haette nicht passieren duerfen");
		return 0;
	}

	public byte player()
	{
		return kontroller.playerFaktor();
	}

	/**
	 * Pr�ft den wert des Spielfeldes ab
	 * -Obsolet
	 */
	public int wertPruefer()
	{
		int wert = 0;
		for (int i = 0; i < 8; i++)
		{
			for (int j = 0; j < 8; j++)
			{
				if (inhalt(i, j) == 8)
				{
					wert = wert + 1;
				}
				if (inhalt(i, j) == 2 || inhalt(i, j) == 4)
				{
					wert = wert + 3;
				}
				if (inhalt(i, j) == 1)
				{
					wert = wert + 5;
				}
				if (inhalt(i, j) == 3)
				{
					wert = wert + 9;
				}
				if (inhalt(i, j) == 16)
				{
					wert = wert + 10000000;
				}
				if (inhalt(i, j) == 0)
				{
					wert = wert + 0;
				}
				if (inhalt(i, j) == -8)
				{
					wert = wert + 1;
				}
				if (inhalt(i, j) == -2 || inhalt(i, j) == -4)
				{
					wert = wert + 3;
				}
				if (inhalt(i, j) == -1)
				{
					wert = wert + 5;
				}
				if (inhalt(i, j) == -3)
				{
					wert = wert + 9;
				}
				if (inhalt(i, j) == -16)
				{
					wert = wert + 10000000;
				}
			}
		}
		return wert;
	}

	public void zugAusgabe(int[] zug)
	{
		zugAusgabe(zug[0],zug[1],zug[2],zug[3]);
	}
	
	public void zugAusgabe(int posx, int posy, int zielx, int ziely)
	{
		kontroller.meldungAusgeben("[Zug]: Von " + intToChar(posx)+ (posy+1)+ " Auf " + intToChar(zielx) + (ziely+1));
	}
	
	private char intToChar(int feld)
	{
		return (char)('A' + feld);
	}

	// Hook zum aufrufen der KI
	public abstract boolean machZug();

	public int tauscheBauer()
	{
		return 4;
	}

	public void chat(String s) throws Exception
	{
	}
}
