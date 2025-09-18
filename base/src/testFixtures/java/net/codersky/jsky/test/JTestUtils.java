package net.codersky.jsky.test;

import java.io.File;
import java.nio.file.Path;

/**
 * Shared utility class for tests. This class can be
 * accessed by the tests of every JSky subproject.
 */
public class JTestUtils {

	/** The folder reserved for temporary files that can be created by tests. */
	public final static File TMP_FOLDER = Path.of("./src/test/tmp/").toFile();

	public static void deleteFolder(File folder) {
		if (!folder.exists())
			return;
		final File[] files = folder.listFiles();
		if (files != null)
			for (File file : files)
				file.delete();
		folder.delete();
	}
}
