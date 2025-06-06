package net.codersky.jsky.strings;

import net.codersky.jsky.collections.JCollections;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Class used to replace parts of a {@link String} with other objects.
 * This class is designed to make text replacement easy, providing
 * the ability to apply the same replacements on multiple Strings.
 * <p>
 * The most useful feature about the Replacer {@code class} is
 * that it preserves the instance of objects used as replacements,
 * meaning that if the state of a replacement object changes, the
 * change will be shown the next time the Replacer is applied.
 * <p>
 * Also, check the {@link Replacement} {@code interface}.
 * {@link Replacement#asReplacement()} is used instead of
 * {@link Object#toString()} for replacements that implement it.
 *
 * @since JSky 1.0.0
 * 
 * @see #Replacer(Object...)
 *
 * @author xDec0de_
 */
public class Replacer implements Cloneable {

	final HashMap<String, Object> replacementsMap = new HashMap<>();

	/**
	 * Creates a new {@link Replacer} with the specified {@code replacements}.
	 * Keep in mind that the size of the {@code replacements} <b>must be even</b>.
	 *
	 * @param replacements The replacements to use. This parameter will be translated
	 * to a {@link HashMap map}, so the format goes as: [key1, value1, key2, value2...].
	 * The keys are the text to replace, while the values are the replacements. So for
	 * example ["%n", 42], would replace "%n" with "42". Keep in mind that as
	 * a {@link HashMap map} is used internally, new replacements for the same key will
	 * overwrite its value. {@code null} values will be added as the string literal <i>"null"</i>.
	 *
	 * @throws IllegalArgumentException If the amount of {@code replacements} is not even,
	 * more technically, if {@code replacements} size % 2 is not equal to 0.
	 * @throws NullPointerException If {@code replacements} itself is {@code null}.
	 * 
	 * @since JSky 1.0.0
	 * 
	 * @see #add(Replacer...)
	 * @see #add(Object...)
	 * @see #replaceAt(String)
	 */
	public Replacer(@Nullable Object... replacements) {
		add(replacements);
	}

	/**
	 * Adds new {@code replacements} to an existing {@link Replacer}.
	 * The amount of replacements must also be even.
	 *
	 * @param replacements The replacements to use. This parameter will be translated
	 * to a {@link HashMap map}, so the format goes as: [key1, value1, key2, value2...].
	 * The keys are the text to replace, while the values are the replacements. So for
	 * example ["%n", 42], would replace "%n" with "42". Keep in mind that as
	 * a {@link HashMap map} is used internally, new replacements for the same key will
	 * overwrite its value. {@code null} values will be added as the string literal <i>"null"</i>.
	 * 
	 * @return This {@link Replacer} with the new {@code replacements} added to it.
	 *
	 * @throws IllegalArgumentException If the amount of {@code replacements} is not even,
	 * more technically, if {@code replacements} size % 2 is not equal to 0.
	 * @throws NullPointerException If {@code replacements} itself is {@code null}.
	 *
	 * @since JSky 1.0.0
	 * 
	 * @see #add(Replacer...)
	 * @see #replaceAt(String)
	 */
	@NotNull
	public Replacer add(@Nullable Object... replacements) {
		Objects.requireNonNull(replacements, "Replacements cannot be null");
		if (replacements.length % 2 != 0)
			throw new IllegalArgumentException("Invalid Replacer size: " + replacements.length);
		for (int i = 0; i <= replacements.length - 1; i += 2) {
			final Object key = replacements[i];
			final Object value = replacements[i + 1];
			replacementsMap.put(key == null ? "null" : key.toString(), value == null ? "null" : value);
		}
		return this;
	}

	/**
	 * Adds the replacements of the specified {@code replacers} to this {@link Replacer}, joining them.
	 *
	 * @param replacers The {@link Replacer Replacers} to join to the existing {@link Replacer}.
	 * 
	 * @return The old {@link Replacer} with the replacements of {@code replacer} added to it.
	 * 
	 * @since JSky 1.0.0
	 *
	 * @see #add(Object...) 
	 * @see #replaceAt(String)
	 */
	@NotNull
	public Replacer add(@NotNull Replacer... replacers) {
		for (final Replacer replacer : replacers)
			this.replacementsMap.putAll(replacer.replacementsMap);
		return this;
	}

	/**
	 * Clones this {@link Replacer} creating a new one with the same replacements.
	 *
	 * @return A copy of this {@link Replacer}.
	 *
	 * @since JSky 1.0.0
	 *
	 * @see #Replacer(Object...)
	 * @see #add(Replacer...)
	 */
	@NotNull
	@Override
	public Replacer clone() {
		return new Replacer().add(this);
	}

	/*
	 - String replacements
	 */

	/**
	 * Applies this {@link Replacer} to the specified {@link String}.
	 * 
	 * @param str The {@link String} to apply the replacements to.
	 * 
	 * @return A new {@link String} with all {@link #getReplacementMap() replacements} applied to it.
	 *
	 * @throws NullPointerException if {@code str} is {@code null}.
	 *
	 * @since JSky 1.0.0
	 * 
	 * @see #replaceAt(String...)
	 * @see #replaceAtStrings(List)
	 */
	@NotNull
	public String replaceAt(@NotNull String str) {
		final int mapSize = replacementsMap.size();
		if (mapSize == 0 || str.isEmpty())
			return str;
		final StringBuilder res = new StringBuilder(str);
		for (Map.Entry<String, Object> entry : replacementsMap.entrySet()) {
			final String toSearch = entry.getKey();
			final String replacement = getStringValue(entry.getValue());
			final int replacementLen = replacement.length();
			final int searchLen = toSearch.length();
			int index = res.indexOf(toSearch);
			while (index != -1) {
				res.replace(index, index + searchLen, replacement);
				index = res.indexOf(toSearch, index + replacementLen);
			}
		}
		return res.toString();
	}

	private @NotNull String getStringValue(final Object replacement) {
		if (replacement instanceof final Replacement rep)
			return rep.asReplacement();
		return replacement.toString();
	}

	/*
	 - List replacements
	 */

	/**
	 * Applies this {@link Replacer} to the specified {@link List} of {@link String strings}.
	 * 
	 * @param list The {@link String} {@link List} to apply the replacements to.
	 * 
	 * @return A new <b>modifiable</b> {@link String} {@link List} with the replacements applied to it.
	 *
	 * @throws NullPointerException if {@code list} or any element of it is {@code null}.
	 * 
	 * @since JSky 1.0.0
	 *
	 * @see #replaceAt(String)
	 * @see #replaceAt(String...)
	 */
	@NotNull
	public List<String> replaceAtStrings(@NotNull List<String> list) {
		return JCollections.map(list, this::replaceAt);
	}

	/**
	 * Applies this {@link Replacer} to the specified {@link String strings}.
	 *
	 * @param strings The {@link String strings} to apply the replacements to.
	 *
	 * @return A new <b>modifiable</b> {@link String} {@link List} with the replacements applied to it.
	 *
	 *
	 * @throws NullPointerException if {@code strings} or any {@link String} is {@code null}.
	 *
	 * @since JSky 1.0.0
	 *
	 * @see #replaceAt(String)
	 * @see #replaceAtStrings(List)
	 */
	@NotNull
	public List<String> replaceAt(@NotNull String... strings) {
		return replaceAtStrings(List.of(strings));
	}

	/**
	 * Gets the replacements being used by this {@link Replacer}.
	 * Modifying this list will have no effect, it can be used
	 * for debugging or to create your own {@link Replacer} type
	 * while being able to {@link #clone()} it. This can be done
	 * by calling the {@link #Replacer(Object...)} constructor.
	 * 
	 * @return The replacements being used by this {@link Replacer}.
	 * 
	 * @since JSky 1.0.0
	 */
	@NotNull
	public Map<String, Object> getReplacementMap() {
		return new HashMap<>(replacementsMap);
	}

	/*
	 - Object override
	 */

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof final Replacer other)
			return this == other || this.replacementsMap.equals(other.replacementsMap);
		return false;
	}

	@NotNull
	@Override
	public String toString() {
		return "Replacer" + replacementsMap;
	}
}
