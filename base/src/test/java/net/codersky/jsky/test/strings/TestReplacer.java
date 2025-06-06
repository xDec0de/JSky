package net.codersky.jsky.test.strings;

import net.codersky.jsky.strings.Replacement;
import net.codersky.jsky.strings.Replacer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestReplacer {

	@Test
	public void testConstructor() {
		assertThrows(IllegalArgumentException.class, () -> new Replacer("not even"));
		assertThrows(NullPointerException.class, () -> new Replacer((Object[]) null));
	}

	@Test
	public void testReplace() {

		// Basic replacer
		final Replacer world = new Replacer("%w", "world", null);
		assertEquals("Hello world", world.replaceAt("Hello %w"));

		// Replacement interface
		final ExampleReplacement replacement = new ExampleReplacement();

		final Replacer withrep = new Replacer("%r", replacement);
		world.add(withrep);
		assertEquals("world, replacement", world.replaceAt("%w, %r"));

		// Value change in replacement
		replacement.replacement = "Hello";
		assertEquals("Hello world", world.replaceAt("%r %w"));
	}

	@Test
	public void testClone() {
		final Replacer original = new Replacer("hi", "bye", "code", 42);
		final Replacer clone = original.clone();
		assertEquals(clone, original);
	}

	private static class ExampleReplacement implements Replacement {

		String replacement = "replacement";

		@Override
		public @NotNull String asReplacement() {
            return replacement;
        }
	}
}
