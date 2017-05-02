package com.superschach.superschach.ki;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.superschach.superschach.kontroller.Kontroller;
/**
 * Write a description of class Datenbank here.
 * 
 * @author Felix Sch&uuml;tze
 * @version (a version number or a date)
 */
public class Datenbank
{
    String host="localhost";//"172.31.0.200"; //"super-schach.com";
    Socket socket=null;
    OutputStream output;
    InputStream input;
    public boolean erfolg=true;
    //byte [] letzterZug;
    public boolean moeglich=true;
    byte[] zug=new byte[4];
    Kontroller kontroller;
    /**
     * Constructor for objects of class Datenbank
     */
    public Datenbank(Kontroller kontroller)
    {

        this.kontroller=kontroller;
        try
        {
            socket = new Socket (host,1339);
            input = socket.getInputStream();
            output = socket.getOutputStream();
            erfolg=true;
        } catch (Exception e) {
            erfolg=false;
        }
    }

    public void senden()
    {
        byte[] letzterZug=kontroller.letzterZug();
        try
        {
            output.write(letzterZug);
        } catch (Exception e) {
            moeglich=false;
        }
    }

    public byte[] empfangen()
    { 
        for(int i=0;i<4;i++)
        {
            zug[i]=0;
        }
        if(moeglich && erfolg)
        {
            System.out.println("Datenbank wartet auf Daten");
            try
            {
                int erhalten = input.read();
                for(int i=0;i<4;i++)
                {
                    zug[3-i]=(byte)(erhalten%10);
                    erhalten=erhalten/10;
                }
            } catch (Exception e) {
                for(int i=0;i<4;i++)
                {
                    zug[i]=0;
                }
                System.out.println("Empfangen der Daten fehlgeschlagen");
            }
            System.out.println("Datenbank"+zug[0]+zug[1]+zug[2]+zug[3]);
        }
        return zug;
    }
}
