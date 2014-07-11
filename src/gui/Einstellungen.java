package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
/**
 * Beschreiben Sie hier die Klasse newLauncher.
 * 
 * @author Felix Sch&uuml;tze
 * @version 2.0
 */
public class Einstellungen extends JFrame
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel oben=new JPanel();
    JPanel unten=new JPanel();
    JButton speichern=new JButton("OK");
    JTextField ip=new JTextField();
    GUI gUI;
    public Einstellungen(final boolean r)
    {
        setSize(500,100);
        setTitle("IP-Konfiguration");
        setLocation(300,300);
        setLayout(new GridLayout(2,1));
        add(oben);
        add(unten);
        unten.add(speichern);
        oben.setLayout(new GridLayout(1,1));
        oben.add(ip);
        speichern.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    try{
                        FileWriter writer=new FileWriter("ip.ip");
                        BufferedWriter bw=new BufferedWriter(writer);
                        bw.write(ip.getText());
                        bw.close();
                        schliessen(r);
                    }
                    catch(Exception e){}
                }
            });

        try
        {
            FileReader fr = new FileReader("ip.ip");
            BufferedReader br = new BufferedReader(fr);
            ip.setText(br.readLine());
            validate();
            br.close();
        }
        catch(Exception e){}
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
    
    public void start()
    {
        this.gUI=new GUI(true);
        gUI.brett.spiel.ki(2,1,0);
    }
    
    public void schliessen(boolean r)
    {
        if(!r)
        start();
        dispose();
    }
}
