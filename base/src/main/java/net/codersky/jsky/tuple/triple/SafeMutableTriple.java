package net.codersky.jsky.tuple.triple;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * An extension of {@link MutableTriple} that implements the
 * {@link SafeTriple} interface, meaning that the elements stored
 * by this {@link Triple} cannot be {@code null}.
 * 
 * @author xDec0de_
 *
 * @param <F> The type of the first element to store.
 * @param <S> The type of the second element to store.
 * 
 * @since JSky 1.0.0
 * 
 * @see SafeImmutableTriple
 */
public class SafeMutableTriple<F, S, T> extends MutableTriple<F, S, T> implements SafeTriple<F, S, T> {

	/**
	 * Constructs a new {@link SafeMutableTriple} that contains
	 * two elements which can't be {@code null}.
	 *
	 * @param first the first element to store.
	 * @param second the second element to store.
	 * @param third the third element to store.
	 *
	 * @throws NullPointerException if either {@code first},
	 * {@code second} or {@code third} are {@code null}.
	 *
	 * @since JSky 1.0.0
	 */
	public SafeMutableTriple(@NotNull F first, @NotNull S second, @NotNull T third) {
		super(Objects.requireNonNull(first), Objects.requireNonNull(second), Objects.requireNonNull(third));
	}

	/**
	 * Gets the first element stored in this {@link SafeMutableTriple},
	 * which will <b>never</b> be {@code null}.
	 *
	 * @return The first element stored in this {@link SafeMutableTriple},
	 * never {@code null}
	 *
	 * @since JSky 1.0.0
	 */
	@NotNull
	@Override
	@SuppressWarnings("DataFlowIssue")
	public F getFirst() {
		return super.getFirst();
	}

	/**
	 * Sets the first element stored on this {@link SafeMutableTriple}
	 * to {@code first}, which can't be {@code null}.
	 *
	 * @param first The new value of the first element.
	 *
	 * @return {@code first}, for convenience.
	 *
	 * @throws NullPointerException if {@code first} is {@code null}.
	 *
	 * @since JSky 1.0.0
	 */
	@NotNull
	public F setFirst(@NotNull F first) {
		return super.setFirst(Objects.requireNonNull(first));
	}

	/**
	 * Gets the second element stored in this {@link SafeMutableTriple},
	 * which will <b>never</b> be {@code null}.
	 *
	 * @return The second element stored in this {@link SafeMutableTriple},
	 * never {@code null}
	 *
	 * @since JSky 1.0.0
	 */
	@NotNull
	@Override
	@SuppressWarnings("DataFlowIssue")
	public S getSecond() {
		return super.getSecond();
	}

	/**
	 * Sets the second element stored on this {@link SafeMutableTriple}
	 * to {@code second}, which can't be {@code null}.
	 * 
	 * @param second The new value of the second element.
	 * 
	 * @return {@code second}, for convenience.
	 *
	 * @throws NullPointerException if {@code second} is {@code null}.
	 * 
	 * @since JSky 1.0.0
	 */
	@NotNull
	public S setSecond(@Nullable S second) {
		return super.setSecond(Objects.requireNonNull(second));
	}

	/**
	 * Gets the third element stored in this {@link SafeMutableTriple},
	 * which will <b>never</b> be {@code null}.
	 *
	 * @return The third element stored in this {@link SafeMutableTriple},
	 * never {@code null}
	 *
	 * @since JSky 1.0.0
	 */
	@NotNull
	@Override
	@SuppressWarnings("DataFlowIssue")
	public T getThird() {
		return super.getThird();
	}

	/**
	 * Sets the third element stored on this {@link SafeMutableTriple}
	 * to {@code third}, which can't be {@code null}.
	 *
	 * @param third The new value of the third element.
	 *
	 * @return {@code third}, for convenience.
	 *
	 * @throws NullPointerException if {@code third} is {@code null}.
	 *
	 * @since JSky 1.0.0
	 */
	@NotNull
	public T setThird(@Nullable T third) {
		return super.setThird(Objects.requireNonNull(third));
	}
}