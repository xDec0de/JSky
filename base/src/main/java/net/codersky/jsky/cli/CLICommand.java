package net.codersky.jsky.cli;

import net.codersky.jsky.collections.JCollections;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public abstract class CLICommand {

	private final String name;
	private final String[] aliases;

	public CLICommand(@NotNull String name, @NotNull String @NotNull ... aliases) {
		this.name = Objects.requireNonNull(name, "CLICommand name cannot be null");
		this.aliases = Objects.requireNonNull(aliases, "CLICommand aliases array cannot be null");
		for (String alias : aliases)
			Objects.requireNonNull(alias, "CLICommand alias cannot be null");
	}

	public CLICommand(@NotNull String name) {
		this(name, new String[0]);
	}

	public String getName() {
		return name;
	}

	public String[] getAliases() {
		return aliases;
	}

	public boolean matches(@NotNull String name) {
		return name.equalsIgnoreCase(this.name) ||
				JCollections.contains(this.aliases, alias -> alias.equalsIgnoreCase(name));
	}

	public boolean conflictsWith(@NotNull CLICommand other) {
		if (matches(other.getName()))
			return false;
		for (String alias : other.getAliases())
			if (matches(alias))
				return true;
		return false;
	}

	public abstract boolean onCommand(@NotNull String @NotNull [] args);

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof final CLICommand other)
			return Objects.equals(name, other.name) && Objects.deepEquals(aliases, other.aliases);
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, Arrays.hashCode(aliases));
	}
}
