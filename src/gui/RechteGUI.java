package gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileFilter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Beschreiben Sie hier die Klasse RechteGUI.
 * 
 * @author Felix Sch&uuml;tze
 * @version (eine Versionsnummer oder ein Datum)
 */
public class RechteGUI extends JTabbedPane
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTextField eingabe = new JTextField();
	JTextArea text = new JTextArea();
	JTextArea rechts = new JTextArea();
	JScrollPane jsp = new JScrollPane();
	String name = System.getProperty("user.name");
	GUI gUI;
	String speicher = "";
	JPanel panel = new JPanel();
	boolean dreh = false;
	private boolean vbild = true;
	private boolean protokoll = false;
	private File f;

	public RechteGUI(GUI gUI)
	{
		this.gUI = gUI;
		addTab("Chat", rechts);
		addTab("Optionen", panel);
		addTab("KI Einstellungen",new KIOptionen(this));
		seiten();
		rechts();
	}
	
	public File getFile()
	{
		return f;
	}
	
	public void zug()
	{
		protokoll = false;
		kn[2][1].setText("Spiel laden");
	}

	public class MyFilter extends FileFilter
	{
		public boolean accept(File f)
		{
			if (f == null)
			{
				return false;
			}
			if (f.isDirectory())
			{
				return true;
			}
			return f.getName().toLowerCase().endsWith(".schach")
					|| f.getName().toLowerCase().endsWith(".slog");
		}

		public String getDescription()
		{
			return "Spielstände";
		}
	}
	
	private Knopf[][] kn = new Knopf[3][2];

	public void rechts()
	{
		panel.setLayout(new GridLayout(3, 2));
		
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 2; j++)
			{
				kn[i][j] = new Knopf(j + i);
				panel.add(kn[i][j]);
			}
		}
		kn[0][0].setText("Namen \n ändern");
		kn[0][0].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				setSelectedIndex(0);
				eingabe.setText("/name ");
			}
		});
		kn[0][1].setText("Spielfeld \n drehen");
		kn[0][1].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				drehen();
			}
		});
		kn[1][0].setText("Protokoll speichern");
		kn[1][0].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				gUI.brett.spiel.logSpeichern(new MyFilter());
			}
		});
		kn[1][1].setText("Zug zurück");
		kn[1][1].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				gUI.brett.spiel.zurueck();
			}
		});

		kn[2][0].setText("Spiel speichern");
		kn[2][0].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				JFileChooser chooser = new JFileChooser();
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setFileFilter(new MyFilter());
				try
				{
					FileReader fr = new FileReader(spiel.Schnittstelle
							.verzeichnis() + "verzeichnis.v");

					BufferedReader br = new BufferedReader(fr);
					chooser.setCurrentDirectory(new File(br.readLine()));
					br.close();
				} catch (Exception e)
				{
				}
				int rueckgabeWert = chooser.showSaveDialog(null);
				if (rueckgabeWert == JFileChooser.APPROVE_OPTION)
				{
					// if
					// (!gUI.brett.spiel.speichern(chooser.getSelectedFile()))
					if (!gUI.brett.spiel.speichern(new File(
							(chooser.getSelectedFile().toString()
									.indexOf(".schach") == -1) ? chooser
									.getSelectedFile().toString() + ".schach"
									: chooser.getSelectedFile().toString())))
						;
					{
						// gUI.nachricht("Spielstand konnte nicht gespeichert werden!");
					}
					try
					{
						FileWriter fw = new FileWriter(spiel.Schnittstelle
								.verzeichnis() + "verzeichnis.v");
						BufferedWriter bw = new BufferedWriter(fw);
						bw.write(chooser.getSelectedFile().getParent()
								.toString());
						bw.close();
					} catch (Exception e)
					{
						System.out.println(e);
					}
				}
			}
		});
		kn[2][1].setText("Spiel laden");
		kn[2][1].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if (!protokoll)
				{
				JFileChooser chooser = new JFileChooser();
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setFileFilter(new MyFilter());
				try
				{
					FileReader fr = new FileReader(spiel.Schnittstelle
							.verzeichnis() + "verzeichnis.v");
					BufferedReader br = new BufferedReader(fr);
					chooser.setCurrentDirectory(new File(br.readLine()));
					br.close();
				} catch (Exception e)
				{
				}

				int rueckgabeWert = chooser.showOpenDialog(null);
				if (rueckgabeWert == JFileChooser.APPROVE_OPTION)
				{
					if (chooser.getSelectedFile().toString().toLowerCase()
							.endsWith(".schach"))
					{
						try
						{
							gUI.brett.spiel.laden(chooser.getSelectedFile());
						} catch (FileNotFoundException e)
						{
							gUI.nachricht("Konnte nicht gefunden werden");
						} catch (Exception e)
						{
							gUI.nachricht("Datei ung�ltig");
						}
					}
					else
					{
							f=chooser.getSelectedFile();
							gUI.brett.spiel.aktiviereKI(3, (byte) 1, 0);
							protokoll = true;
							kn[2][1].setText("N�chster Zug");
					}
				}
				} else
				{
					gUI.brett.spiel.ki(1);
				}
			}
			
		});
	}

	public void anzeigen(String s)
	{
		try
		{
			if (s.charAt(0) == '/')
			{
				String[] slash = s.split("/", 2);
				String[] befehl = slash[1].split(" ", 2);
				if (befehl[0].equals("name"))
				{
					gUI.brett.spiel.chat(name + " hat den Namen in "
							+ befehl[1] + " ge�ndert");
					anzeige(name + " hat den Namen in " + befehl[1]
							+ " ge�ndert");
					name = befehl[1];
				} else
				{
					if (befehl[0].equals("einstellungen"))
					{
						new Einstellungen(true);
					} else
					{
						if (befehl[0].equals("drehen"))
						{
							try
							{
								drehen();
							} catch (Exception e)
							{
							}
						} else
						{
							if (befehl[0].equals("menu"))
							{
								menu();
							} else
							{
								if (befehl[0].equals("hilfe"))
								{
									hilfe();
								} else
								{
									if (befehl[0].equals("ich"))
									{
										s = name + " " + befehl[1];
										anzeige(s);
										gUI.brett.spiel.chat(s);
									} else
									{
										if (befehl[0].equals("schliessen"))
										{
											gUI.brett.spiel
													.chat(name
															+ "hat das Spiel verlassen");
											System.exit(0);
										} else
										{
											if (befehl[0].equals("vollbild"))
											{
												eingabe.setText("");
												vbild = !vbild;
												gUI.fullscreen(vbild);
											} else
											{
												if (befehl[0].equals("farbe"))
												{
													gUI.brett.setzeFarbe();
													anzeige(gUI.brett.farbe() ? "Farben eingeschaltet"
															: "Farben ausgeschaltet");
												} else
												{
													if (befehl[0].equals("zug"))
													{
														gUI.brett.spiel
																.uebersetzen(befehl[1]);
													} else
													{
														anzeige("Befehl \"/"
																+ befehl[0]
																+ "\" nicht gefunden. \n Probieren Sie /hilfe f�r weitere Informationen.");
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			} else
			{
				s = "<" + name + "> " + s;
				anzeige(s);
				gUI.brett.spiel.chat(s);
			}
			eingabe.setText("");
		} catch (Exception e)
		{
		}
	}

	public void menu()
	{
		gUI.menu();
		validate();
	}

	public void hilfe()
	{
		anzeige("M�gliche Befehle:\n" + "/name [neuer Name]\n"
				+ "/einstellungen\n" + "/drehen\n" + "/ich [text]\n"
				+ "/schliessen\n" + "/vollbild\n" + "/farbe\n");
	}

	public void seiten()
	{
		rechts.setLayout(new BorderLayout());
		jsp.setViewportView(text);
		rechts.add("Center", (jsp));
		text.setEditable(false);
		EmptyBorder eBorder = new EmptyBorder(2, 5, 2, 5); // oben, rechts,
															// unten, links
		LineBorder lBorder = new LineBorder(new Color(100, 100, 100));
		text.setBorder(BorderFactory.createCompoundBorder(lBorder, eBorder));
		rechts.add("South", (eingabe));
		eingabe.addKeyListener(new KeyListener()
		{
			public void keyTyped(KeyEvent e)
			{
			}

			public void keyReleased(KeyEvent e)
			{
			}

			public void keyPressed(KeyEvent e)
			{
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER)
				{
					speicher = eingabe.getText();
					anzeigen(speicher);
				}
				if (key == KeyEvent.VK_UP)
				{
					eingabe.setText(speicher);
				}
				if (key == KeyEvent.VK_DOWN)
				{
					eingabe.setText("");
				}
			}
		});
		text.setText("Willkommen, " + name);
	}

	public void drehen()
	{
		gUI.brett.spielfeldLeeren();
		gUI.brett.spielfeldBauen(dreh);
		dreh = !dreh;
		gUI.brett.aktualisieren();
		this.repaint();
	}

	public void anzeige(String s)
	{
		text.append("\n" + s);
		text.setCaretPosition(text.getText().length());
		gUI.brett.blink();
		validate();
	}
}
