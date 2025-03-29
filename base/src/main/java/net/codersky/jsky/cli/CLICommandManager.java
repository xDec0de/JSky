package net.codersky.jsky.cli;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

public class CLICommandManager {

	private HashMap<String, CLICommand> commands = new HashMap<>();

	public boolean registerCommand(@NotNull String id, @NotNull CLICommand command) {
		Objects.requireNonNull(id, "Command id cannot be null");
		Objects.requireNonNull(command, "Command cannot be null");
		return commands.putIfAbsent(id, command) == command;
	}
}
