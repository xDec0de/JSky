package net.codersky.jsky.cli;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A basic <b>C</b>ommand <b>L</b>ine <b>I</b>nterface. This
 * CLI works with a {@link CLIScannerThread} in order to parse
 * commands on a separate thread that won't interrupt your
 * application's main thread.
 *
 * @since JSky 1.0.0
 *
 * @author xDec0de_
 */
public class JCLI {

	private final ArrayList<CLICommand> commands = new ArrayList<>();
	private final CLICommandPool pool;
	private CLIScannerThread scannerThread = null;
	private boolean allowBlankArgs = false;

	private Consumer<String> onUnknownCommand = cmd -> System.err.println("Unknown command: " + cmd);

	public JCLI(@Nullable CLICommandPool pool) {
		this.pool = pool;
	}

	public JCLI() {
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
	public JCLI start(@NotNull InputStream stream) {
		if (isRunning())
			return this;
		this.scannerThread = new CLIScannerThread(this);
		this.scannerThread.start();
		return this;
	}

	@NotNull
	public JCLI start() {
		return start(System.in);
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
	 - JCLI Customization
	 */

	@NotNull
	public JCLI setOnUnknownCommand(@Nullable Consumer<String> action) {
		this.onUnknownCommand = action;
		return this;
	}

	@NotNull
	public JCLI setAllowBlankArgs(boolean allowBlankArgs) {
		this.allowBlankArgs = allowBlankArgs;
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
			else if (!onQuote && c == ' ')
				appendToSplitInput(inList, arg);
			else
				arg.append(c);
		}
		if (!arg.isEmpty())
			appendToSplitInput(inList, arg);
		return inList.toArray(new String[0]);
	}

	private void appendToSplitInput(List<String> inList, StringBuilder arg) {
		final String argStr = arg.toString();
		if (allowBlankArgs || !argStr.isBlank())
			inList.add(argStr);
		arg.setLength(0);
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
