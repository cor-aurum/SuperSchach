package com.superschach.superschach.ki;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.superschach.superschach.kontroller.figuren.Bauer;
import com.superschach.superschach.kontroller.figuren.Dame;
import com.superschach.superschach.kontroller.figuren.Janus;
import com.superschach.superschach.kontroller.figuren.JanusKoenig;
import com.superschach.superschach.kontroller.figuren.Koenig;
import com.superschach.superschach.kontroller.figuren.Laeufer;
import com.superschach.superschach.kontroller.figuren.Springer;
import com.superschach.superschach.kontroller.figuren.Turm;
import com.superschach.superschach.spiel.Spiel;
public class KISchnittstelleTest {
	
	private static KISchnittstelle ki=KITests.testKi;
	private static Spiel spiel = KITests.testSpiel;

	@Test
	public void testWert() throws Exception {
		assertEquals(100,ki.wert(new Bauer(spiel,0,0, (byte)0, null, 0)));
		assertEquals(900,ki.wert(new Dame(spiel,0,0, (byte)0, null, 0)));
		assertEquals(600,ki.wert(new Janus(spiel,0,0, (byte)0, null, 0)));
		assertEquals(0,ki.wert(new JanusKoenig(spiel,0,0, (byte)0, null, 0)));
		assertEquals(0,ki.wert(new Koenig(spiel,0,0, (byte)0, null, 0)));
		assertEquals(300,ki.wert(new Laeufer(spiel,0,0, (byte)0, null, 0)));
		assertEquals(300,ki.wert(new Springer(spiel,0,0, (byte)0, null, 0)));
		assertEquals(500,ki.wert(new Turm(spiel,0,0, (byte)0, null, 0)));
	}
	
	@Test
	public void testPlayer() throws Exception {
		assertEquals(spiel.playerFaktor(), ki.player());
	}
	
	@Test
	public void testWertPruefer() throws Exception {
		assertEquals(20000078, ki.wertPruefer());
	}
}
