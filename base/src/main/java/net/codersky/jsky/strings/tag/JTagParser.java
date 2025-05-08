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

	private static final JTag[] EMPTY_TAG_ARRAY = new JTag[0];

	/*
	 - Parse array
	 */

	public static @NotNull JTag[] parse(@NotNull final String input) {
		return parse(input, 0, Integer.MAX_VALUE, null);
	}

	public static @NotNull JTag[] parse(@NotNull final String input, final int fromIndex) {
		return parse(input, fromIndex, Integer.MAX_VALUE, null);
	}

	public static @NotNull JTag[] parse(@NotNull final String input, final int fromIndex, final int maxDepth) {
		return parse(input, fromIndex, maxDepth, null);
	}

	public static @NotNull JTag[] parse(@NotNull final String input, @Nullable final StringBuilder excess) {
		return parse(input, 0, Integer.MAX_VALUE, excess);
	}

	public static @NotNull JTag[] parse(@NotNull final String input, final int fromIndex, @Nullable final StringBuilder excess) {
		return parse(input, fromIndex, Integer.MAX_VALUE, excess);
	}

	public static @NotNull JTag[] parse(@NotNull final String input, final int fromIndex, final int maxDepth, @Nullable final StringBuilder excess) {
		final List<JTag> tags = new ArrayList<>();
		final int inputLen = input.length();
		int index = fromIndex;
		while (index < inputLen) {
			final int openBracket = findOpenBracket(input, index);
			if (openBracket == -1) {
				if (excess != null && index < inputLen)
					excess.append(input.substring(index));
				break;
			}
			if (excess != null && index < openBracket)
				excess.append(input.substring(index, openBracket));
			final int closeBracket = findCloseBracket(input, openBracket);
			if (closeBracket == -1) {
				index = openBracket + 1;
				continue;
			}
			final String tagContent = input.substring(openBracket + 1, closeBracket);
			final JTag tag = parseTag(tagContent, maxDepth);
			if (tag != null)
				tags.add(tag);
			index = closeBracket + 1;
		}
		return tags.toArray(EMPTY_TAG_ARRAY);
	}

	/*
	 - Parse one
	 */

	public static @Nullable JTag parseOne(@NotNull final String input) {
		return parseOne(input, 0, Integer.MAX_VALUE, null);
	}

	public static @Nullable JTag parseOne(@NotNull final String input, final int fromIndex) {
		return parseOne(input, fromIndex, Integer.MAX_VALUE, null);
	}

	public static @Nullable JTag parseOne(@NotNull final String input, final int fromIndex, final int maxDepth) {
		return parseOne(input, fromIndex, maxDepth, null);
	}

	public static @Nullable JTag parseOne(@NotNull final String input, @Nullable final StringBuilder excess) {
		return parseOne(input, 0, Integer.MAX_VALUE, excess);
	}

	public static @Nullable JTag parseOne(@NotNull final String input, final int fromIndex, @Nullable final StringBuilder excess) {
		return parseOne(input, fromIndex, Integer.MAX_VALUE, excess);
	}

	public static @Nullable JTag parseOne(@NotNull final String input, final int fromIndex, final int maxDepth, @Nullable final StringBuilder excess) {
		final int openBracket = findOpenBracket(input, fromIndex);
		if (openBracket == -1) {
			if (excess != null && fromIndex < input.length())
				excess.append(input.substring(fromIndex));
			return null;
		}
		if (excess != null && fromIndex < openBracket)
			excess.append(input, fromIndex, openBracket);
		final int closeBracket = findCloseBracket(input, openBracket);
		if (closeBracket == -1)
			return null;
		if (excess != null && closeBracket + 1 < input.length())
			excess.append(input.substring(closeBracket + 1));
		return parseTag(input.substring(openBracket + 1, closeBracket), maxDepth);
	}

	/*
	 - Tag parsing
	 */

	private static @Nullable JTag parseTag(@NotNull final String tagContent, final int maxDepth) {
		if (!tagContent.contains(":"))
			return tagContent.isBlank() ? null : new JTag(tagContent, "", EMPTY_TAG_ARRAY);
		final int colonPos = tagContent.indexOf(':');
		final String name = tagContent.substring(0, colonPos).trim();
		final String content = tagContent.substring(colonPos + 1);
		if (name.isBlank() || content.isBlank())
			return null;
		if (maxDepth <= 0)
			return new JTag(name, unescapeBrackets(content), EMPTY_TAG_ARRAY);
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
			final String childContent = content.substring(openBracket + 1, closeBracket);
			final JTag childTag = parseTag(childContent, remainingDepth);
			if (childTag != null)
				children.add(childTag);
			else
				result.append(content, openBracket, closeBracket + 1);
			i = closeBracket + 1;
		}
		return unescapeBrackets(result.toString());
	}

	/*
	 - Bracket utility
	 */

	private static int findOpenBracket(@NotNull final String str, final int fromIndex) {
		int i = fromIndex;
		while ((i = str.indexOf('<', i)) != -1) {
			if (i == 0 || str.charAt(i - 1) != '\\')
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
		return depth == 0 ? pos - 1 : -1;
	}

	private static String unescapeBrackets(@NotNull final String str) {
		return str.replace("\\<", "<").replace("\\>", ">");
	}
}
