package net.codersky.jsky.predicate;

import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/**
 * Extension of the {@link Predicate} {@code interface} for primitive {@code short} comparison.
 * This {@code interface} provides methods that avoid autoboxing to and unboxing from
 * the {@link Short} {@code class}. Keep in mind that this {@code interface} is not
 * designed to be fast but rather to avoid creating unnecessary {@link Short} instances.
 *
 * @since JSky 1.0.0
 *
 * @author xDec0de_
 *
 * @see #testShort(short)
 * @see #and(ShortPredicate)
 * @see #or(ShortPredicate)
 * @see #negate()
 */
@FunctionalInterface
public interface ShortPredicate extends Predicate<Short> {

	/*
	 - Test
	 */

	/**
	 * Evaluates this {@link ShortPredicate} on the given {@link Short} {@code s}.
	 *
	 * @deprecated Use {@link #testShort(short)} instead.
	 *
	 * @param s The {@link Short} to test.
	 *
	 * @return {@code true} if {@code s} matches the predicate, {@code false} otherwise.
	 * 
	 * @see #testShort(short)
	 */
	@Override
	@Deprecated
	default boolean test(Short s) {
		return testShort(s);
	}

	/**
	 * Evaluates this {@link ShortPredicate} on the given {@code short s}.
	 *
	 * @param s The {@code short} to test.
	 *
	 * @return {@code true} if {@code s} matches the predicate, {@code false} otherwise.
	 *
	 * @since JSky 1.0.0
	 */
	boolean testShort(short s);

	/*
	 - Logical AND
	 */

	/**
	 * Returns a composed predicate that represents a short-circuiting logical <b>AND</b> ({@code &&} operator)
	 * of this predicate and the {@code other}. When evaluating the composed predicate, this {@link ShortPredicate}
	 * is the first to be evaluated, meaning that if this {@link ShortPredicate} returns {@code false} or
	 * throws an exception, the {@code other} predicate won't be evaluated.
	 *
	 * @deprecated Use {@link #and(ShortPredicate)}.
	 *
	 * @param other A {@link Predicate} that will be logically-<b>AND</b>ed with this {@link ShortPredicate}
	 *
	 * @return A composed {@link ShortPredicate} that represents the short-circuiting logical
	 * <b>AND</b> of this {@link ShortPredicate} and the {@code other} {@link Predicate}.
	 *
	 * @since JSky 1.0.0
	 * 
	 * @see #and(ShortPredicate)
	 */
	@NotNull
	@Override
	@Deprecated
	default ShortPredicate and(@NotNull Predicate<? super Short> other) {
		return s -> testShort(s) && other.test(s);
	}

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
	 * @since JSky 1.0.0
	 */
	@NotNull
	default ShortPredicate and(@NotNull ShortPredicate other) {
		return s -> testShort(s) && other.testShort(s);
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
	 * @deprecated Use {@link #or(ShortPredicate)}.
	 *
	 * @param other A {@link Predicate} that will be logically-<b>OR</b>ed with this {@link ShortPredicate}
	 *
	 * @return A composed {@link ShortPredicate} that represents the short-circuiting logical
	 * <b>OR</b> of this {@link ShortPredicate} and the {@code other} {@link Predicate}.
	 *
	 * @since JSky 1.0.0
	 * 
	 * @see #or(ShortPredicate)
	 */
	@NotNull
	@Override
	@Deprecated
	default ShortPredicate or(@NotNull Predicate<? super Short> other) {
		return s -> testShort(s) || other.test(s);
	}

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
	 * @since JSky 1.0.0
	 */
	@NotNull
	default ShortPredicate or(@NotNull ShortPredicate other) {
		return s -> testShort(s) || other.testShort(s);
	}

	/*
	 - Negation
	 */

	@NotNull
	@Override
	default ShortPredicate negate() {
		return s -> !testShort(s);
	}
}
