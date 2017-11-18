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
    
    public String toString()
    {
        return "Läufer";
    }

	@Override
	public char getCode() {
		return 'b';
	}

	@Override
	public int getWert() {
		return 3*vorzeichen();
	}
}
