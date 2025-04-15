package net.codersky.jsky.annotations;

import org.intellij.lang.annotations.Pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Due to limitations imposed by the {@link Pattern} annotation,
 * this annotation exists to apply {@link KeyPattern} to arrays
 * and varargs, with the downside of not having static analysis,
 * hence why two annotations were created.
 * <p>
 * Please refer to {@link KeyPattern} for details about the
 * pattern itself and how it must be followed.
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE_USE})
public @interface KeyPatterns {}
