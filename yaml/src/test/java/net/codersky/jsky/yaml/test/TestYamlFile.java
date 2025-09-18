package net.codersky.jsky.yaml.test;

import net.codersky.jsky.test.JTestUtils;
import net.codersky.jsky.yaml.YamlFile;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestYamlFile {

	final YamlFile testFile = new YamlFile(JTestUtils.TMP_FOLDER, "test.yml");

	@Test
	public void testFileCreation() {
		assertTrue(testFile.setup(e -> System.err.println("Exception on YamlFile#setup: " + e.getMessage())));
		assertTrue(testFile.asFile().exists());
	}

	/*
	 - Before & after test actions
	 */

	@BeforeEach
	public void beforeEach() {
		JTestUtils.deleteFolder(JTestUtils.TMP_FOLDER);
	}

	@AfterAll
	public static void afterAll() {
		JTestUtils.deleteFolder(JTestUtils.TMP_FOLDER);
	}
}
