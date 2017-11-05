package com.superschach.superschach.kontroller.figuren;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.superschach.superschach.kontroller.Pruefer;

public class FigurTest {

	private Turm turm = new Turm(FigurenTests.testSpiel, 4, 4, (byte) 0,
			new Pruefer(FigurenTests.testSpiel, new Figur[8][8], new Figur[8][8]), 0);

	@Test
	public void testDiagonalFreiNegativNegativ() throws Exception {
		assertEquals(7, turm.diagonalFreiNegativNegativ());
	}

	@Test
	public void testDiagonalFreiPositivNegativ() throws Exception {
		assertEquals(6, turm.diagonalFreiPositivNegativ());
	}

	@Test
	public void testDiagonalFreiNegativPositiv() throws Exception {
		assertEquals(7, turm.diagonalFreiNegativPositiv());
	}

	@Test
	public void testDiagonalFreiPositivPositiv() throws Exception {
		assertEquals(6, turm.diagonalFreiPositivPositiv());
	}

	@Test
	public void testGeradeFreiYMinus() throws Exception {
		assertEquals(2, turm.geradeFreiYMinus());
	}

	@Test
	public void testGeradeFreiXMinus() throws Exception {
		assertEquals(0, turm.geradeFreiXMinus());
	}

	@Test
	public void testGeradeFreiYPlus() throws Exception {
		assertEquals(6, turm.geradeFreiYPlus());
	}

	@Test
	public void testGeradeFreiXPlus() throws Exception {
		assertEquals(7, turm.geradeFreiXPlus());
	}

	@Test
	public void testSetzeBewegt() throws Exception {
		turm.setzeBewegt(true);
		assertEquals(1, turm.bewegt());
		turm.setzeBewegt(0);
		assertEquals(0, turm.bewegt());
	}
}
