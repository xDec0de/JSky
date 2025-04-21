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

	public static @NotNull JTag[] parse(@NotNull final String input) {
		final List<JTag> tags = new ArrayList<>();
		final int inputLen = input.length();
		int index = 0;
		while (index < inputLen) {
			final int openBracket = findOpenBracket(input, index);
			if (openBracket == -1)
				break;
			int closeBracket = findCloseBracket(input, openBracket);
			if (closeBracket == -1) {
				if (input.indexOf('>') == -1) // Parent tag not closed
					return new JTag[0];
				// Parent tag closed, but not with the right depth
				final JTag tag = parseTag(input.substring(openBracket + 1, inputLen - 1));
				return tag == null ? new JTag[0] : new JTag[] { tag };
			}
			final String tagContent = input.substring(openBracket + 1, closeBracket);
			final JTag tag = parseTag(tagContent);
			if (tag != null)
				tags.add(tag);
			index = closeBracket + 1;
		}
		return tags.toArray(new JTag[0]);
	}

	public static @Nullable JTag parseOne(@NotNull final String input, int fromIndex) {
		final int openBracket = findOpenBracket(input, fromIndex);
		final int closeBracket = findCloseBracket(input, openBracket);
		if (openBracket == -1 || closeBracket == -1)
			return null;
		return parseTag(input.substring(openBracket + 1, closeBracket));
	}

	public static @Nullable JTag parseOne(@NotNull final String input) {
		return parseOne(input, 0);
	}

	@Nullable
	private static JTag parseTag(@NotNull final String tagContent) {
		final int colonPos = tagContent.indexOf(':');
		if (colonPos == -1)
			return null;
		final String name = tagContent.substring(0, colonPos).trim();
		final String remaining = tagContent.substring(colonPos + 1);
		if (name.isBlank() || remaining.isBlank())
			return null;
		final List<JTag> children = new ArrayList<>();
		final String extracted = extractContent(remaining, children);
		return new JTag(name, extracted, children.toArray(new JTag[0]));
	}

	@NotNull
	private static String extractContent(@NotNull final String content, @NotNull final List<JTag> children) {
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
			final int colonPos = childContent.indexOf(':');
			if (colonPos == -1) {
				result.append(content, openBracket, closeBracket + 1);
				i = closeBracket + 1;
				continue;
			}
			children.add(parseTag(childContent));
			i = closeBracket + 1;
		}
		return result.toString().replace("\\<", "<").replace("\\>", ">");
	}

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
}
