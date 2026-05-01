package net.codersky.jsky.test;

import net.codersky.jsky.JColor;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static net.codersky.jsky.JColor.isColor;
import static net.codersky.jsky.JColor.isHex;
import static net.codersky.jsky.JColor.isRgb;
import static net.codersky.jsky.JColor.of;
import static net.codersky.jsky.JColor.ofHex;
import static net.codersky.jsky.JColor.ofRgb;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestJColor {

    /*
     - isHex / ofHex - long form (#rrggbb)
     */

    @Test
    public void testIsHexLong() {
        assertTrue(isHex("#fF00eE"));
        assertTrue(isHex("#000000"));
        assertTrue(isHex("#FFFFFF"));
        assertFalse(isHex("fF00eE"));
        assertFalse(isHex("#fg00ee"));
        assertFalse(isHex("0xff00ee"));
    }

    @Test
    public void testOfHexLong() {
        assertEquals(new Color(0xFF, 0x00, 0xEE), ofHex("#fF00ee"));
        assertEquals(new Color(0, 0, 0), ofHex("#000000"));
        assertEquals(new Color(255, 255, 255), ofHex("#FFFFFF"));
        assertNull(ofHex("fF00eE"));
        assertNull(ofHex("#fg00ee"));
    }

    /*
     - isHex / ofHex - short form (#rgb)
     */

    @Test
    public void testIsHexShort() {
        assertTrue(isHex("#abc"));
        assertTrue(isHex("#FFF"));
        assertTrue(isHex("#000"));
        assertFalse(isHex("abc"));
        assertFalse(isHex("#zz0"));
    }

    @Test
    public void testOfHexShort() {
        assertEquals(new Color(0xAA, 0xBB, 0xCC), ofHex("#abc"));
        assertEquals(new Color(0xFF, 0xFF, 0xFF), ofHex("#FFF"));
        assertEquals(new Color(0xFF, 0x00, 0xAA), ofHex("#f0a"));
        assertEquals(new Color(0x00, 0x00, 0x00), ofHex("#000"));
    }

    /*
     - isHex / ofHex - RGBA forms
     */

    @Test
    public void testIsHexRgba() {
        assertTrue(isHex("#fF00eE80"));
        assertTrue(isHex("#abcd"));
        assertTrue(isHex("#FFFFFFFF"));
        assertFalse(isHex("#fF00eE8"));
        assertFalse(isHex("#fF00eE800"));
        assertFalse(isHex("#fF00eEzz"));
    }

    @Test
    public void testOfHexRgba() {
        assertEquals(new Color(0xFF, 0x00, 0xEE, 0x80), ofHex("#fF00eE80"));
        assertEquals(new Color(0xAA, 0xBB, 0xCC, 0xDD), ofHex("#abcd"));
        assertEquals(new Color(0xFF, 0xFF, 0xFF, 0xFF), ofHex("#FFFFFFFF"));
        assertEquals(new Color(0x00, 0x00, 0x00, 0x00), ofHex("#00000000"));
        assertNull(ofHex("#fF00eE8"));
        assertNull(ofHex("#fF00eEzz"));
    }

    /*
     - isHex / ofHex - invalid lengths
     */

    @Test
    public void testIsHexInvalidLengths() {
        assertFalse(isHex(""));
        assertFalse(isHex("#"));
        assertFalse(isHex("#a"));
        assertFalse(isHex("#ab"));
        assertFalse(isHex("#abcde"));    // 6 chars (5 after #), but 5 is valid for #rgba - this is 6 total, hits 6 not allowed
        assertFalse(isHex("#abcdef0"));  // 8 chars (7 after #) - not allowed
        assertFalse(isHex("#abcdefghi"));// 10 chars - not allowed
    }

    /*
     - isRgb / ofRgb
     */

    @Test
    public void testIsRgb() {
        assertTrue(isRgb("0, 0, 0"));
        assertTrue(isRgb("255, 255, 255"));
        assertTrue(isRgb("0,0,0"));
        assertTrue(isRgb("  255,  0   ,   123  "));
        assertTrue(isRgb("+5, 0, 0"));
        assertFalse(isRgb(""));
        assertFalse(isRgb("0, 0"));
        assertFalse(isRgb("0, 0, 0, 0"));
        assertFalse(isRgb("256, 0, 123"));
        assertFalse(isRgb("-1, 0, 0"));
        assertFalse(isRgb("255, 12a, 123"));
        assertFalse(isRgb(" , 0, 0"));
        assertFalse(isRgb("0, 0, "));
    }

    @Test
    public void testOfRgb() {
        assertEquals(new Color(255, 0, 123), ofRgb("255, 0, 123"));
        assertEquals(new Color(255, 0, 123), ofRgb("255,0,123"));
        assertEquals(new Color(255, 0, 123), ofRgb("  255,  0   ,   123  "));
        assertEquals(new Color(0, 0, 0), ofRgb("0, 0, 0"));
        assertEquals(new Color(255, 255, 255), ofRgb("255, 255, 255"));
        assertEquals(new Color(5, 0, 0), ofRgb("+5, 0, 0"));
        assertNull(ofRgb(""));
        assertNull(ofRgb("0, 0"));
        assertNull(ofRgb("256, 0, 0"));
        assertNull(ofRgb("-1, 0, 0"));
        assertNull(ofRgb("255, 12a, 0"));
    }

    /*
     - isColor / of dispatch
     */

    @Test
    public void testIsColor() {
        assertTrue(isColor("#fF00eE"));
        assertTrue(isColor("#abc"));
        assertTrue(isColor("#abcd"));
        assertTrue(isColor("#fF00eE80"));
        assertTrue(isColor("0, 0, 0"));
        assertFalse(isColor(""));
        assertFalse(isColor(" "));
        assertFalse(isColor("not a color"));
    }

    @Test
    public void testOf() {
        assertEquals(new Color(0xFF, 0x00, 0xEE), of("#fF00ee"));
        assertEquals(new Color(0xAA, 0xBB, 0xCC), of("#abc"));
        assertEquals(new Color(0xFF, 0x00, 0xEE, 0x80), of("#fF00eE80"));
        assertEquals(new Color(255, 0, 123), of("255, 0, 123"));
        assertNull(of("not a color"));
        assertNull(of(""));
        assertNull(of("#"));      // hex dispatch returns null
        assertNull(of(" "));      // rgb dispatch returns null
    }

    /*
     - Instantiation
     */

    @Test
    public void testCannotInstantiate() throws NoSuchMethodException {
        final Constructor<JColor> ctor = JColor.class.getDeclaredConstructor();
        ctor.setAccessible(true);
        final InvocationTargetException ex = assertThrows(InvocationTargetException.class, ctor::newInstance);
        assertNotNull(ex.getCause());
        assertInstanceOf(UnsupportedOperationException.class, ex.getCause());
    }
}
