package com.superschach.superschach.kontroller.figuren;

import com.superschach.superschach.kontroller.Kontroller;
import com.superschach.superschach.kontroller.Pruefer;

/**
 * Write a description of class Koenig here.
 * 
 * @author (Felix Sch&uuml;tze)
 * @version (a version number or a date)
 */
public class Koenig extends Figur {
	Kontroller kontroller;
	Pruefer pruefer;

	public Koenig(Kontroller kontroller, int x, int y, byte faktorplayer, Pruefer pruefer, int index) {
		super(kontroller, x, y, 16, faktorplayer, index);
		this.kontroller = kontroller;
		this.pruefer = pruefer;
	}

	public void zug(int x, int y) {
		if (zugMoeglich(x, y)) {
			versetzen(x, y);
		} else {
			if (rochadeMoeglich(x, y)) {
				/*
				 * if(x==gebePosX()+2) { kontroller.verschiebe(7,y,5,y);
				 * //kontroller.figur[x+1][y].versetzen(x-1,y); } else {
				 * kontroller.verschiebe(0,y,3,y); //kontroller.figur[x-2][y].versetzen(x+1,y);
				 * } versetzen(x,y);
				 */
				rochade(x, y);
			}
		}
		// return ret;
	}

	public void rochade(int x, int y) {
		if (x == gebePosX() + 2) {
			kontroller.zug(7, y, 5, y);
			// kontroller.figur[x+1][y].versetzen(x-1,y);
		} else {
			kontroller.zug(0, y, 3, y);
			// kontroller.figur[x-2][y].versetzen(x+1,y);
		}
		kontroller.togglePlayer();
		versetzen(x, y);
	}

	public boolean zugMoeglich(int x, int y) {
		boolean ret = false;
		if ((((pruefer.diagonal(gebePosX(), gebePosY(), x, y)) || (pruefer.gerade(gebePosX(), gebePosY(), x, y)))
				&& (kontroller.inhalt(x, y) * vorzeichen() <= 0) && pruefer.diffEins(gebePosX(), gebePosY(), x, y))) {
			ret = true;
		}
		return ret;
	}

	public boolean rochadeMoeglich(int x, int y) {
		boolean ret = false;
		if ((!wurdeBewegt()) && (gebePosY() == y)) {
			if (x == gebePosX() + 2) // Kleine Rochade
			{
				// if((kontroller.inhalt(7,gebePosY())*kontroller.playerFaktor())==1)
				// {
				if (kontroller.inhalt(gebePosX() + 2, gebePosY()) == 0) {
					if (kontroller.inhalt(gebePosX() + 1, gebePosY()) == 0) {
						if (!kontroller.wurdeBewegt(7, y)) {
							if (!(pruefer.istSchach() || pruefer.istBedroht(5, y) || pruefer.istBedroht(6, y))) {
								ret = true;
							}
						}
					}
				}
				// }
			} else {
				if (x == gebePosX() - 2) // Gro�e Rochade
				{
					// if(kontroller.inhalt(gebePosX()-4,gebePosY())*kontroller.playerFaktor()==1)
					// {
					if (!kontroller.wurdeBewegt(0, y)) {
						if ((kontroller.inhalt(1, y) == 0) && (kontroller.inhalt(2, y) == 0)
								&& (kontroller.inhalt(3, y) == 0)) {
							if (!(pruefer.istSchach() || pruefer.istBedroht(2, y) || pruefer.istBedroht(3, y)))

							{
								ret = true;
							}
						}
					}
					// }
				}
			}

		}
		return ret;
	}

	public boolean freiUnbedroht(int von, int biskleiner) {
		for (int i = von; i < biskleiner; i++) {
			if (kontroller.inhalt(i, gebePosY()) != 0 || pruefer.istBedroht(i, gebePosY())) {
				return false;
			}
		}
		return true;
	}

	public String toString() {
		return "König";
	}

	@Override
	public char getCode() {
		return 'k';
	}

	@Override
	public int getWert() {
		return 100000*vorzeichen();
	}
}
