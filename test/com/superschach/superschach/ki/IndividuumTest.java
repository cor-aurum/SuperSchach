package com.superschach.superschach.ki;

import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import com.superschach.superschach.AllTests;
import com.superschach.superschach.kontroller.Kontroller;
import com.superschach.superschach.spiel.Spiel;

/**
 * Diese Klasse ist im Rahmen meiner Studienarbeit entstanden
 * 
 * @author felix
 *
 */
public class IndividuumTest {

	
	@Test
	public void testCreateZufall() throws Exception {
		Kontroller kontroller = new Spiel(new AllTests.TestGUI());
		
		Individuum i=Individuum.createZufall(kontroller, 0,new Population((byte)1,kontroller,0).zaehleMoeglicheZuege(kontroller),(byte)1);
		assertNotEquals(null, i);
	}
}
