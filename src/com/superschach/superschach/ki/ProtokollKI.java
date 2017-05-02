package com.superschach.superschach.ki;

import java.io.BufferedReader;
import java.io.FileReader;

import com.superschach.superschach.kontroller.Kontroller;
import com.superschach.superschach.spiel.AbstractGUI;
import com.superschach.superschach.spiel.Spiel;

public class ProtokollKI implements KI
{
	Spiel spiel;
	String p = "";
	String[] protokoll;
	//int zaehler = 0;

	public ProtokollKI(Spiel spiel)
	{
		this.spiel=spiel;
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(spiel.getFile()));
			String s;
			reader.readLine();
			while ((s = reader.readLine()) != null)
			{
				p = p + " " + s;
			}
			reader.close();
		} catch (Exception e)
		{
			spiel.nachricht(AbstractGUI.meldung("protokollUngueltig"));
		}

		String[] pro = p.split("\\.");
		// System.out.println(pro[1]+pro[pro.length-1]);
		p = "";
		for (int i = 0; i < pro.length - 1; i++)
		{
			p = p + pro[i].substring(0, pro[i].length() - 1);
		}
		p = p + pro[pro.length - 1];
		System.out.println(p);
		p = p.substring(1);
		protokoll = p.split(" ");
	}

	// spiel ist hier null
	public void zug(Kontroller k, Zug zug) throws Exception
	{
		int zaehler=spiel.gebeZugAnz();
		if (zaehler < protokoll.length)
		{
			System.out.println(protokoll[zaehler]);
			String[] koo = protokoll[zaehler].split("-");
			int ax;
			int ay;
			int nx;
			int ny;
			int figur = 0;
			//spiel.chaterhalten(protokoll[zaehler]);
			if (Character.isLetter(koo[0].charAt(1)))
			{
				figur = koo[0].charAt(0) == 'T' ? 1
						: koo[0].charAt(0) == 'L' ? 2
								: koo[0].charAt(0) == 'D' ? 3 : koo[0]
										.charAt(0) == 'S' ? 4 : koo[0]
										.charAt(0) == 'J' ? 6 : koo[0]
										.charAt(0) == 'K' ? 16 : 0;
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
					// spiel.meldungAusgeben("Zug ausgef�hrt");
				} else
				{
					spiel.nachricht(AbstractGUI.meldung("protokollUngueltig"));
				}
				zug.zug(ax, ay, nx, ny);
				zug.tausch(figur);				
			} catch (Exception e)
			{
				spiel.nachricht(AbstractGUI.meldung("protokollUngueltig"));
			}
			//zaehler++;
		} else
		{
			spiel.nachricht(AbstractGUI.meldung("protokollFertig"));
		}
	}

	@Override
	public void tellMatt(Kontroller spiel)
	{
		// TODO Auto-generated method stub
		
	}
}
