package net.codersky.jsky;

import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Class used to do <b>fast</b> performance tests. Currently,
 * this class uses {@link ThreadMXBean#getCurrentThreadCpuTime()}
 * to get the actual execution time of the methods, as well as creating
 * a new {@link Thread} every time a test runs, sleeping before every execution
 * to try and have the most accurate results possible.
 * <p>
 * <b>Note:</b> Even with all the features mentioned above, times
 * might not be 100% accurate and may be subject to CPU load, so please make sure
 * to run tests with the minimum CPU load possible. As also mentioned, this method
 * exists for <b><i>fast testing</i></b>, not <b><i>precise results</i></b>,
 * if you really want precision, using an actual profiler is much better than this.
 * 
 * @author xDec0de_
 * 
 * @since JSky 1.0.0
 * 
 * @see #addTest(String, Runnable)
 * @see #run(PrintStream, int)
 */
public class PerformanceTest {

	private final long amount;
	private final long sleep;

	private final LinkedHashMap<String, Runnable> tests = new LinkedHashMap<>();
	private final LinkedHashMap<String, List<Long>> results = new LinkedHashMap<>();

	/**
	 * Creates a new {@link PerformanceTest} with the specified {@code amount}
	 * of times to run and the time to {@code sleep} per execution.
	 * 
	 * @param amount the amount of times to call all tasks per execution, if <= 0, 1 will be used.
	 * @param sleep the time (In milliseconds) to sleep per execution, if <= 0, 1 will be used.
	 * 
	 * @since JSky 1.0.0
	 * 
	 * @see #addTest(String, Runnable)
	 * @see #run(PrintStream, int)
	 */
	public PerformanceTest(long amount, long sleep) {
		this.amount = amount <= 0 ? 1 : amount;
		this.sleep = sleep <= 0 ? 1 : sleep;
	}

	/**
	 * Adds a {@code task} with the specified {@code id} to this {@link PerformanceTest},
	 * if a task with said {@code id} already exists, it will be replaced, if either
	 * {@code id} or {@code task} are null, this method won't do anything.
	 * 
	 * @param id the id of the task to add, will be the name displayed on the test results.
	 * @param task the task to run, usually just the method you want to test, note that
	 * defining the task outside this method does affect performance, so keep that in mind.
	 * 
	 * @return This {@link PerformanceTest}.
	 *
	 * @throws NullPointerException if either {@code id} or {@code task} are {@code null}.
	 * 
	 * @since JSky 1.0.0
	 */
	@NotNull
	public PerformanceTest addTest(@NotNull String id, @NotNull Runnable task) {
		tests.put(Objects.requireNonNull(id), Objects.requireNonNull(task));
		results.put(id, new ArrayList<>());
		return this;
	}

	/**
	 * Runs all tasks stored on this {@link PerformanceTest} the specified
	 * amount of {@code times}, printing test details as well as minimum, average
	 * and maximum results with the provided {@code printAction}.
	 *
	 * @param printAction A {@link Consumer} that will {@link Consumer#accept(Object) accept} any
	 * String to be printed by the {@link PerformanceTest}.
	 * @param times the amount of times to repeat this {@link PerformanceTest}, test
	 * execution will run on a separate thread but not multiple threads, must be higher or
	 * equal to 1.
	 *
	 * @return This {@link PerformanceTest}
	 *
	 * @since JSky 1.0.0
	 */
	@NotNull
	public PerformanceTest run(@NotNull Consumer<String> printAction, int times) {
		final int checkedTimes = Math.max(times, 1);
		printAction.accept("Starting performance test (Repeat: "+checkedTimes+" | Amount: "+amount+" | Sleep: "+sleep+")");
		printAction.accept(" ");
		new PerformanceTestThread(printAction, checkedTimes).start();
		return this;
	}

	/**
	 * Runs all tasks stored on this {@link PerformanceTest} the specified
	 * amount of {@code times}, printing test details as well as minimum, average
	 * and maximum results on {@code stream}.
	 * 
	 * @param stream the stream to print all information, normally {@link System#out}
	 * @param times the amount of times to repeat this {@link PerformanceTest}, test
	 * execution will run on a separate thread but not multiple threads, must be higher or
	 * equal to 1.
	 * 
	 * @return This {@link PerformanceTest}
	 * 
	 * @since JSky 1.0.0
	 */
	@NotNull
	public PerformanceTest run(@NotNull PrintStream stream, int times) {
		return run(stream::println, times);
	}

	class PerformanceTestThread extends Thread {

		private final Consumer<String> printAction;
		private final long times;

		PerformanceTestThread(@NotNull Consumer<String> printAction, long repeat) {
			this.printAction = printAction;
			this.times = repeat;
		}

		@Override
		public void run() {
			final ThreadMXBean threadMx = ManagementFactory.getThreadMXBean();
			for (long i = 0; i < times; i++) {
				tests.forEach((id, runnable) -> {
					try {
						runnable.run(); // Method "warm-up"
						Thread.sleep(sleep); // Sleeping to reduce load.
						final long start = threadMx.getCurrentThreadCpuTime(); // Getting actual CPU time used by this thread
						for (int j = 0; j < amount; j++)
							runnable.run(); // Calling the method itself
						final long total = Math.round((float) (threadMx.getCurrentThreadCpuTime() - start) / 1000000);
						results.get(id).add(total);
					} catch (InterruptedException | IllegalArgumentException e) {
						printAction.accept("Failed to run performance test: " + e.getMessage());
						e.printStackTrace();
					}
				});
			}
			printResults();
		}

		private void printResults() {
			results.forEach((id, resultList) -> {
				long min = Long.MAX_VALUE;
				long max = Long.MIN_VALUE;
				long sum = 0;
				for (long time : resultList) {
					if (time < min)
						min = time;
					if (time > max)
						max = time;
					sum += time;
				}
				printAction.accept(id + " - Min: " + min + "ms | Average: "+ (sum / resultList.size()) +"ms | Max: " + max+"ms");
			});
		}
	}
}
