package gui;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public abstract class Dialog extends StackPane {
	public Dialog()
	{
		setMaxWidth(350);
		setMaxHeight(200);
		Pane p=new Pane();
		p.setStyle("-fx-background-color: #006464;");
		getChildren().add(p);
		
	}
}
