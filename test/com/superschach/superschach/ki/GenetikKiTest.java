package com.superschach.superschach.ki;

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
public class GenetikKiTest {

	@Test
	public void testZug() throws Exception {
		Kontroller k = new Spiel(new AllTests.TestGUI());
		GenetikKi ki = new GenetikKi();
		Zug zug = new Zug();
		ki.zug(k, zug);
		assertTrue(k.zugMoeglich(zug.getPosX(), zug.getPosY(), zug.getZielX(), zug.getZielY()) > 0);
	}
}