package com.superschach.superschach.kontroller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.superschach.superschach.spiel.Spiel;

public class KontrollerTest {

	Kontroller kontroller = KontrollerTests.testSpiel;

	@Test
	public void testMachFigur() throws Exception {
		assertEquals(8, kontroller.figur[0][1].nummer);
		assertEquals(1, kontroller.figur[0][0].nummer);
	}

	@Test
	public void testAnzFiguren() throws Exception {
		assertEquals((byte) 8, kontroller.anzFiguren((byte) 0, 5));
		assertEquals((byte) 8, kontroller.anzFiguren(5));
	}

	@Test
	public void testLetzterZug() throws Exception {
		assertEquals(kontroller.letzterZug(0), kontroller.letzterZug()[0]);
	}

	@Test
	public void testGetFen() throws Exception {
		assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 "+(kontroller.gebeZugAnz()+1), kontroller.getFen());
	}

	@Test
	public void testGebeZugAnz() throws Exception {
		int t=kontroller.gebeZugAnz();
		assertTrue(kontroller.zug(1, 0, 0, 2));
		assertTrue(kontroller.zug(1, 7, 0, 5));
		assertTrue(kontroller.zug(0, 2, 1, 0));
		assertTrue(kontroller.zug(0, 5, 1, 7));
		assertEquals(t+4, kontroller.gebeZugAnz());
	}
	@Test
	public void testWurdeBewegt() throws Exception {
		assertEquals(false, kontroller.wurdeBewegt(0, 0));
	}

	@Test
	public void testLoesche() throws Exception {
		kontroller.loesche(7, 0);
		assertEquals((byte) 1, kontroller.anzFiguren((byte) 0, 3));
		try {
			kontroller.machTurm(7, 0);
		} catch (Exception e) {
		}
	}
	
	@Test
	public void testZug() throws Exception {
		assertTrue(kontroller.zug(1, 0, 0, 2));
		assertTrue(kontroller.zug(1, 7, 0, 5));
		assertTrue(kontroller.zug(0, 2, 1, 0));
		assertTrue(kontroller.zug(0, 5, 1, 7));
	}
	
	@Test
	public void testEquals() throws Exception {
		assertTrue(new Spiel(KontrollerTests.testGUI).equals(new Spiel(KontrollerTests.testGUI)));
	}
}
