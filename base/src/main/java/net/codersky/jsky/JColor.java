package net.codersky.jsky;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;

/**
 * Utility class to help with {@link Color Colors}.
 * This class mostly provides fast methods to create
 * new {@link Color} instances without having to deal
 * with formatting exceptions, as well as checking for
 * color formats before attempting to convert.
 *
 * @author xDec0de_
 *
 * @since JSky 1.0.0
 *
 * @see #of(String)
 */
public class JColor {

	/**
	 * Checks if the provided {@code color} matches any
	 * of the color formats supported by this class.
	 * You can see details about each format here:
	 * <ul>
	 *     <li>{@link #isHex(String) Hexadecimal format}</li>
	 *     <li>{@link #isRgb(String) RGB format}</li>
	 * </ul>
	 *
	 * @param color The String to check.
	 *
	 * @return {@code true} if {@code color} matches any
	 * supported color format, {@code false} otherwise.
	 *
	 * @since JSky 1.0.0
	 *
	 * @see #of(String)
	 */
	public static boolean isColor(@NotNull String color) {
		return isHex(color) || isRgb(color);
	}

	/**
	 * Attempts to convert {@code color} to a {@link Color}
	 * instance with all color formats supported by this
	 * class. Said formats can be found {@link #isColor(String) here}.
	 *
	 * @param color The String to convert.
	 *
	 * @return A {@link Color} instance if a valid color format
	 * was provided, {@code null} otherwise.
	 *
	 * @since JSky 1.0.0
	 *
	 * @see #isColor(String)
	 * @see #ofHex(String)
	 * @see #ofRgb(String)
	 */
	@Nullable
	public static Color of(@NotNull String color) {
		Color col;
		if ((col = ofHex(color)) != null)
			return col;
		if ((col = ofRgb(color)) != null)
			return col;
		return null;
	}

	/*
	 - Hexadecimal colors
	 */

	/**
	 * Checks if the provided {@code hex} String follows this pattern:
	 * <ul>
	 *     <li>#rrggbb</li>
	 * </ul>
	 * Where each character (Except '#') must be a
	 * {@link JNumbers#isHexChar(char) hexadecimal character}. That
	 * means that the string consist of a single '#' character followed
	 * by 6 consecutive hexadecimal characters.
	 *
	 * @param hex The String to check.
	 *
	 * @return {@code true} if {@code hex} follows the hex
	 * format explained above, {@code false} otherwise.
	 *
	 * @since JSky 1.0.0
	 * 
	 * @see #ofHex(String)
	 * @see #isColor(String)
	 */
	public static boolean isHex(@NotNull String hex) {
		return hex.length() == 7 && JNumbers.isHex(hex, true);
	}

	/**
	 * Gets a {@link Color} from the provided {@code hex} String,
	 * Given that {@code hex} has a valid hex format. Details
	 * about the format can be found {@link #isHex(String) here}.
	 *
	 * @param hex The hex String to convert.
	 *
	 * @return A {@link Color} instance, given that {@code hex}
	 * follows the pattern mentioned above, {@code null} otherwise.
	 *
	 * @since JSky 1.0.0
	 *
	 * @see #isHex(String)
	 * @see #of(String)
	 */
	public static Color ofHex(@NotNull String hex) {
		return isHex(hex) ? Color.decode(hex) : null;
	}

	/*
	 - RGB colors
	 */

	/**
	 * Checks if the provided {@code rgb} String follows this pattern:
	 * <ul>
	 *     <li>r, g, b</li>
	 * </ul>
	 *
	 * Where each letter consists of an integer between 0 and 255.
	 * Integers must be separated by commas and any given number
	 * of spaces, so the following inputs are allowed:
	 * <ul>
	 *     <li>0, 100, 255</li>
	 *     <li>0,   100,   255</li>
	 *     <li>0,100,255</li>
	 * </ul>
	 *
	 * @param rgb The String to check.
	 *
	 * @return {@code true} if {@code rgb} follows the rgb
	 * pattern explained above, {@code false} otherwise.
	 *
	 * @since JSky 1.0.0
	 *
	 * @see #ofRgb(String)
	 * @see #isColor(String)
	 */
	public static boolean isRgb(@NotNull String rgb) {
		return getRgbValues(rgb) != null;
	}

	/**
	 * Gets a {@link Color} from the provided {@code rgb} String,
	 * Given that {@code rgb} has a valid rgb format. Details
	 * about the format can be found {@link #isRgb(String) here}.
	 *
	 * @param rgb The rgb String to convert.
	 *
	 * @return A {@link Color} instance, given that {@code rgb}
	 * follows the pattern mentioned above, {@code null} otherwise.
	 *
	 * @since JSky 1.0.0
	 *
	 * @see #isRgb(String)
	 * @see #of(String)
	 */
	@Nullable
	public static Color ofRgb(@NotNull String rgb) {
		final int[] values = getRgbValues(rgb);
		return values == null ? null : new Color(values[0], values[1], values[2]);
	}

	private static int @Nullable [] getRgbValues(@NotNull String rgb) {
		final String[] parts = rgb.split(",");
		if (parts.length != 3)
			return null;
		final int[] values = new int[3];
		for (int i = 0; i < 3; i++) {
			final String part = parts[i].trim();
			if (!JNumbers.isInteger(part))
				return null;
			final int value = Integer.parseInt(part);
			if (value < 0 || value > 255)
				return null;
			values[i] = value;
		}
		return values;
	}
}
