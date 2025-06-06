package net.codersky.jsky.strings;

import org.jetbrains.annotations.NotNull;

/**
 * An interface used to indicate that an {@link Object} can be directly
 * used by a {@link Replacer} with the {@link #asReplacement()} method.
 * 
 * @since JSky 1.0.0
 *
 * @author xDec0de_
 */
@FunctionalInterface
public interface Replacement {

	/**
	 * A {@link String} representing how this {@link Replacement}
	 * should be represented by a {@link Replacer}.
 	 *
	 * @return A {@link String} identifying this {@link Replacement} which will be used by
	 * {@link Replacer Replacers} to represent it when this object is used as a replacement.
	 * This {@link String} is required to <b>never</b> be {@code null}. {@code null}
	 * can be represented with the "null" string literal.
	 * 
	 * @since JSky 1.0.0
	 */
	@NotNull
	String asReplacement();

	/**
	 * Gets the {@link String} value of the provided {@code obj}ect.
	 * This is an internal utility method that attempts to cast
	 * {@code obj} to {@link Replacement}. If successful,
	 * {@link Replacement#asReplacement()} is returned, if not,
	 * {@link Object#toString()} is returned instead.
	 *
	 * @param obj The {@link Object} to convert to {@link String}.
	 *
	 * @return The {@link String} value of the provided {@code replacement}.
	 */
	@NotNull
	static String toStringValue(@NotNull final Object obj) {
		if (obj instanceof final Replacement rep)
			return rep.asReplacement();
		return obj.toString();
	}
}
