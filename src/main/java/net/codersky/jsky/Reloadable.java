package net.codersky.jsky;

/**
 * {@link FunctionalInterface Functional interface} that provides
 * the {@link #reload()} method for any type of object that may be reloaded.
 *
 * @author xDec0de_
 */
@FunctionalInterface
public interface Reloadable {

	/**
	 * Reloads this {@link Reloadable}. Depending on the implementation
	 * this may take some time, so keep that in mind.
	 *
	 * @return {@code true} if the reload was successful, {@code false} otherwise.
	 */
	boolean reload();
}
