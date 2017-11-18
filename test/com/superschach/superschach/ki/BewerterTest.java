package com.superschach.superschach.ki;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.superschach.superschach.ki.GenerikKi.Bewerter;
import com.superschach.superschach.kontroller.Pruefer;
import com.superschach.superschach.kontroller.figuren.Bauer;
import com.superschach.superschach.kontroller.figuren.Dame;
import com.superschach.superschach.kontroller.figuren.Figur;
import com.superschach.superschach.kontroller.figuren.FigurenTests;
import com.superschach.superschach.kontroller.figuren.Laeufer;
import com.superschach.superschach.kontroller.figuren.Springer;
import com.superschach.superschach.kontroller.figuren.Turm;

/**
 * Diese Klasse ist im Rahmen meiner Studienarbeit entstanden
 * 
 * @author felix
 *
 */
public class BewerterTest {

	@Test
	public void testMinimax() throws Exception {
		Figur[] f = new Figur[4];
		f[0] = new Turm(FigurenTests.testSpiel, 4, 4, (byte) 1,
				new Pruefer(FigurenTests.testSpiel, new Figur[8][8], new Figur[8][8]), 0);
		f[1] = new Springer(FigurenTests.testSpiel, 4, 4, (byte) 1,
				new Pruefer(FigurenTests.testSpiel, new Figur[8][8], new Figur[8][8]), 0);
		f[2] = new Bauer(FigurenTests.testSpiel, 4, 4, (byte) -1,
				new Pruefer(FigurenTests.testSpiel, new Figur[8][8], new Figur[8][8]), 0);
		f[3] = new Laeufer(FigurenTests.testSpiel, 4, 4, (byte) -1,
				new Pruefer(FigurenTests.testSpiel, new Figur[8][8], new Figur[8][8]), 0);
		Bewerter b=new Bewerter();
		assertEquals(4, b.minimax(f));
		f[3]=null;
		assertEquals(7, b.minimax(f));
		f[3]=new Dame(FigurenTests.testSpiel, 4, 4, (byte) -1,
				new Pruefer(FigurenTests.testSpiel, new Figur[8][8], new Figur[8][8]), 0);
		assertEquals(-2, b.minimax(f));
	}
	
	@Test
	public void testBewerte() throws Exception {
		Figur[][] f = new Figur[2][2];
		f[0][0] = new Turm(FigurenTests.testSpiel, 4, 4, (byte) 1,
				new Pruefer(FigurenTests.testSpiel, new Figur[8][8], new Figur[8][8]), 0);
		f[0][1] = new Springer(FigurenTests.testSpiel, 4, 4, (byte) 1,
				new Pruefer(FigurenTests.testSpiel, new Figur[8][8], new Figur[8][8]), 0);
		f[1][0] = new Bauer(FigurenTests.testSpiel, 4, 4, (byte) -1,
				new Pruefer(FigurenTests.testSpiel, new Figur[8][8], new Figur[8][8]), 0);
		f[1][1] = new Laeufer(FigurenTests.testSpiel, 4, 4, (byte) -1,
				new Pruefer(FigurenTests.testSpiel, new Figur[8][8], new Figur[8][8]), 0);
		Bewerter b=new Bewerter();
		assertEquals(400, b.bewerte(f));
		f[1][1]=null;
		assertEquals(700, b.bewerte(f));
		f[1][1]=new Dame(FigurenTests.testSpiel, 4, 4, (byte) -1,
				new Pruefer(FigurenTests.testSpiel, new Figur[8][8], new Figur[8][8]), 0);
		assertEquals(-200, b.bewerte(f));
	}
	
}
