package com.superschach.superschach.kontroller.figuren;

import com.superschach.superschach.kontroller.Kontroller;
import com.superschach.superschach.kontroller.Pruefer;

public class JanusKoenig extends Koenig
{
	public JanusKoenig(Kontroller kontroller, int x, int y, byte faktorplayer,
			Pruefer pruefer, int index)
	{
		super(kontroller, x, y, faktorplayer, pruefer, index);
	}

	public boolean rochadeMoeglich(int x, int y)
	{
		boolean ret = false;
		if ((!wurdeBewegt()) && (gebePosY() == y))
		{
			if (x == kontroller.XMax - 1) // Kleine Rochade
			{
				if (!kontroller.wurdeBewegt(kontroller.XMax, y))
				{
					if (freiUnbedroht(gebePosX() + 1, kontroller.XMax))
					{
						if (!pruefer.istSchach())
						{
							return true;
						}
					}
				}
				return false;
			} else
			{
				if (x == 1) // Gro�e Rochade
				{
					if (!kontroller.wurdeBewegt(kontroller.XMax, y))
					{
						if (freiUnbedroht(1, gebePosX()))
						{
							if (!pruefer.istSchach())
							{
								return true;
							}
						}
					}
				}
				return false;
			}
		}
		return ret;
	}

	public void rochade(int x, int y)
	{
		if (x > gebePosX())
		{
			kontroller.zug(kontroller.XMax, y, x-1, y);
			// kontroller.figur[x+1][y].versetzen(x-1,y);
		} else
		{
			kontroller.zug(0, y,x+1, y);
			// kontroller.figur[x-2][y].versetzen(x+1,y);
		}
		kontroller.togglePlayer();
		versetzen(x, y);
	}
}