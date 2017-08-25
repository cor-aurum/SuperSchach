package com.superschach.superschach.network;

public interface Commands
{

	char SEPERATOR='/';
	char SEPERATOR2=' ';
	
	boolean hasParam();

	boolean answer();
	
	int ordinal();
	
	String name();

}
