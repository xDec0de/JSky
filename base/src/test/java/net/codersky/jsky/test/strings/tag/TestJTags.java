package net.codersky.jsky.test.strings.tag;

import net.codersky.jsky.strings.tag.JTag;
import net.codersky.jsky.strings.tag.JTagParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TestJTags {

	@Test
	void testUnrelatedInput() {
		assertEquals(0, JTagParser.parse("").length);
		assertEquals(0, JTagParser.parse("Random string").length);
	}

	@Test
	void testSimpleValidTag() {
		final JTag nameOnly = JTagParser.parseOne("<name>");
		assertNotNull(nameOnly);
		assertEquals("name", nameOnly.getName());
		assertEquals("", nameOnly.getContent());
		assertEquals(0, nameOnly.getChildren().length);

		final JTag[] full = JTagParser.parse("<test:content>");
		assertEquals(1, full.length);
		assertEquals("test", full[0].getName());
		assertEquals("content", full[0].getContent());
		assertEquals(0, full[0].getChildren().length);
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
		assertNull(JTagParser.parseOne("<>"));
		assertNull(JTagParser.parseOne("< >"));
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
	void testUnclosedTags() {
		assertEquals(0, JTagParser.parse("<unclosed:content").length);
		assertEquals(0, JTagParser.parse("<< >").length);
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
		final JTag[] tags = JTagParser.parse("<outer:<valid:ok>< ><another:tag>>");
		assertEquals(1, tags.length);
		assertEquals("< >", tags[0].getContent());
		assertEquals(2, tags[0].getChildren().length);
	}

	@Test
	void testMultipleInvalidTags() {
		final JTag[] tags = JTagParser.parse("<parent:< ><valid:ok><>>< >");
		assertEquals(1, tags.length);
		assertEquals("< ><>", tags[0].getContent());
		assertEquals(1, tags[0].getChildren().length);
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

	@Test
	void testInvalidChildContent() {
		final JTag noName = JTagParser.parseOne("<parent:content<:invalid>>");
		assertNotNull(noName);
		assertEquals("content<:invalid>", noName.getContent());
		assertEquals(0, noName.getChildren().length);

		final JTag blankName = JTagParser.parseOne("<parent:content< :value>>");
		assertNotNull(blankName);
		assertEquals("content< :value>", blankName.getContent());
		assertEquals(0, blankName.getChildren().length);

		final JTag blankContent = JTagParser.parseOne("<parent:content<child: >>");
		assertNotNull(blankContent);
		assertEquals("content<child: >", blankContent.getContent());
		assertEquals(0, blankContent.getChildren().length);
	}

	@Test
	void testDepthLimit() {
		final JTag depth0 = JTagParser.parseOne("<root:<child:value>>", 0, 0);
		assertNotNull(depth0);
		assertEquals("<child:value>", depth0.getContent());
		assertEquals(0, depth0.getChildren().length);

		final JTag depth1 = JTagParser.parseOne("<root:<child:value>>", 0, 1);
		assertNotNull(depth1);
		assertEquals("", depth1.getContent());
		assertEquals(1, depth1.getChildren().length);

		final JTag depth1Nested = JTagParser.parseOne("<root:<child:<grand:value>>>", 0, 1);
		assertNotNull(depth1Nested);
		assertEquals("<grand:value>", depth1Nested.getChildren()[0].getContent());
	}
}
