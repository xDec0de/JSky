package net.codersky.jsky.test;

import org.junit.jupiter.api.Test;

import static net.codersky.jsky.strings.JStrings.hasContent;
import static net.codersky.jsky.strings.JStrings.testAllChars;
import static net.codersky.jsky.strings.JStrings.testAnyChar;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestJStrings {

	final Class<NullPointerException> NPE = NullPointerException.class;

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
		assertThrows(NPE, () -> testAllChars(ch -> true, "", null));
		assertThrows(NPE, () -> testAllChars(null, "a"));
	}

	@Test
	public void testTestAnyChar() {
		assertTrue(testAnyChar(ch -> ch == 'a', "aaaaaa"));
		assertTrue(testAnyChar(ch -> ch == 'b', "aaabaaa"));
		assertFalse(testAnyChar(ch -> ch == 'c', "aaabaaa", "bbbabbb"));
		assertFalse(testAnyChar(ch -> ch == 'a', ""));
		assertThrows(NPE, () -> testAnyChar(ch -> true, "", null));
		assertThrows(NPE, () -> testAnyChar(null, "a"));
	}
}
