package net.codersky.jsky.cli;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Consumer;

public class CLICommandManager {

	private final HashMap<String, CLICommand> commands = new HashMap<>();
	private CLIScannerThread scannerThread = null;

	public boolean registerCommand(@NotNull String id, @NotNull CLICommand command) {
		Objects.requireNonNull(id, "Command id cannot be null");
		Objects.requireNonNull(command, "Command cannot be null");
		return commands.putIfAbsent(id, command) == command;
	}

	public boolean registerConsumer(@NotNull String id, @NotNull Consumer<String[]> command) {
		return registerCommand(id, args -> { command.accept(args); return true; });
	}

	public boolean unregisterCommand(@NotNull String id) {
		return commands.remove(id) != null;
	}

	@Nullable
	public CLICommand getCommand(@NotNull String id) {
		return commands.get(id);
	}

	public boolean isRunning() {
		return scannerThread != null;
	}

	public void start() {
		if (isRunning())
			return;
		this.scannerThread = new CLIScannerThread(this);
		this.scannerThread.start();
	}

	public boolean stop() {
		if (!isRunning())
			return false;
		this.scannerThread.interrupt();
		this.scannerThread = null;
		return true;
	}

	public boolean process(@NotNull String input) {
		final String[] parts = Objects.requireNonNull(input).split(" ");
		if (parts.length == 0)
			return false;
		final CLICommand cmd = getCommand(parts[0]);
		if (cmd == null)
			return false;
		if (parts.length == 1)
			return cmd.onCommand(new String[0]);
		else
			return cmd.onCommand(Arrays.copyOfRange(parts, 1, parts.length));
	}
}
