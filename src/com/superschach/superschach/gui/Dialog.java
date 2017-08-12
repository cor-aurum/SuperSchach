package com.superschach.superschach.gui;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public abstract class Dialog extends StackPane {
	public Dialog()
	{
		this(350,200);
	}
	
	public Dialog(int width, int height)
	{
		setMaxWidth(width);
		setMaxHeight(height);
		Pane p=new Pane();
		p.setId("dialog");
		getChildren().add(p);
	}
}
