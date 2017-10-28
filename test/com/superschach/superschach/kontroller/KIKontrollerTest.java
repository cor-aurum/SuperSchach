package com.superschach.superschach.kontroller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

public class KIKontrollerTest {

	@Test
	public void testCopy() throws Exception {
		KIKontroller kontroller=new KIKontroller(KontrollerTests.testSpiel);
		assertEquals(KontrollerTests.testSpiel.figurListe.length,kontroller.figurListe.length);
	}
}
