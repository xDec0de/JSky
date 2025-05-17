package net.codersky.jsky.strings.tag;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JTagParseAllResult implements Iterable<Object> {

	private final ArrayList<Object> info = new ArrayList<>();

	JTagParseAllResult() {}

	void add(@NotNull Object obj) {
		if (obj instanceof JTag || obj instanceof String)
			info.add(obj);
	}

	/*
	 - Info
	 */

	public int size() {
		return info.size();
	}

	/*
	 - Individual index getters
	 */

	@Nullable
	public Object get(final int index) {
		return this.info.get(index);
	}

	@Nullable
	public JTag getTag(final int index) {
		final Object obj = get(index);
		return obj instanceof final JTag tag ? tag : null;
	}

	@Nullable
	public String getString(final int index) {
		final Object obj = get(index);
		return obj instanceof final String str ? str : null;
	}

	/*
	 - Full getters
	 */

	public List<JTag> getTags() {
		final List<JTag> tags = new ArrayList<>(this.info.size());
		for (Object obj : this.info)
			if (obj instanceof final JTag tag)
				tags.add(tag);
		return tags;
	}

	public List<String> getStrings() {
		final List<String> strings = new ArrayList<>(this.info.size());
		for (Object obj : this.info)
			if (obj instanceof final String str)
				strings.add(str);
		return strings;
	}

	/*
	 - Extra getters
	 */

	@NotNull
	public String getExcess() {
		final StringBuilder excess = new StringBuilder();
		for (Object obj : this.info)
			if (obj instanceof final String str)
				excess.append(str);
		return excess.toString();
	}

	/*
	 - Iterable
	 */

	@NotNull
	@Override
	public Iterator<Object> iterator() {
		return info.iterator();
	}
}
