package com.superschach.superschach.kontroller.figuren;
import com.superschach.superschach.kontroller.Kontroller;
import com.superschach.superschach.kontroller.Pruefer;

/**
 * Write a description of class Turm here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Turm extends Figur
{ 
    Kontroller kontroller;
    Pruefer pruefer;
    public byte[][] ziele=new byte[15][2];
    private int anzZiele;
    public Turm(Kontroller kontroller, int x, int y,byte faktorplayer, Pruefer pruefer, int index)
    {
        super(kontroller,x,y,1,faktorplayer,index);
        this.kontroller=kontroller;
        this.pruefer=pruefer;
    }
    
    public boolean zugMoeglich(int x, int y)//
    {
        return ((kontroller.inhalt(x,y)*vorzeichen()<=0)&&pruefer.gerade(gebePosX(),gebePosY(),x,y)&&pruefer.frei(gebePosX(),gebePosY(),x,y));
    }

    public int moeglicheZiele()
    {
        byte x=(byte)gebePosX();
        byte y=(byte)gebePosY();
        if(kontroller.letzterZug(0)==x||kontroller.letzterZug(1)==y||kontroller.letzterZug(2)==x||kontroller.letzterZug(3)==y)//vorletzter Zug fehlt noch. 
        //Was ist bei rochade/enPassent..Enpassent ist beim Turm keine Problem weil dann entweder xoder y trotzdem gleich ist.
        {
            byte z=0;
            byte xplus=geradeFreiXPlus();
            byte xminus=geradeFreiXMinus();
            byte yplus=geradeFreiYPlus();
            byte yminus=geradeFreiYMinus();

            //byte[][] ret=new byte[(xplus-xminus)+(yplus-yminus)][2];

            for(byte i=(byte)xminus; i<=xplus; i++)
            {
                if(i!=x)
                {
                    ziele[z][0]=i;
                    ziele[z++][1]=y;
                    //z++;
                }
            }

            for(byte i=(byte)yminus; i<=yplus; i++)
            {
                if(i!=y)
                {
                    ziele[z][0]=x;
                    ziele[z++][1]=i;
                    //z++;
                }
            }
            anzZiele=z;
            return z;
        }
        else
        return anzZiele;
    }
    public String toString()
    {
        return "Turm";
    }

	@Override
	public char getCode() {
		return 'r';
	}
}
