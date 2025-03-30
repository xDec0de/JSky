package net.codersky.jsky.cli;

import java.io.IOException;
import java.util.Scanner;

class CLIScannerThread extends Thread {

	private final CLICommandManager manager;
	private boolean running = false;

	CLIScannerThread(CLICommandManager manager) {
		this.manager = manager;
	}

	@Override
	public void run() {
		running = true;
		final Scanner scanner = new Scanner(System.in);
		while (running) {
			try {
				if (System.in.available() == 0)
					continue;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			if (!manager.process(scanner.nextLine()))
				running = false;
		}
	}

	@Override
	public void interrupt() {
		this.running = false;
		super.interrupt();
	}
}
