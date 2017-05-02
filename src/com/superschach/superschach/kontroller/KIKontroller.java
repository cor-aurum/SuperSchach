package com.superschach.superschach.kontroller;
import com.superschach.superschach.kontroller.figuren.*;

public class KIKontroller extends Kontroller
{
    public KIKontroller(Figur[][] figur,Figur[] koenig)
    {
        super(figur,koenig);
        this.koenig=koenig;
    }

    public KIKontroller(Kontroller kontroller, Figur[][] figur,Figur[] koenig, Figur[][] figurListe)
    {
        super(figur,koenig);
        this.figurListe=figurListe;
        copy(kontroller);
    }
    
    public KIKontroller(Kontroller kontroller)
    {
        super((byte)(kontroller.XMax+1),(byte)(kontroller.YMax+1));
        copy(kontroller);
    }
    
    private int copy(Kontroller kontroller)
    {
        int ret=0;
        for(int i=0; i<=XMax; i++)
        {
            for(int j=0; j<=YMax; j++)
            {
                int z=kontroller.inhalt(i,j);
                int f=1;
                setPlayer((byte) 0);
                if (z<0)
                {
                    setPlayer((byte) 1);
                    f=-1;
                    z=z*f;
                }
                if(z>0)
                {
                    ret++;
                    if (z==8)
                    {
                        machBauer(i,j);
                        if(kontroller.wurdeBewegt(i,j))
                            figur[i][j].setzeBewegt(1);
                    }
                    else
                    {
                        if (z==1)
                        {
                            machTurm(i,j);
                            if(kontroller.wurdeBewegt(i,j))
                                figur[i][j].setzeBewegt(1);
                        }
                        else
                        {
                            if (z==4)
                            {
                                machSpringer(i,j);
                            }
                            else
                            {
                                if (z==2)
                                {
                                    machLaeufer(i,j);
                                }
                                else
                                {
                                    if (z==3)
                                    {
                                        machDame(i,j);
                                    }
                                    else
                                    {
                                        if (z==16)
                                        {
                                            machKoenig(i,j);
                                            if(kontroller.wurdeBewegt(i,j))
                                                figur[i][j].setzeBewegt(1);
                                        }
                                        else
                                        {
                                            if(z==6)
                                            {
                                                machJanus(i,j);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if(f==-1)
                    {
                        togglePlayer();
                    }
                }
                else
                {
                    figur[i][j]=null;
                }
            }
        }
        setPlayer(kontroller.getPlayer());
        System.arraycopy(kontroller.letzterZug, 0, this.letzterZug, 0, letzterZug.length);
        return ret;
    }
}
