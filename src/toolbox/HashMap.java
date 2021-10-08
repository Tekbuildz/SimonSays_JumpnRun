package toolbox;

import java.util.Map;

/**
 *
 * the HashMap class contains a function which enables the return of a key of
 * a HashMap, when given the corresponding value
 *
 * @author Thomas Bundi
 * @version 0.3
 * @since 1.7
 */
public class HashMap {

    /**
     *
     * gets the key if given the map and value
     * this function will return the key assigned to the given value by
     * querying the given map
     * <p>
     * since the HashMap itself does not have a getKey() function,
     * Map.Entry<K, V> can be used since Map.Entry has a getKey() function.
     * Therefore the entry can be checked if it contains the value in question
     * and the key assigned to it can be retrieved
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
