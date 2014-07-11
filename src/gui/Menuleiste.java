package gui;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * Beschreiben Sie hier die Klasse Menuleiste.
 * 
 * @author Felix Sch&uuml;tze
 * @version (eine Versionsnummer oder ein Datum)
 */
public class Menuleiste extends MenuBar
{
	private static final long serialVersionUID = 1L;
	MenuItem[][] schwierigkeit=new MenuItem[10][2];
    public Menuleiste (final Brett brett) { // Menüleiste anlegen
        //MenuBar Menuleiste = new MenuBar (); // Eine Menüleiste anlegen
        Menu datei = new Menu ("Datei");
        MenuItem neu = new MenuItem ("Neu");
        datei.add (neu);
        MenuItem akt = new MenuItem ("aktualisieren");

        datei.add (akt);
        MenuItem beenden = new MenuItem ("Beenden");
        datei.add (beenden);
        MenuItem zurueck = new MenuItem ("Zug Zurueck");
        datei.add(zurueck);
        this.add(datei);
        neu.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    //new GUI(false);
                    brett.aufbauen();
                }
            });
        beenden.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    System.exit(0);
                }
            });

        akt.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    //throw new RuntimeException();
                    brett.aktualisieren();
                }
            });

        zurueck.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    brett.zurueck();
                }
            });

        Menu ki = new Menu ("Weiss"); // Ein Menü hinzufügen
        MenuItem zufallski = new MenuItem ("Ivan Zufallski"); // Einen Menüeintrag anlegen
        ki.add (zufallski);  // Den Eintrag dem Menü hinzufügen
        Menu schleifenki = new Menu ("Schleifen KI");
        ki.add(schleifenki);
        MenuItem schleifenkitest = new MenuItem ("Schleifen KI Test");
        ki.add(schleifenkitest);
        MenuItem ki_felix = new MenuItem ("KI"); // Einen Menüeintrag anlegen
        ki.add (ki_felix);  // Den Eintrag dem Menü hinzufügen
        MenuItem aus = new MenuItem ("KI aus"); // Einen Menüeintrag anlegen
        ki.add (aus); // Den Eintrag dem Menü hinzufügen
        this.add(ki); // Das Menü der Leiste hinzufügen
        zufallski.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    brett.spiel.ki(1,0,0);
                }
            });

        schleifenkitest.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    brett.spiel.ki(3,0,5);
                }
            });

        ki_felix.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    brett.spiel.ki(2,0,0);
                }
            });

        aus.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    brett.spiel.ki(0,0,0);
                }
            });

        Menu ki2 = new Menu ("Schwarz"); // Ein Menü hinzufügen
        MenuItem zufallski2 = new MenuItem ("Ivan Zufallski"); // Einen Menüeintrag anlegen
        ki2.add (zufallski2);  // Den Eintrag dem Menü hinzufügen
        Menu schleifenki2 = new Menu ("Schleifen KI");
        ki2.add(schleifenki2);
        MenuItem schleifenkitest2 = new MenuItem ("Schleifen KI Test");
        ki2.add(schleifenkitest2);
        MenuItem ki_felix2 = new MenuItem ("KI"); // Einen Menüeintrag anlegen
        ki2.add (ki_felix2);  // Den Eintrag dem Menü hinzufügen
        MenuItem aus2 = new MenuItem ("KI aus"); // Einen Menüeintrag anlegen
        ki2.add (aus2); // Den Eintrag dem Menü hinzufügen
        this.add(ki2); // Das Menü der Leiste hinzufügen
        zufallski2.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    brett.spiel.ki(1,1,0);
                }
            });

        for(int i=0;i<2;i++)
        {
            for(int j=0;j<10;j++)
            {
                schwierigkeit[j][i]=new MenuItem("Schwierigkeit "+(j+1));
                listener(i,j,brett);
                if(i==0)
                {
                    schleifenki.add(schwierigkeit[j][i]);
                }
                else
                    schleifenki2.add(schwierigkeit[j][i]);
            }
        }

        schleifenkitest2.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    brett.spiel.ki(3,1,5);
                }
            });

        ki_felix2.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    brett.spiel.ki(2,1,0);
                }
            });

        aus2.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    brett.spiel.ki(0,1,0);
                }
            });
        //return Menuleiste;
    }

    public void listener(final int i,final int j,final Brett brett)
    {
        schwierigkeit[j][i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    brett.spiel.ki(4,i,j+1);
                }
            });
    }
}
