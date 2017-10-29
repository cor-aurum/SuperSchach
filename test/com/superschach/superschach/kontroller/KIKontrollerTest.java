package com.superschach.superschach.kontroller;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.superschach.superschach.kontroller.KIKontroller;

public class KIKontrollerTest {

	@Test
	public void testCopy() throws Exception {
		KIKontroller kontroller=new KIKontroller(KontrollerTests.testSpiel);
		assertEquals(KontrollerTests.testSpiel.getFigurListe().length,kontroller.getFigurListe().length);
	}
}
