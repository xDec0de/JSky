package net.codersky.jsky.predicate;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Represents a predicate ({@code boolean}-valued function) of one {@code float}-valued argument.
 * This is the {@code float}-consuming primitive type specialization of {@link Predicate}.
 * <p>
 * This is a {@link FunctionalInterface} whose functional method is {@link #test(float)}.
 *
 * @since JSky 1.0.0
 *
 * @author xDec0de_
 *
 * @see Predicate
 * @see #test(float)
 * @see #and(FloatPredicate)
 * @see #or(FloatPredicate)
 * @see #negate()
 */
@FunctionalInterface
public interface FloatPredicate {

	/*
	 - Test
	 */

	/**
	 * Evaluates this {@link FloatPredicate} on the given {@code float value}.
	 *
	 * @param value The {@code float} to test.
	 *
	 * @return {@code true} if {@code value} matches the predicate, {@code false} otherwise.
	 *
	 * @since JSky 1.0.0
	 */
	boolean test(float value);

	/*
	 - Logical AND
	 */

	/**
	 * Returns a composed predicate that represents a short-circuiting logical <b>AND</b> ({@code &&} operator)
	 * of this predicate and the {@code other}. When evaluating the composed predicate, this {@link FloatPredicate}
	 * is the first to be evaluated, meaning that if this {@link FloatPredicate} returns {@code false} or
	 * throws an exception, the {@code other} predicate won't be evaluated.
	 *
	 * @param other A {@link FloatPredicate} that will be logically-<b>AND</b>ed with this {@link FloatPredicate}
	 *
	 * @return A composed {@link FloatPredicate} that represents the short-circuiting logical
	 * <b>AND</b> of this {@link FloatPredicate} and the {@code other} {@link FloatPredicate}.
	 *
	 * @throws NullPointerException If {@code other} is {@code null}.
	 *
	 * @since JSky 1.0.0
	 */
	@NotNull
	default FloatPredicate and(@NotNull FloatPredicate other) {
		Objects.requireNonNull(other);
		return f -> test(f) && other.test(f);
	}

	/*
	 - Logical OR
	 */

	/**
	 * Returns a composed predicate that represents a short-circuiting logical <b>OR</b> ({@code ||} operator)
	 * of this predicate and the {@code other}. When evaluating the composed predicate, this {@link FloatPredicate}
	 * is the first to be evaluated, meaning that if this {@link FloatPredicate} throws an exception, the
	 * {@code other} predicate won't be evaluated.
	 *
	 * @param other A {@link FloatPredicate} that will be logically-<b>OR</b>ed with this {@link FloatPredicate}
	 *
	 * @return A composed {@link FloatPredicate} that represents the short-circuiting logical
	 * <b>OR</b> of this {@link FloatPredicate} and the {@code other} {@link FloatPredicate}.
	 *
	 * @throws NullPointerException If {@code other} is {@code null}.
	 *
	 * @since JSky 1.0.0
	 */
	@NotNull
	default FloatPredicate or(@NotNull FloatPredicate other) {
		Objects.requireNonNull(other);
		return f -> test(f) || other.test(f);
	}

	/*
	 - Negation
	 */

	/**
	 * Returns a {@link FloatPredicate} that represents the logical negation of this {@link FloatPredicate}.
	 *
	 * @return a {@link FloatPredicate} that represents the logical negation of this {@link FloatPredicate}.
	 *
	 * @since JSky 1.0.0
	 */
	@NotNull
	default FloatPredicate negate() {
		return f -> !test(f);
	}
}
