package net.codersky.jsky.storage;

import net.codersky.jsky.collections.JCollections;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public abstract class DataManager extends DataProvider {

	protected DataManager(@NotNull DataMap map) {
		super(map);
	}

	protected DataManager(boolean useNesting) {
		super(useNesting);
	}

	/*
	 - Key removal
	 */

	@NotNull
	public final DataManager removeEntries(@NotNull String... keys) {
		getMap().removeEntries(keys);
		return this;
	}

	@NotNull
	public DataManager clear() {
		getMap().clear();
		return this;
	}

	/*
	 - Setters - Strings
	 */

	@NotNull
	public String setString(@NotNull String key, @NotNull String value) {
		return getMap().set(key, value);
	}

	@NotNull
	public List<String> setStrings(@NotNull String key, @NotNull List<String> value) {
		return getMap().setList(key, value);
	}

	/*
	 - Setters - Characters
	 */

	public char setChar(@NotNull String key, char value) {
		return getMap().set(key, value);
	}

	@NotNull
	public List<Character> setChars(@NotNull String key, @NotNull List<Character> value) {
		return getMap().setList(key, value);
	}

	/*
	 - Setters - Booleans
	 */

	public boolean setBoolean(@NotNull String key, boolean value) {
		return getMap().set(key, value);
	}

	@NotNull
	public List<Boolean> setBooleans(@NotNull String key, @NotNull List<Boolean> value) {
		return getMap().setList(key, value);
	}

	/*
	 - Setters - Bytes
	 */

	public byte setByte(@NotNull String key, byte value) {
		return getMap().set(key, value);
	}

	@NotNull
	public List<Byte> setBytes(@NotNull String key, @NotNull List<Byte> value) {
		return getMap().setList(key, value);
	}

	@NotNull
	public List<Byte> setBytes(@NotNull String key, byte[] value) {
		return setBytes(key, JCollections.asByteList(value));
	}

	/*
	 - Setters - Shorts
	 */

	public short setShort(@NotNull String key, short value) {
		return getMap().set(key, value);
	}

	@NotNull
	public List<Short> setShorts(@NotNull String key, @NotNull List<Short> value) {
		return getMap().setList(key, value);
	}

	@NotNull
	public List<Short> setShorts(@NotNull String key, short[] value) {
		return setShorts(key, JCollections.asShortList(value));
	}

	/*
	 - Setters - Integers
	 */
	public int setInt(@NotNull String key, int value) {
		return getMap().set(key, value);
	}

	@NotNull
	public List<Integer> setInts(@NotNull String key, @NotNull List<Integer> value) {
		return getMap().setList(key, value);
	}

	@NotNull
	public List<Integer> setInts(@NotNull String key, int[] value) {
		return setInts(key, JCollections.asIntList(value));
	}

	/*
	 - Setters - Longs
	 */

	public long setLong(@NotNull String key, long value) {
		return getMap().set(key, value);
	}

	@NotNull
	public List<Long> setLongs(@NotNull String key, @NotNull List<Long> value) {
		return getMap().setList(key, value);
	}

	@NotNull
	public List<Long> setLongs(@NotNull String key, long[] value) {
		return setLongs(key, JCollections.asLongList(value));
	}

	/*
	 - Setters - Floats
	 */

	public float setFloat(@NotNull String key, float value) {
		return getMap().set(key, value);
	}

	@NotNull
	public List<Float> setFloats(@NotNull String key, @NotNull List<Float> value) {
		return getMap().setList(key, value);
	}

	@NotNull
	public List<Float> setShorts(@NotNull String key, float[] value) {
		return setFloats(key, JCollections.asFloatList(value));
	}

	/*
	 - Setters - Doubles
	 */

	public double setDouble(@NotNull String key, double value) {
		return getMap().set(key, value);
	}

	@NotNull
	public List<Double> setDoubles(@NotNull String key, @NotNull List<Double> value) {
		return getMap().setList(key, value);
	}

	@NotNull
	public List<Double> setDoubles(@NotNull String key, double[] value) {
		return setDoubles(key, JCollections.asDoubleList(value));
	}

	/*
	 - Setters - UUIDs
	 */

	@NotNull
	public UUID setUUID(@NotNull String key, @NotNull UUID value) {
		return getMap().set(key, value);
	}

	@NotNull
	public List<UUID> setUUIDs(@NotNull String key, @NotNull List<UUID> value) {
		return getMap().setList(key, value);
	}

	/*
	 - Conversion setters - Date
	 */

	@NotNull
	public Date setDate(@NotNull String key, @NotNull Date value) {
		getMap().set(key, value.toInstant().toEpochMilli());
		return value;
	}

	@NotNull
	public List<Date> setDates(@NotNull String key, @NotNull List<Date> value) {
		setLongs(key, JCollections.map(value, date -> date.toInstant().toEpochMilli()));
		return value;
	}
}
