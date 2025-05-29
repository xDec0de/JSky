package net.codersky.jsky;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

	public void setLevel(final @NotNull Level level) {
		logger.setLevel(toJUL(level));
	}

	@Override
	public boolean isLoggable(Level level) {
		return logger.isLoggable(toJUL(level));
	}

	private java.util.logging.Level toJUL(Level level) {
		return switch (level) {
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
	public void log(Level level, ResourceBundle bundle, String msg, Throwable thrown) {
		if (isLoggable(level))
			logger.log(toJUL(level), msg, thrown);
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
	public void log(@NotNull Level level, @Nullable String msg) {
		log(level, null, msg, (Throwable) null);
	}

	@Override
	public void log(@NotNull Level level, String msg, Throwable thrown) {
		log(level, null, msg, thrown);
	}

	@Override
	public void log(@NotNull Level level, String format, Object... params) {
		log(level, null, format, params);
	}

	/*
	 - Debug
	 */

	public void setDebugLevel() {
		setLevel(Level.DEBUG);
	}

	public void debug(@Nullable String msg) {
		log(Level.DEBUG, msg);
	}

	public void debug(@Nullable String msg, @Nullable Object @Nullable ... params) {
		log(Level.DEBUG, msg, params);
	}

	/*
	 - Info
	 */

	public void setInfoLevel() {
		setLevel(Level.INFO);
	}

	public void info(@Nullable String msg) {
		log(Level.INFO, msg);
	}

	/*
	 - Warning
	 */

	public void setWarningLevel() {
		setLevel(Level.WARNING);
	}

	public void warning(@Nullable String msg) {
		log(Level.WARNING, msg);
	}

	/*
	 - Error
	 */

	public void setErrorLevel() {
		setLevel(Level.ERROR);
	}

	public void error(@Nullable String msg) {
		log(Level.ERROR, msg);
	}

	public void error(@Nullable String msg, @Nullable Throwable thrown) {
		log(Level.ERROR, msg, thrown);
	}
}