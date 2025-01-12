package net.codersky.jsky.predicate;

import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/**
 * Extension of the {@link Predicate} {@code interface} for primitive {@code byte} comparison.
 * This {@code interface} provides methods that avoid autoboxing to and unboxing from
 * the {@link Byte} {@code class}. Keep in mind that this {@code interface} is not
 * designed to be fast but rather to avoid creating unnecessary {@link Byte} instances.
 *
 * @since JSky 1.0.0
 *
 * @author xDec0de_
 *
 * @see #testByte(byte)
 * @see #and(BytePredicate)
 * @see #or(BytePredicate)
 * @see #negate()
 */
@FunctionalInterface
public interface BytePredicate extends Predicate<Byte> {

	/*
	 - Test
	 */

	/**
	 * Evaluates this {@link BytePredicate} on the given {@link Byte} {@code b}.
	 *
	 * @deprecated Use {@link #testByte(byte)} instead.
	 *
	 * @param b The {@link Byte} to test.
	 *
	 * @return {@code true} if {@code b} matches the predicate, {@code false} otherwise.
	 * 
	 * @see #testByte(byte)
	 */
	@Override
	@Deprecated
	default boolean test(Byte b) {
		return testByte(b);
	}

	/**
	 * Evaluates this {@link BytePredicate} on the given {@code byte b}.
	 *
	 * @param b The {@code byte} to test.
	 *
	 * @return {@code true} if {@code b} matches the predicate, {@code false} otherwise.
	 *
	 * @since JSky 1.0.0
	 */
	boolean testByte(byte b);

	/*
	 - Logical AND
	 */

	/**
	 * Returns a composed predicate that represents a short-circuiting logical <b>AND</b> ({@code &&} operator)
	 * of this predicate and the {@code other}. When evaluating the composed predicate, this {@link BytePredicate}
	 * is the first to be evaluated, meaning that if this {@link BytePredicate} returns {@code false} or
	 * throws an exception, the {@code other} predicate won't be evaluated.
	 *
	 * @deprecated Use {@link #and(BytePredicate)}.
	 *
	 * @param other A {@link Predicate} that will be logically-<b>AND</b>ed with this {@link BytePredicate}
	 *
	 * @return A composed {@link BytePredicate} that represents the short-circuiting logical
	 * <b>AND</b> of this {@link BytePredicate} and the {@code other} {@link Predicate}.
	 *
	 * @since JSky 1.0.0
	 * 
	 * @see #and(BytePredicate)
	 */
	@NotNull
	@Override
	@Deprecated
	default BytePredicate and(@NotNull Predicate<? super Byte> other) {
		return b -> testByte(b) && other.test(b);
	}

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
	 * @since JSky 1.0.0
	 */
	@NotNull
	default BytePredicate and(@NotNull BytePredicate other) {
		return b -> testByte(b) && other.testByte(b);
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
	 * @deprecated Use {@link #or(BytePredicate)}.
	 *
	 * @param other A {@link Predicate} that will be logically-<b>OR</b>ed with this {@link BytePredicate}
	 *
	 * @return A composed {@link BytePredicate} that represents the short-circuiting logical
	 * <b>OR</b> of this {@link BytePredicate} and the {@code other} {@link Predicate}.
	 *
	 * @since JSky 1.0.0
	 * 
	 * @see #or(BytePredicate)
	 */
	@NotNull
	@Override
	@Deprecated
	default BytePredicate or(@NotNull Predicate<? super Byte> other) {
		return b -> testByte(b) || other.test(b);
	}

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
	 * @since JSky 1.0.0
	 */
	@NotNull
	default BytePredicate or(@NotNull BytePredicate other) {
		return b -> testByte(b) || other.testByte(b);
	}

	/*
	 - Negation
	 */

	@NotNull
	@Override
	default BytePredicate negate() {
		return b -> !testByte(b);
	}
}
