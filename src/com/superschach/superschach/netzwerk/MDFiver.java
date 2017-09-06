package com.superschach.superschach.netzwerk;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MDFiver {

	private MessageDigest md;
	private byte[] buff=new byte[32];
	
	public MDFiver() throws NoSuchAlgorithmException {
		md = MessageDigest.getInstance("MD5");
	}

	private byte toASCII(int diggit)
	{
		diggit&=15;
		return (byte) (diggit<10?diggit+48:diggit+87);
	}
	
	public String md5(String str)
	{
		try {
			byte[] bytes=str.getBytes("UTF-8");
			synchronized(md)
			{
				md.update(bytes);
				byte[] md5=md.digest();
				for(int j=0,i=0; i<md5.length; i++)
				{					
					buff[j++]=toASCII((md5[i]>>4));
					buff[j++]=toASCII(md5[i]);
				}
				String ret=new String(buff);
				return ret;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return str;
		}

	}
}
