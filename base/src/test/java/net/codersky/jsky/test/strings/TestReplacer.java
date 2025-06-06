package net.codersky.jsky.test.strings;

import net.codersky.jsky.strings.Replacement;
import net.codersky.jsky.strings.Replacer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestReplacer {

	@Test
	public void testConstructor() {
		assertThrows(IllegalArgumentException.class, () -> new Replacer("not even"));
		assertThrows(NullPointerException.class, () -> new Replacer((Object[]) null));
		assertEquals("Replacer{hello=world}", new Replacer("hello", "world").toString());
		assertEquals("Replacer{null=null}", new Replacer(null, null).toString());
	}

	@Test
	public void testClone() {
		final Replacer original = new Replacer("hi", "bye", "code", 42);
		final Replacer clone = original.clone();
		assertEquals(clone, original);
	}

	@Test
	public void testOverwrite() {
		final Replacer helloWorld = new Replacer("%hw", "world");
		helloWorld.add("%hw", "hello world");
		assertEquals("hello world", helloWorld.replaceAt("%hw"));
	}

	@Test
	public void testReplaceAtString() {

		// Basic replacer
		final Replacer world = new Replacer("%w", "world");
		assertEquals("Hello world", world.replaceAt("Hello %w"));

		// Empty string and replacements
		assertEquals("", world.replaceAt(""));
		assertEquals("hi", new Replacer().replaceAt("hi"));

		// Replacement interface
		final ExampleReplacement replacement = new ExampleReplacement();

		final Replacer withRep = new Replacer("%r", replacement);
		world.add(withRep);
		assertEquals("world, replacement", world.replaceAt("%w, %r"));

		// Value change in replacement
		replacement.replacement = "Hello";
		assertEquals("Hello world", world.replaceAt("%r %w"));
	}

	@Test
	public void testReplaceAtList() {
		final Replacer rep = new Replacer("%h", "hello", "%w", "world");
		final List<String> replaced = rep.replaceAt("hello", "world");
		assertEquals("hello", replaced.getFirst());
		assertEquals("world", replaced.getLast());
	}

	@Test
	public void testEquals() {
		final Replacer one = new Replacer("%h", "hello");
		assertNotEquals(new Replacer("%w", "hello"), one);
		assertEquals(new Replacer("%h", "hello"), one);

		// Just for branch coverage :P
		assertNotEquals(one, "%h");
		assertEquals(one, one);
		assertEquals(one.getReplacementMap(), one.clone().getReplacementMap());
	}

	private static class ExampleReplacement implements Replacement {

		String replacement = "replacement";

		@Override
		public @NotNull String asReplacement() {
            return replacement;
        }
	}
}
