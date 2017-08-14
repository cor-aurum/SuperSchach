package com.superschach.superschach.kontroller;

import com.superschach.superschach.gui.GUI;
import com.superschach.superschach.kontroller.figuren.Figur;

/**
 * Write a description of class Pr�fer here.
 * 
 * @author (your name)
 * @version (a version number or a date)
 */
public class Pruefer
{
	private Kontroller kontroller;
	private Figur[][] figur;
	private Figur[][] figurListe;

	public Pruefer(Kontroller kontroller, Figur[][] figur, Figur[][] figurListe)
	{
		this.kontroller = kontroller;
		this.figurListe = figurListe;
		this.figur = figur;
	}

	public boolean gerade(int xa, int ya, int xb, int yb)
	{
		return ((xa == xb) || (ya == yb));
	}

	public boolean frei(int xa, int ya, int xb, int yb)
	{
		boolean ret = true;
		byte fx = 1;
		byte fy = 1;
		if (xb < xa)
		{
			fx = -1;
		}
		if (yb < ya)
		{
			fy = -1;
		}
		if (xa == xb)
		{
			for (int j = 1; j < ((yb - ya) * fy); j++)
			{
				if (kontroller.inhalt(xa, ya + j * fy) != 0)
				{
					ret = false;
				}
			}
		} else
		{
			if (ya == yb)
			{
				for (int j = 1; j < ((xb - xa) * fx); j++)
				{
					if (kontroller.inhalt(xa + j * fx, ya) != 0)
					{
						ret = false;
					}
				}
			} else
			{
				if (diagonal(xa, ya, xb, yb))
				{
					for (int i = 1; i < ((xb - xa) * fx); i++)
					{
						if (kontroller.inhalt(xa + i * fx, ya + i * fy) != 0)
							ret = false;
					}
				} else
				{
					ret = false;
				}
			}
		}
		return ret;
	}

	public boolean diagonal(int xa, int ya, int xb, int yb)
	{
		/*
		 * int xdiff=xb-xa; return ((xdiff==(yb-ya))||(xdiff==(ya-yb)));
		 */
		return Math.abs(xb - xa) == Math.abs(ya - yb);
	}

	public void aktualisiereFigur(Figur[][] figur)
	{
		this.figur = figur;
	}

	public boolean diffEins(int xa, int ya, int xb, int yb)
	{
		/*
		 * boolean ret=false; for(int i=-1; i<=1; i=i+2) { for(int j=-1; j<=1;
		 * j=j+2) {
		 * if((((xb-xa)*i)==1)|(((yb-ya)*j)==1))//|(((xb-xa)*j)==0)|(((yb
		 * -ya)*j)==0)) { return true; } } } return ret;
		 */
		int xdiff = xa - xb;
		int ydiff = ya - yb;
		return (!(xa == xb && ya == yb))
				&& (xdiff == 1 || xdiff == -1 || ydiff == 1 || ydiff == -1);
	}

	public boolean vorne(int ya, int yb)
	{
		return (((yb - ya) * kontroller.playerFaktor()) > 0);
	}

	public boolean istSchach()
	{
		return istBedroht(kontroller.koenigPosX(), kontroller.koenigPosY());
	}

	/*
	 * public boolean istSchachFelix() { boolean ret=false; byte
	 * player=kontroller.playerFaktor(); int koenigx=kontroller.koenigPosX();
	 * int koenigy=kontroller.koenigPosY();
	 * 
	 * if(kontroller.inhalt(figur[koenigx][koenigy].geradeFreiYPlus(),koenigy)==
	 * 
	 * }
	 */

	public boolean istSchachNeu()
	{
		return istBedrohtNeu(kontroller.koenigPosX(), kontroller.koenigPosY(),
				1 - kontroller.getPlayer());
	}

	public boolean istBedrohtNeu(int koenigx, int koenigy, int player)
	{
		for (short i = 0; i < figurListe[player].length; i++)
		{
			if (figurListe[player][i] != null)
			{
				if (figurListe[player][i].zugMoeglich(koenigx, koenigy))
				{
					return true;
				}
			}
		}
		return false;
	}

	public boolean istBedrohtdgjk(int x, int y)
	{
		kontroller.togglePlayer();
		// Bauer
		if ((((x + 1) <= kontroller.XMax) && kontroller.inhaltFaktor(x + 1, y
				- kontroller.playerFaktor()) == 8)
				|| ((x < 0) && kontroller.inhaltFaktor(x - 1,
						y - kontroller.playerFaktor()) == 8))
		{
			kontroller.togglePlayer();
			return true;
		}

		// Koenig
		for (int i = -1; i <= 1; i++)
			for (int j = -1; j <= 1; j++)
			{
				if (kontroller.inhaltFaktor(x + i, y + j) == 16)
				{
					kontroller.togglePlayer();
					return true;
				}
			}

		// Ohne schleifen aus performance Gr�nden
		if (((kontroller.inhaltFaktor(x + 2, y + 1) & 4) == 4) // Springer
				|| ((kontroller.inhaltFaktor(x + 2, y - 1) & -2147483644) == 4)
				|| ((kontroller.inhaltFaktor(x - 2, y + 1) & -2147483644) == 4)
				|| ((kontroller.inhaltFaktor(x - 2, y - 1) & -2147483644) == 4)
				|| ((kontroller.inhaltFaktor(x + 1, y + 2) & -2147483644) == 4)
				|| ((kontroller.inhaltFaktor(x + 1, y - 2) & -2147483644) == 4)
				|| ((kontroller.inhaltFaktor(x - 1, y + 2) & -2147483644) == 4)
				|| ((kontroller.inhaltFaktor(x - 1, y - 2) & -2147483644) == 4))
		{
			kontroller.togglePlayer();
			return true;
		}

		// Turm
		for (int i = x + 1; i <= kontroller.XMax; i++)
		{
			int inhalt = kontroller.inhaltFaktor(i, y);
			if (inhalt != 0)
			{
				if ((inhalt & -Integer.MAX_VALUE) == 1)
				{
					kontroller.togglePlayer();
					return true;
				}
				break;
			}
		}
		for (int i = x - 1; i >= 0; i--)
		{
			int inhalt = kontroller.inhaltFaktor(i, y);
			if (inhalt != 0)
			{
				if ((inhalt & -Integer.MAX_VALUE) == 1)
				{
					kontroller.togglePlayer();
					return true;
				}
				break;
			}
		}
		for (int i = y + 1; i <= kontroller.YMax; i++)
		{
			int inhalt = kontroller.inhaltFaktor(x, i);
			if (inhalt != 0)
			{
				if ((inhalt & -Integer.MAX_VALUE) == 1)
				{
					kontroller.togglePlayer();
					return true;
				}
				break;
			}
		}
		for (int i = y - 1; i >= 0; i--)
		{
			int inhalt = kontroller.inhaltFaktor(x, i);
			if (inhalt != 0)
			{
				if ((inhalt & -Integer.MAX_VALUE) == 1)
				{
					kontroller.togglePlayer();
					return true;
				}
				break;
			}
		}

		// L�ufer
		int randx = (kontroller.XMax - x <= kontroller.YMax - y ? kontroller.XMax
				: kontroller.XMax - (kontroller.YMax - y));
		int tx = x;
		int ty = y;
		while (tx < randx)
		{
			int inhalt = kontroller.inhaltFaktor(++tx, ++ty);
			if (inhalt != 0)
			{
				if ((inhalt & -2147483646) == 2)
				{
					kontroller.togglePlayer();
					return true;
				}
				break;
			}
		}

		randx = ((kontroller.XMax - x) <= (y) ? kontroller.XMax : (x + y));
		tx = x;
		ty = y;
		while (tx < randx)
		{
			int inhalt = kontroller.inhaltFaktor(++tx, --ty);
			if (inhalt != 0)
			{
				if ((inhalt & -2147483646) == 2)
				{
					kontroller.togglePlayer();
					return true;
				}
				break;
			}
		}

		randx = (x <= kontroller.YMax - y ? 0 : x - (kontroller.YMax - y));
		tx = x;
		ty = y;
		while (tx > randx)
		{
			int inhalt = kontroller.inhaltFaktor(--tx, ++ty);
			if (inhalt != 0)
			{
				if ((inhalt & -2147483646) == 2)
				{
					kontroller.togglePlayer();
					return true;
				}
				break;
			}
		}

		randx = ((x) <= (y) ? 0 : (x - y));
		tx = x;
		ty = y;
		while (tx > randx)
		{
			int inhalt = kontroller.inhaltFaktor(--tx, --ty);
			if (inhalt != 0)
			{
				if ((inhalt & -2147483646) == 2)
				{
					kontroller.togglePlayer();
					return true;
				}
				break;
			}
		}
		kontroller.togglePlayer();
		return false;
	}

	public boolean istBedroht(int koenigx, int koenigy)
	{
		int player = ((1 - kontroller.getPlayer()) * 2) - 1;
		for (byte i = 0; i < figur.length; i++)
		{
			for (byte j = 0; j < figur[i].length; j++)
			{

				if (figur[i][j] != null)
				{
					if ((figur[i][j].gebePlayer() != player)
							&& figur[i][j].zugMoeglich(koenigx, koenigy))
					{
						return true;
					}
				}

			}
		}
		return false;
	}

	public boolean keinZugMoeglich()
	{
		// boolean ret=true;
		for (int i = 0; i < figur.length; i++)
		{
			for (int j = 0; j < figur[i].length; j++)
			{
				for (int k = 0; k < figur.length; k++)
				{
					for (int l = 0; l < figur[i].length; l++)
					{
						if (kontroller.zugMoeglich(i, j, k, l) > 0)// ausSchach(i,
																	// j, k, l))
						{
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	public boolean remis()
	{
		for (int i = 0; i < figur.length; i++)
		{
			for (int j = 0; j < figur[i].length; j++)
			{
				if ((figur[i][j] != null) && (figur[i][j].gebeTyp() != 5)
						&& (figur[i][j].gebeTyp() != -5))
					return false;
			}
		}
		return true;
	}

	public boolean ausSchach(int posx, int posy, int zielx, int ziely)
	{
		boolean ret = false;
		if (figur[posx][posy] != null)
		{
			if ((kontroller.inhaltFaktor(posx, posy) > 0))
			{
				boolean swap = kontroller.aktualisieren;
				boolean zweitesziel = false;
				kontroller.aktualisieren = false;
				Figur ziel = figur[zielx][ziely];
				Figur ziel2 = null;
				if (((ziely == 2) && (kontroller.inhalt(posx, posy) == -8))
						|| ((ziely == kontroller.YMax - 2) && (kontroller
								.inhalt(posx, posy) == 8)))
				{
					ziel2 = figur[zielx][ziely
							- kontroller.vorzeichen(posx, posy)];
					zweitesziel = true;
				}
				int bewegt = figur[posx][posy].bewegt();
				figur[posx][posy].versetzen(zielx, ziely);

				if (!istSchach())
				{
					ret = true;
				}
				if (figur[zielx][ziely] == null)
					GUI.logger.debug(posx + " " + posy + " " + zielx + " "
							+ ziely);
				figur[zielx][ziely].versetzen(posx, posy); // r�ckg�ngig
															// machen
				figur[posx][posy].setzeBewegt(bewegt); // bewegt
														// wiederherstellen
				if (ziel != null)
				{
					figur[zielx][ziely] = ziel;
					figurListe[(1 - ziel.gebePlayer()) / 2][ziel.gebeIndex()] = ziel;
				}
				if (zweitesziel)// ziel2!=null&&(((ziely==2)&&(kontroller.inhalt(posx,posy)==-8))||((ziely==5)&&(kontroller.inhalt(posx,posy)==8))))
				{
					figur[zielx][ziely - kontroller.vorzeichen(posx, posy)] = ziel2;
					if (ziel2 != null)
						figurListe[(1 - ziel2.gebePlayer()) / 2][ziel2
								.gebeIndex()] = ziel2;
				}
				kontroller.aktualisieren = swap;
			}

		}

		return ret;
	}

	public boolean springer(int xa, int ya, int xb, int yb) // das kann man
															// bestimmt noch
															// optimieren
	{
		boolean ret = false;
		for (int i = -1; i <= 1; i = i + 2)
		{
			for (int j = -1; j <= 1; j = j + 2)
			{
				if (((((xb - xa) * i) == 2) && (((yb - ya) * j) == 1))
						|| ((((xb - xa) * i) == 1) && (((yb - ya) * j) == 2)))
				{
					return true;
				}
			}
		}
		return ret;
	}
}
