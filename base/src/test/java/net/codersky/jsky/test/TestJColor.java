package net.codersky.jsky.test;

import org.junit.jupiter.api.Test;

import java.awt.Color;

import static net.codersky.jsky.JColor.isColor;
import static net.codersky.jsky.JColor.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestJColor {

	@Test
	public void testHex() {
		assertEquals(Color.decode("#fF00ee"), of("#fF00ee"));
		assertTrue(isColor("#fF00eE"));
		assertFalse(isColor("fF00eE"));
		assertFalse(isColor("#ff00"));
		assertFalse(isColor("#fF00eEee"));
		assertFalse(isColor("#fg00ee"));
	}

	@Test
	public void testRgb() {
		assertEquals(new Color(255, 0, 123), of("255, 0, 123"));
		assertEquals(new Color(255, 0, 123), of("255,0,123"));
		assertEquals(new Color(255, 0, 123), of("  255,  0   ,   123  "));
		assertEquals(new Color(255, 255, 255), of("255, 255, 255"));
		assertEquals(new Color(0, 0, 0), of("0, 0, 0"));
		assertTrue(isColor("0, 0, 0"));
		assertFalse(isColor("0, 0"));
		assertFalse(isColor("0, 0, 0, 0"));
		assertFalse(isColor("256, 0, 123"));
		assertFalse(isColor("255, -1, 123"));
		assertFalse(isColor("255, 12a, 123"));
	}

	@Test
	public void testInvalid() {
		assertFalse(isColor(""));
		assertFalse(isColor(" "));
		assertNull(of("no"));
	}
}
