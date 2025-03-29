package net.codersky.jsky.cli;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface CLICommand {

	void onCommand(@NotNull String @NotNull [] args);
}
