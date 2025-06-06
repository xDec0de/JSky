package net.codersky.jsky.collections;

import net.codersky.jsky.math.JNumbers;
import net.codersky.jsky.predicate.BytePredicate;
import net.codersky.jsky.predicate.CharPredicate;
import net.codersky.jsky.predicate.FloatPredicate;
import net.codersky.jsky.predicate.ShortPredicate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.DoublePredicate;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

/**
 * Utility class designed to create and use {@link Collection collections} fast.
 * <p>
 * Please keep in mind that this class doesn't intend to replace
 * {@link Collections} and the use of that class is preferred over this one
 * when possible, think of this class as an extension of said class, even though
 * it doesn't extend it (Because we can't).
 * <p>
 * This class is currently designed to work with the following types of {@link List lists}:
 * <ul>
 *     <li>{@link ArrayList} - Default for {@link List}</li>
 *     <li>{@link LinkedList}</li>
 * </ul>
 * And {@link Set sets}:
 * <ul>
 *     <li>{@link HashSet} - Default for {@link Set}</li>
 *     <li>{@link LinkedHashSet}</li>
 *     <li>{@link TreeSet}</li>
 *     <li>{@link EnumSet}</li>
 * </ul>
 *
 * @since JSky 1.0.0
 *
 * @author xDec0de_
 */
public class JCollections {

	private JCollections() {
		throw new UnsupportedOperationException("Can't instantiate JCollections.");
	}

	/*
	 - List creation - ArrayList
	 */

	@NotNull
	public static <E> ArrayList<E> asArrayList(@NotNull E element) {
		final ArrayList<E> list = new ArrayList<>(1);
		list.add(element);
		return list;
	}

	@NotNull
	@SafeVarargs
	public static <E> ArrayList<E> asArrayList(@NotNull E... elements) {
		return add(new ArrayList<>(elements.length), elements);
	}

	@NotNull
	public static <E> ArrayList<E> asArrayList(@NotNull Iterable<E> elements) {
		return add(new ArrayList<>(), elements);
	}

	/*
	 - List creation - LinkedList
	 */

	public static <E> LinkedList<E> asLinkedList(@NotNull E element) {
		final LinkedList<E> list = new LinkedList<>();
		list.add(element);
		return list;
	}

	@NotNull
	@SafeVarargs
	public static <E> LinkedList<E> asLinkedList(@NotNull E... elements) {
		return add(new LinkedList<>(), elements);
	}

	@NotNull
	public static <E> LinkedList<E> asLinkedList(@NotNull Iterable<E> elements) {
		return add(new LinkedList<>(), elements);
	}

	/*
	 - List creation - Primitives
	 */

	@NotNull
	public static List<Boolean> asBooleanList(boolean... booleans) {
		final ArrayList<Boolean> list = new ArrayList<>(booleans.length);
		for (boolean b : booleans)
			list.add(b);
		return list;
	}

	@NotNull
	public static List<Character> asCharList(char... chars) {
		final ArrayList<Character> list = new ArrayList<>(chars.length);
		for (char c : chars)
			list.add(c);
		return list;
	}

	@NotNull
	public static List<Byte> asByteList(byte... bytes) {
		final ArrayList<Byte> list = new ArrayList<>(bytes.length);
		for (byte b : bytes)
			list.add(b);
		return list;
	}

	@NotNull
	public static List<Short> asShortList(short... shorts) {
		final ArrayList<Short> list = new ArrayList<>(shorts.length);
		for (short s : shorts)
			list.add(s);
		return list;
	}

	@NotNull
	public static List<Integer> asIntList(int... ints) {
		final ArrayList<Integer> list = new ArrayList<>(ints.length);
		for (int i : ints)
			list.add(i);
		return list;
	}

	@NotNull
	public static List<Long> asLongList(long... longs) {
		final ArrayList<Long> list = new ArrayList<>(longs.length);
		for (long l : longs)
			list.add(l);
		return list;
	}

	@NotNull
	public static List<Float> asFloatList(float... floats) {
		final ArrayList<Float> list = new ArrayList<>(floats.length);
		for (float f : floats)
			list.add(f);
		return list;
	}

	@NotNull
	public static List<Double> asDoubleList(double... doubles) {
		final ArrayList<Double> list = new ArrayList<>(doubles.length);
		for (double d : doubles)
			list.add(d);
		return list;
	}

	/*
	 - Set creation - HashSet
	 */

	@NotNull
	public static <E> HashSet<E> asHashSet(@NotNull E element) {
		final HashSet<E> set = new HashSet<>();
		set.add(element);
		return set;
	}

	@NotNull
	@SafeVarargs
	public static <E> HashSet<E> asHashSet(@NotNull E... elements) {
		return add(new HashSet<>(elements.length), elements);
	}

	@NotNull
	public static <E> HashSet<E> asHashSet(@NotNull Iterable<E> elements) {
		return add(new HashSet<>(), elements);
	}

	/*
	 - Set creation - LinkedHashSet
	 */

	@NotNull
	public static <E> LinkedHashSet<E> asLinkedHashSet(@NotNull E element) {
		final LinkedHashSet<E> set = new LinkedHashSet<>();
		set.add(element);
		return set;
	}

	@NotNull
	@SafeVarargs
	public static <E> LinkedHashSet<E> asLinkedHashSet(@NotNull E... elements) {
		return add(new LinkedHashSet<>(elements.length), elements);
	}

	@NotNull
	public static <E> LinkedHashSet<E> asLinkedHashSet(@NotNull Iterable<E> elements) {
		return add(new LinkedHashSet<>(), elements);
	}

	/*
	 - Set creation - TreeSet
	 */

	@NotNull
	public static <E extends Comparable<C>, C> TreeSet<E> asTreeSet(@NotNull E element) {
		final TreeSet<E> set = new TreeSet<>();
		set.add(element);
		return set;
	}

	@NotNull
	@SafeVarargs
	public static <E extends Comparable<C>, C> TreeSet<E> asTreeSet(@NotNull E... elements) {
		return add(new TreeSet<>(), elements);
	}

	@NotNull
	public static <E extends Comparable<C>, C> TreeSet<E> asTreeSet(@NotNull Iterable<E> elements) {
		return add(new TreeSet<>(), elements);
	}

	/*
	 - Set creation - EnumSet
	 */

	@NotNull
	public static <E extends Enum<E>> EnumSet<E> asEnumSet(@NotNull Class<E> enumClass) {
		return EnumSet.noneOf(enumClass);
	}

	@NotNull
	public static <E extends Enum<E>> EnumSet<E> asEnumSet(@NotNull E element) {
		return EnumSet.of(element);
	}

	@NotNull
	@SafeVarargs
	public static <E extends Enum<E>> EnumSet<E> asEnumSet(@NotNull E first, @NotNull E... rest) {
		return EnumSet.of(first, rest);
	}

	@Nullable
	public static <E extends Enum<E>> EnumSet<E> asEnumSet(@NotNull Collection<E> elements) {
		final E first = elements.stream().findFirst().orElse(null);
		return first == null ? null : add(asEnumSet(first.getDeclaringClass()), elements);
	}

	/*
	 - Collection editor
	 */

	@NotNull
	@SafeVarargs
	public static <C extends Collection<E>, E> C edit(@NotNull C list, Consumer<C>... edits) {
		for (Consumer<C> consumer : edits)
			consumer.accept(list);
		return list;
	}

	/*
	 - Cloning - List
	 */

	@NotNull
	public static <E> List<E> clone(@NotNull List<E> list) {
		return add(new ArrayList<>(list.size()), list);
	}

	@NotNull
	@SafeVarargs
	public static <E> List<E> clone(@NotNull List<E> list, @NotNull Consumer<List<E>>... edits) {
		return edit(clone(list), edits);
	}

	@NotNull
	public static <E> List<E> clone(@NotNull List<E> list, @NotNull Predicate<E> filter) {
		return add(list, new ArrayList<>(), filter);
	}

	/*
	 - Cloning - ArrayList
	 */

	@NotNull
	public static <E> ArrayList<E> clone(@NotNull ArrayList<E> list) {
		return add(new ArrayList<>(list.size()), list);
	}

	@NotNull
	@SafeVarargs
	public static <E> ArrayList<E> clone(@NotNull ArrayList<E> list, @NotNull Consumer<ArrayList<E>>... edits) {
		return edit(clone(list), edits);
	}

	@NotNull
	public static <E> ArrayList<E> clone(@NotNull ArrayList<E> list, @NotNull Predicate<E> filter) {
		return add(list, new ArrayList<>(), filter);
	}

	/*
	 - Cloning - LinkedList
	 */

	@NotNull
	public static <E> LinkedList<E> clone(@NotNull LinkedList<E> list) {
		return add(new LinkedList<>(), list);
	}

	@NotNull
	@SafeVarargs
	public static <E> LinkedList<E> clone(@NotNull LinkedList<E> list, @NotNull Consumer<LinkedList<E>>... edits) {
		return edit(clone(list), edits);
	}

	@NotNull
	public static <E> LinkedList<E> clone(@NotNull LinkedList<E> list, @NotNull Predicate<E> filter) {
		return add(list, new LinkedList<>(), filter);
	}

	/*
	 - Cloning - Set
	 */

	@NotNull
	public static <E> Set<E> clone(@NotNull Set<E> set) {
		return add(new HashSet<>(set.size()), set);
	}

	@NotNull
	@SafeVarargs
	public static <E> Set<E> clone(@NotNull Set<E> set, @NotNull Consumer<Set<E>>... edits) {
		return edit(clone(set), edits);
	}

	@NotNull
	public static <E> Set<E> clone(@NotNull Set<E> set, @NotNull Predicate<E> filter) {
		return add(set, new HashSet<>(), filter);
	}

	/*
	 - Cloning - HashSet
	 */

	@NotNull
	public static <E> HashSet<E> clone(@NotNull HashSet<E> set) {
		return add(new HashSet<>(set.size()), set);
	}

	@NotNull
	@SafeVarargs
	public static <E> HashSet<E> clone(@NotNull HashSet<E> set, @NotNull Consumer<HashSet<E>>... edits) {
		return edit(clone(set), edits);
	}

	@NotNull
	public static <E> HashSet<E> clone(@NotNull HashSet<E> set, @NotNull Predicate<E> filter) {
		return add(set, new HashSet<>(), filter);
	}

	/*
	 - Cloning - LinkedHashSet
	 */

	@NotNull
	public static <E> LinkedHashSet<E> clone(@NotNull LinkedHashSet<E> set) {
		return add(new LinkedHashSet<>(set.size()), set);
	}

	@NotNull
	@SafeVarargs
	public static <E> LinkedHashSet<E> clone(@NotNull LinkedHashSet<E> set, @NotNull Consumer<LinkedHashSet<E>>... edits) {
		return edit(clone(set), edits);
	}

	@NotNull
	public static <E> LinkedHashSet<E> clone(@NotNull LinkedHashSet<E> set, @NotNull Predicate<E> filter) {
		return add(set, new LinkedHashSet<>(), filter);
	}

	/*
	 - Cloning - TreeSet
	 */

	@NotNull
	public static <E extends Comparable<C>, C> TreeSet<E> clone(@NotNull TreeSet<E> set) {
		return add(new TreeSet<>(), set);
	}

	@NotNull
	@SafeVarargs
	public static <E extends Comparable<C>, C> TreeSet<E> clone(@NotNull TreeSet<E> set, @NotNull Consumer<TreeSet<E>>... edits) {
		return edit(clone(set), edits);
	}

	@NotNull
	public static <E extends Comparable<C>, C> TreeSet<E> clone(@NotNull TreeSet<E> set, @NotNull Predicate<E> filter) {
		return add(set, new TreeSet<>(), filter);
	}

	/*
	 - Cloning - EnumSet
	 */

	@NotNull
	public static <E extends Enum<E>> EnumSet<E> clone(@NotNull EnumSet<E> set) {
		return EnumSet.copyOf(set);
	}

	@NotNull
	@SafeVarargs
	public static <E extends Enum<E>> EnumSet<E> clone(@NotNull EnumSet<E> set, @NotNull Consumer<EnumSet<E>>... edits) {
		return edit(clone(set), edits);
	}

	@NotNull
	@SuppressWarnings("unchecked")
	public static <E extends Enum<E>> EnumSet<E> clone(@NotNull EnumSet<E> set, @NotNull Predicate<E> filter) {
		final E first = set.stream().findFirst().orElse(null);
		return first == null ? clone(set) : add(set, asEnumSet(first.getClass()), filter);
	}

	/*
	 - Addition - add
	 */

	@NotNull
	@SafeVarargs
	public static <C extends Collection<E>, E> C add(@NotNull C collection, @NotNull E... elements) {
		Collections.addAll(collection, elements);
		return collection;
	}

	@NotNull
	public static <C extends Collection<E>, E> C add(@NotNull C collection, @NotNull Collection<E> other) {
		collection.addAll(other);
		return collection;
	}

	@NotNull
	public static <C extends Collection<E>, E> C add(@NotNull C collection, @NotNull Iterable<E> elements) {
		for(E element : elements)
			collection.add(element);
		return collection;
	}

	@NotNull
	public static <C extends Collection<E>, E> C add(@NotNull C src, @NotNull C target, @NotNull Predicate<E> condition) {
		for (E element : src)
			if (condition.test(element))
				target.add(element);
		return target;
	}

	/*
	 - Addition - addAll
	 */

	@NotNull
	@SafeVarargs
	public static <C extends Collection<E>, E> C addAll(@NotNull C collection, @NotNull Collection<E>... others) {
		for (Collection<E> other : others)
			collection.addAll(other);
		return collection;
	}

	@NotNull
	@SafeVarargs
	public static <C extends Collection<E>, E> C addAll(@NotNull C collection, @NotNull Predicate<E> condition, @NotNull Collection<E>... others) {
		for (Collection<E> other : others)
			for (E element : other)
				if (condition.test(element))
					collection.add(element);
		return collection;
	}

	/*
	 - Join - Generic
	 */

	/**
	 * Creates a new {@link Collection} of the same type as the first
	 * {@code collection}, containing all elements from the given
	 * {@code collection} and all additional collections ({@code others}).
	 * All elements from {@code collection} and each {@code others} collection
	 * are added in order to the new collection.
	 * <p>
	 * This method uses reflection to instantiate a new empty collection
	 * of the same type as {@code collection}.
	 *
	 * @param collection The base collection from which to derive the new
	 * collection type and whose elements are included.
	 * @param others Additional collections whose elements will be joined.
	 *
	 * @param <C> The type of the first {@code collection}.
	 * @param <E> The type of elements contained in all collections.
	 *
	 * @return A new {@link Collection} of the same type as {@code collection}, containing
	 * all elements from {@code collection} and {@code others}. {@code null} if instantiation fails
	 *
	 * @throws NullPointerException if {@code collection} or any of {@code others} is {@code null}
	 *
	 * @since JSky 1.0.0
	 */
	@Nullable
	@SafeVarargs
	public static <C extends Collection<E>, E> C join(@NotNull C collection, @NotNull Collection<E>... others) {
		final C joined = of(collection);
		return joined == null ? null : addAll(add(joined, collection), others);
	}

	/*
	 - Join - Lists
	 */

	@NotNull
	@SafeVarargs
	public static <E> ArrayList<E> join(@NotNull ArrayList<E> list, @NotNull Collection<E>... others) {
		return addAll(clone(list), others);
	}

	@NotNull
	@SafeVarargs
	public static <E> LinkedList<E> join(@NotNull LinkedList<E> list, @NotNull Collection<E>... others) {
		return addAll(clone(list), others);
	}

	/*
	 - Join - Sets
	 */

	@NotNull
	@SafeVarargs
	public static <E> HashSet<E> join(@NotNull HashSet<E> set, @NotNull Collection<E>... others) {
		return addAll(clone(set), others);
	}

	@NotNull
	@SafeVarargs
	public static <E> LinkedHashSet<E> join(@NotNull LinkedHashSet<E> set, @NotNull Collection<E>... others) {
		return addAll(clone(set), others);
	}

	@NotNull
	@SafeVarargs
	public static <E extends Comparable<C>, C> TreeSet<E> join(@NotNull TreeSet<E> set, @NotNull Collection<E>... others) {
		return addAll(clone(set), others);
	}

	@NotNull
	@SafeVarargs
	public static <E extends Enum<E>> EnumSet<E> join(@NotNull EnumSet<E> set, @NotNull Collection<E>... others) {
		return addAll(clone(set), others);
	}

	/*
	 - Removal
	 */

	@NotNull
	@SafeVarargs
	public static <C extends Collection<E>, E> C remove(@NotNull C collection, @NotNull E... elements) {
		for (E element : elements)
			collection.remove(element);
		return collection;
	}

	@NotNull
	public static <C extends Collection<E>, E> C remove(@NotNull C collection, @NotNull Collection<E> other) {
		collection.removeAll(other);
		return collection;
	}

	@NotNull
	public static <C extends Collection<E>, E> C remove(@NotNull C collection, @NotNull Iterable<E> elements) {
		for (E element : elements)
			collection.remove(element);
		return collection;
	}

	@NotNull
	public static <C extends Collection<E>, E> C remove(@NotNull C collection, @NotNull Predicate<E> condition) {
		collection.removeIf(condition);
		return collection;
	}

	/*
	 - Mapping
	 */

	@NotNull
	public static <T, O> T[] map(@NotNull O[] src, @NotNull T[] target, @NotNull Function<O, T> mapper) {
		for(int i = 0; i < src.length; i++)
			target[i] = mapper.apply(src[i]);
		return target;
	}

	@NotNull
	public static <S extends Collection<O>, T extends Collection<R>, O, R> T map(@NotNull S src, @NotNull T target, @NotNull Function<O, R> mapper) {
		for (O element : src)
			target.add(mapper.apply(element));
		return target;
	}

	/*
	 - Mapping - Collection
	 */

	@NotNull
	public static <S, R> Collection<R> map(@NotNull Collection<S> collection, @NotNull Function<S, R> mapper) {
		return map(collection, new ArrayList<>(collection.size()), mapper);
	}

	/*
	 - Mapping - Lists
	 */

	@NotNull
	public static <S, R> List<R> map(@NotNull List<S> list, @NotNull Function<S, R> mapper) {
		return map(list, new ArrayList<>(list.size()), mapper);
	}

	@NotNull
	public static <S, R> ArrayList<R> map(@NotNull ArrayList<S> list, @NotNull Function<S, R> mapper) {
		return map(list, new ArrayList<>(list.size()), mapper);
	}

	@NotNull
	public static <S, R> LinkedList<R> map(@NotNull LinkedList<S> list, @NotNull Function<S, R> mapper) {
		return map(list, new LinkedList<>(), mapper);
	}

	/*
	 - Mapping - Sets
	 */

	@NotNull
	public static <S, R> Set<R> map(@NotNull Set<S> set, @NotNull Function<S, R> mapper) {
		return map(set, new HashSet<>(set.size()), mapper);
	}

	@NotNull
	public static <S, R> HashSet<R> map(@NotNull HashSet<S> set, @NotNull Function<S, R> mapper) {
		return map(set, new HashSet<>(set.size()), mapper);
	}

	@NotNull
	public static <S, R> LinkedHashSet<R> map(@NotNull LinkedHashSet<S> set, @NotNull Function<S, R> mapper) {
		return map(set, new LinkedHashSet<>(set.size()), mapper);
	}

	@NotNull
	public static <S extends Comparable<SC>, R extends Comparable<RC>, SC, RC> TreeSet<R> map(@NotNull TreeSet<S> set, @NotNull Function<S, R> mapper) {
		return map(set, new TreeSet<>(), mapper);
	}

	/*
	 - Element getters
	 */

	@Nullable
	public static <E> E get(@Nullable E @NotNull [] array, @NotNull Predicate<E> condition) {
		for (E element : array)
			if (condition.test(element))
				return element;
		return null;
	}

	@NotNull
	public static <E> E get(@Nullable E @NotNull [] array, @NotNull Predicate<E> condition, @NotNull E def) {
		final E element = get(array, condition);
		return element == null ? def : element;
	}

	@Nullable
	public static <E> E get(@NotNull Iterable<E> iterable, @NotNull Predicate<E> condition) {
		for (E element : iterable)
			if (condition.test(element))
				return element;
		return null;
	}

	@NotNull
	public static <E> E get(@NotNull Iterable<E> iterable, @NotNull Predicate<E> condition, @NotNull E def) {
		final E element = get(iterable, condition);
		return element == null ? def : element;
	}

	@Nullable
	public static <E> E get(@NotNull Iterable<E> iterable, int index) {
		int i = index;
		for (E element : iterable) {
			if (i == 0)
				return element;
			i--;
		}
		return null;
	}

	/*
	 - Contains element - Generic
	 */

	public static <E> boolean contains(@NotNull Iterable<E> iterable, @NotNull Predicate<E> condition) {
		return get(iterable, condition) != null;
	}

	public static <E> boolean contains(@Nullable E @NotNull [] array, @NotNull Predicate<E> condition) {
		return get(array, condition) != null;
	}

	/*
	 - Contains element - Array primitives
	 */

	public static boolean contains(char @NotNull [] array, @NotNull CharPredicate condition) {
		for (final char c : array)
			if (condition.test(c))
				return true;
		return false;
	}

	public static boolean contains(byte @NotNull [] array, @NotNull BytePredicate condition) {
		for (final byte b : array)
			if (condition.test(b))
				return true;
		return false;
	}

	public static boolean contains(short @NotNull [] array, @NotNull ShortPredicate condition) {
		for (final short s : array)
			if (condition.test(s))
				return true;
		return false;
	}

	public static boolean contains(int @NotNull [] array, @NotNull IntPredicate condition) {
		for (final int i : array)
			if (condition.test(i))
				return true;
		return false;
	}

	public static boolean contains(long @NotNull [] array, @NotNull LongPredicate condition) {
		for (final long l : array)
			if (condition.test(l))
				return true;
		return false;
	}

	public static boolean contains(int @NotNull [] array, @NotNull FloatPredicate condition) {
		for (final float f : array)
			if (condition.test(f))
				return true;
		return false;
	}

	public static boolean contains(long @NotNull [] array, @NotNull DoublePredicate condition) {
		for (final double d : array)
			if (condition.test(d))
				return true;
		return false;
	}

	/*
	 - Element getters - Random
	 */

	@Nullable
	public static <E> E getRandom(@Nullable E @NotNull [] array) {
		return array[JNumbers.random().nextInt(0, array.length)];
	}

	@Nullable
	public static <E> E getRandom(@NotNull Collection<E> collection) {
		return get(collection, JNumbers.random().nextInt(0, collection.size()));
	}

	@Nullable
	public static <E> E getRandom(@NotNull List<E> list) {
		return list.get(JNumbers.random().nextInt(0, list.size()));
	}

	@NotNull
	public static <E> List<E> getRandom(@NotNull Collection<E> collection, int amount, boolean allowDuplicates) {
		if (amount >= collection.size())
			return new ArrayList<>(collection);
		final List<E> res = new ArrayList<>(amount);
		while (res.size() != amount) {
			final E element = getRandom(collection);
			if (allowDuplicates || !res.contains(element))
				res.add(element);
		}
		return res;
	}

	@NotNull
	public static <E> List<E> getRandom(@NotNull Collection<E> collection, int amount) {
		return getRandom(collection, amount, false);
	}

	/*
	 - Size of
	 */

	public static int sizeOf(@NotNull Collection<?>... collections) {
		int size = 0;
		for (Collection<?> collection : collections)
			size += collection.size();
		return size;
	}

	/*
	 - Internal utility
	 */

	@SuppressWarnings("unchecked")
	private static <C extends Collection<E>, E> Constructor<C> getConstructor(C from) {
		final Constructor<?>[] constructors = from.getClass().getDeclaredConstructors();
		for (final Constructor<?> c : constructors)
			if (c.getParameterTypes().length == 0 && c.getModifiers() == Modifier.PUBLIC)
				return (Constructor<C>) c;
		return null;
	}

	@Nullable
	private static <C extends Collection<E>, E> C of(@NotNull C from) {
		final Constructor<C> constructor = getConstructor(from);
		if (constructor == null)
			return null;
		try {
			return constructor.newInstance();
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			return null;
		}
	}
}
