package net.codersky.jsky.cli;

import java.io.IOException;
import java.util.Scanner;

class CLIScannerThread extends Thread {

	private final CLICommandManager manager;
	private boolean interrupted = false;

	CLIScannerThread(CLICommandManager manager) {
		this.manager = manager;
	}

	@Override
	public void run() {
		final Scanner scanner = new Scanner(System.in);
		while (!interrupted) {
			try {
				if (System.in.available() == 0)
					continue;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			manager.process(scanner.nextLine());
		}
	}

	@Override
	public void interrupt() {
		this.interrupted = true;
		super.interrupt();
	}
}
