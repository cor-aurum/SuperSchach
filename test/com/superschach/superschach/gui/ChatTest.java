package com.superschach.superschach.gui;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.superschach.superschach.gui.Chat;
import com.superschach.superschach.gui.GUI;

import javafx.application.Application;

public class ChatTest {
	
	@BeforeClass
	public static void setUpClass() throws InterruptedException {
	    // Initialise Java FX

	    Thread t = new Thread("JavaFX Init Thread") {
	        public void run() {
	            Application.launch(GUI.class, new String[0]);
	        }
	    };
	    t.setDaemon(true);
	    t.start();
	    Thread.sleep(500);
	}

	@Test
	public void testNachrichtErhalten() throws Exception {
		
		Chat chat = new Chat(new GUI());
		chat.nachrichtErhalten("Hallo Welt");
		assertEquals(chat.getUngelesen(), 1);
	}
}
