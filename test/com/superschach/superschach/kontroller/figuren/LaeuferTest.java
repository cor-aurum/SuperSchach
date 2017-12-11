package com.superschach.superschach.kontroller.figuren;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.superschach.superschach.kontroller.Pruefer;

public class LaeuferTest {

	private Laeufer laeufer = new Laeufer(FigurenTests.testSpiel, 4, 4, (byte) 0,
			new Pruefer(FigurenTests.testSpiel, new Figur[8][8], new Figur[8][8]),0);
	
	@Test
	public void testZugMoeglich() throws Exception {
		assertFalse(laeufer.zugMoeglich(1, 4));
		assertFalse(laeufer.zugMoeglich(4, 3));
		assertTrue(laeufer.zugMoeglich(3, 3));
		assertTrue(laeufer.zugMoeglich(6, 6));
	}
	
	@Test
	public void testGetCode() throws Exception {
		assertEquals('b', laeufer.getCode());
	}
}
