package net.codersky.jsky.annotations;

import net.codersky.jsky.strings.JStrings;
import org.intellij.lang.annotations.Pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The KeyPattern annotation claims that any element annotated
 * with it must follow the key format that is commonly used on
 * JSky as a standard. This pattern only allows lowercase
 * characters separated by dashes ('-'). This standard is
 * most commonly used on {@link java.util.Map maps}. The use
 * of this pattern on {@link CharSequence CharSequences} can be verified
 * with {@link JStrings#hasKeyPattern(CharSequence...)}
 * <p>
 * This annotation uses the {@link Pattern} annotation with
 * the pattern <i>"[a-z-]+"</i> for static analysis. Sadly, this means
 * that it cannot be combined with other patterns, not until we find
 * a solution that doesn't rely on the {@link Pattern} annotation.
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
@Pattern("[a-z-]+")
public @interface KeyPattern {}
