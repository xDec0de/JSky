package net.codersky.jsky.predicate;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Represents a predicate ({@code boolean}-valued function) of one {@code short}-valued argument.
 * This is the {@code short}-consuming primitive type specialization of {@link Predicate}.
 * <p>
 * This is a {@link FunctionalInterface} whose functional method is {@link #test(short)}.
 *
 * @since JSky 1.0.0
 *
 * @author xDec0de_
 *
 * @see Predicate
 * @see #test(short)
 * @see #and(ShortPredicate)
 * @see #or(ShortPredicate)
 * @see #negate()
 */
@FunctionalInterface
public interface ShortPredicate {

	/*
	 - Test
	 */

	/**
	 * Evaluates this {@link ShortPredicate} on the given {@code short value}.
	 *
	 * @param value The {@code short} to test.
	 *
	 * @return {@code true} if {@code value} matches the predicate, {@code false} otherwise.
	 *
	 * @since JSky 1.0.0
	 */
	boolean test(short value);

	/*
	 - Logical AND
	 */

	/**
	 * Returns a composed predicate that represents a short-circuiting logical <b>AND</b> ({@code &&} operator)
	 * of this predicate and the {@code other}. When evaluating the composed predicate, this {@link ShortPredicate}
	 * is the first to be evaluated, meaning that if this {@link ShortPredicate} returns {@code false} or
	 * throws an exception, the {@code other} predicate won't be evaluated.
	 *
	 * @param other A {@link ShortPredicate} that will be logically-<b>AND</b>ed with this {@link ShortPredicate}
	 *
	 * @return A composed {@link ShortPredicate} that represents the short-circuiting logical
	 * <b>AND</b> of this {@link ShortPredicate} and the {@code other} {@link ShortPredicate}.
	 *
	 * @throws NullPointerException If {@code other} is {@code null}.
	 *
	 * @since JSky 1.0.0
	 */
	@NotNull
	default ShortPredicate and(@NotNull ShortPredicate other) {
		Objects.requireNonNull(other);
		return s -> test(s) && other.test(s);
	}

	/*
	 - Logical OR
	 */

	/**
	 * Returns a composed predicate that represents a short-circuiting logical <b>OR</b> ({@code ||} operator)
	 * of this predicate and the {@code other}. When evaluating the composed predicate, this {@link ShortPredicate}
	 * is the first to be evaluated, meaning that if this {@link ShortPredicate} throws an exception, the
	 * {@code other} predicate won't be evaluated.
	 *
	 * @param other A {@link ShortPredicate} that will be logically-<b>OR</b>ed with this {@link ShortPredicate}
	 *
	 * @return A composed {@link ShortPredicate} that represents the short-circuiting logical
	 * <b>OR</b> of this {@link ShortPredicate} and the {@code other} {@link ShortPredicate}.
	 *
	 * @throws NullPointerException If {@code other} is {@code null}.
	 *
	 * @since JSky 1.0.0
	 */
	@NotNull
	default ShortPredicate or(@NotNull ShortPredicate other) {
		Objects.requireNonNull(other);
		return s -> test(s) || other.test(s);
	}

	/*
	 - Negation
	 */

	/**
	 * Returns a {@link ShortPredicate} that represents the logical negation of this {@link ShortPredicate}.
	 *
	 * @return a {@link ShortPredicate} that represents the logical negation of this {@link ShortPredicate}.
	 *
	 * @since JSky 1.0.0
	 */
	@NotNull
	default ShortPredicate negate() {
		return s -> !test(s);
	}
}
