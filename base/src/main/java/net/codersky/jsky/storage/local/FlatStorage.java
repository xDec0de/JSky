package net.codersky.jsky.storage.local;

import net.codersky.jsky.JFiles;
import net.codersky.jsky.storage.DataMap;
import net.codersky.jsky.storage.Storage;
import net.codersky.jsky.strings.JStrings;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.function.Function;

/**
 * {@link Storage} class used to store data on simple flat files while trying to keep the size of
 * said files to a minimum. The data is stored as text without spaces nor indentation, supporting basic
 * data types. About the memory usage, this class only stores a {@link DataMap}, provided by
 * {@link Storage}, and the {@link File} itself.
 * 
 * @author xDec0de_
 *
 * @since JSky 1.0.0
 */
public class FlatStorage extends Storage {

	/** The required file extension for {@link FlatStorage} files. */
	public static final String FILE_EXTENSION = ".jflat";
	private final File file;

	public FlatStorage(@NotNull File file) {
		super(false);
		if (!file.getName().endsWith(FILE_EXTENSION))
			throw new IllegalArgumentException("FlatStorage only accepts files with the \"" + FILE_EXTENSION + "\" extension.");
		this.file = file;
	}

	public FlatStorage(@NotNull String path) {
		super(false);
		this.file = new File(fixExtension(path));
	}

	/*
	 - Utility
	 */

	public boolean setup() {
		return JFiles.create(file);
	}

	@NotNull
	public final File asFile() {
		return file;
	}

	public boolean rename(@NotNull String name) {
		return file.renameTo(new File(file.getParent(), fixExtension(name)));
	}

	public final boolean exists() {
		return file.exists();
	}

	public final String fixExtension(@NotNull final String path) {
		return path.endsWith(FILE_EXTENSION) ? path : path + FILE_EXTENSION;
	}

	/*
	 - Saving
	 */

	@Override
	public boolean save() {
		if (!getMap().isModified())
			return true;
		if (!setup())
			return false;
		try (final FileWriter writer = new FileWriter(file)) {
			for (Entry<String, Object> entry : getEntries()) {
				final StringBuilder toWrite = new StringBuilder();
				if (entry.getValue() instanceof final List<?> lst) {
					if (lst.isEmpty())
						continue;
					appendList(toWrite, entry.getKey(), lst);
				} else
					appendSimple(toWrite, entry.getKey(), entry.getValue());
				if (!toWrite.isEmpty())
					writer.write(toWrite.toString());
			}
		} catch (IOException e) {
			return false;
		}
		getMap().setModified(false);
		return true;
	}

	/*
	 - Saving - Utility
	 */

	// NOTE: Number identification character is upper case.
	private char getNumberId(@NotNull final Number n) {
		return n.getClass().getSimpleName().charAt(0);
	}

	private StringBuilder addKey(final StringBuilder builder, char id, final String key) {
		return builder.append(id).append(key).append(':');
	}

	/*
	 - Saving - Simple objects
	 */

	// NOTE: Simple objects are stored as <id><key>:<value>

	private void appendSimple(final StringBuilder b, final String key, final Object value) {
		switch (value) {
			case String s -> addKey(b, 's', key).append(s.replace("\n", "\\n"));
			case Character ch -> addKey(b, 'c', key).append(ch == '\n' ? "\\n" : ch);
			case Boolean bool -> addKey(b, 'b', key).append(bool ? 't' : 'f');
			case UUID uuid -> addKey(b, 'u', key).append(uuid);
			case Number n -> addKey(b, getNumberId(n), key).append(n);
			case null, default -> { }
		}
	}

	/*
	 - Saving - Lists
	 */

	// NOTE: Lists are stored as *<id><key>:<value> (* Prefix)

	/**
	 * @throws ClassCastException If lst contains elements of different types (Try with Arrays.asList("exception", 10))
	 */
	@SuppressWarnings("unchecked")
	private void appendList(final StringBuilder builder, final String key, final List<?> lst) {
		final Object first = lst.getFirst();
		builder.append('*');
		switch (first) {
			case String ignored -> strListAppend(key, builder, (List<String>) lst);
			case Character ignored -> charListAppend(key, builder, (List<Character>) lst);
			case Boolean ignored -> booleanListAppend(key, builder, (List<Boolean>) lst);
			case UUID ignored -> listAppend(addKey(builder, 'u', key), lst, true, Object::toString);
			case Number n -> numListAppend(key, builder, (List<Number>) lst, n);
			case null, default -> { }
		}
	}

	private <T> String listAppend(final StringBuilder b, final List<T> lst, boolean separate, Function<T, String> modifier) {
		final int size = lst.size() - 1;
		for (int lstI = 0; lstI <= size; lstI++) {
			b.append(modifier.apply(lst.get(lstI)));
			if (separate && lstI != size)
				b.append(',');
		}
		// Result will be *?(key):(value), "*?" is appended on toWrite, with ? being the char of the list type.
		return b.toString();
	}

	private void numListAppend(final String key, final StringBuilder b, final List<Number> lst, final Number n) {
		listAppend(addKey(b, getNumberId(n), key), lst, true, Object::toString);
	}

	private void booleanListAppend(final String key, final StringBuilder b, final List<Boolean> lst) {
		listAppend(addKey(b, 'b', key), lst, false, bool -> bool ? "t" : "f");
	}

	private void charListAppend(final String key, final StringBuilder b, final List<Character> lst) {
		listAppend(addKey(b, 'c', key), lst, false, c -> c == '\n' ? "\\n" : c.toString());
	}

	private void strListAppend(final String key, StringBuilder builder, List<String> lst) {
		listAppend(addKey(builder, 's', key), lst, true, str -> {
			final int len = str.length();
			final StringBuilder strBuilder = new StringBuilder(len);
			// NOTE: ',' chars inside strings will be marked with a '\' to avoid breaking load logic.
			for (int i = 0; i < len; i++) {
				final char ch = str.charAt(i);
				if (ch == ',' || ch == '\n')
					strBuilder.append("\\");
				strBuilder.append(ch);
			}
			return strBuilder.toString();
		});
	}

	/*
	 - Loading
	 */

	@Override
	public boolean reload() {
		if (!setup())
			return false;
		try {
			final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line;
			while ((line = reader.readLine()) != null)
				parseLine(line);
			reader.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private boolean parseLine(final String line) {
		int separatorIndex = line.indexOf(':');
		if (separatorIndex == -1)
			return false;
		final String key = line.substring(1, separatorIndex);
		final String value = line.substring(separatorIndex + 1);
		if (line.charAt(0) == '*')
			return loadLstFromLine(line.charAt(1), key.substring(1), value);
		else
			return loadObjFromLine(line.charAt(0), key, value);
	}

	// Loading - Simple objects //

	private boolean loadObjFromLine(final char type, final String key, final String value) {
		return switch (type) {
		case 's' -> setString(key, value.replace("\\n", "\n"));
		case 'c' -> setChar(key, value.equals("\\n") ? '\n' : value.charAt(0));
		case 'b' -> setBoolean(key, value.charAt(0) == 't');
		case 'u' -> setUUID(key, JStrings.toUUID(value));
		case 'B' -> setByte(key, Byte.parseByte(value));
		case 'S' -> setShort(key, Short.parseShort(value));
		case 'I' -> setInt(key, Integer.parseInt(value));
		case 'L' -> setLong(key, Long.parseLong(value));
		case 'F' -> setFloat(key, Float.parseFloat(value));
		case 'D' -> setDouble(key, Double.parseDouble(value));
		default -> null;
		} != null;
	}

	// Loading - Lists //

	private boolean loadLstFromLine(final char type, final String key, final String value) {
		return switch (type) {
		case 's' -> loadStringList(key, value);
		case 'c' -> loadCharList(key, value);
		case 'b' -> loadBoolList(key, value);
		case 'u' -> loadList(key, value, JStrings::toUUID);
		case 'B' -> loadList(key, value, Byte::parseByte);
		case 'S' -> loadList(key, value, Short::parseShort);
		case 'I' -> loadList(key, value, Integer::parseInt);
		case 'L' -> loadList(key, value, Long::parseLong);
		case 'F' -> loadList(key, value, Float::parseFloat);
		case 'D' -> loadList(key, value, Double::parseDouble);
		default -> false;
		};
	}

	private <T> boolean loadList(final String key, final String lstStr, Function<String, T> modifier) {
		final int len = lstStr.length();
		final LinkedList<T> result = new LinkedList<>();
		StringBuilder element = new StringBuilder();
		for (int i = 0; i < len; i++) {
			final char ch = lstStr.charAt(i);
			if (ch == ',') { // Element separator
				result.add(modifier.apply(element.toString()));
				element = new StringBuilder();
			} else
				element.append(ch);
		}
		result.add(modifier.apply(element.toString()));
		getMap().setList(key, result);
		return true;
	}

	// Specific method for strings to handle the '\' character to avoid counting
	// Strings that contain commas as different strings. Also handles the '\n' character.
	private boolean loadStringList(final String key, final String lstStr) {
		final int len = lstStr.length();
		final LinkedList<String> result = new LinkedList<>();
		StringBuilder element = new StringBuilder();
		for (int i = 0; i < len; i++) {
			final char ch = lstStr.charAt(i);
			if (ch == '\\' && len > i) {
				final char next = lstStr.charAt(i + 1);
				if (next == ',')
					element.append(',');
				else if (ch == 'n')
					element.append('\n');
				i++;
			} else if (ch == ',') {
				result.add(element.toString());
				element = new StringBuilder();
			} else
				element.append(ch);
		}
		result.add(element.toString());
		setStrings(key, result);
		return true;
	}

	// Specific method for characters, as characters don't use a separator
	// But the '\n' character is stored as two characters.
	private boolean loadCharList(final String key, final String lstStr) {
		final int len = lstStr.length();
		final LinkedList<Character> result = new LinkedList<>();
		for (int i = 0; i < len; i++) {
			final char ch = lstStr.charAt(i);
			if (ch == '\\' && len > i && lstStr.charAt(i + 1) == 'n') {
				result.add('\n');
				i++;
			} else
				result.add(ch);
		}
		setChars(key, result);
		return true;
	}

	// Specific method for booleans, as booleans don't need a separator.
	private boolean loadBoolList(final String key, final String lstStr) {
		final int len = lstStr.length();
		final LinkedList<Boolean> result = new LinkedList<>();
		for (int i = 0; i < len; i++)
			result.add(lstStr.charAt(i) == 't');
		setBooleans(key, result);
		return true;
	}
}
