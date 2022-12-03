package game;

public class ThreadSlapper extends Thread {

	private Thread thread;

	public ThreadSlapper(Thread daemonThread) {
		this.thread = daemonThread;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			thread.interrupt();
		}
	}

}
