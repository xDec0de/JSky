package net.codersky.jsky.strings;

import org.jetbrains.annotations.NotNull;

/**
 * An interface used to indicate that an {@link Object} can be directly
 * used by a {@link strings.Replacer} with the {@link #asReplacement()} method.
 * 
 * @since JSky 1.0.0
 *
 * @author xDec0de_
 */
@FunctionalInterface
public interface Replacement {

	/**
	 * A {@link String} representing how this {@link Replacement}
	 * should be represented by a {@link strings.Replacer}.
 	 *
	 * @return A {@link String} identifying this {@link Replacement} which will be used by
	 * {@link strings.Replacer Replacers} to represent it when this object is used as a replacement.
	 * This {@link String} is required to <b>never</b> be {@code null}
	 * 
	 * @since JSky 1.0.0
	 */
	@NotNull
	String asReplacement();
}
