package net.codersky.jsky.predicate;

import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/**
 * Extension of the {@link Predicate} {@code interface} for primitive {@code float} comparison.
 * This {@code interface} provides methods that avoid autoboxing to and unboxing from
 * the {@link Float} {@code class}. Keep in mind that this {@code interface} is not
 * designed to be fast but rather to avoid creating unnecessary {@link Float} instances.
 *
 * @since JSky 1.0.0
 *
 * @author xDec0de_
 *
 * @see #testFloat(float)
 * @see #and(FloatPredicate)
 * @see #or(FloatPredicate)
 * @see #negate()
 */
@FunctionalInterface
public interface FloatPredicate extends Predicate<Float> {

	/*
	 - Test
	 */

	/**
	 * Evaluates this {@link FloatPredicate} on the given {@link Float} {@code f}.
	 *
	 * @deprecated Use {@link #testFloat(float)} instead.
	 *
	 * @param f The {@link Float} to test.
	 *
	 * @return {@code true} if {@code f} matches the predicate, {@code false} otherwise.
	 * 
	 * @see #testFloat(float)
	 */
	@Override
	@Deprecated
	default boolean test(Float f) {
		return testFloat(f);
	}

	/**
	 * Evaluates this {@link FloatPredicate} on the given {@code float f}.
	 *
	 * @param f The {@code float} to test.
	 *
	 * @return {@code true} if {@code f} matches the predicate, {@code false} otherwise.
	 *
	 * @since JSky 1.0.0
	 */
	boolean testFloat(float f);

	/*
	 - Logical AND
	 */

	/**
	 * Returns a composed predicate that represents a short-circuiting logical <b>AND</b> ({@code &&} operator)
	 * of this predicate and the {@code other}. When evaluating the composed predicate, this {@link FloatPredicate}
	 * is the first to be evaluated, meaning that if this {@link FloatPredicate} returns {@code false} or
	 * throws an exception, the {@code other} predicate won't be evaluated.
	 *
	 * @deprecated Use {@link #and(FloatPredicate)}.
	 *
	 * @param other A {@link Predicate} that will be logically-<b>AND</b>ed with this {@link FloatPredicate}
	 *
	 * @return A composed {@link FloatPredicate} that represents the short-circuiting logical
	 * <b>AND</b> of this {@link FloatPredicate} and the {@code other} {@link Predicate}.
	 *
	 * @since JSky 1.0.0
	 * 
	 * @see #and(FloatPredicate)
	 */
	@NotNull
	@Override
	@Deprecated
	default FloatPredicate and(@NotNull Predicate<? super Float> other) {
		return f -> testFloat(f) && other.test(f);
	}

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
	 * @since JSky 1.0.0
	 */
	@NotNull
	default FloatPredicate and(@NotNull FloatPredicate other) {
		return f -> testFloat(f) && other.testFloat(f);
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
	 * @deprecated Use {@link #or(FloatPredicate)}.
	 *
	 * @param other A {@link Predicate} that will be logically-<b>OR</b>ed with this {@link FloatPredicate}
	 *
	 * @return A composed {@link FloatPredicate} that represents the short-circuiting logical
	 * <b>OR</b> of this {@link FloatPredicate} and the {@code other} {@link Predicate}.
	 *
	 * @since JSky 1.0.0
	 * 
	 * @see #or(FloatPredicate)
	 */
	@NotNull
	@Override
	@Deprecated
	default FloatPredicate or(@NotNull Predicate<? super Float> other) {
		return f -> testFloat(f) || other.test(f);
	}

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
	 * @since JSky 1.0.0
	 */
	@NotNull
	default FloatPredicate or(@NotNull FloatPredicate other) {
		return f -> testFloat(f) || other.testFloat(f);
	}

	/*
	 - Negation
	 */

	@NotNull
	@Override
	default FloatPredicate negate() {
		return f -> !testFloat(f);
	}
}
