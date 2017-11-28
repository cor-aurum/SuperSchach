package com.superschach.superschach.ki;

import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import com.superschach.superschach.kontroller.Kontroller;
import com.superschach.superschach.kontroller.KontrollerTests;

/**
 * Diese Klasse ist im Rahmen meiner Studienarbeit entstanden
 * 
 * @author felix
 *
 */
public class IndividuumTest {

	
	@Test
	public void testCreateZufall() throws Exception {
		Kontroller kontroller = KontrollerTests.testSpiel;
		Individuum i=Individuum.createZufall(kontroller, 1,new Population((byte)1,kontroller).zaehleMoeglicheZuege(),(byte)1);
		assertNotEquals(null, i);
	}
}
