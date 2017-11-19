package com.superschach.superschach.ki;

import com.superschach.superschach.kontroller.Kontroller;

/**
 * 
 * @author Felix Sch&uuml;tze
 * @version (a version number or a date)
 */
public class ZufallKI implements KI
{
	// instance variables - replace the example below with your own

	// Pruefer pruefer;
	/**
	 * Constructor for objects of class ZufallKI
	 */
	public ZufallKI()// ,Pruefer pruefer)
	{
	}

	public void zug(Kontroller kontroller, Zug zug) throws Exception
	{
		long time = System.currentTimeMillis();
		boolean erfolg = false;
		int xa = 0;
		int ya = 0;
		int xn = 0;
		int yn = 0;
		int ccc = 0;
		while (!erfolg)
		{
			xa = 0;
			ya = 0;
			xn = 0;
			yn = 0;
			// jetzt sollte er auch als weiß spielen können
			do
			{
				xa = (int) (Math.random() * 8);
				ya = (int) (Math.random() * 8);
			} while (kontroller.inhaltFaktor(xa, ya) <= 0);
			for (int counter = 0; counter < 100; counter++)
			{
				if (!erfolg)
				{
					xn = (int) (Math.random() * 8);
					yn = (int) (Math.random() * 8);
					erfolg = kontroller.zugMoeglich(xa, ya, xn, yn) > 0;
					ccc++;
				}
				if (ccc == 10000) // hier gibt er schneller auf
				{
					if (kontroller.keinZugMoeglich()) // aber auch nur wenn er
														// wirklich keinen zug
														// mehr machen kann
					{
						return;
					}
				}
				if (ccc >= 1000000000) // das dauert viel zu lange...
				{
					return;
				}
			}
		}
		long wait = 50 + time - System.currentTimeMillis();
		if (wait > 0)
		{
			//Thread.sleep(wait);
		}
		zug.zug(xa, ya, xn, yn);
	}

	public void tellMatt(Kontroller spiel)
	{
		// TODO Auto-generated method stub
		
	}
}
