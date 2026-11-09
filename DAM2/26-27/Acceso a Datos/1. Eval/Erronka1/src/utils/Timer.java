package utils;

import javax.swing.SwingUtilities;

public class Timer extends Thread {
	private int tickRate;
	private Runnable tick;
	private volatile boolean hasita = false;
	private volatile boolean geldituta = false;
	private volatile boolean amaituta = false;

	public Timer(int tickRate, Runnable tick) {
		this.tickRate = tickRate;
		this.tick = tick;
	}

	public synchronized void gelditu() {
		geldituta = true;
	}

	public synchronized void jarraitu() {
		geldituta = false;
		notifyAll();
	}

	public synchronized void amaitu() {
		amaituta = true;
		notifyAll();
		interrupt();
	}

	public boolean isHasita() {
		return hasita;
	}

	public void hasi() {
		if (!hasita) {
			hasita = true;
			start();
		}
	}

	@Override
	public void run() {
		while (!amaituta) {
			try {
				synchronized (this) {
					while (geldituta && !amaituta) {
						wait();
					}
				}
				if (amaituta) return;

				Thread.sleep(tickRate);

				if (!geldituta && !amaituta) {
					SwingUtilities.invokeLater(tick);
				}
			} catch (InterruptedException e) {
				if (amaituta)
					return;
			}
		}
	}
}
