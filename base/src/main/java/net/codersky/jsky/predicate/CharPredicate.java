package net.codersky.jsky.predicate;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Represents a predicate ({@code boolean}-valued function) of one {@code char}-valued argument.
 * This is the {@code char}-consuming primitive type specialization of {@link Predicate}.
 * <p>
 * This is a {@link FunctionalInterface} whose functional method is {@link #test(char)}.
 *
 * @since JSky 1.0.0
 *
 * @author xDec0de_
 *
 * @see Predicate
 * @see #test(char)
 * @see #and(CharPredicate)
 * @see #or(CharPredicate)
 * @see #negate()
 */
@FunctionalInterface
public interface CharPredicate {

	/*
	 - Test
	 */

	/**
	 * Evaluates this {@link CharPredicate} on the given {@code char value}.
	 *
	 * @param value The {@code char} to test.
	 *
	 * @return {@code true} if {@code value} matches the predicate, {@code false} otherwise.
	 *
	 * @since JSky 1.0.0
	 */
	boolean test(char value);

	/*
	 - Logical AND
	 */

	/**
	 * Returns a composed predicate that represents a short-circuiting logical <b>AND</b> ({@code &&} operator)
	 * of this predicate and the {@code other}. When evaluating the composed predicate, this {@link CharPredicate}
	 * is the first to be evaluated, meaning that if this {@link CharPredicate} returns {@code false} or
	 * throws an exception, the {@code other} predicate won't be evaluated.
	 *
	 * @param other A {@link CharPredicate} that will be logically-<b>AND</b>ed with this {@link CharPredicate}
	 *
	 * @return A composed {@link CharPredicate} that represents the short-circuiting logical
	 * <b>AND</b> of this {@link CharPredicate} and the {@code other} {@link CharPredicate}.
	 *
	 * @throws NullPointerException If {@code other} is {@code null}.
	 *
	 * @since JSky 1.0.0
	 */
	@NotNull
	default CharPredicate and(@NotNull CharPredicate other) {
		Objects.requireNonNull(other);
		return s -> test(s) && other.test(s);
	}

	/*
	 - Logical OR
	 */

	/**
	 * Returns a composed predicate that represents a short-circuiting logical <b>OR</b> ({@code ||} operator)
	 * of this predicate and the {@code other}. When evaluating the composed predicate, this {@link CharPredicate}
	 * is the first to be evaluated, meaning that if this {@link CharPredicate} throws an exception, the
	 * {@code other} predicate won't be evaluated.
	 *
	 * @param other A {@link CharPredicate} that will be logically-<b>OR</b>ed with this {@link CharPredicate}
	 *
	 * @return A composed {@link CharPredicate} that represents the short-circuiting logical
	 * <b>OR</b> of this {@link CharPredicate} and the {@code other} {@link CharPredicate}.
	 *
	 * @throws NullPointerException If {@code other} is {@code null}.
	 *
	 * @since JSky 1.0.0
	 */
	@NotNull
	default CharPredicate or(@NotNull CharPredicate other) {
		Objects.requireNonNull(other);
		return s -> test(s) || other.test(s);
	}

	/*
	 - Negation
	 */

	/**
	 * Returns a {@link CharPredicate} that represents the logical negation of this {@link CharPredicate}.
	 *
	 * @return a {@link CharPredicate} that represents the logical negation of this {@link CharPredicate}.
	 *
	 * @since JSky 1.0.0
	 */
	@NotNull
	default CharPredicate negate() {
		return s -> !test(s);
	}
}
