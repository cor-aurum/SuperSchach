package com.superschach.superschach.ki;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.superschach.superschach.AllTests.TestGUI;
import com.superschach.superschach.AllTests.TestKI;
import com.superschach.superschach.spiel.Spiel;

@RunWith(Suite.class)
@SuiteClasses({ 
	KISchnittstelleTest.class
	})
public class KITests {
	public static TestGUI testGUI=new TestGUI();
	public static Spiel testSpiel=new Spiel(testGUI);
	public static TestKI testKi=new TestKI();
}

