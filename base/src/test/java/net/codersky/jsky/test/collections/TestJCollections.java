package net.codersky.jsky.test.collections;

import net.codersky.jsky.collections.JCollections;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestJCollections {

	@Test
	public void testJoin() {
		final ArrayList<Integer> list = JCollections.asArrayList(1, 2, 3);
		final HashSet<Integer> set = JCollections.asHashSet(4, 5, 6);
		final LinkedList<Integer> linked = JCollections.asLinkedList(7, 8, 9);
		final ArrayList<Integer> joined = JCollections.join(list, set, linked);
		assertNotNull(joined);
		assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9]", Arrays.toString(joined.toArray()));
	}
}
