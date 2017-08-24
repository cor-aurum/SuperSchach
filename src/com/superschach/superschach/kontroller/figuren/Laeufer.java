package com.superschach.superschach.kontroller.figuren;
import com.superschach.superschach.kontroller.Kontroller;
import com.superschach.superschach.kontroller.Pruefer;
/**
 * Write a description of class Laeufer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Laeufer extends Figur
{
    Kontroller kontroller;
    Pruefer pruefer;
    public Laeufer(Kontroller kontroller, int x, int y,byte faktorplayer, Pruefer pruefer, int index)
    {
        super(kontroller,x,y,2,faktorplayer, index);
        this.kontroller=kontroller;
        this.pruefer=pruefer;
    }

    public boolean zugMoeglich(int x, int y)
    {
        return ((kontroller.inhalt(x,y)*vorzeichen()<=0)&&(pruefer.diagonal(gebePosX(),gebePosY(),x,y))&&pruefer.frei(gebePosX(),gebePosY(),x,y));
    }

    public int moeglicheZiele()
    {
        byte[][] tmp=new byte[14][2];
        int z=0;
        //Jaja dashier geht auch mit Schleifen, das w�r aber nicht so perfomant...
        byte x=(byte)gebePosX();
        byte y=(byte)gebePosY();

        for(byte i=(byte)(x+1); i<=7; i++)
        {
            if(kontroller.inhalt(i,y)<=0)
            {
                tmp[z][0]=i;
                tmp[z++][1]=y;
                //z++;
            }
            else
                break;
        }

        for(byte i=(byte)(x-1); i>=0; i--)
        {
            if(kontroller.inhalt(i,y)<=0)
            {
                tmp[z][0]=i;
                tmp[z++][1]=y;
                //z++;
            }
            else
                break;
        }

        for(byte i=(byte)(y+1); i<=7; i++)
        {
            if(kontroller.inhalt(x,i)<=0)
            {
                tmp[z][0]=x;
                tmp[z++][1]=i;
                //z++;
            }
            else
                break;
        }

        for(byte i=(byte)(y-1); i>=0; i--)
        {
            if(kontroller.inhalt(x,i)<=0)
            {
                tmp[z][0]=x;
                tmp[z++][1]=i;
                //z++;
            }
            else
                break;
        }

        return 0;
    }
    
    public String toString()
    {
        return "L�ufer";
    }

	@Override
	public char getCode() {
		return 'b';
	}
}
