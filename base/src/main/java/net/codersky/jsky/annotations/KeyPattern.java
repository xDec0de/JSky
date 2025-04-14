package net.codersky.jsky.annotations;

import net.codersky.jsky.strings.JStrings;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The KeyPattern annotation claims that any element annotated
 * with it must follow the key format that is commonly used on
 * JSky as a standard. This pattern only allows lowercase
 * words separated by single dashes ('-'). This standard is
 * most commonly used on {@link java.util.Map maps}. The
 * {@link JStrings#hasKeyPattern(CharSequence)} method is provided
 * to check if a {@link CharSequence} follows this pattern.
 * <p>
 * The pattern enforces the following rules:
 * <ul>
 *     <li>Only lowercase letters (a-z) and single dashes are allowed</li>
 *     <li>Consecutive dashes are not permitted ("hello--world" is invalid)</li>
 *     <li>Dashes must be between words ("-hello-world-" is invalid)</li>
 *     <li>Empty strings are not allowed</li>
 *     <li>Dashes are optional ("helloworld" is valid)</li>
 * </ul>
 * <p>
 * Note: This annotation does not imply {@link NotNull}. For {@code null}
 * safety, use both annotations together: {@code @NotNull @KeyPattern}
 * <p>
 * This annotation uses the {@link Pattern} annotation with
 * the pattern <i>"[a-z]+(-[a-z]+)*"</i> for static analysis. Sadly, this means
 * that it cannot be combined with other patterns, not until we find
 * a solution that doesn't rely on the {@link Pattern} annotation.
 *
 * @since JSky 1.0.0
 *
 * @see JStrings#hasKeyPattern(CharSequence)
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
@Pattern("[a-z]+(-[a-z]+)*")
public @interface KeyPattern {}
