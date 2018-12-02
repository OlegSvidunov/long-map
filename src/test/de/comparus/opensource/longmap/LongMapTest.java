package de.comparus.opensource.longmap;

import org.junit.*;

import java.util.Random;

import static org.junit.Assert.*;

public class LongMapTest {
    static LongMapImpl<String> map;

    @Before
    @Test
    public void initMapAndTestPut() {
        map = new LongMapImpl<>();
        assertEquals(null, map.put(1, "a"));
        assertEquals(null, map.put(56465, "b"));
        assertEquals(null, map.put(32, "c"));
        assertEquals(null, map.put(922, "d"));
        assertEquals(null, map.put(12312312, "f"));
        assertEquals(null, map.put(768, "g"));
        assertEquals(null, map.put(9213, "h"));
        assertEquals(null, map.put(2, "i"));
        assertEquals(null, map.put(0, "j"));
        assertEquals(null, map.put(3654, "k"));
        assertEquals(null, map.put(-648568, "l"));
        assertEquals(null, map.put(654, "m"));
        assertEquals(null, map.put(-18, "n"));
        assertEquals(null, map.put(Long.MAX_VALUE, "o"));
        assertEquals(null, map.put(Long.MIN_VALUE, "p"));

        assertEquals("c", map.put(32, "l"));
        assertEquals("d", map.put(922, "m"));
        assertEquals("f", map.put(12312312, "n"));
        assertEquals("l", map.put(-648568, "q"));
        assertEquals("o", map.put(Long.MAX_VALUE, "y"));
        assertEquals("p", map.put(Long.MIN_VALUE, "w"));
    }



    @Test
    public void testGet() {
        assertEquals("a", map.get(1));
        assertEquals("q", map.get(-648568));
        assertEquals("y", map.get(Long.MAX_VALUE));
        assertEquals("h", map.get(9213));

        assertEquals(null, map.get(7));
        assertEquals(null, map.get(13));
        assertEquals(null, map.get(9213654));
        assertEquals(null, map.get(-84));
    }

    @Test
    public void testRemove() {
        assertEquals("a", map.remove(1));
        assertEquals("q", map.remove(-648568));
        assertEquals("y", map.remove(Long.MAX_VALUE));
        assertEquals("h", map.remove(9213));

        assertEquals(null, map.remove(7));
        assertEquals(null, map.remove(13));
        assertEquals(null, map.remove(9213654));
        assertEquals(null, map.remove(-84));
    }

    @Test
    public void testIsEmpty() {
        assertEquals(false, map.isEmpty());

        map.clear();
        assertEquals(true, map.isEmpty());

        map.put(1, "a");
        assertEquals(false, map.isEmpty());
    }

    @Test
    public void testContainsKey() {
        assertEquals(true, map.containsKey(1));
        assertEquals(true, map.containsKey(-648568));
        assertEquals(true, map.containsKey(Long.MAX_VALUE));
        assertEquals(true, map.containsKey(9213));

        assertEquals(false, map.containsKey(7));
        assertEquals(false, map.containsKey(13));
        assertEquals(false, map.containsKey(9213654));
        assertEquals(false, map.containsKey(-84));
    }

    @Test
    public void testContainsValue() {
        assertEquals(true, map.containsValue("a"));
        assertEquals(true, map.containsValue("b"));
        assertEquals(true, map.containsValue("l"));
        assertEquals(true, map.containsValue("m"));

        assertEquals(false, map.containsValue("aa"));
        assertEquals(false, map.containsValue(""));
        assertEquals(false, map.containsValue("qq"));
        assertEquals(false, map.containsValue("x"));
    }

    @Test
    public void testGetKeys() {
        map = new LongMapImpl<>();
        assertArrayEquals(null, map.keys());

        map.put(1, "a");
        map.put(-5, "b");
        map.put(300, "c");
        long[] array = {1, -5, 300};
        assertArrayEquals(array, map.keys());
    }

    @Test
    public void testGetValues() {
        map = new LongMapImpl<>();
        assertArrayEquals(null, map.values());

        map.put(1, "a");
        map.put(-5, "b");
        map.put(300, "c");
        String[] array = {"a", "b", "c"};
        assertArrayEquals(array, map.values());
    }

    @Test
    public void testSize() {
        assertEquals(15, map.size());
        map.clear();
        assertEquals(0, map.size());
        map.put(1, "a");
        assertEquals(1, map.size());
        map.put(-123, "b");
        assertEquals(2, map.size());
        map.put(0, "qqq");
        assertEquals(3, map.size());
    }

    @Test
    public void testResize() {
        assertEquals(32, map.getTableLength());

        map.clear();
        assertEquals(16, map.getTableLength());

        map.put(1, "a");
        map.put(56465, "b");
        map.put(32, "c");
        map.put(922, "d");
        map.put(12312312, "f");
        map.put(768, "g");
        map.put(9213, "h");
        map.put(2, "i");
        assertEquals(16, map.getTableLength());

        map.put(0, "j");
        map.put(3654, "k");
        map.put(-648568, "l");
        map.put(654, "m");
        map.put(-18, "n");
        map.put(Long.MAX_VALUE, "o");
        map.put(Long.MIN_VALUE, "p");
        assertEquals(32, map.getTableLength());
    }

    @Test
    public void testMediumSeesaw() {
        map = new LongMapImpl<>();

        int n = new Random().nextInt(3000);
        for (int i = 0; i < n; i++) {
            map.put(i, "a");
        }
        assertEquals(n, map.size());

        for (int i = 0; i < n; i++) {
            map.get(i);
        }
        for (int i = 0; i < n; i++) {
            map.remove(i);
        }
        assertEquals(0, map.size());
    }

    @Test
    public void testLargeSeesaw() {
        map = new LongMapImpl<>();

        int n = 1000000;
        for (int i = 0; i < n; i++) {
            map.put(i, "a");
        }
        assertEquals(n, map.size());

        for (int i = 0; i < n; i++) {
            map.get(i);
        }
        for (int i = 0; i < n; i++) {
            map.remove(i);
        }
        assertEquals(0, map.size());
    }
}
