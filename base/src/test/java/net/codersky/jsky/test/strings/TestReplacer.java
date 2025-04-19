package net.codersky.jsky.test.strings;

import net.codersky.jsky.strings.Replacement;
import net.codersky.jsky.strings.Replacer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestReplacer {

    @Test
    public void testConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new Replacer("not even"));
        assertThrows(NullPointerException.class, () -> new Replacer("NPE", null));
        assertThrows(NullPointerException.class, () -> new Replacer((Object[]) null));
    }

    @Test
    public void testReplace() {
        final Replacer world = new Replacer("%w", "world");
        assertEquals("Hello world", world.replaceAt("Hello %w"));
        final Replacer withrep = new Replacer("%r", new ExampleReplacement());
        world.add(withrep);
        assertEquals("world, replacement", world.replaceAt("%w, %r"));
    }

    public void testClone() {
        final Replacer original = new Replacer("hi", "bye", "code", 42);
        final Replacer clone = original.clone();
        assertEquals(clone, original);
    }

    private class ExampleReplacement implements Replacement {

        @Override
        public @NotNull String asReplacement() {
            return "replacement";
        }
    }
}
