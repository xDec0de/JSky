package net.codersky.jsky.cli;

import net.codersky.jsky.math.JNumbers;
import net.codersky.jsky.tuple.pair.SafeImmutablePair;
import net.codersky.jsky.tuple.pair.SafePair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CLICommandPool {

	private final ArrayList<SafePair<CLICommand, String[]>> pool = new ArrayList<>();

	public synchronized void add(@NotNull CLICommand command, @NotNull String @NotNull [] args) {
		pool.add(new SafeImmutablePair<>(command, args));
	}

	public synchronized int runPending(int limit) {
		if (pool.isEmpty())
			return 0;
		int toRun = limit;
		if (toRun <= 0)
			toRun = pool.size();
		toRun = JNumbers.limit(toRun, 1, pool.size());
		int failed = 0;
		for (int i = 0; i < toRun; i++) {
			final SafePair<CLICommand, String[]> pair = pool.getFirst();
			pair.getFirst().onCommand(pair.getSecond());
			pool.removeFirst();
		}
		return failed;
	}

	public synchronized int runPending() {
		return runPending(0);
	}
}
