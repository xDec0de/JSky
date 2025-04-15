package net.codersky.jsky.strings;

import net.codersky.jsky.annotations.KeyPattern;
import net.codersky.jsky.predicate.CharPredicate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class JStrings {

	/*
	 - String validation
	 */

	/**
	 * Checks if a {@link CharSequence} has any content on it. This will
	 * return {@code false} if {@code seq} is {@code null}, empty
	 * or contains only {@link Character#isWhitespace(char) whitespace}
	 * characters, {@code true} otherwise.
	 *
	 * @param seq the {@link CharSequence} to check.
	 *
	 * @return {@code true} if {@code seq} has content, {@code false} otherwise.
	 *
	 * @since JSky 1.0.0
	 */
	public static boolean hasContent(@Nullable CharSequence seq) {
		if (seq == null)
			return false;
		final int len = seq.length();
		for (int i = 0; i < len; i++)
			if (!Character.isWhitespace(seq.charAt(i)))
				return true;
		return false;
	}

	/*
	 - String validation - CharPredicate test
	 */

	/**
	 * Tests the provided {@code condition} on every character of {@code seq},
	 * starting from the specified {@code beginIndex}. If <b>any</b> character
	 * doesn't satisfy the {@code condition}, then {@code false} is immediately
	 * returned and no further testing is done.
	 *
	 * @param condition The {@link CharPredicate} used to
	 * {@link CharPredicate#test(char) test} all characters.
	 * @param beginIndex The index to start testing from (inclusive).
	 * @param seq The {@link CharSequence} to test.
	 *
	 * @return {@code true} if every character of {@code seq} starting from
	 * {@code beginIndex} satisfies the provided {@code condition},
	 * {@code false} otherwise.
	 *
	 * @throws NullPointerException if either {@code condition} or {@code seq}
	 * are {@code null}
	 * @throws IndexOutOfBoundsException if {@code beginIndex} is negative or
	 * greater than {@code seq.length()}
	 *
	 * @see CharPredicate
	 * @see #testAllChars(CharPredicate, CharSequence)
	 * @see #testAnyChar(CharPredicate, CharSequence)
	 */
	public static boolean testAllChars(@NotNull CharPredicate condition, int beginIndex, @NotNull CharSequence seq) {
		final int len = seq.length();
		if (beginIndex > len)
			throw new IndexOutOfBoundsException(beginIndex);
		for (int i = beginIndex; i < len; i++)
			if (!condition.test(seq.charAt(i)))
				return false;
		return true;
	}

	/**
	 * Tests the provided {@code condition} on every character of {@code seq}.
	 * If <b>any</b> character doesn't satisfy the {@code condition}, then
	 * {@code false} is immediately returned and no further testing is done.
	 *
	 * @param condition The {@link CharPredicate} used to
	 * {@link CharPredicate#test(char) test} all characters.
	 * @param seq The {@link CharSequence} to test.
	 *
	 * @return {@code true} if every character of {@code seq}
	 * satisfies the provided {@code condition}, {@code false} otherwise.
	 *
	 * @throws NullPointerException if either {@code condition} or {@code sequences}
	 * are {@code null}, or if any element in {@code sequences} is {@code null}
	 *
	 * @see CharPredicate
	 * @see #testAllChars(CharPredicate, int, CharSequence)
	 * @see #testAnyChar(CharPredicate, CharSequence)
	 */
	public static boolean testAllChars(@NotNull CharPredicate condition, @NotNull CharSequence seq) {
		return testAllChars(condition, 0, seq);
	}

	/**
	 * Tests the provided {@code condition} on every character of {@code seq},
	 * starting from the specified {@code beginIndex}. If <b>any</b> character
	 * satisfies the {@code condition}, then {@code true} is immediately
	 * returned and no further testing is done.
	 *
	 * @param condition The {@link CharPredicate} used to
	 * {@link CharPredicate#test(char) test} all characters.
	 * @param beginIndex The index to start testing from (inclusive).
	 * @param seq The {@link CharSequence} to test.
	 *
	 * @return {@code true} if any character of {@code seq} starting from
	 * {@code beginIndex} satisfies the provided {@code condition},
	 * {@code false} otherwise.
	 *
	 * @throws NullPointerException if either {@code condition} or {@code seq}
	 * are {@code null}
	 * @throws IndexOutOfBoundsException if {@code beginIndex} is negative or
	 * greater than {@code seq.length()}
	 *
	 * @see CharPredicate
	 * @see #testAnyChar(CharPredicate, CharSequence)
	 * @see #testAllChars(CharPredicate, int, CharSequence)
	 */
	public static boolean testAnyChar(@NotNull CharPredicate condition, int beginIndex, @NotNull CharSequence seq) {
		final int len = seq.length();
		if (beginIndex > len)
			throw new IndexOutOfBoundsException(beginIndex);
		for (int i = beginIndex; i < len; i++)
			if (condition.test(seq.charAt(i)))
				return true;
		return false;
	}

	/**
	 * Tests the provided {@code condition} on every character of {@code seq}.
	 * If <b>any</b> character satisfies the {@code condition}, then
	 * {@code true} is immediately returned and no further testing is done.
	 *
	 * @param condition The {@link CharPredicate} used to
	 * {@link CharPredicate#test(char) test} all characters.
	 * @param seq The {@link CharSequence} to test.
	 *
	 * @return {@code true} if any character of {@code seq}
	 * satisfies the provided {@code condition}, {@code false} otherwise.
	 *
	 * @throws NullPointerException if either {@code condition} or {@code sequences}
	 * are {@code null}, or if any element in {@code sequences} is {@code null}
	 *
	 * @see CharPredicate
	 * @see #testAllChars(CharPredicate, int, CharSequence)
	 * @see #testAnyChar(CharPredicate, CharSequence)
	 */
	public static boolean testAnyChar(@NotNull CharPredicate condition, @NotNull CharSequence seq) {
		return testAnyChar(condition, 0, seq);
	}

	/*
	 - String validation - Annotations
	 */

	/**
	 * Checks if a {@link CharSequence} follows the pattern defined by
	 * {@link KeyPattern} (See details {@link KeyPattern here}):
	 *
	 * @param seq The {@link CharSequence} to check.
	 *
	 * @return {@code true} if the sequence matches the
	 * {@link KeyPattern}, {@code false} otherwise.
	 *
	 * @throws NullPointerException if {@code seq} is {@code null}
	 *
	 * @since JSky 1.0.0
	 */
	public static boolean hasKeyPattern(@NotNull CharSequence seq) {
		int chars = 0;
		boolean lastWasDash = false;
		final int len = seq.length();
		if (len == 0)
			return false;
		for (int i = 0; i < len; i++) {
			final char ch = seq.charAt(i);
			if (ch == '-') {
				if (i == 0 || i == len - 1 || lastWasDash)
					return false;
				lastWasDash = true;
			}
			else if (ch >= 'a' && ch <= 'z') {
				lastWasDash = false;
				chars++;
			} else
				return false;
		}
		return chars > 0;
	}

	/*
	 - To string conversion
	 */

	/**
	 * Gets a {@link CharSequence} {@link Iterable} as a {@link String} with all
	 * elements separated by the specified {@code separator}, Elements that
	 * don't match the specified {@code filter} will be ignored, meaning that they won't
	 * be included on the resulting {@code String}.
	 * <p>
	 * Example:
	 * <p>
	 * Iterable: [" ", "a", null, "b", "c"]<br>
	 * Separator: ", "<br>
	 * Filter: {@link #hasContent(CharSequence)}<br>
	 * Returns: " a, b, c"
	 *
	 * @param iterable the {@link Iterable} to use, for arrays, just use {@link List#of(Object[])}.
	 * @param separator the separator to use, if {@code null} is used then <i>null</i> will be
	 * 	 * used as a separator as specified by {@link StringBuilder#append(CharSequence)}.
	 * @param filter the {@link Predicate} to use in order to filter {@link CharSequence CharSequences}.
	 * using a {@code null} filter means that every {@link CharSequence} present on the {@code iterator}
	 * will be included. If the {@link Predicate} returns {@code true} for a {@link CharSequence},
	 * said {@link CharSequence} will be included, if {@code false} is returned, it will be ignored.
	 *
	 * @return A {@link CharSequence} {@link Iterator} as a {@link String} with all elements that match
	 * {@code filter} separated by {@code separator}.
	 *
	 * @throws NullPointerException if {@code iterable} is {@code null}.
	 *
	 * @since JSky 1.0.0
	 *
	 * @see #asString(Iterable, CharSequence)
	 */
	@NotNull
	public static String asString(@NotNull Iterable<? extends CharSequence> iterable, @Nullable CharSequence separator, @Nullable Predicate<CharSequence> filter) {
		final StringBuilder builder = new StringBuilder();
		for (CharSequence seq : iterable)
			if (filter == null || filter.test(seq))
				(builder.isEmpty() ? builder : builder.append(separator)).append(seq);
		return builder.toString();
	}

	/**
	 * Gets a {@link CharSequence} {@link Iterable} as a {@link String} with all
	 * elements separated by the specified {@code separator}, Elements that
	 * don't have content (See {@link #hasContent(CharSequence)}) will be ignored,
	 * if you want to specify your own filter, use {@link #asString(Iterable, CharSequence, Predicate)}.
	 * <p>
	 * Example:
	 * <p>
	 * Iterable: [" ", "a", null, "b", "c"]<br>
	 * Separator: ", "<br>
	 * Returns: " a, b, c"
	 *
	 * @param iterable the {@link Iterable} to use, for arrays, just use {@link List#of(Object[])}.
	 * @param separator the separator to use, if {@code null} is used then <i>null</i> will be
	 * used as a separator as specified by {@link StringBuilder#append(CharSequence)}.
	 *
	 * @return A {@link CharSequence} {@link Iterator} as a {@link String} with all elements that
	 * have content (See {@link #hasContent(CharSequence)}) separated by {@code separator}.
	 *
	 * @throws NullPointerException if {@code iterable} is {@code null}.
	 *
	 * @since JSky 1.0.0
	 *
	 * @see #asString(Iterable, CharSequence)
	 */
	@NotNull
	public static String asString(@NotNull Iterable<? extends CharSequence> iterable, @Nullable CharSequence separator) {
		return asString(iterable, separator, JStrings::hasContent);
	}

	/*
	 - CharSequence utility
	 */

	/**
	 * Gets the index of {@code toFind} on {@code seq}, starting to search at {@code beginIndex}.
	 * In other words, this method will search for the first occurrence of {@code toFind} inside of
	 * {@code seq} starting at {@code beginIndex}, then return the index at which {@code toFind} <b>starts</b>.
	 *
	 * @param seq the {@link CharSequence} to search on.
	 * @param toFind the {@link CharSequence} to find inside {@code seq}.
	 * @param beginIndex the index of {@code seq} to start searching from, negative values
	 * will make this method <b>always</b> return {@code -1}.
	 *
	 * @return the index of {@code seq} at which {@code toFind} is located, if found. {@code -1} if
	 * {@code toFind} could not be found inside {@code seq}. Another instance where this method returns
	 * {@code -1} is when {@code beginIndex} is negative or higher than the {@link CharSequence#length()
	 * length} of {@code seq}. If {@code toFind}'s {@link CharSequence#length() length} is {@code 0},
	 * then {@code 0} will be returned as an empty string is considered to always match.
	 *
	 * @throws NullPointerException if {@code seq} or {@code toFind} are {@code null}.
	 *
	 * @since JSky 1.0.0
	 */
	public static int indexOf(@NotNull CharSequence seq, @NotNull CharSequence toFind, int beginIndex) {
		final int seqLen = seq.length();
		final int toFindLen = toFind.length();
		if (beginIndex < 0)
			return -1;
		if (toFindLen == 0)
			return 0;
		for (int i = beginIndex; i < seqLen; i++) {
			for (int j = 0; j < toFindLen && i + j < seqLen; j++) {
				if (seq.charAt(i + j) != toFind.charAt(j))
					break;
				if (toFindLen == j + 1)
					return i;
			}
		}
		return -1;
	}

	public static int indexOf(@NotNull CharSequence seq, @NotNull CharSequence toFind) {
		return indexOf(seq, toFind, 0);
	}

	/*
	 - Substrings
	 */

	/**
	 * Gets a substring of {@code src}, starting at {@code from} and ending at {@code to}.
	 *
	 * @param src the source {@link String} to cut.
	 * @param from the {@link String} to match at the beginning of the new substring.
	 * @param to the {@link String} to match at the end of the new substring.
	 * @param inclusive if {@code true}, {@code from} and {@code to} will be included in the
	 * resulting substring, if {@code false}, they won't.
	 *
	 * @return A substring of {@code src} starting at {@code from} and ending at {@code to},
	 * {@code null} if no match is found. Keep in mind that both {@code from} and {@code to}
	 * need to be found in order to return a substring.
	 *
	 * @since JSky 1.0.0
	 *
	 * @see #substring(String, CharSequence, CharSequence)
	 */
	public static String substring(@NotNull String src, @NotNull CharSequence from, @NotNull CharSequence to, boolean inclusive) {
		final int start = indexOf(src, from, 0);
		final int end = indexOf(src, to, start + 1);
		if (start == -1 || end == -1)
			return null;
		return inclusive ? src.substring(start, end + to.length()) : src.substring(start + from.length(), end);
	}

	/**
	 * Gets a substring of {@code src}, starting at {@code from} and ending at {@code to}.
	 * This is equivalent to calling {@link #substring(String, CharSequence, CharSequence, boolean)}
	 * with {@code inclusive} set to {@code true}, meaning that both {@code from} and {@code to} will
	 * be included in the resulting substring.
	 *
	 * @param src the source {@link String} to cut.
	 * @param from the {@link String} to match at the beginning of the new substring.
	 * @param to the {@link String} to match at the end of the new substring.
	 *
	 * @return A substring of {@code src} starting at {@code from} and ending at {@code to},
	 * {@code null} if no match is found. Keep in mind that both {@code from} and {@code to}
	 * need to be found in order to return a substring.
	 *
	 * @since JSky 1.0.0
	 *
	 * @see #substring(String, CharSequence, CharSequence, boolean)
	 */
	public static String substring(@NotNull String src, @NotNull CharSequence from, @NotNull CharSequence to) {
		return substring(src, from, to, true);
	}

	/*
	 - Match
	 */

	/**
	 * This method will attempt to get any number of substrings of {@code src} between {@code from}
	 * and {@code to}. If a substring is found, {@code function} will be {@link Function#apply(Object) applied}
	 * on it, replacing the matched substring with the return value of {@code function}.
	 * Let's see an example:
	 * <p>
	 * <code>match("Match (this)", "(", ")", match -> "done";</code>
	 * <p>
	 * The returning String of this will be "Match done". {@code from}
	 * and {@code to} won't be included on {@code match}, as only the content
	 * between {@code from} and {@code to} is considered to be relevant.
	 *
	 * @param src the source {@link String} to use.
	 * @param from the {@link CharSequence} to match at the beginning of the pattern.
	 * @param to the {@link CharSequence} to match at the end of the pattern.
	 * @param function a {@link Function} that may accept any matching
	 * substrings of {@code src} between {@code from} and {@code to}, returning
	 * the {@link String} that will be used to replace the matching substring.
	 *
	 * @return A clone of {@code src} with any match from the specified pattern replaced with
	 * the return value of the provided {@code function} only if the pattern is found.
	 * If not, {@code src} will be returned as is without creating a new {@link String}.
	 *
	 * @throws NullPointerException if any parameter is {@code null}.
	 *
	 * @since JSky 1.0.0
	 *
	 * @see #match(String, CharSequence, CharSequence, Consumer)
	 * @see #match(String, CharSequence, CharSequence, Consumer, boolean)
	 */
	@NotNull
	public static String match(@NotNull String src, @NotNull CharSequence from, @NotNull CharSequence to, @NotNull Function<String, String> function) {
		int start = indexOf(src, from, 0);
		if (start == -1)
			return src;
		final StringBuilder res = new StringBuilder(src);
		final int toLen = to.length();
		final int fromLen = from.length();
		while (start != -1) {
			final int end = indexOf(res, to, start);
			if (end != -1)
				res.replace(start, end + toLen, function.apply(res.substring(start + fromLen, end)));
			start = indexOf(res, from, start + 1);
		}
		return res.toString();
	}

	/**
	 * This method will attempt to get any amount of substrings of {@code src} between {@code from} and {@code to}.
	 * If a substring is found, {@code action} will {@link Consumer#accept(Object) accept}
	 * it, removing said substring as well as {@code from} and {@code to} from the
	 * returning {@code String} only if {@code remove} is set to {@code true}.
	 * Let's see an example where {@code print} stands for {@code System.out.println}:
	 * <p>
	 * <code>match("Match (this)", "(", ")", match -> print("Match: " + match), true);</code>
	 * <p>
	 * The output of this line will be "Match: this", the returning {@link String} of
	 * the method will be "Match ". If {@code remove} is set to {@code false}, the output will stay the
	 * same, but the returning string will be "Match (this)", without any change.
	 * As you can see, {@code from} and {@code to} will never be sent to the {@link Consumer}.
	 *
	 * @param src the source {@link CharSequence} to use.
	 * @param from the {@link CharSequence} to match at the beginning of the pattern.
	 * @param to the {@link CharSequence} to match at the end of the pattern.
	 * @param action a {@link Consumer} that may accept any matching
	 * substrings of {@code src} between {@code from} and {@code to}.
	 * @param remove if {@code true}, the matching content will be removed
	 * from the resulting {@link String}, if {@code false}, the resulting {@link String}
	 * will be an exact copy of {@code src}.
	 *
	 * @return A clone of {@code src} with any match from the specified pattern removed from it only if
	 * {@code remove} is set to {@code true}, if set to {@code false}, a clone of {@code src} will be returned.
	 * If the pattern isn't found, {@code src} will be returned as is without creating a new {@link String}.
	 *
	 * @throws NullPointerException if any parameter is {@code null}.
	 *
	 * @since JSky 1.0.0
	 *
	 * @see #match(String, CharSequence, CharSequence, Consumer)
	 * @see #match(String, CharSequence, CharSequence, Function)
	 */
	@NotNull
	public static String match(@NotNull String src, @NotNull CharSequence from, @NotNull CharSequence to, @NotNull Consumer<String> action, boolean remove) {
		return match(src, from, to, remove ?
				match -> {action.accept(match); return "";} :
				match -> {action.accept(match); return match;});
	}

	/**
	 * This method will attempt to get any amount of substrings of {@code src} between {@code from} and {@code to}.
	 * If a substring is found, {@code action} will {@link Consumer#accept(Object) accept}
	 * it, removing said substring as well as {@code from} and {@code to} from the returning {@code String}.
	 * Let's see an example where {@code print} stands for {@code System.out.println}:
	 * <p>
	 * <code>match("Match (this)", "(", ")", match -> print("Match: " + match));</code>
	 * <p>
	 * The output of this line will be "Match: this", the returning {@link String} of
	 * the method will be "Match ". As you can see, {@code from} and {@code to} will
	 * never be sent to the {@link Consumer}.
	 *
	 * @param src the source {@link String} to use.
	 * @param from the {@link CharSequence} to match at the beginning of the pattern.
	 * @param to the {@link CharSequence} to match at the end of the pattern.
	 * @param action a {@link Consumer} that may accept any matching
	 * substrings of {@code src} between {@code from} and {@code to}.
	 *
	 * @return A clone of {@code src} with any match from the specified pattern removed from it.
	 * If the pattern isn't found, {@code src} will be returned as is without creating a new {@link String}.
	 *
	 * @throws NullPointerException if any parameter is {@code null}.
	 *
	 * @since JSky 1.0.0
	 *
	 * @see #match(String, CharSequence, CharSequence, Consumer, boolean)
	 * @see #match(String, CharSequence, CharSequence, Function)
	 */
	public static String match(@NotNull String src, @NotNull CharSequence from, @NotNull CharSequence to, @NotNull Consumer<String> action) {
		return match(src, from, to, action, true);
	}

	/*
	 - String conversion - UUID
	 */

	/**
	 * Converts a {@link String} to a {@link UUID} <b>the safe way</b>,
	 * trying to use {@link UUID#fromString(String)} and catching
	 * an {@link IllegalArgumentException} is <b>not</b> the safest approach, use
	 * this method for safe {@link String} to {@link UUID} conversion.
	 * <p>
	 * This method will also convert {@link UUID}s without '-' characters.
	 * <p>
	 * Additionally, this method only allows full {@link UUID}s, that is,
	 * strings which length is either 36 or 32. {@link UUID#fromString(String)}
	 * accepts, for example 1-1-1-1-1" as "00000001-0001-0001-0001-000000000001",
	 * this method doesn't.
	 *
	 * @param uuid the {@link String} to be converted to {@link UUID}
	 *
	 * @return A {@link UUID} by the specified {@code uuid} String,
	 * {@code null} if the string doesn't have a valid {@link UUID} format.
	 */
	@Nullable
	public static UUID toUUID(@Nullable String uuid) {
		final int len = uuid == null ? 0 : uuid.length();
		if (len == 36)
			return parseFullUUID(uuid);
		if (len == 32)
			return parseSmallUUID(uuid);
		return null;
	}

	private static UUID parseFullUUID(String uuid) {
		final char[] chars = uuid.toCharArray();
		for (int i = 0; i < 36; i++) {
			final char ch = chars[i];
			if (i == 8 || i == 13 || i == 18 || i == 23) {
				if (ch != '-')
					return null;
			} else if (!(ch >= '0' && ch <= '9') && !(ch >= 'a' && ch <= 'f') && !(ch >= 'A' && ch <= 'F'))
				return null;
		}
		return UUID.fromString(uuid);
	}

	private static UUID parseSmallUUID(String uuid) {
		final StringBuilder builder = new StringBuilder(36);
		final char[] chars = uuid.toCharArray();
		for (int i = 0, j = 0; i < 36; i++) {
			if (i == 8 || i == 13 || i == 18 || i == 23)
				builder.append('-');
			else {
				final char ch = chars[j];
				if (!(ch >= '0' && ch <= '9') && !(ch >= 'a' && ch <= 'f') && !(ch >= 'A' && ch <= 'F'))
					return null;
				builder.append(ch);
				j++;
			}
		}
		return UUID.fromString(builder.toString());
	}

	/*
	 - Byte array to String conversion
	 */

	/**
	 * Converts the provided array of {@code byte}s to a {@link String} in the given {@code radix}.
	 * For example, a {@code radix} of 16 will result in a hexadecimal {@link String}, though
	 * {@link #toHexString(byte[])} can be used instead.
	 *
	 * @param bytes The array of {@code byte}s to convert.
	 * @param radix The radix of the String representation.
	 * Must be between {@link Character#MIN_RADIX} and {@link Character#MAX_RADIX}
	 *
	 * @return A {@link String} in the given {@code radix} based of the provided {@code bytes} array.
	 *
	 * @since JSky 1.0.0
	 *
	 * @see #toString(byte[])
	 * @see #toHexString(byte[])
	 */
	@NotNull
	public static String toString(byte @NotNull [] bytes, @Range(from = Character.MIN_RADIX, to = Character.MAX_RADIX) int radix) {
		return new BigInteger(1, bytes).toString(radix);
	}

	/**
	 * Converts the provided array of {@code byte}s to a decimal {@link String}.
	 *
	 * @param bytes The array of {@code byte}s to convert.
	 *
	 * @return A decimal {@link String} based of the provided {@code bytes} array.
	 *
	 * @throws NullPointerException if {@code bytes} is {@code null}.
	 *
	 * @since JSky 1.0.0
	 *
	 * @see #toString(byte[], int)
	 */
	@NotNull
	public static String toString(byte @NotNull [] bytes) {
		return toString(bytes, 10);
	}

	/**
	 * Converts the provided array of {@code byte}s to a hexadecimal {@link String}.
	 *
	 * @param bytes The array of {@code byte}s to convert.
	 *
	 * @return A hexadecimal {@link String} based of the provided {@code bytes} array.
	 *
	 * @throws NullPointerException if {@code bytes} is {@code null}.
	 *
	 * @since JSky 1.0.0
	 * 
	 * @see #toString(byte[], int)
	 */
	@NotNull
	public static String toHexString(byte @NotNull [] bytes) {
		return toString(bytes, 16);
	}

	/*
	 - MessageDigest
	 */

	/**
	 * Safely returns a {@link MessageDigest} object that implements the specified digest algorithm.
	 * See {@link MessageDigest#getInstance(String)} for more details. This method just catches the
	 * {@link NoSuchAlgorithmException} for you and returns {@code null} in case it is thrown.
	 *
	 * @param algorithm The name of the algorithm to search for.
	 *
	 * @return A {@link MessageDigest} object that implements the specified digest algorithm.
	 * {@code null} if no algorithm matches the provided {@code algorithm} name.
	 *
	 * @throws NullPointerException If {@code algorithm} is {@code null}.
	 *
	 * @since JSky 1.0.0
	 *
	 * @see MessageDigest#getInstance(String)
	 */
	@Nullable
	public static MessageDigest getMessageDigest(@NotNull String algorithm) {
		try {
			return MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	/**
	 * Converts the provided {@link String} to a {@code byte} array, using
	 * the specified {@code algorithm}. The {@link MessageDigest algorithm} is
	 * obtained with {@link MessageDigest#getInstance(String)}.
	 *
	 * @param str The {@code String} to convert.
	 * @param algorithm The {@link MessageDigest algorithm} to use, see
	 * {@link MessageDigest#getInstance(String)} for more details.
	 *
	 * @return The resulting {@code byte} array. {@code null} if the {@code algorithm} isn't found.
	 *
	 * @throws NullPointerException If either {@code str} or {@code algorithm} are {@code null}.
	 *
	 * @since JSky 1.0.0
	 *
	 * @see MessageDigest#getInstance(String)
	 */
	public static byte @Nullable [] digest(@NotNull String str, @NotNull String algorithm) {
		final MessageDigest digest = getMessageDigest(algorithm);
		return digest == null ? null : digest.digest(str.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Converts the provided {@code str}ing to a {@code byte} array using the specified
	 * {@code algorithm} with {@link #digest(String, String)}. Then the resulting array
	 * is converted to a hexadecimal {@link String} with {@link #toHexString(byte[])}.
	 *
	 * @param str The {@link String} to process.
	 * @param algorithm The {@link MessageDigest algorithm} to use, see
	 * {@link MessageDigest#getInstance(String)} for more details.
	 *
	 * @return The resulting {@code byte} array processed by the {@code algorithm}, then
	 * converted to a hexadecimal {@link String}. {@code null} if the provided
	 * {@code algorithm} isn't found.
	 *
	 * @throws NullPointerException If either {@code str} or {@code algorithm} are {@code null}.
	 *
	 * @since JSky 1.0.0
	 */
	@Nullable
	public static String hexDigest(@NotNull String str, @NotNull String algorithm) {
		final byte[] bytes = digest(str, algorithm);
		return bytes == null ? null : toHexString(bytes);
	}

	/**
	 * Converts the provided {@code str}ing to a {@code byte} array using the specified
	 * {@code algorithm} with {@link #digest(String, String)}. Then the resulting array
	 * is converted to a hexadecimal {@link String} with {@link #toHexString(byte[])}.
	 *
	 * @param str The {@link String} to process.
	 * @param algorithm The {@link MessageDigest algorithm} to use, see
	 * {@link MessageDigest#getInstance(String)} for more details.
	 *
	 * @return The resulting {@code byte} array processed by the {@code algorithm}, then
	 * converted to a hexadecimal {@link String}. {@code null} if the provided
	 * {@code algorithm} isn't found.
	 *
	 * @throws NullPointerException If either {@code str} or {@code algorithm} are {@code null}.
	 *
	 * @since JSky 1.0.0
	 */
	@NotNull
	public static String hexDigest(@NotNull String str, @NotNull MessageDigest algorithm) {
		return toHexString(algorithm.digest(str.getBytes(StandardCharsets.UTF_8)));
	}

	/**
	 * Hashes the provided {@code str}ing with the <b>SHA3-256</b> algorithm.
	 *
	 * @param str The {@link String} to hash.
	 *
	 * @return An <b>almost</b> never {@code null} hexadecimal {@link String}, containing
	 * the SHA3-256 hash calculated from the source {@code str}. {@code null} if for
	 * whatever reason the SHA3-256 algorithm isn't found (Should never happen).
	 *
	 * @since JSky 1.0.0
	 */
	@Nullable
	public static String sha3_256(@NotNull String str) {
		return hexDigest(str, "SHA3-256");
	}

	/**
	 * Hashes the provided {@code str}ing with the <b>MD5</b> algorithm.
	 * <p>
	 * <b>Note</b>: Keep in mind that the MD5 algorithm is considered to be cryptographically
	 * broken, vulnerable to hash collisions. For security purposes, you are better
	 * off with {@link #sha3_256(String)}.
	 *
	 * @param str The {@link String} to hash.
	 *
	 * @return An <b>almost</b> never {@code null} hexadecimal {@link String}, containing
	 * the MD5 hash calculated from the source {@code str}. {@code null} if for
	 * whatever reason the MD5 algorithm isn't found (Should never happen).
	 *
	 * @since JSky 1.0.0
	 */
	@Nullable
	public static String md5(@NotNull String str) {
		return hexDigest(str, "MD5");
	}
}
