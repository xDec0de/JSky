package net.codersky.jsky.cli;

import java.io.IOException;
import java.util.Scanner;

class CLIScannerThread extends Thread {

	private final JCLI cli;

	CLIScannerThread(JCLI cli) {
		this.cli = cli;
	}

	@Override
	public void run() {
		final Scanner scanner = new Scanner(System.in);
		while (!isInterrupted()) {
			try {
				if (System.in.available() == 0)
					continue;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			cli.process(scanner.nextLine());
		}
	}
}
