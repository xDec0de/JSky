package net.codersky.jsky;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * Utility class containing methods related to {@link File files}.
 *
 * @since JSky 1.0.0
 *
 * @author xDec0de_
 */
public class JFiles {

	/**
	 * Ensures a {@link File} is created, creating any necessary
	 * parent directories as well as the file itself if it doesn't
	 * already exist.
	 *
	 * @param file The {@link File} to create.
	 * @param err The {@link Consumer} that will accept any exceptions
	 * thrown by this method. Caught exceptions are only of type
	 * {@link IOException} or {@link SecurityException}.
	 *
	 * @return {@code true} if the {@code file} created successfully.
	 * {@code false} if the {@code file} already exists or if an error occurred.
	 *
	 * @since JSky 1.0.0
	 */
	public static boolean create(@NotNull File file, @NotNull Consumer<Exception> err) {
		if (file.exists())
			return false;
		final File parent = file.getParentFile();
		if (parent != null)
			parent.mkdirs();
		try {
			file.createNewFile();
			return true;
		} catch (IOException | SecurityException e) {
			err.accept(e);
			return false;
		}
	}

	/**
	 * Ensures a {@link File} is created, creating any necessary
	 * parent directories as well as the file itself if it doesn't
	 * already exist.
	 * <p>
	 * Any exceptions produced will just be {@link Exception#printStackTrace() printed}.
	 * If you want to override this behaviour use {@link #create(File, Consumer)}.
	 *
	 * @param file The {@link File} to create.
	 *
	 * @return {@code true} if the {@code file} created successfully.
	 * {@code false} if the {@code file} already exists or if an error occurred.
	 *
	 * @since JSky 1.0.0
	 */
	public static boolean create(@NotNull File file) {
		return create(file, Exception::printStackTrace);
	}

	/**
	 * Removes all files that match the provided {@code filter} inside a {@code directory}.
	 *
	 * @param directory The directory to remove all files that match the {@code filter} from.
	 * @param recursive Whether to enable recursive mode or not. With recursive mode, all
	 * directories inside the specified {@code directory} will also be cleared and then removed.
	 * @param filter The {@link FileFilter} to apply before deleting files, files that return
	 * {@code false} on this {@link FileFilter} won't be removed. If {@code null}, no filter
	 * will be applied and all files will be removed.
	 *
	 * @return {@code true} if all files were deleted successfully, {@code false} otherwise.
	 *
	 * @since JSky 1.0.0
	 */
	public static boolean removeContents(@NotNull File directory, boolean recursive, @Nullable FileFilter filter) {
		final File[] files = directory.listFiles(filter);
		if (files == null)
			return false;
		int errors = 0;
		for (final File file : files) {
			if (recursive && file.isDirectory()) {
				if (!removeContents(file, true, filter))
					errors++;
			} else if (!file.delete())
				errors++;
		}
		return errors == 0;
	}

	/**
	 * Removes all files inside a {@code directory}.
	 *
	 * @param directory The directory to remove all files from.
	 * @param recursive Whether to enable recursive mode or not. With recursive mode, all
	 * directories inside the specified {@code directory} will also be cleared and then removed.
	 *
	 * @return {@code true} if all files were deleted successfully, {@code false} otherwise.
	 *
	 * @since JSky 1.0.0
	 */
	public static boolean removeContents(@NotNull File directory, boolean recursive) {
		return removeContents(directory, recursive, null);
	}
}
