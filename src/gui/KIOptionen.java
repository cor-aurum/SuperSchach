package gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;

public class KIOptionen extends JPanel
{
	RechteGUI rG;
	private MyPanel weiss = new MyPanel(0);
	private MyPanel schwarz = new MyPanel(1);
	private static final long serialVersionUID = 1L;

	public KIOptionen(RechteGUI rG)
	{
		this.rG = rG;
		setLayout(new GridLayout(2, 1));
		add(weiss);
		add(schwarz);
	}

	public class MyPanel extends JLabel
	{
		private static final long serialVersionUID = 1L;
		private int farbe;
		private JToggleButton start = new JToggleButton();
		private JLabel[] beschreibung = new JLabel[2];
		private JSlider level = new JSlider(1, 6);

		public MyPanel(int farbe)
		{
			this.farbe = farbe;
			setLayout(new GridLayout(3, 2));
			level.setValue(4);
			setIcon(farbe == 0 ? new ImageIcon(this.getClass()
					.getClassLoader().getResource("gui/bilder/holz_hell.png"))
					: new ImageIcon(this.getClass().getClassLoader()
							.getResource("gui/bilder/holz_dunkel.png")));
			beschreibung[0] = new JLabel("KI auswählen");
			beschreibung[0].setForeground(farbe == 0 ? Color.white
					: Color.black);
			beschreibung[1] = new JLabel("Stärke wählen");
			beschreibung[1].setForeground(farbe == 0 ? Color.white
					: Color.black);
			add(new JLabel());
			add(new JLabel());
			add(beschreibung[1]);
			add(level);
			add(new JLabel());
			add(start);
			start.setSelected(true);
			start.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					listener();
				}
			});
		}

		public void listener()
		{
			if (!start.isSelected())
			{
				rG.gUI.brett.spiel.ki(4, farbe, level.getValue());
			} 
			else
			{
				rG.gUI.brett.spiel.ki(0, farbe, 0);
			}

			validate();
			repaint();
		}
	}
}
