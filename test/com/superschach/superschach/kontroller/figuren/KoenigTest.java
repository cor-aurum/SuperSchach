package com.superschach.superschach.kontroller.figuren;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.superschach.superschach.kontroller.Pruefer;

public class KoenigTest {

	private Koenig koenig = new Koenig(FigurenTests.testSpiel, 4, 4, (byte) 0,
			new Pruefer(FigurenTests.testSpiel, new Figur[8][8], new Figur[8][8]),0);

	@Test
	public void testToString() throws Exception {
		assertEquals("König", koenig.toString());
	}
	
	@Test
	public void testZugMoeglich() throws Exception {
		assertTrue(koenig.zugMoeglich(3, 4));
		assertTrue(koenig.zugMoeglich(4, 3));
		assertTrue(koenig.zugMoeglich(3, 3));
		assertFalse(koenig.zugMoeglich(4, 2));
	}
	
	@Test
	public void testFreiUnbedroht() throws Exception {
		assertTrue(koenig.freiUnbedroht(3, 4));
	}
	
	@Test
	public void testRochadeMoeglich() throws Exception {
		assertFalse(koenig.rochadeMoeglich(3, 4));
	}
	
	@Test
	public void testMoeglicheZiele() throws Exception {
		assertEquals(0, koenig.moeglicheZiele());
	}
	
	@Test
	public void testGetCode() throws Exception {
		assertEquals('k', koenig.getCode());
	}
}
