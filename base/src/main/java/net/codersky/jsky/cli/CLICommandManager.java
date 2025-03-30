package net.codersky.jsky.cli;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Consumer;

public class CLICommandManager {

	private final HashMap<String, CLICommand> commands = new HashMap<>();
	private final Scanner scanner = new Scanner(System.in);
	private boolean running = false;

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
		return this.running;
	}

	public void start() {
		if (isRunning())
			return;
		this.running = true;
		while (process(scanner.nextLine()))
			continue;
		this.running = false;
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
