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

	private final File file;

	public FlatStorage(@NotNull File file) {
		super(false);
		if (!file.getName().endsWith(".mcufs"))
			throw new IllegalArgumentException("FlatStorage only accepts files with the \".mcufs\" extension.");
		this.file = file;
	}

	public FlatStorage(@NotNull String path) {
		super(false);
		this.file = new File(path.endsWith(".mcufs") ? path : path + ".mcufs");
	}

	/*
	 * Utility
	 */

	public boolean setup() {
		return JFiles.create(file);
	}

	@NotNull
	public final File asFile() {
		return file;
	}

	public boolean rename(@NotNull String name) {
		return file.renameTo(new File(file.getParent() + (name.endsWith(".mcufs") ? name : name + ".mcufs")));
	}

	public final boolean exists() {
		return file.exists();
	}

	/*
	 * Saving
	 */

	@Override
	public boolean save() {
		if (!getMap().isModified())
			return true;
		if (!setup())
			return false;
		int errors = 0;
		try {
			final FileWriter writer = new FileWriter(file);
			for (Entry<String, Object> entry : getEntries()) {
				final String toWrite;
				if (entry.getValue() instanceof final List<?> lst) {
					if (lst.isEmpty())
						continue;
					toWrite = toWrite(entry.getKey(), lst);
				} else
					toWrite = toWrite(entry.getKey(), entry.getValue());
				if (toWrite != null)
					writer.write(toWrite);
				else
					errors++;
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		if (errors != 0)
			System.err.println("Failed to save " + file.getPath() + " because of " + errors + " error(s) shown above.");
		else
			getMap().setModified(false);
		return errors == 0;
	}

	// Saving - Simple objects //

	private String toWrite(String key, Object value) {
		final StringBuilder builder = new StringBuilder();
		if (value instanceof CharSequence)
			builder.append('s').append(key).append(':').append(value.toString().replace("\n", "\\n"));
		else if (value instanceof Character)
			builder.append('c').append(key).append(':').append(((char) value) == '\n' ? "\\n" : (char) value);
		else if (value instanceof Boolean)
			builder.append('b').append(key).append(':').append(((boolean) value ? "t" : "f"));
		else if (value instanceof UUID)
			builder.append('u').append(key).append(':').append(value.toString());
		else if (value instanceof Number) // Number identification character is upper case.
			builder.append(value.getClass().getSimpleName().charAt(0)).append(key).append(':').append(value.toString());
		else {
			System.err.println("Unsupported data of type " + value.getClass().getName() + " with a string value of \"" + value + "\"");
			return null;
		}
		return builder.append('\n').toString();
	}

	// Saving - Lists //

	/**
	 * @throws ClassCastException If lst contains elements of different types (Try with Arrays.asList("exception", 10))
	 */
	@SuppressWarnings("unchecked")
	private String toWrite(String key, List<?> lst) {
		final Object first = lst.getFirst();
		final StringBuilder builder = new StringBuilder("*");
		if (first instanceof CharSequence)
			listAppend(key, builder.append('s'), (List<CharSequence>) lst);
		else if (first instanceof Character)
			listAppend(key, builder.append('c'), (List<Character>) lst, false, c -> c == '\n' ? "\\n" : c.toString());
		else if (first instanceof Boolean)
			listAppend(key, builder.append('b'), (List<Boolean>) lst, false, b -> b ? "t" : "f");
		else if (first instanceof UUID)
			listAppend(key, builder.append('u'), (List<UUID>) lst, true, u -> u.toString());
		else if (first instanceof Number) // Number identification character is upper case.
			listAppend(key, builder.append(first.getClass().getSimpleName().charAt(0)), (List<Number>) lst, true, n -> n.toString());
		else {
			System.err.println("Unsupported list data of type " + first.getClass().getName());
			return null;
		}
		return builder.append('\n').toString();
	}

	private <T> String listAppend(String key, StringBuilder builder, List<T> lst, boolean separate, Function<T, String> modifier) {
		builder.append(key).append(':');
		final int size = lst.size() - 1;
		for (int lstI = 0; lstI <= size; lstI++) {
			builder.append(modifier.apply(lst.get(lstI)));
			if (separate && lstI != size)
				builder.append(',');
		}
		// Result will be *?(key):(value), "*?" is appended on toWrite, with ? being the char of the list type.
		return builder.toString();
	}

	private String listAppend(String key, StringBuilder builder, List<CharSequence> lst) {
		return listAppend(key, builder, lst, true, seq -> {
			final int len = seq.length();
			final StringBuilder seqBuilder = new StringBuilder(len);
			// ',' chars inside strings will be marked with a '\' to avoid breaking load logic.
			for (int seqI = 0; seqI < len; seqI++) {
				final char ch = seq.charAt(seqI);
				if (ch == ',')
					seqBuilder.append("\\,");
				else if (ch == '\n')
					seqBuilder.append("\\n");
				else
					seqBuilder.append(ch);
			}
			return seqBuilder.toString();
		});
	}

	/*
	 * Loading
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
