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

    public String toString()
    {
        return "Springer";
    }



	@Override
	public char getCode() {
		return 'n';
	}



	@Override
	public int getWert() {
		return 3*vorzeichen();
	}
}

