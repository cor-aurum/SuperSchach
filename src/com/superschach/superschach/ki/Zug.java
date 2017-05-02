package com.superschach.superschach.ki;

public class Zug
{
	private int[] zug;
	private int tausch;
	private String nachricht="";
	
	public Zug()
	{
		zug=new int[4];
	}
	
	public void zug(int[] zug)
	{
		this.zug=zug;
	}
	
	public void zug(int posx, int posy, int zielx, int ziely)
	{
		zug[0]=posx;
		zug[1]=posy;
		zug[2]=zielx;
		zug[3]=ziely;
	}
	
	public void tausch(int figur)
	{
		tausch=figur;
	}

	public int[] getZug()
	{
		return zug;
	}
	
	public int getPosX()
	{
		return zug[0];
	}
	
	public int getPosY()
	{
		return zug[1];
	}
	
	public int getZielX()
	{
		return zug[2];
	}
	
	public int getZielY()
	{
		return zug[3];
	}

	public int getTausch()
	{
		return tausch;
	}
	
	public String nachricht()
	{
		return nachricht;
	}
	
	public String nachricht(String nachricht)
	{
		String temp=this.nachricht;
		this.nachricht=nachricht;
		return temp;
	}
}
