package net.codersky.jsky.strings.tag;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

public class JTag {

	private final String raw;
	private final String name;
	private final String content;
	private final JTag[] children;

	JTag(@NotNull String raw, @NotNull String name, @NotNull String content, @NotNull JTag @NotNull [] children) {
		this.raw = "<" + Objects.requireNonNull(raw) + ">";
		this.name = Objects.requireNonNull(name);
		this.content = Objects.requireNonNull(content);
		this.children = Objects.requireNonNull(children);
	}

	public JTag(@NotNull String name, @NotNull String content, @NotNull JTag @NotNull [] children) {
		this(rawWithChildren(name, content, children), name, content, children);
	}

	private static String rawWithChildren(String name, String content, JTag[] children) {
		final StringBuilder result = new StringBuilder(name).append(":").append(content);
		for (JTag child : children)
			result.append(child.getRaw());
		return result.toString();
	}

	public JTag(@NotNull String name, @NotNull String content) {
		this(name + ":" + content,  name, content, JTagParser.EMPTY_TAG_ARRAY);
	}

	public JTag(@NotNull String name) {
		this(name, name, "", JTagParser.EMPTY_TAG_ARRAY);
	}

	@NotNull
	public String getName() {
		return name;
	}

	@NotNull
	public String getContent() {
		return content;
	}

	@NotNull
	public JTag @NotNull [] getChildren() {
		return children;
	}

	/*
	 - Raw
	 */

	@NotNull
	public String getRaw() {
		return this.raw;
	}

	/*
	 - Equals
	 */

	@Override
	public boolean equals(@Nullable Object obj) {
		if (obj == this)
			return true;
		if (obj instanceof final JTag other) {
			if (!Objects.equals(getName(), other.getName()))
				return false;
			if (!Objects.equals(getContent(), other.getContent()))
				return false;
			return Arrays.equals(getChildren(), other.getChildren());
		}
		return false;
	}

	/*
	 - toString
	 */

	@Override
	public String toString() {
		return String.format("JTag{name=\"%s\", content=\"%s\", children=[%s]}",
				getName(), getContent(), childrenToStr());
	}

	private String childrenToStr() {
		if (getChildren().length == 0)
			return "";
		final StringBuilder children = new StringBuilder();
		for (final JTag child : getChildren())
			(children.isEmpty() ? children : children.append(", ")).append(child);
		return children.toString();
	}
}
