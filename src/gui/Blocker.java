package gui;

public class Blocker {

	private boolean blocked;
	
	public Blocker() {
		blocked=true;
	}
	
	public synchronized void block()
	{
		while(blocked)
		try {
			wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public synchronized void release()
	{
		blocked=false;
		notify();
	}

}
