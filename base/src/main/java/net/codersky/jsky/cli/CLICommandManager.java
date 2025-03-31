package net.codersky.jsky.cli;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CLICommandManager {

	private final ArrayList<CLICommand> commands = new ArrayList<>();
	private CLIScannerThread scannerThread = null;

	public synchronized boolean registerCommand(@NotNull CLICommand command) {
		Objects.requireNonNull(command, "Command cannot be null");
		for (CLICommand registered : commands)
			if (command.conflictsWith(registered))
				return false;
		return commands.add(command);
	}

	public synchronized boolean registerCommand(@NotNull String name, @NotNull Predicate<String[]> command) {
		return registerCommand(new CLICommand(name) {
			@Override
			public boolean onCommand(@NotNull String @NotNull [] args) {
				return command.test(args);
			}
		});
	}

	public synchronized boolean registerCommand(@NotNull String name, @NotNull Consumer<String[]> command) {
		return registerCommand(name, args -> { command.accept(args); return true; });
	}

	public synchronized boolean unregisterCommand(@NotNull String name) {
		final CLICommand cmd = getCommand(name);
		return cmd != null && commands.remove(cmd);
	}

	@Nullable
	public synchronized CLICommand getCommand(@NotNull String name) {
		for (CLICommand cmd : this.commands)
			if (cmd.matches(name))
				return cmd;
		return null;
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
			return true;
		final CLICommand cmd = getCommand(parts[0]);
		if (cmd == null)
			return true;
		if (parts.length == 1)
			return cmd.onCommand(new String[0]);
		else
			return cmd.onCommand(Arrays.copyOfRange(parts, 1, parts.length));
	}
}
