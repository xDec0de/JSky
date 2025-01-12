package net.codersky.jsky.predicate;

import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/**
 * Extension of the {@link Predicate} {@code interface} for primitive {@code double} comparison.
 * This {@code interface} provides methods that avoid autoboxing to and unboxing from
 * the {@link Double} {@code class}. Keep in mind that this {@code interface} is not
 * designed to be fast but rather to avoid creating unnecessary {@link Double} instances.
 *
 * @since JSky 1.0.0
 *
 * @author xDec0de_
 *
 * @see #testDouble(double)
 * @see #and(DoublePredicate)
 * @see #or(DoublePredicate)
 * @see #negate()
 */
@FunctionalInterface
public interface DoublePredicate extends Predicate<Double> {

	/*
	 - Test
	 */

	/**
	 * Evaluates this {@link DoublePredicate} on the given {@link Double} {@code d}.
	 *
	 * @deprecated Use {@link #testDouble(double)} instead.
	 *
	 * @param d The {@link Double} to test.
	 *
	 * @return {@code true} if {@code d} matches the predicate, {@code false} otherwise.
	 * 
	 * @see #testDouble(double)
	 */
	@Override
	@Deprecated
	default boolean test(Double d) {
		return testDouble(d);
	}

	/**
	 * Evaluates this {@link DoublePredicate} on the given {@code double d}.
	 *
	 * @param d The {@code double} to test.
	 *
	 * @return {@code true} if {@code d} matches the predicate, {@code false} otherwise.
	 *
	 * @since JSky 1.0.0
	 */
	boolean testDouble(double d);

	/*
	 - Logical AND
	 */

	/**
	 * Returns a composed predicate that represents a short-circuiting logical <b>AND</b> ({@code &&} operator)
	 * of this predicate and the {@code other}. When evaluating the composed predicate, this {@link DoublePredicate}
	 * is the first to be evaluated, meaning that if this {@link DoublePredicate} returns {@code false} or
	 * throws an exception, the {@code other} predicate won't be evaluated.
	 *
	 * @deprecated Use {@link #and(DoublePredicate)}.
	 *
	 * @param other A {@link Predicate} that will be logically-<b>AND</b>ed with this {@link DoublePredicate}
	 *
	 * @return A composed {@link DoublePredicate} that represents the short-circuiting logical
	 * <b>AND</b> of this {@link DoublePredicate} and the {@code other} {@link Predicate}.
	 *
	 * @since JSky 1.0.0
	 * 
	 * @see #and(DoublePredicate)
	 */
	@NotNull
	@Override
	@Deprecated
	default DoublePredicate and(@NotNull Predicate<? super Double> other) {
		return d -> testDouble(d) && other.test(d);
	}

	/**
	 * Returns a composed predicate that represents a short-circuiting logical <b>AND</b> ({@code &&} operator)
	 * of this predicate and the {@code other}. When evaluating the composed predicate, this {@link DoublePredicate}
	 * is the first to be evaluated, meaning that if this {@link DoublePredicate} returns {@code false} or
	 * throws an exception, the {@code other} predicate won't be evaluated.
	 *
	 * @param other A {@link DoublePredicate} that will be logically-<b>AND</b>ed with this {@link DoublePredicate}
	 *
	 * @return A composed {@link DoublePredicate} that represents the short-circuiting logical
	 * <b>AND</b> of this {@link DoublePredicate} and the {@code other} {@link DoublePredicate}.
	 *
	 * @since JSky 1.0.0
	 */
	@NotNull
	default DoublePredicate and(@NotNull DoublePredicate other) {
		return d -> testDouble(d) && other.testDouble(d);
	}

	/*
	 - Logical OR
	 */

	/**
	 * Returns a composed predicate that represents a short-circuiting logical <b>OR</b> ({@code ||} operator)
	 * of this predicate and the {@code other}. When evaluating the composed predicate, this {@link DoublePredicate}
	 * is the first to be evaluated, meaning that if this {@link DoublePredicate} throws an exception, the
	 * {@code other} predicate won't be evaluated.
	 *
	 * @deprecated Use {@link #or(DoublePredicate)}.
	 *
	 * @param other A {@link Predicate} that will be logically-<b>OR</b>ed with this {@link DoublePredicate}
	 *
	 * @return A composed {@link DoublePredicate} that represents the short-circuiting logical
	 * <b>OR</b> of this {@link DoublePredicate} and the {@code other} {@link Predicate}.
	 *
	 * @since JSky 1.0.0
	 * 
	 * @see #or(DoublePredicate)
	 */
	@NotNull
	@Override
	@Deprecated
	default DoublePredicate or(@NotNull Predicate<? super Double> other) {
		return d -> testDouble(d) || other.test(d);
	}

	/**
	 * Returns a composed predicate that represents a short-circuiting logical <b>OR</b> ({@code ||} operator)
	 * of this predicate and the {@code other}. When evaluating the composed predicate, this {@link DoublePredicate}
	 * is the first to be evaluated, meaning that if this {@link DoublePredicate} throws an exception, the
	 * {@code other} predicate won't be evaluated.
	 *
	 * @param other A {@link DoublePredicate} that will be logically-<b>OR</b>ed with this {@link DoublePredicate}
	 *
	 * @return A composed {@link DoublePredicate} that represents the short-circuiting logical
	 * <b>OR</b> of this {@link DoublePredicate} and the {@code other} {@link DoublePredicate}.
	 *
	 * @since JSky 1.0.0
	 */
	@NotNull
	default DoublePredicate or(@NotNull DoublePredicate other) {
		return d -> testDouble(d) || other.testDouble(d);
	}

	/*
	 - Negation
	 */

	@NotNull
	@Override
	default DoublePredicate negate() {
		return d -> !testDouble(d);
	}
}
