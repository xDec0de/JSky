package net.codersky.jsky.storage;

import net.codersky.jsky.Reloadable;
import org.jetbrains.annotations.NotNull;

/**
 * {@link DataManager} {@code abstract class} extension that provides
 * the {@link #setup()} and {@link #save()} {@code abstract} methods.
 * While extending this class will result in a usable storage, hence
 * its name, keep in mind that <b>not all</b> storages should be used
 * as databases, as they may not be the most efficient. This <b>only
 * identifies</b> classes capable of storing and loading data, not databases.
 *
 * @since JSky 1.0.0
 *
 * @author xDec0de_
 */
public abstract class Storage extends DataManager implements Reloadable {

	protected Storage(@NotNull DataMap map) {
		super(map);
	}

	protected Storage(boolean useNesting) {
		super(useNesting);
	}

	/**
	 * Does any necessary tasks in order to set up this {@link Storage}.
	 * Keep in mind that this does <b>NOT</b> load the storage, this method
	 * is intended for tasks such as creating any necessary file, obviously
	 * depending on the {@link Storage} type. To load the {@link Storage},
	 * use {@link #reload()}.
	 *
	 * @return {@code true} if the {@link Storage} was set up correctly,
	 * {@code false} otherwise.
	 *
	 * @since JSky 1.0.0
	 */
	public abstract boolean setup();

	/**
	 * Saves the cached data of this {@link Storage} to the actual storage.
	 * Keep in mind that some storage types may take a long time to save.
	 *
	 * @return {@code true} if this {@link Storage} was saved, {@code false} otherwise.
	 *
	 * @since JSky 1.0.0
	 */
	public abstract boolean save();
}
