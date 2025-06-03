package net.codersky.jsky.test.collections;

import net.codersky.jsky.collections.JCollections;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestJCollections {

	@Test
	public void testInstance() {
		try {
			final Constructor<?> constructor = JCollections.class.getDeclaredConstructor();
			constructor.setAccessible(true);
			constructor.newInstance();
		} catch (final InvocationTargetException | InstantiationException | IllegalAccessException |
		               NoSuchMethodException e) {
			assertEquals(UnsupportedOperationException.class, e.getCause().getClass());
			assertEquals("Can't instantiate JCollections.", e.getCause().getMessage());
		}
	}

	/*
	 - Collection creation
	 */

	private void testGenericCreate(boolean strictOrder,
	                               Function<String, Collection<String>> one,
	                               Function<String[], Collection<String>> more,
	                               Function<Iterable<String>, Collection<String>> iter) {
		assertEquals("[one]", toStr(one.apply("one")));
		final Iterable<String> two = more.apply(new String[]{"one", "two"});
		final String varargs = toStr(two);
		final String iterable = toStr(iter.apply(two));
		if (strictOrder) {
			assertEquals("[one, two]", varargs);
			assertEquals("[one, two]", iterable);
		} else {
			assertTrue(varargs.equals("[one, two]") || varargs.equals("[two, one]"));
			assertTrue(iterable.equals("[one, two]") || iterable.equals("[two, one]"));
		}
	}

	@Test
	public void testAsGenericListAndSet() { // NOTE: HashSet element order is implementation-specific.
		testGenericCreate(true, JCollections::asArrayList, JCollections::asArrayList, JCollections::asArrayList);
		testGenericCreate(true, JCollections::asLinkedList, JCollections::asLinkedList, JCollections::asLinkedList);
		testGenericCreate(false, JCollections::asHashSet, JCollections::asHashSet, JCollections::asHashSet);
		testGenericCreate(true, JCollections::asLinkedHashSet, JCollections::asLinkedHashSet, JCollections::asLinkedHashSet);
		testGenericCreate(true, JCollections::asTreeSet, JCollections::asTreeSet, JCollections::asTreeSet);
	}

	@Test
	public void testAsPrimitiveList() {
		assertEquals("[true, false]", toStr(JCollections.asBooleanList(true, false)));
		assertEquals("[1, 2]", toStr(JCollections.asCharList('1', '2')));
		assertEquals("[1, 2]", toStr(JCollections.asByteList((byte) 1, (byte) 2)));
		assertEquals("[1, 2]", toStr(JCollections.asShortList((short) 1, (short) 2)));
		assertEquals("[1, 2]", toStr(JCollections.asIntList(1, 2)));
		assertEquals("[1, 2]", toStr(JCollections.asLongList(1, 2)));
		assertEquals("[1.0, 2.0]", toStr(JCollections.asFloatList(1, 2)));
		assertEquals("[1.0, 2.0]", toStr(JCollections.asDoubleList(1, 2)));
	}

	@Test
	public void testAsEnumSet() {
		assertEquals("[MONDAY]", toStr(JCollections.asEnumSet(DayOfWeek.MONDAY)));
		final EnumSet<DayOfWeek> mt = JCollections.asEnumSet(DayOfWeek.MONDAY, DayOfWeek.TUESDAY);
		assertEquals("[MONDAY, TUESDAY]", toStr(mt));
		assertEquals("[MONDAY, TUESDAY]", toStr(JCollections.asEnumSet(mt)));
		assertNull(JCollections.asEnumSet(new ArrayList<DayOfWeek>()));
	}

	/*
	 - Join
	 */

	@Test
	public void testArrayJoin() {
		final ArrayList<Integer> list = JCollections.asArrayList(1, 2, 3);
		final HashSet<Integer> set = JCollections.asHashSet(4, 5, 6);
		final LinkedList<Integer> linked = JCollections.asLinkedList(7, 8, 9);
		final ArrayList<Integer> joined = JCollections.join(list, set, linked);
		assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9]", toStr(joined));
	}

	/*
	 - Util
	 */

	private String toStr(@NotNull Collection<?> collection) {
		return Arrays.toString(collection.toArray());
	}

	private String toStr(@NotNull Iterable<?> iterable) {
		final ArrayList<Object> list = new ArrayList<>();
		for (Object o : iterable)
			list.add(o);
		return toStr(list);
	}
}
