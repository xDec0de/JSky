package net.codersky.jsky.test.storage;

import net.codersky.jsky.storage.DataManager;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestDataProvider {

    private static DataManager flat() {
        return new DataManager(false) {
        };
    }

    private static DataManager nested() {
        return new DataManager(true) {
        };
    }

    /*
     - Common state and key access
     */

    @Test
    public void testSupportsKeyNestingReflectsConstructor() {
        assertFalse(flat().supportsKeyNesting());
        assertTrue(nested().supportsKeyNesting());
    }

    @Test
    public void testContainsKeyTrueWhenPresent() {
        final DataManager dm = flat();
        dm.setString("k", "v");
        assertTrue(dm.containsKey("k"));
    }

    @Test
    public void testContainsKeyFalseWhenAbsent() {
        assertFalse(flat().containsKey("missing"));
    }

    @Test
    public void testGetKeysReflectsStoredEntries() {
        final DataManager dm = flat();
        dm.setString("a", "1");
        dm.setString("b", "2");
        assertEquals(2, dm.getKeys().size());
    }

    @Test
    public void testGetEntriesReflectsStoredEntries() {
        final DataManager dm = flat();
        dm.setString("a", "1");
        assertEquals(1, dm.getEntries().size());
    }

    /*
     - Strings
     */

    @Test
    public void testStringRoundTrip() {
        final DataManager dm = flat();
        dm.setString("k", "value");
        assertEquals("value", dm.getString("k"));
    }

    @Test
    public void testStringDefaultWhenAbsent() {
        assertEquals("def", flat().getString("missing", "def"));
    }

    @Test
    public void testStringNullWhenAbsent() {
        assertNull(flat().getString("missing"));
    }

    @Test
    public void testStringListRoundTrip() {
        final DataManager dm = flat();
        dm.setStrings("k", List.of("a", "b", "c"));
        assertEquals(List.of("a", "b", "c"), dm.getStrings("k"));
    }

    @Test
    public void testStringListDefaultWhenAbsent() {
        final List<String> def = List.of("x");
        assertSame(def, flat().getStrings("missing", def));
    }

    /*
     - Integers (representative for the numeric primitive family)
     */

    @Test
    public void testIntRoundTrip() {
        final DataManager dm = flat();
        dm.setInt("k", 42);
        assertEquals(42, dm.getInt("k", 0));
    }

    @Test
    public void testIntDefaultWhenAbsent() {
        assertEquals(7, flat().getInt("missing", 7));
    }

    @Test
    public void testIntNullWhenAbsent() {
        assertNull(flat().getInt("missing"));
    }

    @Test
    public void testIntListRoundTrip() {
        final DataManager dm = flat();
        dm.setInts("k", List.of(1, 2, 3));
        assertEquals(List.of(1, 2, 3), dm.getInts("k"));
    }

    @Test
    public void testIntListFromArrayRoundTrip() {
        final DataManager dm = flat();
        dm.setInts("k", new int[]{4, 5, 6});
        assertEquals(List.of(4, 5, 6), dm.getInts("k"));
    }

    /*
     - Longs (sanity check, since Date is stored as long internally)
     */

    @Test
    public void testLongRoundTrip() {
        final DataManager dm = flat();
        dm.setLong("k", 9_999_999_999L);
        assertEquals(9_999_999_999L, dm.getLong("k", 0L));
    }

    @Test
    public void testLongListRoundTrip() {
        final DataManager dm = flat();
        dm.setLongs("k", List.of(1L, 2L, 3L));
        assertEquals(List.of(1L, 2L, 3L), dm.getLongs("k"));
    }

    /*
     - Booleans
     */

    @Test
    public void testBooleanRoundTrip() {
        final DataManager dm = flat();
        dm.setBoolean("t", true);
        dm.setBoolean("f", false);
        assertTrue(dm.getBoolean("t", false));
        assertFalse(dm.getBoolean("f", true));
    }

    @Test
    public void testBooleanDefaultWhenAbsent() {
        assertTrue(flat().getBoolean("missing", true));
    }

    @Test
    public void testBooleanListRoundTrip() {
        final DataManager dm = flat();
        dm.setBooleans("k", List.of(true, false, true));
        assertEquals(List.of(true, false, true), dm.getBooleans("k"));
    }

    /*
     - Characters
     */

    @Test
    public void testCharRoundTrip() {
        final DataManager dm = flat();
        dm.setChar("k", 'x');
        assertEquals('x', dm.getChar("k", 'a'));
    }

    @Test
    public void testCharDefaultWhenAbsent() {
        assertEquals('z', flat().getChar("missing", 'z'));
    }

    @Test
    public void testCharListRoundTrip() {
        final DataManager dm = flat();
        dm.setChars("k", List.of('a', 'b', 'c'));
        assertEquals(List.of('a', 'b', 'c'), dm.getChars("k"));
    }

    /*
     - UUIDs
     */

    @Test
    public void testUUIDRoundTrip() {
        final DataManager dm = flat();
        final UUID u = UUID.fromString("11111111-1111-1111-1111-111111111111");
        dm.setUUID("k", u);
        assertEquals(u, dm.getUUID("k"));
    }

    @Test
    public void testUUIDDefaultWhenAbsent() {
        final UUID def = UUID.fromString("00000000-0000-0000-0000-000000000000");
        assertEquals(def, flat().getUUID("missing", def));
    }

    @Test
    public void testUUIDListRoundTrip() {
        final DataManager dm = flat();
        final UUID a = UUID.fromString("00000000-0000-0000-0000-00000000000a");
        final UUID b = UUID.fromString("00000000-0000-0000-0000-00000000000b");
        dm.setUUIDs("k", List.of(a, b));
        assertEquals(List.of(a, b), dm.getUUIDs("k"));
    }

    /*
     - Dates (single value stored as epoch millis under the hood)
     */

    @Test
    public void testDateRoundTrip() {
        final DataManager dm = flat();
        final Date d = new Date(123_456_789L);
        dm.setDate("k", d);
        assertEquals(d, dm.getDate("k"));
    }

    @Test
    public void testDateDefaultWhenAbsent() {
        final Date def = new Date(0L);
        assertEquals(def, flat().getDate("missing", def));
    }

    @Test
    public void testDateNullWhenAbsent() {
        assertNull(flat().getDate("missing"));
    }

    /*
     - Date lists (round-trip through epoch millis storage)
     */

    @Test
    public void testDateListRoundTrip() {
        final DataManager dm = flat();
        final List<Date> dates = new ArrayList<>();
        dates.add(new Date(0L));
        dates.add(new Date(1_000L));
        dates.add(new Date(2_000_000_000L));
        dm.setDates("k", dates);
        assertEquals(dates, dm.getDates("k"));
    }

    @Test
    public void testDateListNullWhenAbsent() {
        assertNull(flat().getDates("missing"));
    }

    @Test
    public void testDateListDefaultWhenAbsent() {
        final List<Date> def = List.of(new Date(42L));
        assertSame(def, flat().getDates("missing", def));
    }

    @Test
    public void testDateListPrefersStoredOverDefault() {
        final DataManager dm = flat();
        final List<Date> stored = new ArrayList<>();
        stored.add(new Date(123L));
        dm.setDates("k", stored);

        final List<Date> def = List.of(new Date(999L));
        assertEquals(stored, dm.getDates("k", def));
    }

    /*
     - Type mismatch returns null on the typed getters
     */

    @Test
    public void testTypeMismatchReturnsNull() {
        final DataManager dm = flat();
        dm.setInt("k", 42);
        assertNull(dm.getString("k"));
        assertNull(dm.getBoolean("k"));
        assertNull(dm.getChar("k"));
    }

    @Test
    public void testTypeMismatchHonorsDefault() {
        final DataManager dm = flat();
        dm.setInt("k", 42);
        assertEquals("def", dm.getString("k", "def"));
    }

    /*
     - Nested keys
     */

    @Test
    public void testNestedRoundTrip() {
        final DataManager dm = nested();
        dm.setString("parent.child", "v");
        assertEquals("v", dm.getString("parent.child"));
    }
}
