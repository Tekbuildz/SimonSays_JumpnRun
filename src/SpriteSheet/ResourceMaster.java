package SpriteSheet;

import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 *
 * the ResourceMaster class contains two Hashmaps which hold all the loaded
 * images and SpriteSheets
 *
 * @author Thomas Bundi
 * @version 0.2
 * @since 2.5
 */
public class ResourceMaster {

    private static final HashMap<String, SpriteSheet> spriteSheetMap = new HashMap<>();
    private static final HashMap<String, BufferedImage> imageMap = new HashMap<>();

    /**
     *
     * adds a SpriteSheet to the map
     *
     * @param key - the key as a string under which the image is found in the
     *            map
     * @param value - the image to be stored in the map
     */
    public static void addSpriteSheetToMap(String key, SpriteSheet value) {
        spriteSheetMap.put(key, value);
    }

    /**
     *
     * gets the SpriteSheet corresponding to the key given
     *
     * @param key - the key for the map to find the associated image
     * @return the image matching with the given key
     */
    public static SpriteSheet getSpriteSheetFromMap(String key) {
        return spriteSheetMap.get(key);
    }

    /**
     *
     * adds a new image element to the map
     *
     * @param key - the key as a string under which the image is found in the
     *            map
     * @param value - the image to be stored in the map
     */
    public static void addImageToMap(String key, BufferedImage value) {
        imageMap.put(key, value);
    }

    /**
     *
     * gets the image from the map using the indicatorName as a key
     *
     * @param key - key for the map to find the associated image
     * @return the image matching with the given key
     */
    public static BufferedImage getImageFromMap(String key) {
        return imageMap.get(key);
    }
}
