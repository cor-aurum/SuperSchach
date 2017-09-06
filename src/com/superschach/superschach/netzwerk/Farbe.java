package com.superschach.superschach.netzwerk;

public enum Farbe {
	WEISS(0),
	SCHWARZ(1),
	EGAL(2);
	
	final public byte nummer;
	Farbe(int nummer)
	{
		this.nummer=(byte)nummer;
	}
}
