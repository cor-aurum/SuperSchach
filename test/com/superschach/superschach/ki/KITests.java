package com.superschach.superschach.ki;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.superschach.superschach.spiel.AbstractGUI;
import com.superschach.superschach.spiel.Spiel;

@RunWith(Suite.class)
@SuiteClasses({ 
	KISchnittstelleTest.class
	})
public class KITests {
	
	public static TestGUI testGUI=new TestGUI();
	public static Spiel testSpiel=new Spiel(testGUI);
	
	public static class TestKI extends KISchnittstelle{

		public TestKI() {
			super(testSpiel);
		}

		@Override
		public boolean machZug() {
			return false;
		}
		
	}
	
	public static class TestGUI extends AbstractGUI{

		@Override
		public boolean sollThread() {
			return false;
		}

		@Override
		public void leaveLobby() {
		}

		@Override
		public int figurMenu() {
			return 0;
		}

		@Override
		public void meldungAusgeben(String meldung) {
		}

		@Override
		public void aktualisieren(int x, int y) {		
		}

		@Override
		public void aktualisieren() {
		}

		@Override
		public void nachricht(String s) {
		}

		@Override
		public String[] getLogin(boolean falsch) {
			return null;
		}

		@Override
		public void gegnerSpielVerlassen() {
		}

		@Override
		public void herausforderungAbbrechen(int herausforderungID) {
		}	
	}
	public static TestKI testKi=new TestKI();
	
}

