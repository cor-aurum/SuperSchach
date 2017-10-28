package com.superschach.superschach.kontroller;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.superschach.superschach.AllTests.TestGUI;
import com.superschach.superschach.spiel.Spiel;

@RunWith(Suite.class)
@SuiteClasses({ 
	KIKontrollerTest.class,
	KontrollerTest.class,
	PrueferTest.class
	})
public class KontrollerTests {
	public static TestGUI testGUI=new TestGUI();
	public static Spiel testSpiel=new Spiel(testGUI);
}
