package com.superschach.superschach.ki;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.superschach.superschach.AllTests;
import com.superschach.superschach.kontroller.Kontroller;
import com.superschach.superschach.spiel.Spiel;

/**
 * Diese Klasse ist im Rahmen meiner Studienarbeit entstanden
 * 
 * @author felix
 *
 */
public class NumerikKITest {
	@Test
	public void testGetMoeglicheZuege() {
		Kontroller k = new Spiel(new AllTests.TestGUI());
		NumerikKI ki = new NumerikKI();
		StringBuilder t = new StringBuilder();
		ki.getMoeglicheZuege(k).forEach(t::append);
		String s = "Zug von: 0 1 nach: 0 2Zug von: 0 1 nach: 0 3Zug von: 1 0 nach: 0 2Zug von: 1 0 nach: 2 2Zug von: 1 1 nach: 1 2Zug von: 1 1 nach: 1 3Zug von: 2 1 nach: 2 2Zug von: 2 1 nach: 2 3Zug von: 3 1 nach: 3 2Zug von: 3 1 nach: 3 3Zug von: 4 1 nach: 4 2Zug von: 4 1 nach: 4 3Zug von: 5 1 nach: 5 2Zug von: 5 1 nach: 5 3Zug von: 6 0 nach: 5 2Zug von: 6 1 nach: 6 2Zug von: 6 1 nach: 6 3";
		assertEquals(s, t.toString());
		k.zug(0, 1, 0, 2);
		t = new StringBuilder();
		ki.getMoeglicheZuege(k).forEach(t::append);

		s = "Zug von: 0 6 nach: 0 4Zug von: 0 6 nach: 0 5Zug von: 1 6 nach: 1 4Zug von: 1 6 nach: 1 5Zug von: 1 7 nach: 0 5Zug von: 1 7 nach: 2 5Zug von: 2 6 nach: 2 4Zug von: 2 6 nach: 2 5Zug von: 3 6 nach: 3 4Zug von: 3 6 nach: 3 5Zug von: 4 6 nach: 4 4Zug von: 4 6 nach: 4 5Zug von: 5 6 nach: 5 4Zug von: 5 6 nach: 5 5Zug von: 6 6 nach: 6 4Zug von: 6 6 nach: 6 5Zug von: 6 7 nach: 5 5";
		assertEquals(s, t.toString());
	}

	@Test
	public void testZug() throws Exception {
		Kontroller k = new Spiel(new AllTests.TestGUI());
		NumerikKI ki = new NumerikKI();
		Zug zug = new Zug();
		ki.zug(k, zug);
		assertTrue(k.zugMoeglich(zug.getPosX(), zug.getPosY(), zug.getZielX(), zug.getZielY()) > 0);
	}

	@Test
	public void testVersucheZug() throws Exception {
		Kontroller k = new Spiel(new AllTests.TestGUI());
		NumerikKI ki = new NumerikKI();
		assertEquals(0, ki.versucheZug(new Moeglichkeit(new int[] { 0, 1, 0, 2 }, k),1));
		assertTrue(k.equals(new Spiel(new AllTests.TestGUI())));
	}
}
