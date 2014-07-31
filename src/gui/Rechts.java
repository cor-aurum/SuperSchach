package gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class Rechts extends TabPane{

	GUI gUI;
	VBox settings=new VBox();
	
	
	public Rechts(GUI gUI)
	{
		this.gUI=gUI;
		this.setPrefWidth(200);
		Tab einstellungen = new Tab();
		einstellungen.setText("Einstellungen");
		getTabs().add(einstellungen);
		einstellungen.setContent(settings);
		
		ToggleGroup map = new ToggleGroup();
	    RadioButton map_marmor = new RadioButton("Marmor");
	    map_marmor.setToggleGroup(map);
	    map_marmor.setSelected(true);
	    RadioButton map_holz = new RadioButton("Holz");
	    map_holz.setToggleGroup(map);
	    map.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
	        public void changed(ObservableValue<? extends Toggle> ov,
	            Toggle old_toggle, Toggle new_toggle) {
	                if (map.getSelectedToggle()==map_holz) {
	                    gUI.hintergrund="holz";
	                }  
	                else if (map.getSelectedToggle()==map_marmor) {
	                    gUI.hintergrund="marmor";
	                } 
	                gUI.aktualisiereMap();
	            }
	    });
	    settings.getChildren().add(new Label("Oberfläche"));
	    settings.getChildren().add(map_marmor);
	    settings.getChildren().add(map_holz);
	    addLeer(settings);
	}
	
	public void addLeer(VBox vbox)
	{
		Label j = new Label("\n");
		j.setMaxHeight(20);
		vbox.getChildren().add(j);
	}
}
