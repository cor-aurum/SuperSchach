package com.superschach.superschach.kontroller.figuren;
import com.superschach.superschach.kontroller.Kontroller;
import com.superschach.superschach.kontroller.Pruefer;
/**
 * Write a description of class Springer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Springer extends Figur
{
    Kontroller kontroller;
    Pruefer pruefer;
    public Springer(Kontroller kontroller, int x, int y,byte faktorplayer, Pruefer pruefer, int index)
    {
        super(kontroller,x,y,4,faktorplayer, index);
        this.kontroller=kontroller;
        this.pruefer=pruefer;
    }

    

    public boolean zugMoeglich(int x, int y)
    {
        return (pruefer.springer(gebePosX(),gebePosY(),x,y))&(kontroller.inhalt(x,y)*vorzeichen()<=0);
    }

    public int moeglicheZiele()
    {
        byte[][] tmp=new byte[8][2];
        int z=0;
        //Jaja dashier geht auch mit Schleifen, das w�r aber nicht so perfomant...
        byte x,y;

        x=(byte)(gebePosX()-1); y=(byte)(gebePosY()-2);
        if(x>=0)
        {
            if(y>=0)
                if(kontroller.inhalt(x,y)<=0)
                {
                    tmp[z][0]=x;
                    tmp[z++][1]=y;
                    //z++;
            }
            y=(byte)(gebePosY()+2);
            if(y<=7)
                if(kontroller.inhalt(x,y)<=0)
                {
                    tmp[z][0]=x;
                    tmp[z++][1]=y;
                    //z++;
            }
            x=(byte)(gebePosX()-2); y=(byte)(gebePosY()-1);
            if(x>=0)
            {
                if(y>=0)
                    if(kontroller.inhalt(x,y)<=0)
                    {
                        tmp[z][0]=x;
                        tmp[z++][1]=y;
                        //z++;
                }
                y=(byte)(gebePosY()+1);
                if(y<=7)
                    if(kontroller.inhalt(x,y)<=0)
                    {
                        tmp[z][0]=x;
                        tmp[z++][1]=y;
                        //z++;
                }
            }
        }

        x=(byte)(gebePosX()+1); y=(byte)(gebePosY()-2);
        if(x<=7)
        {
            if(y>=0)
                if(kontroller.inhalt(x,y)<=0)
                {
                    tmp[z][0]=x;
                    tmp[z++][1]=y;
                    //z++;
            }
            y=(byte)(gebePosY()+2);
            if(y<=7)
                if(kontroller.inhalt(x,y)<=0)
                {
                    tmp[z][0]=x;
                    tmp[z++][1]=y;
                    //z++;
            }
            x=(byte)(gebePosX()+2); y=(byte)(gebePosY()-1);
            if(x<=7)
            {
                if(y>=0)
                    if(kontroller.inhalt(x,y)<=0)
                    {
                        tmp[z][0]=x;
                        tmp[z++][1]=y;
                        //z++;
                }
                y=(byte)(gebePosY()+1);
                if(y<=7)
                    if(kontroller.inhalt(x,y)<=0)
                    {
                        tmp[z][0]=x;
                        tmp[z++][1]=y;
                        //z++;
                }
            }
        }
        
        return 0;
    }
    
    public String toString()
    {
        return "Springer";
    }
}

