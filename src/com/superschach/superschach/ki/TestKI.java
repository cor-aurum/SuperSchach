package com.superschach.superschach.ki;

import com.superschach.superschach.kontroller.KIKontroller;
import com.superschach.superschach.kontroller.Kontroller;
import com.superschach.superschach.kontroller.figuren.Figur;

/**
 * Springer= 3 Bauern Laeufer= 3 Bauern Turm = 5 Bauern Dame = 9 Bauern
 * 
 * @author Jan Hofmeier, Felix Sch&uuml;tze
 * @version 2.1
 */
public class TestKI extends KISchnittstelle
{
	final Kontroller kontroller;
	int[][] threadergebnisse;
	int[][][] ziele;
	short pfade[][][];
	int level = 3; // denkt level + 1 Z�ge vorraus

	/**
	 * Constructor for objects of class ZufallKI
	 */
	public TestKI(Kontroller kontroller, int schwierigkeit)
	{
		super(kontroller);
		level = schwierigkeit - 1;
		this.kontroller = kontroller;
	}

	public boolean machZug()
	{
		boolean ret = true;
		/**
		 * try{ db.senden(); } catch(Exception
		 * e){System.out.println("Senden Fehlgeschlagen");}; byte[] temp=new
		 * byte[4]; temp=db.empfangen();
		 * System.out.println("KI"+temp[0]+temp[1]+temp[2]+temp[3]);
		 * if(temp[0]==0 && temp[1]==0 && temp[2]==0 && temp[3]==0) {
		 */
		ret = hauptschleife();
		/**
		 * } else if(kontroller.zugMoeglich(temp[0],temp[1],temp[2],temp[3])> 0)
		 * { ret= hauptschleife(); } else { ret=
		 * zug(temp[0],temp[1],temp[2],temp[3]); } try{ db.senden(); }
		 * catch(Exception e){System.out.println("Senden Fehlgeschlagen");}
		 */
		return ret;
	}

	private boolean hauptschleife()
	{
		// testKontroller.aktualisieren=false;
		int[] posx = new int[30];
		int[] posy = new int[30];
		int[] zielx = new int[30];
		int[] ziely = new int[30];
		posx[0] = -1;
		posy[0] = -1;
		zielx[0] = -1;
		ziely[0] = -1;

		KIThread[][] kiThread = new KIThread[kontroller.XMax + 1][kontroller.YMax + 1];
		threadergebnisse = new int[kontroller.XMax + 1][kontroller.YMax + 1];
		ziele = new int[kontroller.XMax + 1][kontroller.YMax + 1][2];
		pfade = new short[kontroller.XMax + 1][kontroller.YMax + 1][4];
		//System.out.println(System.currentTimeMillis()-time);
		for (int i = 0; i <= kontroller.XMax; i++)
		{
			for (int j = 0; j <= kontroller.YMax; j++)
			{
				kiThread[i][j] = new KIThread();
				kiThread[i][j].setup(i, j);
				// kontroller.equals(kiThread[i][j].testKontroller);
				kiThread[i][j].start();

			}
		}

		boolean outofbounds = false;
		int ergebnis = -400000000;
		int moeglichkeit = 0;
		for (int i = 0; i <= kontroller.XMax; i++)
		{
			for (int j = 0; j <= kontroller.YMax; j++)
			{
				try
				{
					kiThread[i][j].join();
					if (threadergebnisse[i][j] >= ergebnis)
					{
						if (threadergebnisse[i][j] > ergebnis)
						{
							moeglichkeit = 0;
							outofbounds = false;
							ergebnis = threadergebnisse[i][j];
							posx[moeglichkeit] = i;
							posy[moeglichkeit] = j;
							zielx[moeglichkeit] = ziele[i][j][0];
							ziely[moeglichkeit] = ziele[i][j][1];
						} else
						{
							posx[moeglichkeit] = i;
							posy[moeglichkeit] = j;
							zielx[moeglichkeit] = ziele[i][j][0];
							ziely[moeglichkeit] = ziele[i][j][1];
							if ((moeglichkeit == posx.length - 1)
									|| outofbounds)
							{
								outofbounds = true;
								moeglichkeit = (int) (Math.random() * 30);
							} else
							{
								moeglichkeit++;
							}
						}
					}
				} catch (Exception e)
				{
					System.out.println(e);
					e.printStackTrace();
				}
			}
		}

		// System.out.println("Alles ausgewertet, versuche zug");
		// boolean ret=
		// zug(besterZug[0],besterZug[1],besterZug[2],besterZug[3]);
		boolean ret = false;
		// testKontroller.aktualisieren=true;
		if ((posx[0] >= 0) & (posy[0] >= 0) & (zielx[0] >= 0) & (ziely[0] >= 0))
		{
			int r;
			if (outofbounds)
				r = (int) (Math.random() * 30);
			else
				r = (int) (Math.random() * moeglichkeit);
			ret = zug(posx[r], posy[r], zielx[r], ziely[r]);
		}

		if (!ret)
			System.out.println("Fehlerli in der KI");

		/*
		 * if(wertPruefer()<=20) { level=6; }
		 */
		return ret;
	}

	public class KIThread extends Thread implements Runnable
	{
		int i;
		int j;
		final Figur[][] testFigur = new Figur[8][8];
		final Figur[][] figurListe = new Figur[2][16];
		final Figur[] testKoenig = new Figur[2];
		final KIKontroller testKontroller = new KIKontroller(kontroller,
				testFigur, testKoenig, figurListe);

		public void setup(int xalt, int yalt)
		{
			i = xalt;
			j = yalt;
		}

		public int inhalt(int x, int y)
		{
			return testKontroller.inhalt(x, y);
		}

		public void run()
		{
			int[] zielx = new int[30];
			int[] ziely = new int[30];
			zielx[0] = -1;
			ziely[0] = -1;
			boolean outofbounds = false;
			int ergebnis =Integer.MIN_VALUE;
			int moeglichkeit = 0;

			short pfadx = 0;
			short pfady = 0;
			short pfadzx = 0;
			short pfadzy = 0;

			//KIKontroller verKontroller;
			//if (Kontroller.debug)
			//	verKontroller = new KIKontroller(testKontroller);
			for (int k = 0; k <= kontroller.XMax; k++)
			{
				for (int l = 0; l <= kontroller.YMax; l++)
				{
					int neuergebnis = versucheZug(i, j, k, l, level);
					if (neuergebnis >= ergebnis)
					{
						if (neuergebnis > ergebnis)
						{
							moeglichkeit = 0;
							outofbounds = false;
							ergebnis = neuergebnis;
							zielx[moeglichkeit] = k;
							ziely[moeglichkeit] = l;
						} 
						else
						{
							zielx[moeglichkeit] = k;
							ziely[moeglichkeit] = l;

							if ((moeglichkeit == 29) || outofbounds)
							{
								outofbounds = true;
								moeglichkeit = (int) (Math.random() * 30);
							} else
							{
								moeglichkeit++;
							}
						}
						ergebnis = neuergebnis;
						long stelle = (long) Math.pow(10, level);
						pfadx = (short) (pfade[this.i][this.j][0] + i * stelle);
						pfady = (short) (pfade[this.i][this.j][1] + j * stelle);
						pfadzx = (short) (pfade[this.i][this.j][2] + k * stelle);
						pfadzy = (short) (pfade[this.i][this.j][3] + l * stelle);

						pfade[this.i][this.j][0] = 0;
						pfade[this.i][this.j][1] = 0;
						pfade[this.i][this.j][2] = 0;
						pfade[this.i][this.j][3] = 0;
					}
				}
			}
			if ((zielx[0] >= 0) & (ziely[0] >= 0))
			{
				int r;
				if (outofbounds)
					r = (int) (Math.random() * 30);
				else
					r = (int) (Math.random() * moeglichkeit);
				// ret=zug(posx[r],posy[r],zielx[r],ziely[r]);
				ziele[i][j][0] = zielx[r];
				ziele[i][j][1] = ziely[r];
				threadergebnisse[i][j] = ergebnis;
				pfade[this.i][this.j][0] = pfadx;
				pfade[this.i][this.j][1] = pfady;
				pfade[this.i][this.j][2] = pfadzx;
				pfade[this.i][this.j][3] = pfadzy;
			}
		}

		private int teste(int zuege) // direkter ungepr�fter zugriff aus
										// Performence Gr�nden, wenn Kontroller
										// sauber Programmiert ist sollte nichts
										// schief gehen...
		{
			zuege--;
			short pfadx = 0;
			short pfady = 0;
			short pfadzx = 0;
			short pfadzy = 0;
			int ergebnis = -10000;
			testKontroller.togglePlayer();
			for (int i = 0; i <= kontroller.XMax; i++)
			{
				for (int j = 0; j <= kontroller.YMax; j++)
				{
					for (int k = 0; k <= kontroller.XMax; k++)
					{
						for (int l = 0; l <= kontroller.YMax; l++)
						{
							int neuergebnis = versucheZug(i, j, k, l, zuege);
							if (neuergebnis > 3000000)
							{
								if (neuergebnis == 4000000)
								{
									i = 10;
									j = 10;
									k = 10;
									l = 10;
								} else
								{
									neuergebnis = neuergebnis - 10;
								}
							}
							// neuergebnis = - neuergebnis;
							if (neuergebnis > ergebnis)
							{
								ergebnis = neuergebnis;
								long stelle = (long) Math.pow(10, zuege);
								pfadx = (short) (pfade[this.i][this.j][0] + i
										* stelle);
								pfady = (short) (pfade[this.i][this.j][1] + j
										* stelle);
								pfadzx = (short) (pfade[this.i][this.j][2] + k
										* stelle);
								pfadzy = (short) (pfade[this.i][this.j][3] + l
										* stelle);

								pfade[this.i][this.j][0] = 0;
								pfade[this.i][this.j][1] = 0;
								pfade[this.i][this.j][2] = 0;
								pfade[this.i][this.j][3] = 0;
							}
						}
					}
				}
			}
			if (ergebnis <= -10000)
				if (testKontroller.istSchach())
					ergebnis = -4000000;
			testKontroller.togglePlayer();

			pfade[this.i][this.j][0] = pfadx;
			pfade[this.i][this.j][1] = pfady;
			pfade[this.i][this.j][2] = pfadzx;
			pfade[this.i][this.j][3] = pfadzy;

			return ergebnis;
		}

		private int versucheZug(int i, int j, int k, int l, int zuege)
		{
			int neuergebnis = Integer.MIN_VALUE;
			int z = testKontroller.zugMoeglich(i, j, k, l);
			if (z > 0)
			{
				neuergebnis = 0;
				if (z == 2)
				{
					neuergebnis = wert(testFigur[k][l]);
				}
				if (zuege > 0)
				{
					if ((z <= 2)
							&& (!(((l == 0) || (kontroller.YMax == l)) && (inhalt(
									i, j) == 8))))
					{
						Figur ziel = testFigur[k][l];
						int bewegt = testFigur[i][j].bewegt();
						testFigur[i][j].versetzen(k, l);
						neuergebnis -= teste(zuege);
						testFigur[k][l].versetzen(i, j); // r�ckg�ngig
															// machen
						testFigur[i][j].setzeBewegt(bewegt); // bewegt
																// wiederherstellen
						// testFigur[k][l]=ziel;
						if (ziel != null)
						{
							testFigur[k][l] = ziel;
							figurListe[(1 - ziel.gebePlayer()) / 2][ziel
									.gebeIndex()] = ziel;
						}
					} else
					{
						testKontroller.speicherVerlauf();
						if (z == 3)
						{
							testFigur[i][j].rochade(k, l);
						} else
						{
							testFigur[i][j].zug(k, l);
							if (((l == 0) || (kontroller.YMax == l))
									&& (inhalt(k, l) == 8))
								testKontroller.machDame(k, l);
							neuergebnis += 900;
						}
						//neuergebnis -= teste(zuege);
						testKontroller.ladeVerlauf();
						testKontroller.togglePlayer();
					}
				}
			}
			return neuergebnis;
		}
	}
}