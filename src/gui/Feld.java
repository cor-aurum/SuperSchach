package gui;

import javafx.geometry.Point3D;

public class Feld extends Point3D{

	int x;
	int y;
	GUI gUI;
	public Feld(GUI gUI, int x, int y)
	{
		super(gUI.translateX(x+4)+30,gUI.translateY(y-4)+30,gUI.feld.getTranslateZ()+5);
		this.x=x;
		this.y=y;
		this.gUI=gUI;
	}
	
	public int gebeInhalt()
    {
        return gUI.spiel.figur(x,y);
    }
}
