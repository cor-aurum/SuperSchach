package com.superschach.superschach.ki;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

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
public class PopulationTest {

	@Test
	public void testGetBestes() throws Exception {
		Kontroller k = new Spiel(new AllTests.TestGUI());
		Population pop = new Population((byte) -1, k, 4);
		assertEquals(-10, pop.getBestes().get().getWert());
	}

	@Test
	public void testGetGroesse() throws Exception {
		Kontroller k = new Spiel(new AllTests.TestGUI());
		Population pop = new Population((byte) -1, k, 1);
		assertEquals(2, pop.getGroesse());
		pop = new Population((byte) -1, k, 4);
		assertEquals(16, pop.getGroesse());
	}

	@Test
	public void testExistiertIndividuum() throws Exception {
		Kontroller k = new Spiel(new AllTests.TestGUI());
		Population pop = new Population((byte) -1, k, 1);
		assertTrue(pop.existiertIndividuum(0, 1, 0, 2));
		assertTrue(pop.existiertIndividuum(0, 1, 0, 3));
		assertFalse(pop.existiertIndividuum(0, 1, 0, 4));
	}

	@Test
	public void testZaehleMoeglicheZuege() throws Exception {
		Kontroller k = new Spiel(new AllTests.TestGUI());
		Population pop = new Population((byte) 1, k, 0);
		Collection<int[]> zuege = pop.zaehleMoeglicheZuege(new Spiel(new AllTests.TestGUI()));
		ArrayList<int[]> list = new ArrayList<int[]>();
		list.add(new int[] { 0, 1, 0, 2 });
		list.add(new int[] { 0, 1, 0, 3 });
		list.add(new int[] { 1, 0, 0, 2 });
		list.add(new int[] { 1, 0, 2, 2 });
		list.add(new int[] { 1, 1, 1, 2 });
		list.add(new int[] { 1, 1, 1, 3 });
		list.add(new int[] { 2, 1, 2, 2 });
		list.add(new int[] { 2, 1, 2, 3 });
		list.add(new int[] { 3, 1, 3, 2 });
		list.add(new int[] { 3, 1, 3, 3 });
		list.add(new int[] { 4, 1, 4, 2 });
		list.add(new int[] { 4, 1, 4, 3 });
		list.add(new int[] { 5, 1, 5, 2 });
		list.add(new int[] { 5, 1, 5, 3 });
		list.add(new int[] { 6, 1, 6, 2 });
		list.add(new int[] { 6, 1, 6, 3 });
		list.add(new int[] { 7, 1, 7, 2 });
		list.add(new int[] { 7, 1, 7, 3 });
		list.add(new int[] { 6, 0, 5, 2 });
		list.add(new int[] { 6, 0, 7, 2 });
		for (int[] i : zuege) {
			assertTrue(list.stream().anyMatch(z -> Arrays.equals(z, i)));
		}
	}
}