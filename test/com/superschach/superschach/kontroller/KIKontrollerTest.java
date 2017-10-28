package com.superschach.superschach.kontroller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import com.superschach.superschach.ki.KITests;

public class KIKontrollerTest {

	@Test
	public void testCopy() throws Exception {
		KIKontroller kontroller=new KIKontroller(KITests.testSpiel);
		assertEquals(KITests.testSpiel.figurListe.length,kontroller.figurListe.length);
	}
}
