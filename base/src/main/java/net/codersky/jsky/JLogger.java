package net.codersky.jsky;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * <b>Basic</b> {@link System.Logger Logger} implementation for
 * applications that want a lightweight logger that doesn't
 * require any other dependencies.
 *
 * @author xDec0de_
 *
 * @since JSky 1.0.0
 */
public class JLogger implements System.Logger {

	private final Logger logger;

	/**
	 * Creates a new {@link JLogger} with the provided {@code name}.
	 * {@link Logger#getLogger(String)} is used to create the internal
	 * logger, check that method for more details.
	 *
	 * @param name The name of the logger.
	 *
	 * @throws NullPointerException if {@code name} is {@code null}.
	 *
	 * @since JSky 1.0.0
	 */
	public JLogger(final @NotNull String name) {
		this.logger = Logger.getLogger(name);
	}

	/*
	 - Logger identification
	 */

	@NotNull
	@Override
	public String getName() {
		return logger.getName();
	}

	/*
	 - Log level
	 */

	@NotNull
	public JLogger setLevel(@NotNull final Level level) {
		logger.setLevel(toJUL(level));
		return this;
	}

	@Override
	public boolean isLoggable(@NotNull final Level level) {
		return logger.isLoggable(toJUL(level));
	}

	private java.util.logging.Level toJUL(@NotNull final Level level) {
		return switch (Objects.requireNonNull(level)) {
			case ALL -> java.util.logging.Level.ALL;
			case TRACE -> java.util.logging.Level.FINEST;
			case DEBUG -> java.util.logging.Level.FINE;
			case INFO -> java.util.logging.Level.INFO;
			case WARNING -> java.util.logging.Level.WARNING;
			case ERROR -> java.util.logging.Level.SEVERE;
			case OFF -> java.util.logging.Level.OFF;
		};
	}

	/*
	 - Log override
	 */

	@Override
	public void log(@NotNull Level level, ResourceBundle bundle, String message, Throwable thrown) {
		if (isLoggable(level))
			logger.log(toJUL(level), message, thrown);
	}

	@Override
	public void log(@NotNull Level level, ResourceBundle bundle, @Nullable String format, Object... params) {
		if (!isLoggable(level))
			return;
		if (format == null)
			logger.log(toJUL(level), "null");
		else {
			final String msg = (params == null || params.length == 0) ? format : String.format(format, params);
			logger.log(toJUL(level), msg);
		}
	}

	@Override
	public void log(@NotNull Level level, @Nullable String message) {
		log(level, null, message, (Throwable) null);
	}

	@Override
	public void log(@NotNull Level level, String message, Throwable thrown) {
		log(level, null, message, thrown);
	}

	@Override
	public void log(@NotNull Level level, String format, Object... params) {
		log(level, null, format, params);
	}

	/*
	 - Debug
	 */

	@NotNull
	public JLogger setDebugLevel() {
		return setLevel(Level.DEBUG);
	}

	public void debug(@Nullable String message) {
		log(Level.DEBUG, message);
	}

	public void debug(@Nullable String message, @Nullable Object @Nullable ... params) {
		log(Level.DEBUG, message, params);
	}

	/*
	 - Info
	 */

	@NotNull
	public JLogger setInfoLevel() {
		return setLevel(Level.INFO);
	}

	public void info(@Nullable String message) {
		log(Level.INFO, message);
	}

	/*
	 - Warning
	 */

	@NotNull
	public JLogger setWarningLevel() {
		return setLevel(Level.WARNING);
	}

	public void warning(@Nullable String message) {
		log(Level.WARNING, message);
	}

	/*
	 - Error
	 */

	@NotNull
	public JLogger setErrorLevel() {
		return setLevel(Level.ERROR);
	}

	public void error(@Nullable String message) {
		log(Level.ERROR, message);
	}

	public void error(@Nullable String message, @Nullable Throwable thrown) {
		log(Level.ERROR, message, thrown);
	}
}