package com.superschach.superschach.ki;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.stream.Collectors;

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
		ArrayList<Moeglichkeit> list = new ArrayList<Moeglichkeit>();
		list.add(new Moeglichkeit(new int[] { 0, 1, 0, 2 }, k));
		list.add(new Moeglichkeit(new int[] { 0, 1, 0, 3 }, k));
		list.add(new Moeglichkeit(new int[] { 1, 0, 0, 2 }, k));
		list.add(new Moeglichkeit(new int[] { 1, 0, 2, 2 }, k));
		list.add(new Moeglichkeit(new int[] { 1, 1, 1, 2 }, k));
		list.add(new Moeglichkeit(new int[] { 1, 1, 1, 3 }, k));
		list.add(new Moeglichkeit(new int[] { 2, 1, 2, 2 }, k));
		list.add(new Moeglichkeit(new int[] { 2, 1, 2, 3 }, k));
		list.add(new Moeglichkeit(new int[] { 3, 1, 3, 2 }, k));
		list.add(new Moeglichkeit(new int[] { 3, 1, 3, 3 }, k));
		list.add(new Moeglichkeit(new int[] { 4, 1, 4, 2 }, k));
		list.add(new Moeglichkeit(new int[] { 4, 1, 4, 3 }, k));
		list.add(new Moeglichkeit(new int[] { 5, 1, 5, 2 }, k));
		list.add(new Moeglichkeit(new int[] { 5, 1, 5, 3 }, k));
		list.add(new Moeglichkeit(new int[] { 6, 1, 6, 2 }, k));
		list.add(new Moeglichkeit(new int[] { 6, 1, 6, 3 }, k));
		list.add(new Moeglichkeit(new int[] { 7, 1, 7, 2 }, k));
		list.add(new Moeglichkeit(new int[] { 7, 1, 7, 3 }, k));
		list.add(new Moeglichkeit(new int[] { 6, 0, 5, 2 }, k));
		list.add(new Moeglichkeit(new int[] { 6, 0, 7, 2 }, k));
		assertEquals(20, ki.getMoeglicheZuege(k).collect(Collectors.toList()).size());
		ki.getMoeglicheZuege(k).forEach(m -> {
			assertTrue(list.contains(m));
		});
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
		assertEquals(0, ki.versucheZug(new Moeglichkeit(new int[] { 0, 1, 0, 2 }, k), 1));
		assertTrue(k.equals(new Spiel(new AllTests.TestGUI())));
	}
}
