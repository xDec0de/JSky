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

	@Override
	public boolean equals(@Nullable Object obj) {
		if (obj == this)
			return true;
		if (obj instanceof final JTag other) {
			if (!Objects.equals(getName(), other.getName()))
				return false;
			if (!Objects.equals(getContent(), other.getContent()))
				return false;
			if (getChildren().length != other.getChildren().length)
				return false;
			for (int i = 0; i < getChildren().length; i++)
				if (!getChildren()[i].equals(other.getChildren()[i]))
					return false;
			return true;
		}
		return false;
	}
}
