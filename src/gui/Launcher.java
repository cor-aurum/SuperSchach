package gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

/**
 * Write a description of class Launcher here.
 * 
 * @author Felix Sch&uuml;tze
 * @version (a version number or a date)
 */
public class Launcher extends JFrame
{
	/**
	 * 
	 */
	private JButton rechts = new JButton();
	private JPanel grund = new JPanel();
	private static final long serialVersionUID = 1L;
	SwingSchnittstelle schnittstelle;
	private JPanel panel = new JPanel();
	private Knopf[] start = new Knopf[4];
	//public String lan;

	// private String[] modi ={ "Schach", "Janus-Schach", "Dame" };

	// public JTextField name=new JTextField("Hier bitte Nicknamen eingeben");
	public Launcher()
	{
		setLocation(300, 300);
		setIconImage(getToolkit().getImage(
				getClass().getResource("bilder/turm_schwarz.png")));
		panel.setLayout(new GridLayout(3, 1));
		start[0] = new Knopf("Einzelspiel", 1);
		start[1] = new Knopf("Mehrspieler", 1);
		start[2] = new Knopf("Online spielen", 1);
		start[3] = new Knopf("Lan Spiel", 1);
		setSize(400, 200);
		setResizable(false);
		setTitle("Super Schach");
		add(grund);
		rechts.setBorderPainted(false);
        rechts.setContentAreaFilled(false);
		grund.setLayout(new GridLayout(1, 2));
		rechts.setIcon(new ImageIcon(this.getClass().getClassLoader().getResource("gui/bilder/turm_schwarz.png")));
		rechts.setEnabled(false);
		grund.add(panel);
		grund.add(rechts);
		start[0].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				GUI gui = new GUI(true);
				gui.brett.spiel.ki(4, 1, 5);
				dispose();
			}
		});
		start[1].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				new GUI(true);
				dispose();
			}
		});
		start[2].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				try
				{
					FileWriter writer = new FileWriter(spiel.Schnittstelle.verzeichnis()+"ip.ip");
					BufferedWriter bw = new BufferedWriter(writer);
					bw.write("super-schach.com");
					bw.close();
				} catch (Exception e)
				{
				}
				GUI gui = new GUI(true);
				gui.brett.spiel.ki(2, 1, 0);
				dispose();
			}
		});
		start[3].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				panel.removeAll();
				panel.setLayout(new GridLayout(2, 1));
				Knopf[] lan = new Knopf[2];
				lan[0] = new Knopf("Spiel erstellen", 0);
				lan[1] = new Knopf("Spiel beitreten", 1);
				lan[0].addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent arg0)
					{
						new Thread()
						{
							public void run()
							{
								new server.Server();
							}
						}.start();
						String s = "";
						try
						{
							s = "" + InetAddress.getLocalHost();
							String[] splitResult = s.split("/");
							s = splitResult[splitResult.length - 1];
							FileWriter writer = new FileWriter(spiel.Schnittstelle.verzeichnis()+"ip.ip");
							BufferedWriter bw = new BufferedWriter(writer);
							bw.write(s);
							bw.close();
						} catch (Exception e)
						{
						}
						GUI gui = new GUI(true);
						gui.brett.spiel.ki(2, 1, 0);
						gui.rG.anzeige("IP: " + s);
						dispose();
					}
				});
				lan[1].addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent arg0)
					{
						new Einstellungen(false);
						dispose();
					}
				});
				for (int i = 0; i < 2; i++)
				{
					panel.add(lan[i]);
				}
				validate();
			}
		});
		for (int i = 0; i < 4; i++)
		{
			if (i != 2)
				panel.add(start[i]);
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public static void main(String args[])
	{
		//System.out.println("Checkpoint 0");
		new Launcher();
	}
}
