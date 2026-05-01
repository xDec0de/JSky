package net.codersky.jsky.test.storage;

import net.codersky.jsky.storage.DataMap;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestDataMap {

    /*
     - Construction and nesting flag
     */

    @Test
    public void testUsesNestingReflectsConstructor() {
        assertFalse(new DataMap(false).usesNesting());
        assertTrue(new DataMap(true).usesNesting());
    }

    /*
     - Modification flag
     */

    @Test
    public void testModificationFlagDefaultsFalse() {
        assertFalse(new DataMap(false).isModified());
    }

    @Test
    public void testModificationFlagAfterSet() {
        final DataMap map = new DataMap(false);
        map.set("k", 1);
        assertTrue(map.isModified());
    }

    @Test
    public void testModificationFlagAfterSetList() {
        final DataMap map = new DataMap(false);
        map.setList("k", List.of(1, 2));
        assertTrue(map.isModified());
    }

    @Test
    public void testModificationFlagCanBeReset() {
        final DataMap map = new DataMap(false);
        map.set("k", 1);
        map.setModified(false);
        assertFalse(map.isModified());
    }

    /*
     - get / set basics
     */

    @Test
    public void testSetReturnsTheValue() {
        assertEquals("v", new DataMap(false).set("k", "v"));
    }

    @Test
    public void testGetReturnsStoredValue() {
        final DataMap map = new DataMap(false);
        map.set("s", "hello");
        map.set("i", 42);
        map.set("u", UUID.fromString("00000000-0000-0000-0000-000000000001"));

        assertEquals("hello", map.get("s"));
        assertEquals(42, map.get("i"));
        assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000001"), map.get("u"));
    }

    @Test
    public void testGetReturnsNullWhenAbsent() {
        assertNull(new DataMap(false).get("missing"));
    }

    /*
     - get with type filter
     */

    @Test
    public void testGetWithTypeReturnsValueWhenMatch() {
        final DataMap map = new DataMap(false);
        map.set("k", "v");
        assertEquals("v", map.get("k", String.class));
    }

    @Test
    public void testGetWithTypeReturnsNullOnMismatch() {
        final DataMap map = new DataMap(false);
        map.set("k", 42);
        assertNull(map.get("k", String.class));
    }

    @Test
    public void testGetWithTypeReturnsNullWhenAbsent() {
        assertNull(new DataMap(false).get("missing", String.class));
    }

    /*
     - get with default
     */

    @Test
    public void testGetWithDefaultReturnsStored() {
        final DataMap map = new DataMap(false);
        map.set("k", "stored");
        assertEquals("stored", map.get("k", "fallback"));
    }

    @Test
    public void testGetWithDefaultReturnsDefaultWhenAbsent() {
        assertEquals("fallback", new DataMap(false).get("missing", "fallback"));
    }

    /*
     - List get / set
     */

    @Test
    public void testGetListRoundTrip() {
        final DataMap map = new DataMap(false);
        map.setList("k", List.of(1, 2, 3));
        assertEquals(List.of(1, 2, 3), map.getList("k", Integer.class));
    }

    @Test
    public void testGetListReturnsNullWhenAbsent() {
        assertNull(new DataMap(false).getList("missing", Integer.class));
    }

    @Test
    public void testGetListEmptyReturnsEmpty() {
        final DataMap map = new DataMap(false);
        map.setList("k", List.of());
        final List<Integer> got = map.getList("k", Integer.class);
        assertNotNull(got);
        assertTrue(got.isEmpty());
    }

    /*
     - Key access
     */

    @Test
    public void testContainsKeysAllPresent() {
        final DataMap map = new DataMap(false);
        map.set("a", 1);
        map.set("b", 2);
        assertTrue(map.containsKeys("a", "b"));
    }

    @Test
    public void testContainsKeysAnyMissing() {
        final DataMap map = new DataMap(false);
        map.set("a", 1);
        assertFalse(map.containsKeys("a", "b"));
    }

    @Test
    public void testGetKeysReturnsAll() {
        final DataMap map = new DataMap(false);
        map.set("a", 1);
        map.set("b", 2);
        assertEquals(2, map.getKeys().size());
        assertTrue(map.getKeys().contains("a"));
        assertTrue(map.getKeys().contains("b"));
    }

    @Test
    public void testGetEntriesReturnsAll() {
        final DataMap map = new DataMap(false);
        map.set("a", 1);
        map.set("b", 2);
        assertEquals(2, map.getEntries().size());
    }

    @Test
    public void testRemoveEntries() {
        final DataMap map = new DataMap(false);
        map.set("a", 1);
        map.set("b", 2);
        map.set("c", 3);
        map.removeEntries("a", "b");
        assertFalse(map.containsKeys("a"));
        assertFalse(map.containsKeys("b"));
        assertTrue(map.containsKeys("c"));
    }

    @Test
    public void testRemoveEntriesIgnoresMissing() {
        final DataMap map = new DataMap(false);
        map.set("a", 1);
        map.removeEntries("missing");
        assertTrue(map.containsKeys("a"));
    }

    @Test
    public void testClearEmptiesTheMap() {
        final DataMap map = new DataMap(false);
        map.set("a", 1);
        map.set("b", 2);
        map.clear();
        assertTrue(map.getKeys().isEmpty());
    }

    /*
     - Nesting
     */

    @Test
    public void testNestedSetAndGet() {
        final DataMap map = new DataMap(true);
        map.set("parent.child", "v");
        assertEquals("v", map.get("parent.child"));
    }

    @Test
    public void testNestedDeepSetAndGet() {
        final DataMap map = new DataMap(true);
        map.set("a.b.c.d", 1);
        assertEquals(1, map.get("a.b.c.d"));
    }

    @Test
    public void testGetKeysOnNestedParent() {
        final DataMap map = new DataMap(true);
        map.set("parent.first", 1);
        map.set("parent.second", 2);
        assertEquals(2, map.getKeys("parent").size());
    }

    @Test
    public void testGetEntriesOnNestedParent() {
        final DataMap map = new DataMap(true);
        map.set("parent.first", 1);
        map.set("parent.second", 2);
        assertEquals(2, map.getEntries("parent").size());
    }

    @Test
    public void testGetKeysOnAbsentNestedParentReturnsEmpty() {
        assertTrue(new DataMap(true).getKeys("missing").isEmpty());
    }

    @Test
    public void testRemoveEntriesNested() {
        final DataMap map = new DataMap(true);
        map.set("parent.child", 1);
        map.removeEntries("parent.child");
        assertNull(map.get("parent.child"));
    }

    /*
     - Update
     */

    @Test
    public void testUpdateAddsNewKeys() {
        final DataMap map = new DataMap(false);
        map.set("a", 1);

        final HashMap<String, Object> updated = new HashMap<>();
        updated.put("a", 1);
        updated.put("b", 2);

        assertTrue(map.update(updated, null));
        assertEquals(1, map.get("a", Integer.class));
        assertEquals(2, map.get("b", Integer.class));
    }

    @Test
    public void testUpdateRemovesMissingKeys() {
        final DataMap map = new DataMap(false);
        map.set("a", 1);
        map.set("b", 2);
        map.set("c", 3);

        final HashMap<String, Object> updated = new HashMap<>();
        updated.put("a", 1);

        assertTrue(map.update(updated, null));
        assertEquals(1, map.get("a", Integer.class));
        assertNull(map.get("b", Integer.class));
        assertNull(map.get("c", Integer.class));
    }

    @Test
    public void testUpdateRespectsIgnored() {
        final DataMap map = new DataMap(false);
        map.set("a", 1);
        map.set("b", 2);

        final HashMap<String, Object> updated = new HashMap<>();
        updated.put("a", 1);

        assertFalse(map.update(updated, List.of("b")));
        assertEquals(2, map.get("b", Integer.class));
    }

    @Test
    public void testUpdateIgnoredAlsoBlocksAdditions() {
        final DataMap map = new DataMap(false);
        map.set("a", 1);

        final HashMap<String, Object> updated = new HashMap<>();
        updated.put("a", 1);
        updated.put("b", 2);

        assertFalse(map.update(updated, List.of("b")));
        assertNull(map.get("b", Integer.class));
    }

    @Test
    public void testUpdateReturnsFalseWhenKeySetsMatch() {
        final DataMap map = new DataMap(false);
        map.set("a", 1);
        map.set("b", 2);

        final HashMap<String, Object> updated = new HashMap<>();
        updated.put("a", 999);
        updated.put("b", 999);

        assertFalse(map.update(updated, null));
        assertEquals(1, map.get("a", Integer.class));
        assertEquals(2, map.get("b", Integer.class));
    }

    @Test
    public void testUpdateFromAnotherDataMap() {
        final DataMap a = new DataMap(false);
        a.set("a", 1);
        a.set("b", 2);

        final DataMap b = new DataMap(false);
        b.set("a", 1);

        assertTrue(a.update(b, null));
        assertEquals(1, a.get("a", Integer.class));
        assertNull(a.get("b", Integer.class));
    }
}
