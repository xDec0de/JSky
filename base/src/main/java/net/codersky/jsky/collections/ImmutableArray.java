package net.codersky.jsky.collections;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Array wrapper to make it unmodifiable while keeping
 * memory usage to a minimum. This class only stores the array
 * provided on its {@link ImmutableArray#ImmutableArray(Object[]) constructor}.
 *
 * @since JSky 1.0.0
 *
 * @author xDec0de_
 *
 * @param <E> The type of elements of the array.
 */
public class ImmutableArray<E> implements Iterable<E> {

	/** The internal array of this {@link ImmutableArray} */
	protected final E[] arr;

	/**
	 * Creates a new {@link ImmutableArray} that holds the
	 * provided {@code array}. The {@code array} <b>WON'T</b> be
	 * cloned by the constructor, so avoid modifying it directly.
	 *
	 * @param array the array to store, can contain {@code null}
	 * {@link E elements}.
	 *
	 * @throws NullPointerException if {@code array} is {@code null}.
	 *
	 * @since JSky 1.0.0
	 */
	public ImmutableArray(@Nullable final E @NotNull [] array) {
		this.arr = Objects.requireNonNull(array, "The array to store cannot be null");
	}

	/*
	 - Iterable implementation
	 */

	@Override
	public void forEach(@NotNull Consumer<? super E> action) {
		for (E value : arr)
			action.accept(value);
	}

	@NotNull
	@Override
	public Iterator<E> iterator() {
		return new Itr();
	}

	private class Itr implements Iterator<E> {

		int index = 0;

		@Override
		public boolean hasNext() {
			return index < arr.length;
		}

		@Override
		public E next() {
			return arr[index++];
		}
	}

	/*
	 - Additional methods
	 */

	/**
	 * Gets the element at the specified {@code index} from the array.
	 *
	 * @param index the index of the element to get.
	 *
	 * @return The element at the specified {@code index}.
	 *
	 * @throws ArrayIndexOutOfBoundsException if {@code index} is higher than {@link #length()}.
	 *
	 * @since JSky 1.0.0
	 */
	@Nullable
	public E get(int index) {
		return arr[index];
	}

	/**
	 * Gets the length of the array.
	 *
	 * @return The length of the array.
	 *
	 * @since JSky 1.0.0
	 */
	public int length() {
		return arr.length;
	}

	/**
	 * Maps every element of this array into the {@code dest} array.
	 *
	 * @param dest The destination array to contain mapped elements on. This
	 * array must be big enough to fit all elements, otherwise an
	 * {@link ArrayIndexOutOfBoundsException} will be thrown.
	 * @param mapper The {@link Function} used to map the elements.
	 *
	 * @param <R> The type of elements of {@code dest}.
	 *
	 * @return Just {@code dest}.
	 *
	 * @throws ArrayIndexOutOfBoundsException if the length of {@code dest}
	 * is smaller than {@link #length()}.
	 *
	 * @since JSky 1.0.0
	 */
	@NotNull
	public <R> R[] map(@NotNull R[] dest, @NotNull Function<E, R> mapper) {
		return JCollections.map(this.arr, dest, mapper);
	}

	/**
	 * Maps every element of this array into a new {@link ArrayList}.
	 *
	 * @param mapper The {@link Function} used to map the elements.
	 *
	 * @param <R> The type of elements of the new {@link ArrayList}.
	 *
	 * @return A new {@link ArrayList} of size {@link #length()} containing
	 * all mapped elements.
	 *
	 * @since JSky 1.0.0
	 */
	@NotNull
	public <R> ArrayList<R> map(@NotNull Function<E, R> mapper) {
		final ArrayList<R> res = new ArrayList<>(length());
		for (E value : arr)
			res.add(mapper.apply(value));
		return res;
	}

	/**
	 * Maps every element of this array into a new {@link ArrayList}.
	 * Then, adds any {@code extra} elements to it.
	 *
	 * @param mapper The {@link Function} used to map the elements.
	 * @param extra The extra elements to add to the new {@link ArrayList}.
	 *
	 * @param <R> The type of elements of the new {@link ArrayList}.
	 *
	 * @return A new {@link ArrayList} of size {@link #length()} + the length
	 * of {@code extra} that contains all mapped elements plus those provided
	 * at {@code extra}.
	 *
	 * @since JSky 1.0.0
	 */
	@NotNull
	@SafeVarargs
	public final <R> ArrayList<R> map(@NotNull Function<E, R> mapper, R... extra) {
		final ArrayList<R> res = new ArrayList<>(length() + extra.length);
		for (E value : arr)
			res.add(mapper.apply(value));
		Collections.addAll(res, extra);
		return res;
	}
}
