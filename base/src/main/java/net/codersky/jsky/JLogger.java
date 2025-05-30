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

	/**
	 * Gets the name of this logger.
	 *
	 * @return The name of this logger, never {@code null}.
	 *
	 * @since JSky 1.0.0
	 */
	@NotNull
	@Override
	public String getName() {
		return logger.getName();
	}

	/*
	 - Log level
	 */

	/**
	 * Gets the current logging {@link Level level} of this
	 * {@link JLogger}. Messages sent to this logger that have
	 * a lower {@link Level#getSeverity() severity} than the
	 * severity of this {@link Level level} will be discarded.
	 *
	 * @return The current logging {@link Level level} of this {@link JLogger}.
	 *
	 * @since JSky 1.0.0
	 */
	@NotNull
	public Level getLevel() {
		return fromJUL(logger.getLevel());
	}

	/**
	 * Sets the logging {@link Level level} of this {@link JLogger}
	 * to the provided {@code level}. Messages with a lower
	 * {@link Level#getSeverity() severity} will be discarded.
	 *
	 * @param level The new {@link Level} of this {@link JLogger}.
	 *
	 * @return This {@link JLogger}.
	 *
	 * @throws NullPointerException if {@code level} is {@code null}.
	 *
	 * @since JSky 1.0.0
	 */
	@NotNull
	public JLogger setLevel(@NotNull final Level level) {
		logger.setLevel(toJUL(level));
		return this;
	}

	/**
	 * Checks whether the provided {@code level} would
	 * be logged or not according to the current log
	 * {@link Level level} of this {@link JLogger}.
	 *
	 * @param level The {@link Level level} to check.
	 *
	 * @return {@code true} if a message at the provided
	 * {@code level} would be logged, {@code false} otherwise.
	 *
	 * @throws NullPointerException if {@code level} is {@code null}.
	 *
	 * @since JSky 1.0.0
	 */
	@Override
	public boolean isLoggable(@NotNull final Level level) {
		return logger.isLoggable(toJUL(level));
	}

	/*
	 - Level conversion utility
	 */

	@NotNull
	private Level fromJUL(@NotNull final java.util.logging.Level level) {
		final int value = level.intValue();
		if (value == java.util.logging.Level.ALL.intValue())
			return Level.ALL;
		if (value == java.util.logging.Level.FINEST.intValue())
			return Level.TRACE;
		if (value == java.util.logging.Level.FINE.intValue())
			return Level.DEBUG;
		if (value == java.util.logging.Level.INFO.intValue())
			return Level.INFO;
		if (value == java.util.logging.Level.WARNING.intValue())
			return Level.WARNING;
		if (value == java.util.logging.Level.SEVERE.intValue())
			return Level.ERROR;
		if (value == java.util.logging.Level.OFF.intValue())
			return Level.OFF;
		throw new IllegalArgumentException("Unknown level: " + level);
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

	/**
	 * Shortcut to {@link #setLevel(Level) set the level}
	 * of this {@link JLogger} to {@link Level#DEBUG DEBUG}.
	 *
	 * @return This {@link JLogger}
	 *
	 * @since JSky 1.0.0
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

	/**
	 * Shortcut to {@link #setLevel(Level) set the level}
	 * of this {@link JLogger} to {@link Level#INFO INFO}.
	 *
	 * @return This {@link JLogger}
	 *
	 * @since JSky 1.0.0
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

	/**
	 * Shortcut to {@link #setLevel(Level) set the level}
	 * of this {@link JLogger} to {@link Level#WARNING WARNING}.
	 *
	 * @return This {@link JLogger}
	 *
	 * @since JSky 1.0.0
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

	/**
	 * Shortcut to {@link #setLevel(Level) set the level}
	 * of this {@link JLogger} to {@link Level#ERROR ERROR}.
	 *
	 * @return This {@link JLogger}
	 *
	 * @since JSky 1.0.0
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