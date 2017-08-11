package com.superschach.superschach.network.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.superschach.superschach.ki.KI;
import com.superschach.superschach.ki.Zug;
import com.superschach.superschach.kontroller.Kontroller;

public class OnlineSpieler implements KI
{


    private OutputStream output;
    private InputStream input;
    
	public OnlineSpieler(Socket spielSocket) throws IOException
	{
		output=spielSocket.getOutputStream();
		input=spielSocket.getInputStream();
	}
	

    private boolean schreiben(Kontroller kontroller)
    {
        byte [] letzterZug=kontroller.letzterZug();
        if(!((letzterZug[0]==0)&&(letzterZug[1]==0)&&(letzterZug[2]==0)&&(letzterZug[3]==0))) //wenn noch kein Zug gemacht wurde (alle 4 werte=0), soll auch kein Zug �bertragen werden
        {
            try
            {
                synchronized(output)
                {
                	output.write(letzterZug,0,letzterZug.length);
                	output.flush();
                    return true;
                }
            } catch (Exception e) 
            {

            }
            return false;
        }
        return true;
    }

    private void erhalten(Zug zug)
    {
        synchronized(input)
        {
            try
            {
            	byte[] buff=new byte[5];
            	input.read(buff,0,buff.length);
            	zug.zug(buff[0],buff[1],buff[2],buff[3]);
                zug.tausch(buff[4]);
            } catch (IOException e) {}
        }

    }


	
	//spiel ist hier null
	public void zug(Kontroller spiel, Zug zug) throws Exception
	{
		if(schreiben(spiel))
		{
			erhalten(zug);
		}
	}


	public void tellMatt(Kontroller spiel)
	{
		schreiben(spiel);		
	}
	
}


