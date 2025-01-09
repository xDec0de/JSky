package net.codersky.jsky.storage;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public abstract class DataProvider {

	private final DataMap map;

	protected DataProvider(@NotNull DataMap map) {
		this.map = map;
	}

	protected DataProvider(boolean useNesting) {
		this(new DataMap(useNesting));
	}

	/*
	 - DataMap access & information
	 */

	/**
	 * Provides access to the <b>internal</b> {@link DataMap} that
	 * this {@link DataProvider} is using. Keep in mind once again
	 * that this is for <b>internal</b> usage only, and you should
	 * not use it unless you <b>really</b> know what you are doing.
	 * That being said, for any {@link DataProvider} extension
	 * that saves this map, please remember that it features a
	 * {@link DataMap#isModified() modification} check and that you
	 * should {@link DataMap#setModified(boolean) reset} it every
	 * time you save in order to avoid saving unmodified data.
	 *
	 * @return the <b>internal</b> {@link DataMap} that this
	 * {@link DataProvider} is using.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	@ApiStatus.Internal
	protected DataMap getMap() {
		return map;
	}

	/**
	 * Checks whether this {@link DataProvider} supports key nesting or not.
	 * Key nesting essentially means that keys can have maps as a value,
	 * here is an example of key nesting:
	 * <pre>
	 * notNested: 1
	 * key:
	 *   nestedKey: 2
	 *   anotherNestedKey: 3
	 * </pre>
	 * {@code notNested}'s key would be just <i>"notNested"</i>, however,
	 * {@code nestedKey}'s key would be <i>"key.nestedKey"</i>. Internally, this means
	 * that {@code key}'s value is another map with its own keys.
	 *
	 * @return {@code true} if this {@link DataProvider} supports key nesting,
	 * {@code false} otherwise.
	 */
	public boolean supportsKeyNesting() {
		return getMap().usesNesting();
	}

	/*
	 - Key access
	 */

	@NotNull
	public Set<Map.Entry<String, Object>> getEntries() {
		return getMap().getEntries();
	}

	/**
	 * Gets the {@link Set} of keys that are currently
	 * cached on this {@link DataProvider}. This set
	 * supports element removal but not addition as
	 * explained on {@link HashMap#keySet()}. Elements
	 * removed from this {@link Set} will also be removed
	 * from the storage cache. This can be used, for example
	 * to {@link Set#clear() clear} the file.
	 *
	 * @return The {@link Set} of keys that are currently
	 * cached on this {@link DataProvider}.
	 *
	 * @since SkyUtils 1.0.0
	 *
	 * @see #containsKey(String...)
	 */
	public Set<String> getKeys() {
		return getMap().getKeys();
	}

	@NotNull
	public Set<String> getKeys(@NotNull String parent) {
		return getMap().getKeys(parent);
	}

	public boolean containsKey(@NotNull String... keys) {
		return getMap().containsKeys(keys);
	}

	/*
	 - Getters - Strings
	 */

	/**
	 * Gets a {@link String} from this {@link DataProvider}.
	 *
	 * @param key the key to get the {@link String} from.
	 *
	 * @return The stored {@link String} or {@code null} if {@code key}
	 * didn't exist or contains a different type of value.
	 *
	 * @throws NullPointerException if {@code key} is {@code null}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@Nullable
	public String getString(@NotNull String key) {
		return getMap().get(key, String.class);
	}

	@NotNull
	public String getString(@NotNull String key, @NotNull String def) {
		final String str = getString(key);
		return str == null ? def : str;
	}

	@Nullable
	public List<String> getStrings(@NotNull String key) {
		return getMap().getList(key, String.class);
	}

	@NotNull
	public List<String> getStrings(@NotNull String key, @NotNull List<String> def) {
		final List<String> lst = getStrings(key);
		return lst == null ? def : lst;
	}

	/*
	 - Getters - Characters
	 */

	@Nullable
	public Character getChar(@NotNull String key) {
		return getMap().get(key, Character.class);
	}

	public char getChar(@NotNull String key, char def) {
		final Character ch = getChar(key);
		return ch == null ? def : ch;
	}

	@Nullable
	public List<Character> getChars(@NotNull String key) {
		return getMap().getList(key, Character.class);
	}

	@NotNull
	public List<Character> getChars(@NotNull String key, @NotNull List<Character> def) {
		final List<Character> lst = getChars(key);
		return lst == null ? def : lst;
	}

	/*
	 - Getters - Booleans
	 */

	@Nullable
	public Boolean getBoolean(@NotNull String key) {
		return getMap().get(key, Boolean.class);
	}

	public boolean getBoolean(@NotNull String key, boolean def) {
		final Boolean bool = getBoolean(key);
		return bool == null ? def : bool;
	}

	@Nullable
	public List<Boolean> getBooleans(@NotNull String key) {
		return getMap().getList(key, Boolean.class);
	}

	@NotNull
	public List<Boolean> getBooleans(@NotNull String key, @NotNull List<Boolean> def) {
		final List<Boolean> lst = getBooleans(key);
		return lst == null ? def : lst;
	}

	/*
	 - Getters - Bytes
	 */

	@Nullable
	public Byte getByte(@NotNull String key) {
		return getMap().get(key, Byte.class);
	}

	public byte getByte(@NotNull String key, byte def) {
		final Byte b = getByte(key);
		return b == null ? def : b;
	}

	@Nullable
	public List<Byte> getBytes(@NotNull String key) {
		return getMap().getList(key, Byte.class);
	}

	@NotNull
	public List<Byte> getBytes(@NotNull String key, @NotNull List<Byte> def) {
		final List<Byte> lst = getBytes(key);
		return lst == null ? def : lst;
	}

	/*
	 - Getters - Shorts
	 */

	@Nullable
	public Short getShort(@NotNull String key) {
		return getMap().get(key, Short.class);
	}

	public short getShort(@NotNull String key, short def) {
		final Short s = getShort(key);
		return s == null ? def : s;
	}

	@Nullable
	public List<Short> getShorts(@NotNull String key) {
		return getMap().getList(key, Short.class);
	}

	@NotNull
	public List<Short> getShorts(@NotNull String key, @NotNull List<Short> def) {
		final List<Short> lst = getShorts(key);
		return lst == null ? def : lst;
	}

	/*
	 - Getters - Integers
	 */

	@Nullable
	public Integer getInt(@NotNull String key) {
		return getMap().get(key, Integer.class);
	}

	public int getInt(@NotNull String key, int def) {
		final Integer i = getInt(key);
		return i == null ? def : i;
	}

	@Nullable
	public List<Integer> getInts(@NotNull String key) {
		return getMap().getList(key, Integer.class);
	}

	@NotNull
	public List<Integer> getInts(@NotNull String key, @NotNull List<Integer> def) {
		final List<Integer> lst = getInts(key);
		return lst == null ? def : lst;
	}

	/*
	 - Getters - Longs
	 */

	@Nullable
	public Long getLong(@NotNull String key) {
		return getMap().get(key, Long.class);
	}

	public long getLong(@NotNull String key, long def) {
		final Long l = getLong(key);
		return l == null ? def : l;
	}

	@Nullable
	public List<Long> getLongs(@NotNull String key) {
		return getMap().getList(key, Long.class);
	}

	@NotNull
	public List<Long> getLongs(@NotNull String key, @NotNull List<Long> def) {
		final List<Long> lst = getLongs(key);
		return lst == null ? def : lst;
	}

	/*
	 - Getters - Floats
	 */

	@Nullable
	public Float getFloat(@NotNull String key) {
		return getMap().get(key, Float.class);
	}

	public float getFloat(@NotNull String key, float def) {
		final Float f = getFloat(key);
		return f == null ? def : f;
	}

	@Nullable
	public List<Float> getFloats(@NotNull String key) {
		return getMap().getList(key, Float.class);
	}

	@NotNull
	public List<Float> getFloats(@NotNull String key, @NotNull List<Float> def) {
		final List<Float> lst = getFloats(key);
		return lst == null ? def : lst;
	}

	/*
	 - Getters - Doubles
	 */

	@Nullable
	public Double getDouble(@NotNull String key) {
		return getMap().get(key, Double.class);
	}

	public double getDouble(@NotNull String key, double def) {
		final Double d = getDouble(key);
		return d == null ? def : d;
	}

	@Nullable
	public List<Double> getDoubles(@NotNull String key)  {
		return getMap().getList(key, Double.class);
	}

	@NotNull
	public List<Double> getDoubles(@NotNull String key, @NotNull List<Double> def) {
		final List<Double> lst = getDoubles(key);
		return lst == null ? def : lst;
	}

	/*
	 - Getters - UUIDs
	 */

	@Nullable
	public UUID getUUID(@NotNull String key) {
		return getMap().get(key, UUID.class);
	}

	@NotNull
	public UUID getUUID(@NotNull String key, @NotNull UUID def) {
		final UUID uuid = getUUID(key);
		return uuid == null ? def : uuid;
	}

	@Nullable
	public List<UUID> getUUIDs(@NotNull String key) {
		return getMap().getList(key, UUID.class);
	}

	@NotNull
	public List<UUID> getUUIDs(@NotNull String key, @NotNull List<UUID> def) {
		final List<UUID> lst = getUUIDs(key);
		return lst == null ? def : lst;
	}
}
