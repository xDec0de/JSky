package net.codersky.jsky.yaml.test;

import net.codersky.jsky.test.JTestUtils;
import net.codersky.jsky.yaml.YamlFile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestYamlFile {

	final YamlFile testFile = new YamlFile(JTestUtils.TMP_FOLDER, "test.yml");

	@Test
	public void testFileCreation() {
		JTestUtils.TMP_FOLDER.delete();
		assertTrue(testFile.setup());
		assertTrue(testFile.asFile().exists());
	}
}
