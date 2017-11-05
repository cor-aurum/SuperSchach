package com.superschach.superschach.kontroller.figuren;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.superschach.superschach.kontroller.Pruefer;

public class BauerTest {

	private Bauer bauer = new Bauer(FigurenTests.testSpiel, 4, 4, (byte) -1,
			new Pruefer(FigurenTests.testSpiel, new Figur[8][8], new Figur[8][8]),0);

	@Test
	public void testToString() throws Exception {
		assertEquals("Bauer", bauer.toString());
	}
	
	@Test
	public void testZugMoeglich() throws Exception {
		assertTrue(bauer.zugMoeglich(4, 3));
		assertFalse(bauer.zugMoeglich(4, 5));
		assertFalse(bauer.zugMoeglich(4, 4));
		assertTrue(bauer.zugMoeglich(4, 2));
		assertFalse(bauer.zugMoeglich(4, 1));
	}
	
	@Test
	public void testMoeglicheZiele() throws Exception {
		assertEquals(0, bauer.moeglicheZiele());
	}
	
	@Test
	public void testGetCode() throws Exception {
		assertEquals('p', bauer.getCode());
	}
	
	@Test
	public void testEnPassantMoeglich() throws Exception {
		assertFalse(bauer.enPassantMoeglich(4, 2));
	}
	
	
}
