package net.codersky.jsky.yaml;

import net.codersky.jsky.JFiles;
import net.codersky.jsky.Reloadable;
import net.codersky.jsky.storage.DataManager;
import net.codersky.jsky.storage.DataMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * {@link DataManager} for {@link Yaml} files implementing the {@link Reloadable} {@code interface}.
 * <p>
 * A {@link YamlFile} has two paths that are used internally. For clarification purposes,
 * we will refer to the path used to obtain the
 * {@link ClassLoader#getResourceAsStream(String) resource} of this file as the
 * <b>resource path</b>. This is (generally) the path where your default yaml file
 * is located inside your jar file. The <b>disk path</b> on the other hand, is where
 * the actual file will be saved for the end user.
 * <p>
 * Let's see a practical example. If you have a file named "config.yml" on your jar's
 * resources, but you want it to be stored inside the "path/to/files" folder for the end user,
 * then the <b>disk path</b> will be {@code new File("path/to/files")}, and the <b>resource</b>
 * path will just be "config.yml".
 *
 * @since JSky 1.0.0
 *
 * @see #YamlFile(ClassLoader, File, String)
 * @see #setup()
 * @see #save()
 * @see #update(List)
 */
public class YamlFile extends DataManager implements Reloadable {

	protected final ClassLoader loader;
	protected final Yaml yaml;
	protected final File file;
	protected final String resourcePath;

	/**
	 * Creates a new {@link YamlFile} with the provided {@link ClassLoader loader},
	 * {@code diskPath} {@link File} and {@code resourcePath}.
	 * <p>
	 * A detailed explanation about the {@code diskPath} and {@code resourcePath} parameters
	 * can be found at {@link YamlFile}. Understanding what the <b>resource path</b>
	 * and <b>disk path</b> are is essential to use {@link YamlFile yaml files}.
	 *
	 * @param loader The {@link ClassLoader} used to obtain the {@link #getUpdatedStream() updated
	 * stream} to {@link #update() update} the file. In other words, the {@link ClassLoader}
	 * that contains the {@link ClassLoader#getResourceAsStream(String) resource} of this file.
	 *
	 * @param diskPath The {@link Nullable} {@link File} that will be combined with the
	 * provided {@code resourcePath} to {@link File#File(File, String) create} the <b>disk path</b>
	 * {@link #asFile() file} of this {@link YamlFile}. This parameter may be {@code null} if
	 * the <b>resource path</b> is the same as the <b>disk path</b>.
	 *
	 * @param resourcePath The <b>resource path</b>. If you want this file to be
	 * {@link #update() updated}, this path must point to a valid resource on the {@code loader}.
	 *
	 * @since JSky 1.0.0
	 *
	 * @see YamlFile
	 * @see #YamlFile(File, String)
	 */
	public YamlFile(@NotNull ClassLoader loader, @Nullable File diskPath, @NotNull String resourcePath) {
		super(new DataMap(true));
		this.loader = Objects.requireNonNull(loader);
		this.yaml = getNewYaml();
		this.file = new File(diskPath, resourcePath);
		this.resourcePath = resourcePath;
	}

	/**
	 * Creates a new {@link YamlFile} with the provided {@code diskPath} {@link File}
	 * and {@code resourcePath}. The {@link ClassLoader} used is
	 * {@link Thread#getContextClassLoader()} on {@link Thread#currentThread()}.
	 * <p>
	 * A detailed explanation about the {@code diskPath} and {@code resourcePath} parameters
	 * can be found at {@link YamlFile}. Understanding what the <bf>resource path</b>
	 * and <b>disk path</b> are is essential to use {@link YamlFile yaml files}.
	 *
	 * @param diskPath The {@link Nullable} {@link File} that will be combined with the
	 * provided {@code resourcePath} to {@link File#File(File, String) create} the <b>disk path</b>
	 * {@link #asFile() file} of this {@link YamlFile}. This parameter may be {@code null} if
	 * the <b>resource path</b> is the same as the <b>disk path</b>.
	 *
	 * @param resourcePath The <b>resource path</b>. If you want this file to be
	 * {@link #update() updated}, this path must point to a valid resource on the {@code loader}.
	 *
	 * @since JSky 1.0.0
	 *
	 * @see YamlFile
	 * @see #YamlFile(ClassLoader, File, String)
	 */
	public YamlFile(@Nullable File diskPath, @NotNull String resourcePath) {
		this(Thread.currentThread().getContextClassLoader(), diskPath, resourcePath);
	}

	/*
	 - SnakeYaml utilities
	 */

	/**
	 * Method used to generate a new {@link Yaml} instance.
	 * This is the instance that is used internally in order to
	 * {@link #reload()} and {@link #save()} the file. You may
	 * override this method to use your own configuration.
	 * <p>
	 * By default, this method will just create a new {@link Yaml}
	 * with the default flow style set to {@link DumperOptions.FlowStyle#BLOCK}
	 *
	 * @return A new {@link Yaml} instance. Must not be {@code null},
	 * otherwise, trying to use this {@link YamlFile} will throw exceptions.
	 *
	 * @since JSky 1.0.0
	 */
	@NotNull
	protected Yaml getNewYaml() {
		final DumperOptions dumperOptions = new DumperOptions();
		dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		return new Yaml(dumperOptions);
	}

	/*
	 - File handling
	 */

	/**
	 * Gets the internal {@link File} instance of this {@link YamlFile}.
	 * This {@link File} points to the path <b>on disk</b>, not the
	 * {@link #getUpdatedStream() resource}.
	 *
	 * @return The internal {@link File} instance of this {@link YamlFile}.
	 *
	 * @since JSky 1.0.0
	 */
	@NotNull
	public File asFile() {
		return file;
	}

	/**
	 * Checks if this {@link YamlFile} exists on disk.
	 * This is just a call to {@link File#exists()} using {@link #asFile()}.
	 *
	 * @throws SecurityException Details specified on {@link File#exists()}.
	 *
	 * @return {@code true} if this {@link YamlFile} exists on disk, {@code false} otherwise.
	 *
	 * @since JSky 1.0.0
	 */
	public boolean exists() {
		return file.exists();
	}

	/**
	 * Does any necessary tasks for this file to be effectively usable.
	 * This method has two possible paths:
	 * <p>
	 * If the already {@link #exists() exists}, it will just be {@link #reload() reloaded}.
	 * <p>
	 * If the file doesn't {@link #exists() exist}, it will first be
	 * {@link JFiles#create(File) created}, then {@link #update() updated} and
	 * {@link #save() saved} to write any contents from the resource file to it.
	 *
	 * @return {@code true} if every setup action succeeded, {@code false} otherwise.
	 *
	 * @since JSky 1.0.0
	 */
	public boolean setup() {
		if (exists())
			return reload();
		return JFiles.create(file) && update() && save();
	}

	/**
	 * Saves the <b>cached</b> contents of this {@link YamlFile} to disk.
	 * If the internal {@link DataMap cache} hasn't been modified, nothing
	 * will be done. If the file doesn't {@link #exists() exist}, this method
	 * will attempt to {@link JFiles#create(File) create} it, checking if it
	 * was able to do so.
	 * <p>
	 * Keep in mind that if you save the file before {@link #reload() loading}
	 * it, the result will be an empty file, loosing any contents on it.
	 *
	 * @param onException A {@link Consumer} that will accept any exception
	 * produced by this method.
	 *
	 * @return {@code true} if the file was saved successfully. {@code false}
	 * otherwise. {@code false} could indicate that the file doesn't
	 * {@link #exists() exist} and couldn't be created or that an {@link IOException}
	 * occurred when trying to write the contents to the existing file.
	 *
	 * @since JSky 1.0.0
	 */
	public boolean save(@NotNull Consumer<Exception> onException) {
		if (!getMap().isModified())
			return true;
		if (!exists() && !JFiles.create(file, onException))
			return false;
		try {
			final FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8);
			yaml.dump(getMap().getInternalMap(), writer);
			writer.close();
			getMap().setModified(false);
			return true;
		} catch (IOException ex) {
			onException.accept(ex);
			return false;
		}
	}

	/**
	 * Saves the <b>cached</b> contents of this {@link YamlFile} to disk.
	 * If the internal {@link DataMap cache} hasn't been modified, nothing
	 * will be done. If the file doesn't {@link #exists() exist}, this method
	 * will attempt to {@link JFiles#create(File) create} it, checking if it
	 * was able to do so.
	 * <p>
	 * Keep in mind that if you save the file before {@link #reload() loading}
	 * it, the result will be an empty file, loosing any contents on it.
	 *
	 * @return {@code true} if the file was saved successfully. {@code false}
	 * otherwise. {@code false} could indicate that the file doesn't
	 * {@link #exists() exist} and couldn't be created or that an {@link IOException}
	 * occurred when trying to write the contents to the existing file.
	 *
	 * @since JSky 1.0.0
	 */
	public boolean save() {
		return save(e -> {});
	}

	/*
	 - Reloadable implementation
	 */

	/**
	 * Reloads this {@link YamlFile}, reading the contents of the file on disk
	 * to cache them into the internal {@link DataMap}.
	 *
	 * @param onException A {@link Consumer} that will accept any exception
	 * produced by this method.
	 *
	 * @return {@code true} upon a successful reload, {@code false} only if an
	 * {@link Exception} is thrown.
	 *
	 * @since JSky 1.0.0
	 */
	public boolean reload(@NotNull Consumer<Exception> onException) {
		try {
			getMap().clear();
			final HashMap<String, Object> loadedMap = this.yaml.load(new FileInputStream(this.file));
			if (loadedMap != null) // May be null on empty files
				getMap().getInternalMap().putAll(loadedMap);
			return true;
		} catch (FileNotFoundException | SecurityException ex) {
			onException.accept(ex);
			return false;
		}
	}

	/**
	 * Reloads this {@link YamlFile}, reading the contents of the file on disk
	 * to cache them into the internal {@link DataMap}.
	 *
	 * @return {@code true} upon a successful reload, {@code false} only if an
	 * {@link Exception} is thrown.
	 *
	 * @since JSky 1.0.0
	 */
	public boolean reload() {
		return reload(e -> {});
	}

	/*
	 - File updating
	 */

	/**
	 * Gets the updated {@link InputStream} of this {@link YamlFile}.
	 * By default, this method uses the provided {@link ClassLoader} at the
	 * {@link YamlFile#YamlFile(ClassLoader, File, String)} constructor, using
	 * only the {@code path} without the parent {@link File} to
	 * {@link ClassLoader#getResourceAsStream(String) get the resource}.
	 * The return value of this method directly affects {@link #update(List)},
	 * as this is the {@link InputStream} that said method will compare against.
	 * If {@code null}, the file won't update.
	 *
	 * @return The updated {@link InputStream} of this {@link YamlFile}.
	 *
	 * @since JSky 1.0.0
	 */
	@Nullable
	public InputStream getUpdatedStream() {
		return loader.getResourceAsStream(resourcePath);
	}

	/**
	 * Updates this {@link YamlFile}, comparing its <b>cached</b> contents to those
	 * on the {@link #getUpdatedStream() updated} {@link InputStream}. This method
	 * relies on {@link DataMap#update(HashMap, List)}. Details are provided there.
	 * <p>
	 * The file will only be {@link #save() saved} if necessary.
	 *
	 * @param ignored The list of paths to ignore. Ignored paths won't be affected
	 * and will remain unchanged. See {@link DataMap#update(HashMap, List)} for details.
	 *
	 * @param onException A {@link Consumer} that will accept any exception
	 * produced by this method.
	 *
	 * @return {@code true} if the update was successful, {@code false} if
	 * {@link #getUpdatedStream()} returns {@code null} or any exceptions occur
	 * during the process.
	 *
	 * @since JSky 1.0.0
	 */
	public boolean update(@Nullable List<String> ignored, @NotNull Consumer<IOException> onException) {
		final InputStream updated = getUpdatedStream();
		if (updated == null)
			return false;
		final HashMap<String, Object> updatedMap = getNewYaml().load(updated);
		if (getMap().update(updatedMap, ignored))
			save();
		try {
			updated.close();
			return true;
		} catch (IOException ex) {
			onException.accept(ex);
			return false;
		}
	}

	/**
	 * Updates this {@link YamlFile}, comparing its <b>cached</b> contents to those
	 * on the {@link #getUpdatedStream() updated} {@link InputStream}. This method
	 * relies on {@link DataMap#update(HashMap, List)}. Details are provided there.
	 * <p>
	 * The file will only be {@link #save() saved} if necessary.
	 *
	 * @param ignored The list of paths to ignore. Ignored paths won't be affected
	 * and will remain unchanged. See {@link DataMap#update(HashMap, List)} for details.
	 *
	 * @return {@code true} if the update was successful, {@code false} if
	 * {@link #getUpdatedStream()} returns {@code null} or any exceptions occur
	 * during the process.
	 *
	 * @since JSky 1.0.0
	 */
	public boolean update(@Nullable List<String> ignored) {
		return update(ignored, e -> {});
	}

	/**
	 * Updates this {@link YamlFile}, comparing its <b>cached</b> contents to those
	 * on the {@link #getUpdatedStream() updated} {@link InputStream}. This method
	 * relies on {@link DataMap#update(HashMap, List)}. Details are provided there.
	 * <p>
	 * The file will only be {@link #save() saved} if necessary.
	 *
	 * @param onException A {@link Consumer} that will accept any exception
	 * produced by this method.
	 *
	 * @return {@code true} if the update was successful, {@code false} if
	 * {@link #getUpdatedStream()} returns {@code null} or any exceptions occur
	 * during the process.
	 *
	 * @since JSky 1.0.0
	 */
	public boolean update(@NotNull Consumer<IOException> onException) {
		return update(null, onException);
	}

	/**
	 * Updates this {@link YamlFile}, comparing its <b>cached</b> contents to those
	 * on the {@link #getUpdatedStream() updated} {@link InputStream}. This method
	 * relies on {@link DataMap#update(HashMap, List)}. Details are provided there.
	 * <p>
	 * The file will only be {@link #save() saved} if necessary.
	 *
	 * @return {@code true} if the update was successful, {@code false} if
	 * {@link #getUpdatedStream()} returns {@code null} or any exceptions occur
	 * during the process.
	 *
	 * @since JSky 1.0.0
	 */
	public boolean update() {
		return update(null, e -> {});
	}

	/*
	 - Object class
	 */

	@Override
	public boolean equals(@Nullable Object obj) {
		final YamlFile other;
		if (obj == null || !YamlFile.class.isAssignableFrom(obj.getClass()))
			return false;
		other = (YamlFile) obj;
		return other.resourcePath.equals(this.resourcePath)
				&& other.file.equals(this.file) && other.loader.equals(this.loader)
				&& other.yaml.equals(this.yaml) && other.getMap().equals(this.getMap());
	}

	@Override
	public int hashCode() {
		return Objects.hash(resourcePath, file, loader, yaml, getMap());
	}

	@Override
	public String toString() {
		return "YamlFile" + getMap();
	}
}
