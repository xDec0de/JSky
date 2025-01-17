package net.codersky.jsky.predicate;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Represents a predicate ({@code boolean}-valued function) of one {@code byte}-valued argument.
 * This is the {@code byte}-consuming primitive type specialization of {@link Predicate}.
 * <p>
 * This is a {@link FunctionalInterface} whose functional method is {@link #test(byte)}.
 *
 * @since JSky 1.0.0
 *
 * @author xDec0de_
 *
 * @see Predicate
 * @see #test(byte)
 * @see #and(BytePredicate)
 * @see #or(BytePredicate)
 * @see #negate()
 */
@FunctionalInterface
public interface BytePredicate {

	/*
	 - Test
	 */

	/**
	 * Evaluates this {@link BytePredicate} on the given {@code byte value}.
	 *
	 * @param value The {@code byte} to test.
	 *
	 * @return {@code true} if {@code value} matches the predicate, {@code false} otherwise.
	 *
	 * @since JSky 1.0.0
	 */
	boolean test(byte value);

	/*
	 - Logical AND
	 */

	/**
	 * Returns a composed predicate that represents a short-circuiting logical <b>AND</b> ({@code &&} operator)
	 * of this predicate and the {@code other}. When evaluating the composed predicate, this {@link BytePredicate}
	 * is the first to be evaluated, meaning that if this {@link BytePredicate} returns {@code false} or
	 * throws an exception, the {@code other} predicate won't be evaluated.
	 *
	 * @param other A {@link BytePredicate} that will be logically-<b>AND</b>ed with this {@link BytePredicate}
	 *
	 * @return A composed {@link BytePredicate} that represents the short-circuiting logical
	 * <b>AND</b> of this {@link BytePredicate} and the {@code other} {@link BytePredicate}.
	 *
	 * @throws NullPointerException If {@code other} is {@code null}.
	 *
	 * @since JSky 1.0.0
	 */
	@NotNull
	default BytePredicate and(@NotNull BytePredicate other) {
		Objects.requireNonNull(other);
		return b -> test(b) && other.test(b);
	}

	/*
	 - Logical OR
	 */

	/**
	 * Returns a composed predicate that represents a short-circuiting logical <b>OR</b> ({@code ||} operator)
	 * of this predicate and the {@code other}. When evaluating the composed predicate, this {@link BytePredicate}
	 * is the first to be evaluated, meaning that if this {@link BytePredicate} throws an exception, the
	 * {@code other} predicate won't be evaluated.
	 *
	 * @param other A {@link BytePredicate} that will be logically-<b>OR</b>ed with this {@link BytePredicate}
	 *
	 * @return A composed {@link BytePredicate} that represents the short-circuiting logical
	 * <b>OR</b> of this {@link BytePredicate} and the {@code other} {@link BytePredicate}.
	 *
	 * @throws NullPointerException If {@code other} is {@code null}.
	 *
	 * @since JSky 1.0.0
	 */
	@NotNull
	default BytePredicate or(@NotNull BytePredicate other) {
		Objects.requireNonNull(other);
		return b -> test(b) || other.test(b);
	}

	/*
	 - Negation
	 */

	/**
	 * Returns a {@link BytePredicate} that represents the logical negation of this {@link BytePredicate}.
	 *
	 * @return a {@link BytePredicate} that represents the logical negation of this {@link BytePredicate}.
	 *
	 * @since JSky 1.0.0
	 */
	@NotNull
	default BytePredicate negate() {
		return b -> !test(b);
	}
}
