package net.codersky.jsky.test.math;

import org.junit.jupiter.api.Test;

import static net.codersky.jsky.math.JNumbers.isHex;
import static net.codersky.jsky.math.JNumbers.random;
import static net.codersky.jsky.math.JNumbers.range;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestJNumbers {

	@Test
	public void testIntRange() {
		assertArrayEquals(new int[] {1, 2, 3}, range(1, 3));
		assertArrayEquals(new int[] {1}, range(1, 1));
		assertArrayEquals(new int[] {-2, -1, 0, 1, 2}, range(-2, 2));
		assertArrayEquals(new int[] {2, 1, 0, -1, -2}, range(2, -2));
	}

	@Test
	public void testLongRange() {
		assertArrayEquals(new long[] {1, 2, 3}, range(1L, 3L));
		assertArrayEquals(new long[] {1}, range(1L, 1L));
		assertArrayEquals(new long[] {-2, -1, 0, 1, 2}, range(-2L, 2L));
		assertArrayEquals(new long[] {2, 1, 0, -1, -2}, range(2L, -2L));
	}

	@Test
	public void testRandomInstance() {
		assertNotNull(random());
		assertSame(random(), random());
	}

	// TODO: Add missing methods from random() here to isHex() here.

	@Test
	public void testIsHex() {
		assertTrue(isHex("00112233445566778899aAbBcCdDeEfF"));
		assertFalse(isHex("00112233445566778899aAbBcCdDeEfF", true));
		assertTrue(isHex("#ff0000", true));
		assertTrue(isHex("#ff0000"));
		assertTrue(isHex("ff0000"));
		assertTrue(isHex("#ff0000", false));
	}
}
