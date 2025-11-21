package ttt_game_service.infrastructure.tests;

public class Synchroniser {

	private boolean syncDone;
	
	public Synchroniser() {
		syncDone = false;
	}
	
	public synchronized void awaitSync() throws InterruptedException {
		while (!syncDone) {
			wait();
		}
	}
	
	public synchronized void notifySync() {
		syncDone = true;
		notifyAll();
	}
}
