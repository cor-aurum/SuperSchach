package com.superschach.superschach.kontroller.figuren;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.superschach.superschach.kontroller.Pruefer;

public class TurmTest {

	private Turm turm = new Turm(FigurenTests.testSpiel, 4, 4, (byte) 0,
			new Pruefer(FigurenTests.testSpiel, new Figur[8][8], new Figur[8][8]),0);

	@Test
	public void testToString() throws Exception {
		assertEquals("Turm", turm.toString());
	}
	
	@Test
	public void testZugMoeglich() throws Exception {
		assertTrue(turm.zugMoeglich(1, 4));
		assertTrue(turm.zugMoeglich(4, 3));
		assertFalse(turm.zugMoeglich(3, 3));
	}
	
	@Test
	public void testMoeglicheZiele() throws Exception {
		assertEquals(0, turm.moeglicheZiele());
	}
	
	@Test
	public void testGetCode() throws Exception {
		assertEquals('r', turm.getCode());
	}
}
