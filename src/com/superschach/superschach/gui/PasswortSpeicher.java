package com.superschach.superschach.gui;

import java.io.Serializable;

public class PasswortSpeicher implements Serializable{
	private static final long serialVersionUID = -8401193702048698816L;
	private String passwort="";
	private String name="";
	public String getPasswort() {
		return passwort;
	}
	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
