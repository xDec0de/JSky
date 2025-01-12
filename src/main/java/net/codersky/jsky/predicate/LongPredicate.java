package net.codersky.jsky.predicate;

import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/**
 * Extension of the {@link Predicate} {@code interface} for primitive {@code long} comparison.
 * This {@code interface} provides methods that avoid autoboxing to and unboxing from
 * the {@link Long} {@code class}. Keep in mind that this {@code interface} is not
 * designed to be fast but rather to avoid creating unnecessary {@link Long} instances.
 *
 * @since JSky 1.0.0
 *
 * @author xDec0de_
 *
 * @see #testLong(long)
 * @see #and(LongPredicate)
 * @see #or(LongPredicate)
 * @see #negate()
 */
@FunctionalInterface
public interface LongPredicate extends Predicate<Long> {

	/*
	 - Test
	 */

	/**
	 * Evaluates this {@link LongPredicate} on the given {@link Long} {@code l}.
	 *
	 * @deprecated Use {@link #testLong(long)} instead.
	 *
	 * @param l The {@link Long} to test.
	 *
	 * @return {@code true} if {@code l} matches the predicate, {@code false} otherwise.
	 * 
	 * @see #testLong(long)
	 */
	@Override
	@Deprecated
	default boolean test(Long l) {
		return testLong(l);
	}

	/**
	 * Evaluates this {@link LongPredicate} on the given {@code long l}.
	 *
	 * @param l The {@code long} to test.
	 *
	 * @return {@code true} if {@code l} matches the predicate, {@code false} otherwise.
	 *
	 * @since JSky 1.0.0
	 */
	boolean testLong(long l);

	/*
	 - Logical AND
	 */

	/**
	 * Returns a composed predicate that represents a short-circuiting logical <b>AND</b> ({@code &&} operator)
	 * of this predicate and the {@code other}. When evaluating the composed predicate, this {@link LongPredicate}
	 * is the first to be evaluated, meaning that if this {@link LongPredicate} returns {@code false} or
	 * throws an exception, the {@code other} predicate won't be evaluated.
	 *
	 * @deprecated Use {@link #and(LongPredicate)}.
	 *
	 * @param other A {@link Predicate} that will be logically-<b>AND</b>ed with this {@link LongPredicate}
	 *
	 * @return A composed {@link LongPredicate} that represents the short-circuiting logical
	 * <b>AND</b> of this {@link LongPredicate} and the {@code other} {@link Predicate}.
	 *
	 * @since JSky 1.0.0
	 * 
	 * @see #and(LongPredicate)
	 */
	@NotNull
	@Override
	@Deprecated
	default LongPredicate and(@NotNull Predicate<? super Long> other) {
		return l -> testLong(l) && other.test(l);
	}

	/**
	 * Returns a composed predicate that represents a short-circuiting logical <b>AND</b> ({@code &&} operator)
	 * of this predicate and the {@code other}. When evaluating the composed predicate, this {@link LongPredicate}
	 * is the first to be evaluated, meaning that if this {@link LongPredicate} returns {@code false} or
	 * throws an exception, the {@code other} predicate won't be evaluated.
	 *
	 * @param other A {@link LongPredicate} that will be logically-<b>AND</b>ed with this {@link LongPredicate}
	 *
	 * @return A composed {@link LongPredicate} that represents the short-circuiting logical
	 * <b>AND</b> of this {@link LongPredicate} and the {@code other} {@link LongPredicate}.
	 *
	 * @since JSky 1.0.0
	 */
	@NotNull
	default LongPredicate and(@NotNull LongPredicate other) {
		return l -> testLong(l) && other.testLong(l);
	}

	/*
	 - Logical OR
	 */

	/**
	 * Returns a composed predicate that represents a short-circuiting logical <b>OR</b> ({@code ||} operator)
	 * of this predicate and the {@code other}. When evaluating the composed predicate, this {@link LongPredicate}
	 * is the first to be evaluated, meaning that if this {@link LongPredicate} throws an exception, the
	 * {@code other} predicate won't be evaluated.
	 *
	 * @deprecated Use {@link #or(LongPredicate)}.
	 *
	 * @param other A {@link Predicate} that will be logically-<b>OR</b>ed with this {@link LongPredicate}
	 *
	 * @return A composed {@link LongPredicate} that represents the short-circuiting logical
	 * <b>OR</b> of this {@link LongPredicate} and the {@code other} {@link Predicate}.
	 *
	 * @since JSky 1.0.0
	 * 
	 * @see #or(LongPredicate)
	 */
	@NotNull
	@Override
	@Deprecated
	default LongPredicate or(@NotNull Predicate<? super Long> other) {
		return l -> testLong(l) || other.test(l);
	}

	/**
	 * Returns a composed predicate that represents a short-circuiting logical <b>OR</b> ({@code ||} operator)
	 * of this predicate and the {@code other}. When evaluating the composed predicate, this {@link LongPredicate}
	 * is the first to be evaluated, meaning that if this {@link LongPredicate} throws an exception, the
	 * {@code other} predicate won't be evaluated.
	 *
	 * @param other A {@link LongPredicate} that will be logically-<b>OR</b>ed with this {@link LongPredicate}
	 *
	 * @return A composed {@link LongPredicate} that represents the short-circuiting logical
	 * <b>OR</b> of this {@link LongPredicate} and the {@code other} {@link LongPredicate}.
	 *
	 * @since JSky 1.0.0
	 */
	@NotNull
	default LongPredicate or(@NotNull LongPredicate other) {
		return l -> testLong(l) || other.testLong(l);
	}

	/*
	 - Negation
	 */

	@NotNull
	@Override
	default LongPredicate negate() {
		return l -> !testLong(l);
	}
}
