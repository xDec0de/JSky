package net.codersky.jsky.test.strings.tag;

import net.codersky.jsky.strings.tag.JTag;
import net.codersky.jsky.strings.tag.JTagParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class JTagParserTest {

	@Test
	void testUnrelatedInput() {
		assertEquals(0, JTagParser.parse("").length);
		assertEquals(0, JTagParser.parse("Random string").length);
	}

	@Test
	void testSimpleValidTag() {
		final JTag[] tags = JTagParser.parse("<test:content>");
		assertEquals(1, tags.length);
		assertEquals("test", tags[0].getName());
		assertEquals("content", tags[0].getContent());
		assertEquals(0, tags[0].getChildren().length);
	}

	@Test
	void testMultipleParents() {
		final JTag[] tags = JTagParser.parse("<one:one><two:two><three:three>");
		assertEquals(3, tags.length);
		assertEquals("one", tags[0].getName());
		assertEquals("one", tags[0].getContent());
		assertEquals("two", tags[1].getContent());
		assertEquals("two", tags[1].getContent());
		assertEquals("three", tags[2].getContent());
		assertEquals("three", tags[2].getContent());
	}

	@Test
	void testParseOne() {
		final String input = "a<first:first><second:second>";
		final JTag first = JTagParser.parseOne(input);
		assertNotNull(first);
		assertEquals("first", first.getName());
		assertEquals("first", first.getContent());
		final JTag second = JTagParser.parseOne(input, 14);
		assertNotNull(second);
		assertEquals("second", second.getName());
		assertEquals("second", second.getContent());
		assertNull(JTagParser.parseOne("invalid"));
		assertNull(JTagParser.parseOne("<invalid>"));
		assertNull(JTagParser.parseOne("<invalid"));
		assertNull(JTagParser.parseOne("invalid>"));
		assertNull(JTagParser.parseOne(">invalid:brackets<"));
		assertNull(JTagParser.parseOne("<blank_content: >"));
		assertNull(JTagParser.parseOne("< :blank_name>"));
	}

	@Test
	void testNestedValidTags() {
		final JTag[] tags = JTagParser.parse("<parent:content<child:sub>>");
		assertEquals(1, tags.length);
		assertEquals("parent", tags[0].getName());
		assertEquals("content", tags[0].getContent());
		assertEquals(1, tags[0].getChildren().length);
		assertEquals("child", tags[0].getChildren()[0].getName());
		assertEquals("sub", tags[0].getChildren()[0].getContent());
	}

	@Test
	void testInvalidTags() {
		assertEquals(0, JTagParser.parse("<invalid>").length);
		assertEquals(0, JTagParser.parse("<unclosed:content").length);
	}

	@Test
	void testEscapedBracketsInContent() {
		final JTag[] tags = JTagParser.parse("<tag:escaped\\<brackets\\>>");
		assertEquals(1, tags.length);
		assertEquals("tag", tags[0].getName());
		assertEquals("escaped<brackets>", tags[0].getContent());
	}

	@Test
	void testDeeplyNestedTags() {
		final JTag[] tags = JTagParser.parse("<a:<b:<c:d>>>");
		assertEquals(1, tags.length);
		assertEquals("a", tags[0].getName());
		assertEquals(1, tags[0].getChildren().length);
		assertEquals("b", tags[0].getChildren()[0].getName());
		assertEquals(1, tags[0].getChildren()[0].getChildren().length);
	}

	@Test
	void testMixedValidAndInvalid() {
		final JTag[] tags = JTagParser.parse("<outer:<valid:ok><invalid><another:tag>>");
		assertEquals(1, tags.length);
		assertEquals("<invalid>", tags[0].getContent());
		assertEquals(2, tags[0].getChildren().length);
	}

	@Test
	void testMultipleInvalidTags() {
		final JTag[] tags = JTagParser.parse("<parent:<invalid1><valid:ok><invalid2>>");
		assertEquals(1, tags.length);
		assertEquals("<invalid1><invalid2>", tags[0].getContent());
		assertEquals(1, tags[0].getChildren().length);
	}

	@Test
	void testChildNotClosed() {
		final JTag[] colon = JTagParser.parse("<parent:<not_closed>");
		assertEquals(1, colon.length);
		assertEquals("parent", colon[0].getName());
		assertEquals("<not_closed", colon[0].getContent());
		final JTag[] no_colon = JTagParser.parse("<parent<not_closed>");
		assertEquals(0, no_colon.length);
	}

	@Test
	void testMultipleEscapedBrackets() {
		final JTag[] tags = JTagParser.parse("<test:\\\\<escaped\\\\>>");
		assertEquals(1, tags.length);
		assertEquals("\\<escaped\\>", tags[0].getContent());
	}

	@Test
	void testDeeplyNestedWithEscapes() {
		final JTag[] tags = JTagParser.parse("<a:<b:\\<c:\\\\<d\\\\>\\>>>");
		assertEquals(1, tags.length);
		assertEquals("<c:\\<d\\>>", tags[0].getChildren()[0].getContent());
	}
}
