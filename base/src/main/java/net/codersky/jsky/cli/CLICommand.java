package net.codersky.jsky.cli;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface CLICommand {

	boolean onCommand(@NotNull String @NotNull [] args);
}
