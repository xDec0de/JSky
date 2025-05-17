package net.codersky.jsky.strings.tag;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class used to {@link #parse(String) parse} input
 * with JSky's nested tags system.
 *
 * @author xDec0de_
 *
 * @since JSky 1.0.0
 */
public final class JTagParser {

	public static final JTag[] EMPTY_TAG_ARRAY = new JTag[0];

	/*
	 - Parse one
	 */

	public static @NotNull JTagParseResult parse(@NotNull final String input) {
		return parse(input, 0);
	}

	public static @NotNull JTagParseResult parse(@NotNull final String input, final int fromIndex) {
		return parse(input, fromIndex, Integer.MAX_VALUE);
	}

	public static @NotNull JTagParseResult parse(@NotNull final String input, final int fromIndex, final int maxDepth) {
		final int openBracket = findOpenBracket(input, fromIndex);
		if (openBracket == -1)
			return new JTagParseResult(fromIndex < input.length() ? unescape(input.substring(fromIndex)) : "");
		final int closeBracket = findCloseBracket(input, openBracket);
		if (closeBracket == -1)
			return new JTagParseResult(unescape(input));
		final String remaining = closeBracket < input.length() ? unescape(input.substring(closeBracket)) : "";
		final JTag tag = parseTag(input.substring(openBracket + 1, closeBracket - 1), maxDepth);
		if (tag == null)
			return new JTagParseResult(input, null, "");
		final String skipped = fromIndex < openBracket ? unescape(input.substring(fromIndex, openBracket)) : null;
		return new JTagParseResult(skipped, tag, remaining);
	}

	/*
	 - Parse all
	 */

	public static @NotNull JTagParseAllResult parseAll(@NotNull final String input) {
		return parseAll(input, 0);
	}

	public static @NotNull JTagParseAllResult parseAll(@NotNull final String input, final int fromIndex) {
		return parseAll(input, fromIndex, Integer.MAX_VALUE);
	}

	public static @NotNull JTagParseAllResult parseAll(@NotNull final String input, final int fromIndex, final int maxDepth) {
		final JTagParseAllResult all = new JTagParseAllResult();
		JTagParseResult one = parse(input, fromIndex, maxDepth);
		while (true) {
			if (!one.getSkipped().isEmpty())
				all.add(one.getSkipped());
			if (one.getTag() != null)
				all.add(one.getTag());
			if (one.getRemaining().isBlank())
				break;
			one = parse(one.getRemaining(), 0, maxDepth);
		}
		return all;
	}

	/*
	 - Internal tag parsing
	 */

	private static @Nullable JTag parseTag(@NotNull final String tagContent, final int maxDepth) {
		if (!tagContent.contains(":"))
			return tagContent.isBlank() ? null : new JTag(unescape(tagContent), "", EMPTY_TAG_ARRAY);
		final int colonPos = tagContent.indexOf(':');
		final String name = unescape(tagContent.substring(0, colonPos).trim());
		final String content = unescape(tagContent.substring(colonPos + 1));
		if (name.isBlank() || content.isBlank())
			return null;
		if (maxDepth <= 0)
			return new JTag(name, content, EMPTY_TAG_ARRAY);
		final List<JTag> children = new ArrayList<>();
		final String extracted = extractContent(content, children, maxDepth - 1);
		return new JTag(name, extracted, children.toArray(EMPTY_TAG_ARRAY));
	}

	private static @NotNull String extractContent(@NotNull final String content, @NotNull final List<JTag> children, final int remainingDepth) {
		final StringBuilder result = new StringBuilder();
		int i = 0;
		while (i < content.length()) {
			final int openBracket = findOpenBracket(content, i);
			if (openBracket == -1) {
				result.append(content.substring(i));
				break;
			}
			result.append(content, i, openBracket);
			final int closeBracket = findCloseBracket(content, openBracket);
			if (closeBracket == -1) {
				result.append(content.substring(openBracket));
				break;
			}
			final String childContent = content.substring(openBracket + 1, closeBracket - 1);
			final JTag childTag = parseTag(childContent, remainingDepth);
			if (childTag != null)
				children.add(childTag);
			else
				result.append(content, openBracket, closeBracket);
			i = closeBracket + 1;
		}
		return unescape(result.toString());
	}

	/*
	 - Bracket utility
	 */

	private static int findOpenBracket(@NotNull final String str, final int fromIndex) {
		int i = fromIndex;
		while ((i = str.indexOf('<', i)) != -1) {
			int escapeChars = 0;
			int pos = i - 1;
			while (pos >= 0 && str.charAt(pos) == '\\') {
				escapeChars++;
				pos--;
			}
			if (escapeChars % 2 == 0)
				return i;
			i++;
		}
		return -1;
	}

	private static int findCloseBracket(@NotNull final String input, final int openPos) {
		int depth = 1;
		int pos = openPos + 1;
		while (pos < input.length() && depth > 0) {
			final char c = input.charAt(pos);
			if (c == '\\') {
				pos += 2;
				continue;
			}
			if (c == '<')
				depth++;
			else if (c == '>')
				depth--;
			pos++;
		}
		return depth == 0 ? pos : -1;
	}

	private static String unescape(@NotNull final String str) {
		return str.replace("\\<", "<")
				.replace("\\>", ">")
				.replace("\\\\", "\\");
	}
}
