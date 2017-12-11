package com.superschach.superschach.kontroller.figuren;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.superschach.superschach.kontroller.Pruefer;

public class DameTest {

	private Dame dame = new Dame(FigurenTests.testSpiel, 4, 4, (byte) 0,
			new Pruefer(FigurenTests.testSpiel, new Figur[8][8], new Figur[8][8]),0);
	
	@Test
	public void testZugMoeglich() throws Exception {
		assertTrue(dame.zugMoeglich(3, 3));
		assertTrue(dame.zugMoeglich(1, 4));
		assertTrue(dame.zugMoeglich(4, 3));
		assertTrue(dame.zugMoeglich(1, 1));
		assertTrue(dame.zugMoeglich(6, 6));
	}
	
	@Test
	public void testGetCode() throws Exception {
		assertEquals('q', dame.getCode());
	}
}
