package com.superschach.superschach.gui;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;

public class FigurenMenue extends Dialog{

	int ret=3;
	public FigurenMenue(Blocker blocker, Node[] figuren, GUI gUI)
	{
		setMaxWidth(450);
		setMaxHeight(450);
		GridPane auswahl = new GridPane();
		//Node[] figuren = new Node[4];

		for (int i = 0; i < 4; i++) {
			try {
				auswahl.add(figuren[i], i & 1, i < 2 ? 0 : 1);
				if(figuren[i] instanceof MeshView)
				{
					//SubScene scene =new SubScene(auswahl,450,450);
					//scene.setCamera(new PerspectiveCamera());
					figuren[i].setScaleX(80);
					figuren[i].setScaleY(80);
					figuren[i].setScaleZ(80);
					//figuren[i].setRotate(180);
					
					((MeshView)figuren[i]).setRotationAxis(Rotate.X_AXIS);
					figuren[i].setRotate(270);
					auswahl.setRotate(180);
				}
				else
				{
					figuren[i].setScaleX(4);
					figuren[i].setScaleY(4);
					figuren[i].setScaleZ(4);
				}
				final int icopy = i;
				figuren[i].setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						ret = icopy;
						getChildren().remove(auswahl);
						gUI.feld.getChildren().remove(FigurenMenue.this);
						blocker.release();
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		auswahl.setPadding(new Insets(50));
		auswahl.setHgap(300);
		auswahl.setVgap(300);
		getChildren().add(auswahl);
		gUI.feld.getChildren().add(this);
	}
}
