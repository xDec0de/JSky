package net.codersky.jsky.cli;

import net.codersky.jsky.strings.JStrings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CLICommandManager {

	private final ArrayList<CLICommand> commands = new ArrayList<>();
	private final CLICommandPool pool;
	private CLIScannerThread scannerThread = null;

	private Consumer<String> onUnknownCommand = cmd -> System.err.println("Unknown command: " + cmd);

	public CLICommandManager(@Nullable CLICommandPool pool) {
		this.pool = pool;
	}

	public CLICommandManager() {
		this(null);
	}

	/*
	 - Command registration
	 */

	public synchronized boolean registerCommand(@NotNull CLICommand command) {
		Objects.requireNonNull(command, "Command cannot be null");
		for (CLICommand registered : commands)
			if (command.conflictsWith(registered))
				return false;
		return commands.add(command);
	}

	public synchronized boolean registerPredicate(@NotNull String name, @NotNull Predicate<String[]> command) {
		return registerCommand(new CLICommand(name) {
			@Override
			public boolean onCommand(@NotNull String @NotNull [] args) {
				return command.test(args);
			}
		});
	}

	public synchronized boolean registerConsumer(@NotNull String name, @NotNull Consumer<String[]> command) {
		return registerPredicate(name, args -> { command.accept(args); return true; });
	}

	public synchronized boolean unregisterCommand(@NotNull String name) {
		final CLICommand cmd = getCommand(name);
		return cmd != null && commands.remove(cmd);
	}

	/*
	 - Command getter
	 */

	@Nullable
	public synchronized CLICommand getCommand(@NotNull String name) {
		for (CLICommand cmd : this.commands)
			if (cmd.matches(name))
				return cmd;
		return null;
	}

	/*
	 - Start / stop
	 */

	public boolean isRunning() {
		return scannerThread != null;
	}

	@NotNull
	public CLICommandManager start() {
		if (isRunning())
			return this;
		this.scannerThread = new CLIScannerThread(this);
		this.scannerThread.start();
		return this;
	}

	public boolean stop() {
		if (!isRunning())
			return false;
		this.scannerThread.interrupt();
		this.scannerThread = null;
		return true;
	}

	/*
	 - Command pool
	 */

	@Nullable
	public synchronized CLICommandPool getPool() {
		return pool;
	}

	/*
	 - CLI Customization
	 */

	@NotNull
	public CLICommandManager setOnUnknownCommand(@Nullable Consumer<String> action) {
		this.onUnknownCommand = action;
		return this;
	}

	/*
	 - Input processing
	 */

	private String[] splitInput(@NotNull String input) {
		if (input.isBlank())
			return null;
		final StringBuilder arg = new StringBuilder();
		final List<String> inList = new ArrayList<>();
		final String in = normalizeInput(input);
		final int len = in.length();
		boolean onQuote = false;
		for (int i = 0; i < len; i++) {
			char c = in.charAt(i);
			if (c == '\\' && ((i + 1) < len) && (in.charAt(i + 1) == '"')) {
				arg.append('"');
				i++;
			} else if (c == '"')
				onQuote = !onQuote;
			else if (!onQuote && c == ' ') {
				inList.add(arg.toString());
				arg.setLength(0);
			} else
				arg.append(c);
		}
		if (!arg.isEmpty())
			inList.add(arg.toString());
		return inList.toArray(new String[0]);
	}

	@NotNull
	private String normalizeInput(@NotNull String input) {
		final String in = Objects.requireNonNull(input).stripLeading();
		final StringBuilder normalized = new StringBuilder();
		boolean onQuote = false;
		for (int i = 0; i < in.length(); i++) {
			char c = in.charAt(i);
			if (c == '"' && (i != 0 && in.charAt(i - 1) != '\\'))
				onQuote = !onQuote;
			else if (!onQuote && (c == ' ' && in.charAt(i - 1) == ' '))
				continue;
			normalized.append(c);
		}
		return normalized.toString();
	}

	public boolean process(@NotNull String input) {
		final String[] parts = splitInput(input);
		if (parts == null)
			return false;
		final CLICommand cmd = getCommand(parts[0]);
		if (cmd == null) {
			if (onUnknownCommand != null)
				onUnknownCommand.accept(parts[0]);
			return false;
		}
		final String[] args = parts.length == 1 ? new String[0] : Arrays.copyOfRange(parts, 1, parts.length);
		if (getPool() == null)
			cmd.onCommand(args);
		else
			getPool().add(cmd, args);
		return true;
	}
}
