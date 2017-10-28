package com.superschach.superschach.gui;

public class Blocker {

	private boolean blocked;

	public Blocker() {
		blocked = true;
	}

	public synchronized void block() {
		while (blocked)
			try {
				wait();
			} catch (InterruptedException e) {
			}
	}

	public synchronized void release() {
		blocked = false;
		notify();
	}

}