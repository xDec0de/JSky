package net.codersky.jsky.strings.tag;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class JTag {

	private final String name;
	private final String content;
	private final JTag[] children;

	public JTag(@NotNull String name, @NotNull String content, @NotNull JTag @NotNull [] children) {
		this.name = Objects.requireNonNull(name);
		this.content = Objects.requireNonNull(content);
		this.children = Objects.requireNonNull(children);
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
}
