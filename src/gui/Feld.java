package gui;

public class Feld{

	int x;
	int y;
	FxSchnittstelle schnittstelle;
	public Feld(FxSchnittstelle schnittstelle, int x, int y)
	{
		this.x=x;
		this.y=y;
		this.schnittstelle=schnittstelle;
	}
}
