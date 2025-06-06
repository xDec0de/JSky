package net.codersky.jsky.test.strings.tag;

import net.codersky.jsky.strings.tag.JTag;
import net.codersky.jsky.strings.tag.JTagParseAllResult;
import net.codersky.jsky.strings.tag.JTagParseResult;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static net.codersky.jsky.strings.tag.JTagParser.parse;
import static net.codersky.jsky.strings.tag.JTagParser.parseAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestJTags {

	/*
	 - Parse
	 */

	@Test
	void testNoTags() {
		test(parse("No tags"), "No tags", null, "");
		test(parse(""), "", null, "");
	}

	@Test
	void testSimpleTags() {
		test(parse("<simple>"), "", new JTag("simple"), "");
		test(parse("a<simple>b"), "a", new JTag("simple"), "b");
		test(parse("<tag:content>"), "", new JTag("tag", "content"), "");
		// Simple nested tag
		JTag nested = new JTag("c");
		JTag parent = new JTag("a", "b", new JTag[] { nested });
		test(parse("<a:b<c>>"), "", parent, "");
		// Content nested tag
		nested = new JTag("c", "d");
		parent = new JTag("a", "b", new JTag[] { nested });
		test(parse("<a:b<c:d>>"), "", parent, "");
	}

	@Test
	void testEscape() {
		test(parse("\\<Escape\\>"), "<Escape>", null, "");
		test(parse("<\\\\>"), "", new JTag("\\"), "");
		test(parse("<\\\\:\\\\>"), "", new JTag("\\", "\\"), "");
		test(parse("\\\\<\\\\:\\\\>\\\\"), "\\", new JTag("\\", "\\"), "\\");
	}

	@Test
	void testUnclosedTags() {
		// One
		test(parse("\\<unclosed"), "<unclosed", null, "");
		test(parse("<unclosed"), "<unclosed", null, "");
		test(parse("unclosed\\>"), "unclosed>", null, "");
		test(parse("unclosed>"), "unclosed>", null, "");
		// Nested
		test(parse("<tag<unclosed>"), "<tag<unclosed>", null, "");
		test(parse("<tag:<unclosed>"), "<tag:<unclosed>", null, "");
	}

	@Test
	void testBlankTags() {
		// One
		test(parse("< >"), "< >", null, "");
		test(parse("<a: >"), "<a: >", null, "");
		test(parse("< :a>"), "< :a>", null, "");
		test(parse("< : >"), "< : >", null, "");
		// Nested
		test(parse("<a:< : >>"), "", new JTag("a", "< : >"), "");
	}

	@Test
	void testNestLimt() {
		test(parse("<a:b<c:d>>", 0, 0), "", new JTag("a", "b<c:d>"), "");
	}

	/*
	 - Parse all
	 */

	@Test
	void testParseAll() {
		final JTagParseAllResult res = parseAll("<a:b>c<d:e>f");
		assertEquals(new JTag("a", "b"), res.getTag(0));
		assertEquals("c", res.getString(1));
		assertEquals(new JTag("d", "e"), res.getTag(2));
		assertEquals("f", res.getString(3));
	}

	/*
	 - To string
	 */

	@Test
	void testToString() {
		final String pre = "JTag{name=\"a\", content=\"b\", children=";

		final JTag simple = parse("<a:b>").getTag();
		assertNotNull(simple);
		assertEquals( pre + "[]}", simple.toString());

		final JTag nested = parse("<a:b<a:b>>").getTag();
		assertNotNull(nested);
		assertEquals(pre + "[" + pre + "[]}]}", nested.toString());

		final JTag two_nested = parse("<a:b<a:b><a:b>>").getTag();
		assertNotNull(two_nested);
		assertEquals(pre + "[" + pre + "[]}, " + pre + "[]}]}", two_nested.toString());
	}

	/*
	 - Equals
	 */

	@Test
	void testEquals() {
		final JTag ab = new JTag("a", "b");
		final JTag aa = new JTag("a", "a");
		final JTag bb = new JTag("b", "b");
		// Same instance
		assertEquals(aa, aa);
		// Unrelated
		assertNotEquals(aa, "aa");
		// Different name
		assertNotEquals(ab, bb);
		// Different content
		assertNotEquals(ab, aa);
		// Different children length
		final JTag two_children = new JTag("c", "", new JTag[] {aa, bb});
		final JTag aa_child = new JTag("c", "", new JTag[] {aa});
		assertNotEquals(two_children, aa_child);
		// Different children
		assertNotEquals(aa_child, new JTag("c", "", new JTag[] {bb}));
		// Same children
		assertEquals(aa_child, new JTag("c", "", new JTag[] {aa}));
	}

	/*
	 - Raw
	 */

	@Test
	public void testGetRaw() {
		final JTag a = new JTag("a");
		final JTag ab = new JTag("a", "b");
		assertEquals("<a>", a.getRaw());
		assertEquals("<a:b>", ab.getRaw());
		final JTag child = new JTag("ab", "cd", new JTag[] {ab});
		assertEquals("<ab:cd<a:b>>", child.getRaw());
		assertEquals("<a:b>", child.getChildren()[0].getRaw());
		final String parse = "<a:b<c:d>e>";
		assertEquals(parse, parse(parse).getTag().getRaw());
		final String parseNest = "<a:a<bb:bb<ccc:ccc>bb>a>";
		assertEquals(parseNest, parse(parseNest).getTag().getRaw());
	}

	/*
	 - Util
	 */

	private void test(@NotNull JTagParseResult result, String skipped, JTag tag, String remaining) {
		assertEquals(skipped, result.getSkipped());
		assertEquals(tag, result.getTag());
		assertEquals(remaining, result.getRemaining());
	}
}
