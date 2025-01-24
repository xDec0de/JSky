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

public class YamlFile extends DataManager implements Reloadable {

	protected final ClassLoader loader;
	protected final Yaml yaml;
	protected final File file;
	protected final String resourcePath;

	public YamlFile(@NotNull ClassLoader loader, @Nullable File parent, @NotNull String path) {
		super(new DataMap(true));
		this.loader = Objects.requireNonNull(loader);
		this.yaml = getNewYaml();
		this.file = new File(parent, path);
		this.resourcePath = path;
	}

	public YamlFile(@Nullable File parent, @NotNull String path) {
		this(ClassLoader.getPlatformClassLoader(), parent, path);
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

	@NotNull
	public File asFile() {
		return file;
	}

	public boolean exists() {
		return file.exists();
	}

	public boolean setup() {
		if (exists())
			return reload();
		return JFiles.create(file) && update() && save();
	}

	public boolean save() {
		if (!getMap().isModified())
			return true;
		if (!exists() && !JFiles.create(file))
			return false;
		try {
			final FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8);
			yaml.dump(getMap().getInternalMap(), writer);
			writer.close();
			getMap().setModified(false);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/*
	 - Reloadable implementation
	 */

	public boolean reload() {
		try {
			getMap().clear();
			final HashMap<String, Object> loadedMap = this.yaml.load(new FileInputStream(this.file));
			if (loadedMap != null) // May be null on empty files
				getMap().getInternalMap().putAll(loadedMap);
			return true;
		} catch (FileNotFoundException | SecurityException ex) {
			return false;
		}
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
	 * as this is the {@link InputStream} that said method will compare against, if
	 * {@code null}, the file won't update.
	 *
	 * @return The updated {@link InputStream} of this {@link YamlFile}.
	 *
	 * @since JSky 1.0.0
	 */
	@Nullable
	protected InputStream getUpdatedStream() {
		return loader.getResourceAsStream(resourcePath);
	}

	public boolean update(@Nullable List<String> ignored) {
		final InputStream updated = getUpdatedStream();
		if (updated == null)
			return false;
		final HashMap<String, Object> updatedMap = getNewYaml().load(updated);
		if (getMap().update(updatedMap, ignored))
			save();
		try {
			updated.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public boolean update() {
		return update(null);
	}

	/*
	 - Object class
	 */

	@Override
	public boolean equals(@Nullable Object obj) {
		return obj instanceof final YamlFile other && other.getMap().equals(this.getMap());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getMap());
	}

	@Override
	public String toString() {
		return "YamlFile" + getMap();
	}
}
