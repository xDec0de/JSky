package net.codersky.jsky.predicate;

import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/**
 * Extension of the {@link Predicate} {@code interface} for primitive {@code int} comparison.
 * This {@code interface} provides methods that avoid autoboxing to and unboxing from
 * the {@link Integer} {@code class}. Keep in mind that this {@code interface} is not
 * designed to be fast but rather to avoid creating unnecessary {@link Integer} instances.
 *
 * @since JSky 1.0.0
 *
 * @author xDec0de_
 *
 * @see #testInt(int)
 * @see #and(IntPredicate)
 * @see #or(IntPredicate)
 * @see #negate()
 */
@FunctionalInterface
public interface IntPredicate extends Predicate<Integer> {

	/*
	 - Test
	 */

	/**
	 * Evaluates this {@link IntPredicate} on the given {@link Integer} {@code i}.
	 *
	 * @deprecated Use {@link #testInt(int)} instead.
	 *
	 * @param i The {@link Integer} to test.
	 *
	 * @return {@code true} if {@code i} matches the predicate, {@code false} otherwise.
	 * 
	 * @see #testInt(int)
	 */
	@Override
	@Deprecated
	default boolean test(Integer i) {
		return testInt(i);
	}

	/**
	 * Evaluates this {@link IntPredicate} on the given {@code int i}.
	 *
	 * @param i The {@code int} to test.
	 *
	 * @return {@code true} if {@code i} matches the predicate, {@code false} otherwise.
	 *
	 * @since JSky 1.0.0
	 */
	boolean testInt(int i);

	/*
	 - Logical AND
	 */

	/**
	 * Returns a composed predicate that represents a short-circuiting logical <b>AND</b> ({@code &&} operator)
	 * of this predicate and the {@code other}. When evaluating the composed predicate, this {@link IntPredicate}
	 * is the first to be evaluated, meaning that if this {@link IntPredicate} returns {@code false} or
	 * throws an exception, the {@code other} predicate won't be evaluated.
	 *
	 * @deprecated Use {@link #and(IntPredicate)}.
	 *
	 * @param other A {@link Predicate} that will be logically-<b>AND</b>ed with this {@link IntPredicate}
	 *
	 * @return A composed {@link IntPredicate} that represents the short-circuiting logical
	 * <b>AND</b> of this {@link IntPredicate} and the {@code other} {@link Predicate}.
	 *
	 * @since JSky 1.0.0
	 * 
	 * @see #and(IntPredicate)
	 */
	@NotNull
	@Override
	@Deprecated
	default IntPredicate and(@NotNull Predicate<? super Integer> other) {
		return i -> testInt(i) && other.test(i);
	}

	/**
	 * Returns a composed predicate that represents a short-circuiting logical <b>AND</b> ({@code &&} operator)
	 * of this predicate and the {@code other}. When evaluating the composed predicate, this {@link IntPredicate}
	 * is the first to be evaluated, meaning that if this {@link IntPredicate} returns {@code false} or
	 * throws an exception, the {@code other} predicate won't be evaluated.
	 *
	 * @param other A {@link IntPredicate} that will be logically-<b>AND</b>ed with this {@link IntPredicate}
	 *
	 * @return A composed {@link IntPredicate} that represents the short-circuiting logical
	 * <b>AND</b> of this {@link IntPredicate} and the {@code other} {@link IntPredicate}.
	 *
	 * @since JSky 1.0.0
	 */
	@NotNull
	default IntPredicate and(@NotNull IntPredicate other) {
		return i -> testInt(i) && other.testInt(i);
	}

	/*
	 - Logical OR
	 */

	/**
	 * Returns a composed predicate that represents a short-circuiting logical <b>OR</b> ({@code ||} operator)
	 * of this predicate and the {@code other}. When evaluating the composed predicate, this {@link IntPredicate}
	 * is the first to be evaluated, meaning that if this {@link IntPredicate} throws an exception, the
	 * {@code other} predicate won't be evaluated.
	 *
	 * @deprecated Use {@link #or(IntPredicate)}.
	 *
	 * @param other A {@link Predicate} that will be logically-<b>OR</b>ed with this {@link IntPredicate}
	 *
	 * @return A composed {@link IntPredicate} that represents the short-circuiting logical
	 * <b>OR</b> of this {@link IntPredicate} and the {@code other} {@link Predicate}.
	 *
	 * @since JSky 1.0.0
	 * 
	 * @see #or(IntPredicate)
	 */
	@NotNull
	@Override
	@Deprecated
	default IntPredicate or(@NotNull Predicate<? super Integer> other) {
		return i -> testInt(i) || other.test(i);
	}

	/**
	 * Returns a composed predicate that represents a short-circuiting logical <b>OR</b> ({@code ||} operator)
	 * of this predicate and the {@code other}. When evaluating the composed predicate, this {@link IntPredicate}
	 * is the first to be evaluated, meaning that if this {@link IntPredicate} throws an exception, the
	 * {@code other} predicate won't be evaluated.
	 *
	 * @param other A {@link IntPredicate} that will be logically-<b>OR</b>ed with this {@link IntPredicate}
	 *
	 * @return A composed {@link IntPredicate} that represents the short-circuiting logical
	 * <b>OR</b> of this {@link IntPredicate} and the {@code other} {@link IntPredicate}.
	 *
	 * @since JSky 1.0.0
	 */
	@NotNull
	default IntPredicate or(@NotNull IntPredicate other) {
		return i -> testInt(i) || other.testInt(i);
	}

	/*
	 - Negation
	 */

	@NotNull
	@Override
	default IntPredicate negate() {
		return i -> !testInt(i);
	}
}
