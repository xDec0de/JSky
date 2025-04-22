package net.codersky.jsky.test;

import net.codersky.jsky.JFiles;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestJFiles {

	final File one = new File(JTestUtils.TMP_FOLDER, "one");
	final File nest = new File(JTestUtils.TMP_FOLDER, "nest");
	final File nested = new File(nest, "nested");

	@Test
	public void testFileCreation() {
		assertTrue(JFiles.create(one));
		assertTrue(one.exists() && one.isFile());
		assertTrue(JFiles.create(nested));
		assertTrue(nested.exists() && nested.isFile());
		one.delete();
		nested.delete();
		nest.delete();
		JTestUtils.TMP_FOLDER.delete();
	}
}
