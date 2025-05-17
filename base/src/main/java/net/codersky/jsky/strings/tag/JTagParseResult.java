package net.codersky.jsky.strings.tag;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JTagParseResult {

	private final String skipped;
	private final JTag tag;
	private final String remaining;

	JTagParseResult(@Nullable final String skipped, @Nullable final JTag tag, @Nullable final String remaining) {
		this.skipped = skipped == null ? "" : skipped;
		this.tag = tag;
		this.remaining = remaining == null ? "" : remaining;
	}

	JTagParseResult(@NotNull String skipped) {
		this(skipped, null, null);
	}

	@NotNull
	public String getSkipped() {
		return skipped;
	}

	@Nullable
	public JTag getTag() {
		return tag;
	}

	@NotNull
	public String getRemaining() {
		return remaining;
	}
}
