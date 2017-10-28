package com.superschach.superschach.kontroller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PrueferTest {

	Kontroller kontroller = KontrollerTests.testSpiel;
	Pruefer pruefer =new Pruefer(kontroller, kontroller.figur, kontroller.figurListe);
	@Test
	public void testGerade() throws Exception {
		assertTrue(pruefer.gerade(2, 2, 2, 4));
		assertTrue(pruefer.gerade(2, 2, 4, 2));
		assertFalse(pruefer.gerade(2, 2, 4, 4));
	}
	
	@Test
	public void testFrei() throws Exception {
		assertTrue(pruefer.frei(0, 1, 0, 4));
		assertTrue(pruefer.frei(2, 2, 4, 2));
		assertFalse(pruefer.frei(0, 0, 0, 4));
	}
	
	@Test
	public void testDiagonal() throws Exception {
		assertFalse(pruefer.diagonal(2, 2, 2, 4));
		assertFalse(pruefer.diagonal(2, 2, 4, 2));
		assertTrue(pruefer.diagonal(2, 2, 4, 4));
		assertTrue(pruefer.diagonal(2, 2, 4, 0));
	}
	
	@Test
	public void testDiffEins() throws Exception {
		assertFalse(pruefer.diffEins(2, 2, 2, 4));
		assertFalse(pruefer.diffEins(2, 2, 4, 2));
		assertTrue(pruefer.diffEins(2, 2, 3, 2));
		assertTrue(pruefer.diffEins(2, 2, 3, 3));
	}
	
	@Test
	public void testIstSchach() throws Exception {
		assertFalse(pruefer.istSchach());
	}
	
	@Test
	public void testIstBedroht() throws Exception {
		assertFalse(pruefer.istBedroht(4,3));
		assertTrue(pruefer.istBedroht(4,5));
	}
	
	@Test
	public void testKeinZugMoeglich() throws Exception {
		assertFalse(pruefer.keinZugMoeglich());
	}
	
	@Test
	public void testRemis() throws Exception {
		assertFalse(pruefer.remis());
	}
	
	@Test
	public void testAusSchach() throws Exception {
		assertFalse(pruefer.ausSchach(4,0,4,5));
	}
	
	@Test
	public void testSpringer() throws Exception {
		assertFalse(pruefer.springer(2, 2, 2, 4));
		assertFalse(pruefer.springer(2, 2, 4, 2));
		assertTrue(pruefer.springer(2, 2, 3, 4));
		assertTrue(pruefer.springer(2, 2, 4, 3));
	}
}
