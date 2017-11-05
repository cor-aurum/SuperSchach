package com.superschach.superschach.kontroller.figuren;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.superschach.superschach.AllTests.TestGUI;
import com.superschach.superschach.spiel.Spiel;

@RunWith(Suite.class)
@SuiteClasses({ 
	DameTest.class,
	TurmTest.class,
	SpringerTest.class,
	LaeuferTest.class,
	BauerTest.class,
	KoenigTest.class,
	FigurTest.class
	})
public class FigurenTests {
	public static TestGUI testGUI=new TestGUI();
	public static Spiel testSpiel=new Spiel(testGUI);
}
