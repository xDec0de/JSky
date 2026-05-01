package net.codersky.jsky;

import net.codersky.jsky.math.JNumbers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;

/**
 * Utility class to help with {@link Color Colors}. This class mostly provides fast methods to create new {@link Color}
 * instances without having to deal with formatting exceptions, as well as checking for color formats before attempting
 * to convert.
 *
 * @author xDec0de_
 * @see #of(String)
 * @since JSky 1.0.0
 */
public final class JColor {

    private JColor() {
        throw new UnsupportedOperationException("JColor cannot be instantiated.");
    }

    /**
     * Checks if the provided {@code color} matches any of the color formats supported by this class. You can see
     * details about each format here:
     * <ul>
     *     <li>{@link #isHex(String) Hexadecimal format}</li>
     *     <li>{@link #isRgb(String) RGB format}</li>
     * </ul>
     *
     * @param color The String to check.
     *
     * @return {@code true} if {@code color} matches any supported color format, {@code false} otherwise.
     *
     * @see #of(String)
     * @since JSky 1.0.0
     */
    public static boolean isColor(@NotNull String color) {
        return isHex(color) || isRgb(color);
    }

    /**
     * Attempts to convert {@code color} to a {@link Color} instance with all color formats supported by this class.
     * Said formats can be found {@link #isColor(String) here}.
     * <p>
     * The first character of {@code color} is used to dispatch to the correct parser. If it starts with {@code '#'},
     * only hex parsing is attempted. Otherwise, only rgb parsing is attempted.
     *
     * @param color The String to convert.
     *
     * @return A {@link Color} instance if a valid color format was provided, {@code null} otherwise.
     *
     * @see #isColor(String)
     * @see #ofHex(String)
     * @see #ofRgb(String)
     * @since JSky 1.0.0
     */
    public static @Nullable Color of(@NotNull String color) {
        if (color.isEmpty())
            return null;
        return color.charAt(0) == '#' ? ofHex(color) : ofRgb(color);
    }

    /*
     - Hexadecimal colors
     */

    /**
     * Checks if the provided {@code hex} String follows one of the supported hex formats:
     * <ul>
     *     <li>{@code #rgb} (length 4) - shorthand RGB, each digit is duplicated to form a full byte</li>
     *     <li>{@code #rgba} (length 5) - shorthand RGBA, each digit is duplicated to form a full byte</li>
     *     <li>{@code #rrggbb} (length 7) - full RGB</li>
     *     <li>{@code #rrggbbaa} (length 9) - full RGBA</li>
     * </ul>
     * Where each character (except {@code '#'}) must be a {@link JNumbers#isHexChar(char) hexadecimal character}.
     * Both lowercase and uppercase letters are accepted.
     *
     * @param hex The String to check.
     *
     * @return {@code true} if {@code hex} follows any of the formats explained above, {@code false} otherwise.
     *
     * @see #ofHex(String)
     * @see #isColor(String)
     * @since JSky 1.0.0
     */
    public static boolean isHex(@NotNull String hex) {
        final int len = hex.length();
        if (len != 4 && len != 5 && len != 7 && len != 9)
            return false;
        return JNumbers.isHex(hex, true);
    }

    /**
     * Gets a {@link Color} from the provided {@code hex} String, given that {@code hex} has a valid hex format. Details
     * about the supported formats can be found {@link #isHex(String) here}.
     * <p>
     * For shorthand inputs ({@code #rgb} and {@code #rgba}), each digit is expanded by duplication, so {@code #f0a}
     * yields {@code 0xff00aa} and {@code #f0a8} yields {@code 0xff00aa88}.
     *
     * @param hex The hex String to convert.
     *
     * @return A {@link Color} instance, given that {@code hex} follows one of the supported patterns, {@code null}
     * otherwise.
     *
     * @see #isHex(String)
     * @see #of(String)
     * @since JSky 1.0.0
     */
    public static @Nullable Color ofHex(@NotNull String hex) {
        if (!isHex(hex))
            return null;
        final int len = hex.length();
        final int width = (len == 4 || len == 5) ? 1 : 2;
        final int r = parseHexComponent(hex, 1, width);
        final int g = parseHexComponent(hex, 1 + width, width);
        final int b = parseHexComponent(hex, 1 + width * 2, width);
        if (len == 5 || len == 9) {
            final int a = parseHexComponent(hex, 1 + width * 3, width);
            return new Color(r, g, b, a);
        }
        return new Color(r, g, b);
    }

    /*
     - RGB colors
     */

    /**
     * Checks if the provided {@code rgb} String follows this pattern:
     * <ul>
     *     <li>r, g, b</li>
     * </ul>
     * <p>
     * Where each letter consists of an integer between 0 and 255. Integers must be separated by commas and any given
     * number of whitespace characters, so the following inputs are all valid:
     * <ul>
     *     <li>0, 100, 255</li>
     *     <li>0,   100,   255</li>
     *     <li>0,100,255</li>
     * </ul>
     *
     * @param rgb The String to check.
     *
     * @return {@code true} if {@code rgb} follows the rgb pattern explained above, {@code false} otherwise.
     *
     * @see #ofRgb(String)
     * @see #isColor(String)
     * @since JSky 1.0.0
     */
    public static boolean isRgb(@NotNull String rgb) {
        return getRgbValues(rgb) != null;
    }

    /**
     * Gets a {@link Color} from the provided {@code rgb} String, given that {@code rgb} has a valid rgb format. Details
     * about the format can be found {@link #isRgb(String) here}.
     *
     * @param rgb The rgb String to convert.
     *
     * @return A {@link Color} instance, given that {@code rgb} follows the pattern mentioned above, {@code null}
     * otherwise.
     *
     * @see #isRgb(String)
     * @see #of(String)
     * @since JSky 1.0.0
     */
    public static @Nullable Color ofRgb(@NotNull String rgb) {
        final int[] values = getRgbValues(rgb);
        return values == null ? null : new Color(values[0], values[1], values[2]);
    }

    /*
     - Internal helpers
     */

    // Reads width consecutive hex chars starting at start and returns 0..255.
    // For width 1 the digit is duplicated, so f becomes 0xff.
    private static int parseHexComponent(@NotNull String hex, int start, int width) {
        if (width == 1) {
            final int v = hexValue(hex.charAt(start));
            return (v << 4) | v;
        }
        return (hexValue(hex.charAt(start)) << 4) | hexValue(hex.charAt(start + 1));
    }

    private static int hexValue(char c) {
        if (c >= '0' && c <= '9')
            return c - '0';
        if (c >= 'a' && c <= 'f')
            return c - 'a' + 10;
        return c - 'A' + 10;
    }

    // Single-pass rgb parser. No allocations beyond the returned int[3].
    // Accepts an optional '+' sign on each component to preserve the previous behavior.
    private static int @Nullable [] getRgbValues(@NotNull String rgb) {
        final int len = rgb.length();
        final int[] out = new int[3];
        int i = 0;
        for (int part = 0; part < 3; part++) {
            while (i < len && rgb.charAt(i) <= ' ')
                i++;
            if (i < len && rgb.charAt(i) == '+')
                i++;
            int value = 0;
            final int digitStart = i;
            while (i < len) {
                final char c = rgb.charAt(i);
                if (c < '0' || c > '9')
                    break;
                value = value * 10 + (c - '0');
                if (value > 255)
                    return null;
                i++;
            }
            if (i == digitStart)
                return null;
            while (i < len && rgb.charAt(i) <= ' ')
                i++;
            if (part < 2) {
                if (i >= len || rgb.charAt(i) != ',')
                    return null;
                i++;
            }
            out[part] = value;
        }
        return i == len ? out : null;
    }
}
