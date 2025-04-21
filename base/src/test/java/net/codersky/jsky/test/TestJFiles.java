package net.codersky.jsky.test;

import net.codersky.jsky.JFiles;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestJFiles {

	final File one = new File(JTestUtils.TMP_FOLDER, "one");
	final File nested = new File(JTestUtils.TMP_FOLDER, "nest/nested");

	@Test
	public void testFileCreation() {
		JTestUtils.TMP_FOLDER.delete();
		assertTrue(JFiles.create(one));
		assertTrue(one.exists() && one.isFile());
		assertTrue(JFiles.create(nested));
		assertTrue(nested.exists() && nested.isFile());
	}
}
