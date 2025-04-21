package net.codersky.jsky.test.predicate;

import net.codersky.jsky.predicate.BytePredicate;
import net.codersky.jsky.predicate.CharPredicate;
import net.codersky.jsky.predicate.FloatPredicate;
import net.codersky.jsky.predicate.ShortPredicate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestPredicates {

	@Test
	public void testBytePredicate() {
		final byte one = 1;
		final byte two = 2;
		final byte three = 3;
		final BytePredicate isOne = v -> v == 1;
		final BytePredicate isTwo = v -> v == 2;
		assertTrue(isOne.or(isTwo).test(one));
		assertTrue(isOne.or(isTwo).test(two));
		assertFalse(isOne.or(isOne).test(two));
		assertFalse(isOne.and(isTwo).test(one));
		assertTrue(isOne.and(isOne).test(one));
		assertFalse(isOne.and(isTwo).test(two));
		assertFalse(isOne.and(isTwo).test(three));
		assertFalse(isOne.negate().test(one));
		assertTrue(isOne.negate().test(two));
	}

	@Test
	public void testCharPredicate() {
		final char one = '1';
		final char two = '2';
		final char three = '3';
		final CharPredicate isOne = v -> v == '1';
		final CharPredicate isTwo = v -> v == '2';
		assertTrue(isOne.or(isTwo).test(one));
		assertTrue(isOne.or(isTwo).test(two));
		assertFalse(isOne.or(isOne).test(two));
		assertFalse(isOne.and(isTwo).test(one));
		assertTrue(isOne.and(isOne).test(one));
		assertFalse(isOne.and(isTwo).test(two));
		assertFalse(isOne.and(isTwo).test(three));
		assertFalse(isOne.negate().test(one));
		assertTrue(isOne.negate().test(two));
	}

	@Test
	public void testFloatPredicate() {
		final float one = 1;
		final float two = 2;
		final float three = 3;
		final FloatPredicate isOne = v -> v == 1f;
		final FloatPredicate isTwo = v -> v == 2f;
		assertTrue(isOne.or(isTwo).test(one));
		assertTrue(isOne.or(isTwo).test(two));
		assertFalse(isOne.or(isOne).test(two));
		assertFalse(isOne.and(isTwo).test(one));
		assertTrue(isOne.and(isOne).test(one));
		assertFalse(isOne.and(isTwo).test(two));
		assertFalse(isOne.and(isTwo).test(three));
		assertFalse(isOne.negate().test(one));
		assertTrue(isOne.negate().test(two));
	}

	@Test
	public void testShortPredicate() {
		final short one = 1;
		final short two = 2;
		final short three = 3;
		final ShortPredicate isOne = v -> v == 1;
		final ShortPredicate isTwo = v -> v == 2;
		assertTrue(isOne.or(isTwo).test(one));
		assertTrue(isOne.or(isTwo).test(two));
		assertFalse(isOne.or(isOne).test(two));
		assertFalse(isOne.and(isTwo).test(one));
		assertTrue(isOne.and(isOne).test(one));
		assertFalse(isOne.and(isTwo).test(two));
		assertFalse(isOne.and(isTwo).test(three));
		assertFalse(isOne.negate().test(one));
		assertTrue(isOne.negate().test(two));
	}
}
