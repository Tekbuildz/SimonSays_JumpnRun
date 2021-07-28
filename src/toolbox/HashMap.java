package toolbox;

import java.util.Map;

public class HashMap {

    /**
     *
     * @param map - the map to be searched through
     * @param value - the value, the map above should be queried for
     * @param <K> - the key of a key/value pair
     * @param <V> - the value of a key/value pair
     * @return the key matching the value for which the map was queried for
     */
    public static <K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
