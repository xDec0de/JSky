package net.codersky.jsky.cli;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.Scanner;

class CLIScannerThread extends Thread {

    private final InputStream iStream;
    private final JCLI cli;

    CLIScannerThread(@NotNull InputStream iStream, @NotNull JCLI cli) {
        this.iStream = iStream;
        this.cli = cli;
    }

    @Override
    public void run() {
        final Scanner scanner = new Scanner(iStream);
        while (!isInterrupted() && scanner.hasNextLine())
            cli.process(scanner.nextLine());
    }
}
