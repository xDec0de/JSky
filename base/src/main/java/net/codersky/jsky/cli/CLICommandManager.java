package net.codersky.jsky.cli;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class CLICommandManager {

	private final HashMap<String, CLICommand> commands = new HashMap<>();

	public boolean registerCommand(@NotNull String id, @NotNull CLICommand command) {
		Objects.requireNonNull(id, "Command id cannot be null");
		Objects.requireNonNull(command, "Command cannot be null");
		return commands.putIfAbsent(id, command) == command;
	}

	public boolean unregisterCommand(@NotNull String id) {
		return commands.remove(id) != null;
	}

	@Nullable
	public CLICommand getCommand(@NotNull String id) {
		return commands.get(id);
	}

	public boolean process(@NotNull String input) {
		final String[] parts = Objects.requireNonNull(input).split(" ");
		if (parts.length == 0)
			return false;
		final CLICommand cmd = getCommand(parts[0]);
		if (cmd == null)
			return false;
		if (parts.length == 1)
			cmd.onCommand(new String[0]);
		else
			cmd.onCommand(Arrays.copyOfRange(parts, 1, parts.length));
		return true;
	}
}
