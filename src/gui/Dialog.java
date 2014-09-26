package gui;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public abstract class Dialog extends StackPane {
	public Dialog()
	{
		setMaxWidth(350);
		setMaxHeight(200);
		Pane p=new Pane();
		p.setId("dialog");
		getChildren().add(p);
	}
}
