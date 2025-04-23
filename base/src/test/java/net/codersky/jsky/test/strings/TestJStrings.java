package net.codersky.jsky.test.strings;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static net.codersky.jsky.strings.JStrings.asString;
import static net.codersky.jsky.strings.JStrings.endsWith;
import static net.codersky.jsky.strings.JStrings.hasContent;
import static net.codersky.jsky.strings.JStrings.hasKeyPattern;
import static net.codersky.jsky.strings.JStrings.startsWith;
import static net.codersky.jsky.strings.JStrings.testAllChars;
import static net.codersky.jsky.strings.JStrings.testAnyChar;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestJStrings {

	final Class<NullPointerException> NPE = NullPointerException.class;
	final Class<IndexOutOfBoundsException> IOOBE = IndexOutOfBoundsException.class;

	@Test
	public void testHasContent() {
		assertFalse(hasContent(null));
		assertFalse(hasContent(""));
		assertFalse(hasContent("\t"));
		assertTrue(hasContent("Hello"));
	}

	@Test
	public void testTestAllChars() {
		assertTrue(testAllChars(ch -> ch == 'a', "aaaaaa"));
		assertFalse(testAllChars(ch -> ch == 'a', "aaabaaa"));
		assertTrue(testAllChars(ch -> ch == 'a', ""));
		assertTrue(testAllChars(ch -> ch == 'a', 1, "b"));
		assertTrue(testAllChars(ch -> ch == 'a', 1, "ba"));
		assertThrows(IOOBE, () -> testAllChars(ch -> true, 2, "a"));
		assertThrows(NPE, () -> testAllChars(ch -> true, null));
		assertThrows(NPE, () -> testAllChars(null, "a"));
	}

	@Test
	public void testTestAnyChar() {
		assertTrue(testAnyChar(ch -> ch == 'a', "aaaaaa"));
		assertTrue(testAnyChar(ch -> ch == 'b', "aaabaaa"));
		assertFalse(testAnyChar(ch -> ch == 'c', "aaabaaa"));
		assertFalse(testAnyChar(ch -> ch == 'a', ""));
		assertFalse(testAnyChar(ch -> ch == 'a', 1, "a"));
		assertFalse(testAnyChar(ch -> ch == 'b', 1, "ba"));
		assertThrows(IOOBE, () -> testAnyChar(ch -> true, 2, "a"));
		assertThrows(NPE, () -> testAnyChar(ch -> true, null));
		assertThrows(NPE, () -> testAnyChar(null, "a"));
	}

	@Test
	public void testStartsWith() {
		assertTrue(startsWith(true, "pReFfIxStr", "preffix"));
		assertFalse(startsWith(false, "pReFfIxStr", "preffix"));
		assertFalse(startsWith(false, "pReFfIxStr", "preffixIsLongerThanStr"));
	}

	@Test
	public void testEndsWith() {
		assertTrue(endsWith(true, "strSuFfIx", "suffix"));
		assertFalse(endsWith(false, "strSuFfIx", "suffix"));
		assertFalse(endsWith(false, "strSuFfIx", "suffixIsLongerThanStr"));
	}

	@Test
	public void testHasKeyPattern() {
		assertTrue(hasKeyPattern("key-pattern"));
		assertTrue(hasKeyPattern("keypattern"));
		assertFalse(hasKeyPattern("-"));
		assertFalse(hasKeyPattern(" "));
		assertFalse(hasKeyPattern(""));
		assertFalse(hasKeyPattern("-key-pattern"));
		assertFalse(hasKeyPattern("-key-pattern-"));
		assertFalse(hasKeyPattern("key-pattern-"));
	}

	@Test
	public void testAsString() {
		assertEquals("hello world", asString(Arrays.asList("hello", "world"), " "));
		assertEquals("hello world", asString(Arrays.asList("hello", "", "world"), " "));
		assertEquals("hello  world", asString(Arrays.asList("hello", "", "world"), " ", null));
	}
}
