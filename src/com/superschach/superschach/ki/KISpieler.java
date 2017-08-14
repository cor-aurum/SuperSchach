package com.superschach.superschach.ki;

import com.superschach.superschach.kontroller.KIKontroller;
import com.superschach.superschach.kontroller.Kontroller;
import com.superschach.superschach.spiel.Spiel;

public class KISpieler {
	private KI spieler;
	private Kontroller kontroller;
	final public String name;
	Zug zug;// = new Zug();

	/*
	 * public KISpieler(KI spieler, Kontroller kontroller) { this.spieler = spieler;
	 * this.kontroller = kontroller; }
	 */

	public KISpieler(int spieler, String level, Spiel spiel, byte farbe) throws Exception {
		this.kontroller = spiel;
		boolean ret = false;
		switch (spieler) {
		case 4:
			this.spieler = new MultiThreadKI(Integer.parseInt(level));
			name = "Kiana";
			ret = true;
			break;
		case 2:
			this.spieler = new UCIKi(level);
			name = "UCI";
			ret = true;
			break;

		case 3:
			this.spieler = new ProtokollKI(spiel);
			name = "Protokoll";
			spiel.setKISpieler(this, (byte) (1 - farbe));
			ret = true;
			break;
		case 1:
			this.spieler = new ZufallKI();
			name = "Ivan Zufallski";
			ret = true;
			// spiel.setKISpieler(this, (byte) (1-farbe));
			break;
		default:
			name = "";
			break;
		}

		if (ret) {
			spiel.setKISpieler(this, farbe);
		} else {
			spiel.setKISpieler(null, farbe);
		}

	}

	public KISpieler(Spiel spiel, KI ki, byte farbe, String name) {
		this.kontroller = spiel;
		this.name = name;
		this.spieler = ki;
		spiel.setKISpieler(this, farbe);
	}

	public boolean machZug() {
		zug = new Zug();
		try {
			if (name.equals("Online-Partner") || name.equals("Protokoll"))
				spieler.zug(null, zug);
			else
				spieler.zug(new KIKontroller(kontroller), zug);
			return kontroller.zug(zug.getPosX(), zug.getPosY(), zug.getZielX(), zug.getZielY());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public int tauscheBauer() {
		return zug.getTausch();
	}

	public void tellMatt(Spiel spiel) {
		spieler.tellMatt(spiel);

	}
}
