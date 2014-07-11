package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.Toolkit;

/**
 * Write a description of class GUI here.
 * 
 * @author Felix Sch&uuml;tze
 * @version (a version number or a date)
 */
public class GUI extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	SwingSchnittstelle schnittstelle;
	Brett brett;
	JPanel links = new JPanel();
	// JTextArea rechts = new JTextArea();
	// JScrollPane jsp = new JScrollPane();
	JPanel unten = new JPanel();
	JPanel unten1 = new JPanel();
	JPanel unten2 = new JPanel();
	JPanel oben = new JPanel();
	// JTextField eingabe =new JTextField();
	// JTextArea text = new JTextArea();
	Menuleiste mL;// = new Menuleiste(brett);
	// String name = System.getProperty("user.name");
	// boolean dreh=false;
	int i1 = 0;
	int i2 = 0;
	RechteGUI rG;
	public boolean havefocus = false;

	// String speicher="";

	public GUI(boolean launcher)
	{
		brett = new Brett(this);
		mL = new Menuleiste(brett);
		rG = new RechteGUI(this);
		// System.out.println("Checkpoint 1");
		setIconImage(getToolkit().getImage(
				getClass().getResource("bilder/turm_schwarz.png")));
		// System.out.println("Checkpoint 2");
		setLayout(new BorderLayout());
		oben.setLayout(new GridLayout(1, 2));
		setLocation(300, 300);
		setSize(800, 445);
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add("South", (unten));
		unten.setLayout(new GridLayout(1, 2));
		unten.add(unten1);
		unten.add(unten2);
		unten.setPreferredSize(new Dimension(45, 45));
		add("Center", (oben));
		oben.add(links);
		links.setLayout(new GridLayout(1, 1));
		links.add(brett);
		oben.add(rG);
		// System.out.println("Checkpoint 5");
		// seiten();
		if (!launcher)
		{
			setMenuBar(mL);
		}
		setTitle("Super Schach");
		setVisible(true);
		// fullscreen();
	}

	public void fullscreen(boolean vbild)
	{
		dispose();
		GraphicsDevice device;
		device = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getScreenDevices()[0];
		if (!vbild)
		{
			setUndecorated(true);
			setSize(Toolkit.getDefaultToolkit().getScreenSize());

			if (device.isFullScreenSupported())
			{
				device.setFullScreenWindow(this);
				device.setDisplayMode(new DisplayMode(1024, 512, 16, 0));
			} else
				rG.anzeige("Leider unterstützt Ihre Hardwarekonfiguration den Vollbildmodus nicht, das tut uns leid!");
		} else
		{
			setUndecorated(false);
			setSize(800, 445);
			device.setFullScreenWindow(null);
		}
		setVisible(true);
	}

	/*
	 * public void anzeige(String s) { text.append("\n"+s);
	 * text.setCaretPosition(text.getText().length()); brett.blink();
	 * validate(); }
	 * 
	 * private static boolean containsString( String s, String subString ) {
	 * return s.indexOf( subString ) > -1 ? true : false; }
	 * 
	 * public void anzeigen(String s) { try { if(s.charAt(0)=='/') { String[]
	 * slash = s.split("/",2); String[] befehl = slash[1].split(" ",2);
	 * if(befehl[0].equals("name")) {
	 * brett.spiel.chat(name+" hat den Namen in "+ befehl[1]+" geändert");
	 * anzeige(name+" hat den Namen in "+ befehl[1]+" geändert");
	 * name=befehl[1]; } else{ if(befehl[0].equals("einstellungen")) { new
	 * Einstellungen(true); } else{ if(befehl[0].equals("drehen")) { try{
	 * drehen(); } catch(Exception e){} } else{ if(befehl[0].equals("menu")) {
	 * setMenuBar(mL.ml(brett)); validate(); } else{
	 * if(befehl[0].equals("hilfe")) { anzeige("Mögliche Befehle:\n" +
	 * "/name [neuer Name]\n" + "/einstellungen\n" +"/drehen\n" +
	 * "/ich [text]\n" + "/schliessen\n"); } else{ if(befehl[0].equals("ich")) {
	 * s=name+" "+befehl[1]; anzeige(s); brett.spiel.chat(s); } else{
	 * if(befehl[0].equals("schliessen")) {
	 * brett.spiel.chat(name+"hat das Spiel verlassen"); System.exit(0); } else{
	 * anzeige("Befehl \"/" +befehl[0]+
	 * "\" nicht gefunden. \n Probieren Sie /hilfe für weitere Informationen.");
	 * } } } } } } } } else { s="<"+name+"> "+s; anzeige(s);
	 * brett.spiel.chat(s); } eingabe.setText(""); } catch(Exception e){} }
	 * public void drehen() { brett.spielfeldLeeren();
	 * brett.spielfeldBauen(dreh); dreh=!dreh; brett.aktualisieren();
	 * this.repaint(); }
	 * 
	 * 
	 * public void seiten() { //rechts.setLayout(new BorderLayout());
	 * jsp.setViewportView(text); rechts.add("Center",(jsp));
	 * text.setEditable(false); EmptyBorder eBorder = new EmptyBorder(2, 5, 2,
	 * 5); // oben, rechts, unten, links LineBorder lBorder = new LineBorder(new
	 * Color(100, 100, 100));
	 * text.setBorder(BorderFactory.createCompoundBorder(lBorder,eBorder));
	 * rechts.add("South",(eingabe)); eingabe.addKeyListener(new KeyListener() {
	 * public void keyTyped(KeyEvent e) {}
	 * 
	 * public void keyReleased(KeyEvent e) {}
	 * 
	 * public void keyPressed(KeyEvent e) { int key = e.getKeyCode(); if (key ==
	 * KeyEvent.VK_ENTER) { speicher=eingabe.getText(); anzeigen(speicher); } if
	 * (key == KeyEvent.VK_UP) { eingabe.setText(speicher); } if (key ==
	 * KeyEvent.VK_DOWN) { eingabe.setText(""); } } });
	 * text.setText("Willkommen, "+name); }
	 */
	public void stirb(int[] figur)
	{
		unten1.removeAll();
		unten2.removeAll();
		for(int i=0;i<figur.length;i++)
		{
				stirb(figur[i]);
		}
		repaint();
		validate();
	}

	public void stirb(int figur)
	{

		if (figur < 0)
		{
			i1++;
			unten1.setLayout(new GridLayout(1, i1));
			figur = figur * -1;
			switch (figur)
			{
			case 1:
				unten1.add(new JLabel(new ImageIcon(this.getClass()
						.getClassLoader()
						.getResource("gui/bilder/turm_schwarz.png"))));
				break;
			case 4:
				unten1.add(new JLabel(new ImageIcon(this.getClass()
						.getClassLoader()
						.getResource("gui/bilder/springer_schwarz.png"))));
				break;
			case 2:
				unten1.add(new JLabel(new ImageIcon(this.getClass()
						.getClassLoader()
						.getResource("gui/bilder/laeufer_schwarz.png"))));
				break;
			case 3:
				unten1.add(new JLabel(new ImageIcon(this.getClass()
						.getClassLoader()
						.getResource("gui/bilder/dame_schwarz.png"))));
				break;
			case 16:
				unten1.add(new JLabel(new ImageIcon(this.getClass()
						.getClassLoader()
						.getResource("gui/bilder/koenig_schwarz.png"))));
				break;
			case 8:
				unten1.add(new JLabel(new ImageIcon(this.getClass()
						.getClassLoader()
						.getResource("gui/bilder/bauer_schwarz.png"))));
				break;
			default:
				unten1.add(new JLabel(new ImageIcon("")));
				break;
			}
		} else
		{
			i2++;
			unten2.setLayout(new GridLayout(1, i2));
			switch (figur)
			{
			case 1:
				unten2.add(new JLabel(new ImageIcon(this.getClass()
						.getClassLoader()
						.getResource("gui/bilder/turm_weiss.png"))));
				break;
			case 4:
				unten2.add(new JLabel(new ImageIcon(this.getClass()
						.getClassLoader()
						.getResource("gui/bilder/springer_weiss.png"))));
				break;
			case 2:
				unten2.add(new JLabel(new ImageIcon(this.getClass()
						.getClassLoader()
						.getResource("gui/bilder/laeufer_weiss.png"))));
				break;
			case 3:
				unten2.add(new JLabel(new ImageIcon(this.getClass()
						.getClassLoader()
						.getResource("gui/bilder/dame_weiss.png"))));
				break;
			case 16:
				unten2.add(new JLabel(new ImageIcon(this.getClass()
						.getClassLoader()
						.getResource("gui/bilder/koenig_weiss.png"))));
				break;
			case 8:
				unten2.add(new JLabel(new ImageIcon(this.getClass()
						.getClassLoader()
						.getResource("gui/bilder/bauer_weiss.png"))));
				break;
			default:
				unten2.add(new JLabel(new ImageIcon("")));
				break;
			}
		}

		validate();
	}

	public static void main(String args[])
	{
		new GUI(false);
	}

	public static void main() // kein Übergabewert, keine extra nachfrage...
	{
		new GUI(false);
	}

	public void focusGained(FocusEvent event)
	{
		havefocus = true;
	}

	public void focusLost(FocusEvent event)
	{
		havefocus = false;
	}

	public void menu()
	{
		setMenuBar(new Menuleiste(brett));
		// validate();
	}

	public void nachricht(String s)
	{
		JOptionPane.showMessageDialog(
				null,
				s,
				"Super Schach",
				JOptionPane.WARNING_MESSAGE,
				new ImageIcon(this.getClass().getClassLoader()
						.getResource("gui/bilder/bauer_schwarz.png")));
	}
}