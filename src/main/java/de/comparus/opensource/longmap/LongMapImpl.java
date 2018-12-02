package de.comparus.opensource.longmap;

import java.lang.reflect.Array;

/**
 * This implementation based on a hash table with open addressing strategy,
 * and uses linear probing for collision resolution. All entries are
 * stored in hash table itself and there is no need for external
 * data structures(lists, sets etc.). Hence it consumes much less memory
 * compared to hash table based on chaining.
 *
 * <p>Most of the operations have 0(1) time complexity in the best case and 0(n)
 * in the worst one. Bad cases include searching for non-existing key or putting
 * to and removing from parts of table with multiple collisions.
 *
 * <p>May be improved by adding more complex and efficient hash-function.
 */
public class LongMapImpl<V> implements LongMap<V> {

    private final int DEFAULT_TABLE_LENGTH = 16;
    private final float MAX_LOAD_FACTOR = 0.75f;
    private int currentMapSize = 0;

    private Entry[] table;

    public LongMapImpl() {
        table = new Entry[DEFAULT_TABLE_LENGTH];
    }

    /**
     * Return null if there is no @param key in the table yet.
     * If the key already exists, returns old value linked
     * to this key.
     */
    @SuppressWarnings("unchecked")
    public V put(long key, V value) {
        int hash = Math.abs((int) (key % table.length));
        int probe = 0;

        while (table[hash] != null) {
            if (table[hash].key == key) {
                V oldValue = (V) table[hash].value;
                table[hash].value = value;
                return oldValue;
            }
            else {
                probe++;
                hash += probe;
                if (hash >= table.length) {
                    hash = 0;
                    probe = 0;
                }
            }
        }

        table[hash] = new Entry(key, value);
        currentMapSize++;

        if (currentMapSize > (table.length * MAX_LOAD_FACTOR)) {
            resize();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public V get(long key) {
        int keyPosition = searchForKey(key);
        return keyPosition == -1 ? null : (V) table[keyPosition].value;
    }

    /**
     * Return a value, linked to @param key, if the hash table contains this key
     * and removing has been successful. Otherwise return null.
     */
    @SuppressWarnings("unchecked")
    public V remove(long key) {
        int keyPosition = searchForKey(key);

        if (keyPosition == -1) {
            return null;
        }

        V result = (V) table[keyPosition].value;
        table[keyPosition] = null;
        currentMapSize--;

        return result;
    }

    public boolean isEmpty() {
        return currentMapSize == 0;
    }

    public boolean containsKey(long key) {
        int keyPosition = searchForKey(key);
        return keyPosition != -1;
    }

    public boolean containsValue(V value) {
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null && table[i].value.equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return null if there are zero entries in the table.
     */
    public long[] keys() {

        if (currentMapSize > 0) {
            long[] keys = new long[currentMapSize];
            for (int i = 0, j = 0; i < table.length; i++) {
                if (table[i] != null) {
                    keys[j] = table[i].key;
                    j++;
                }
            }
            return keys;
        }
        return null;
    }

    /**
     * Return null if there are zero entries in the table.
     */
    @SuppressWarnings("unchecked")
    public V[] values() {

        if (currentMapSize > 0) {
            V[] values = (V[]) new Object[currentMapSize];
            for (int i = 0, j = 0; i < table.length; i++) {
                if (table[i] != null) {
                    values[j] = (V) (table[i].value);
                    j++;
                }
            }

            V[] result = (V[]) Array.newInstance(values[0].getClass(), currentMapSize);
            System.arraycopy(values, 0, result, 0, currentMapSize);
            return result;
        }
        return null;
    }

    public long size() {
        return currentMapSize;
    }

    public void clear() {
        table = new Entry[DEFAULT_TABLE_LENGTH];
        currentMapSize = 0;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newTableSize = 2 * table.length;
        Entry[] oldTable = table;
        table = new Entry[newTableSize];
        currentMapSize = 0;

        for (int i = 0; i < oldTable.length; i++) {
            if (oldTable[i] != null) {
                put(oldTable[i].key, (V) oldTable[i].value);
            }
        }
    }

    /**
     * Searching for the entry with @param key in the hash table.
     * Returns index of key in the table, otherwise return -1.
     */
    private int searchForKey(long key) {
        int index = Math.abs((int) (key % table.length));
        int probesCounter = 0;
        while (table[index] == null || table[index].key != key) {
            probesCounter++;
            index++;
            if (index >= table.length) {
                index = 0;
            }
            if (probesCounter == table.length) {
                return -1;
            }
        }
        return index;
    }

    static class Entry<V> {
        long key;
        V value;

        private Entry(long key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    /**
     * For tests
     */
    public int getTableLength() {
        return table.length;
    }
}
