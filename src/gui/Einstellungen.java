package gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import spiel.Schnittstelle;

public class Einstellungen extends Fenster{

	GUI gUI;
	VBox settings=new VBox();
	ColorPicker pick_weiss;
	ColorPicker pick_schwarz;
	Button speichern=new Button("Speichern");
	ToggleGroup map = new ToggleGroup();
    RadioButton map_marmor = new RadioButton("Marmor");
    
    RadioButton map_holz = new RadioButton("Holz");
    
    RadioButton map_gras = new RadioButton("Gras");
    
    RadioButton map_glas = new RadioButton("Glas");
    
	
	public Einstellungen(GUI gUI)
	{
		//setzeInhalt(tab);
		super(gUI);
		setzeInhalt(settings);
		this.gUI=gUI;
		//this.setPrefWidth(200);
		map_marmor.setToggleGroup(map);
		map_holz.setToggleGroup(map);
		map_gras.setToggleGroup(map);
		map_glas.setToggleGroup(map);
	    map_marmor.setSelected(true);
	    
	    settings.getChildren().add(new Label("Oberfläche"));
	    settings.getChildren().add(map_marmor);
	    settings.getChildren().add(map_holz);
	    settings.getChildren().add(map_gras);
	    settings.getChildren().add(map_glas);
	    addLeer(settings);
	    
	    HBox boxWeiss=new HBox();
	    HBox boxSchwarz=new HBox();
	    pick_weiss =new ColorPicker(gUI.farbe_weiss.getValue());
	    pick_schwarz =new ColorPicker(gUI.farbe_schwarz.getValue());
	    boxWeiss.getChildren().addAll(new Label("Weiss: "), pick_weiss);
	    boxWeiss.getChildren().addAll(new Label("Schwarz: "), pick_schwarz);
	    pick_weiss.valueProperty().bindBidirectional(gUI.farbe_weiss);
	    pick_schwarz.valueProperty().bindBidirectional(gUI.farbe_schwarz);
	    settings.getChildren().add(new Label("Figuren"));
	    settings.getChildren().add(boxWeiss);
	    settings.getChildren().add(boxSchwarz);
	    addLeer(settings);
	    settings.getChildren().add(speichern);
	    speichern.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				speichern();
			}
		});
	    laden();
	    map.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
	        public void changed(ObservableValue<? extends Toggle> ov,
	            Toggle old_toggle, Toggle new_toggle) {
	                if (map.getSelectedToggle()==map_holz) {
	                    gUI.hintergrund="holz";
	                }  
	                else if (map.getSelectedToggle()==map_marmor) {
	                    gUI.hintergrund="marmor";
	                } 
	                else if (map.getSelectedToggle()==map_gras) {
	                    gUI.hintergrund="gras";
	                } 
	                else if (map.getSelectedToggle()==map_glas) {
	                    gUI.hintergrund="glas";
	                } 
	                gUI.feld.aktualisiereMap();
	            }
	    });
	}
	
	public void addLeer(VBox vbox)
	{
		Label j = new Label("\n");
		j.setMaxHeight(20);
		vbox.getChildren().add(j);
	}
	
	private void speichern()
	{
		try {
			BufferedWriter br=new BufferedWriter(new FileWriter(Schnittstelle.verzeichnis()+"gui.save"));
			br.write(gUI.hintergrund);
			br.write(System.getProperty("line.separator"));
			br.write(toRGBCode(pick_weiss.getValue()));
			br.write(System.getProperty("line.separator"));
			br.write(toRGBCode(pick_schwarz.getValue()));
			br.write(System.getProperty("line.separator"));
			br.write(gUI.name);
			br.flush();
			br.close();
		} catch (IOException e) {
			gUI.spiel.meldungAusgeben("Speichern fehlgeschlagen");
		}
		
	}
	
	private void laden()
	{
		try {
			BufferedReader br=new BufferedReader(new FileReader(Schnittstelle.verzeichnis()+"gui.save"));
			gUI.hintergrund=br.readLine();
			pick_weiss.setValue(Color.web(br.readLine()));
			pick_schwarz.setValue(Color.web(br.readLine()));
			gUI.name=br.readLine();
			br.close();
			
			switch(gUI.hintergrund)
			{
				case "marmor":
					map_marmor.setSelected(true);
					break;
				case "glas":
					map_glas.setSelected(true);
					break;
				case "gras":
					map_gras.setSelected(true);
					break;
				case "holz":
					map_holz.setSelected(true);
					break;
			}
		} catch (IOException e) {
		}
	}
	
	public String toRGBCode( Color color )
    {
        return String.format( "#%02X%02X%02X",
            (int)( color.getRed() * 255 ),
            (int)( color.getGreen() * 255 ),
            (int)( color.getBlue() * 255 ) );
    }
}
