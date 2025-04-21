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
		BytePredicate isOne = v -> v == 1;
		BytePredicate isTwo = v -> v == 2;
		assertTrue(isOne.or(isTwo).test((byte) 1));
		assertFalse(isOne.and(isTwo).test((byte) 1));
		assertFalse(isOne.negate().test((byte) 1));
	}

	@Test
	public void testCharPredicate() {
		CharPredicate isA = v -> v == 'a';
		CharPredicate isB = v -> v == 'b';
		assertTrue(isA.or(isB).test('a'));
		assertFalse(isA.and(isB).test('a'));
		assertFalse(isA.negate().test('a'));
	}

	@Test
	public void testFloatPredicate() {
		FloatPredicate isOne = v -> v == 1f;
		FloatPredicate isTwo = v -> v == 2f;
		assertTrue(isOne.or(isTwo).test(1f));
		assertFalse(isOne.and(isTwo).test(1f));
		assertFalse(isOne.negate().test(1f));
	}

	@Test
	public void testShortPredicate() {
		ShortPredicate isOne = v -> v == 1;
		ShortPredicate isTwo = v -> v == 2;
		assertTrue(isOne.or(isTwo).test((short) 1));
		assertFalse(isOne.and(isTwo).test((short) 1));
		assertFalse(isOne.negate().test((short) 1));
	}
}
