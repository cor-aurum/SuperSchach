package com.superschach.superschach.ki;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.superschach.superschach.spiel.Spiel;
public class KISchnittstelleTest {
	
	private static KISchnittstelle ki=KITests.testKi;
	private static Spiel spiel = KITests.testSpiel;
	
	@Test
	public void testPlayer() throws Exception {
		assertEquals(spiel.playerFaktor(), ki.player());
	}
	
	@Test
	public void testWertPruefer() throws Exception {
		assertEquals(20000078, ki.wertPruefer());
	}
}
