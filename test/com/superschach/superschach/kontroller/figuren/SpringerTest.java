package com.superschach.superschach.kontroller.figuren;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.superschach.superschach.kontroller.Pruefer;

public class SpringerTest {

	private Springer springer = new Springer(FigurenTests.testSpiel, 4, 4, (byte) 0,
			new Pruefer(FigurenTests.testSpiel, new Figur[8][8], new Figur[8][8]),0);

	@Test
	public void testToString() throws Exception {
		assertEquals("Springer", springer.toString());
	}
	
	@Test
	public void testZugMoeglich() throws Exception {
		assertFalse(springer.zugMoeglich(3, 3));
		assertFalse(springer.zugMoeglich(1, 4));
		assertFalse(springer.zugMoeglich(4, 3));
		assertFalse(springer.zugMoeglich(1, 1));
		assertFalse(springer.zugMoeglich(6, 6));
		assertTrue(springer.zugMoeglich(6, 5));
		assertTrue(springer.zugMoeglich(5, 6));
		assertTrue(springer.zugMoeglich(3, 2));
	}
	
	@Test
	public void testMoeglicheZiele() throws Exception {
		assertEquals(0, springer.moeglicheZiele());
	}
	
	@Test
	public void testGetCode() throws Exception {
		assertEquals('n', springer.getCode());
	}
}
