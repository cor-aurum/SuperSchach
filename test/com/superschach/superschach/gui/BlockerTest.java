package com.superschach.superschach.gui;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.superschach.superschach.gui.Blocker;

public class BlockerTest {

	@Test
	public void testBlock() throws Exception {
		Thread t = new Thread() {
			public void run() {
				Blocker b = new Blocker();
				b.block();
			}
		};
		t.start();
		t.interrupt();
		assertTrue(t.isInterrupted());
	}

	@Test
	public void testRelease() throws Exception {
		Blocker b = new Blocker();
		Thread t = new Thread() {
			public void run() {
				b.block();
			}
		};
		t.start();
		b.release();
	}
}
