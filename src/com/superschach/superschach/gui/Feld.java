package com.superschach.superschach.gui;

import javafx.geometry.Point3D;

public class Feld extends Point3D{

	int x;
	int y;
	MyStackPane pane;
	GUI gUI;
	public Feld(GUI gUI, MyStackPane pane, int x, int y)
	{
		super(pane.translateX(x+4)+30,pane.translateY(y-4)+30,pane.getFeld().getTranslateZ()+5);
		this.x=7-x;
		this.y=y;
		this.gUI=gUI;
	}
	
	public int gebeInhalt()
    {
        return gUI.spiel.figur(x,y);
    }
}
